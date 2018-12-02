package net.hunnor.dict.admin.export;

import net.hunnor.dict.admin.config.Language;
import net.hunnor.dict.admin.model.Entry;
import net.hunnor.dict.admin.model.Inflections;
import net.hunnor.dict.admin.model.Lemma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.OutputStream;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl implements ExportService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  @Qualifier("HU")
  private Collator huCollator;

  @Autowired
  @Qualifier("NB")
  private Collator nbCollator;

  @Autowired
  private ParserService parserService;

  @Autowired
  private WriterService writerService;

  private Map<String, Map<Integer, String>> patterns = new HashMap<>();

  private List<Entry> entries = new ArrayList<>();

  private Map<String, Integer> maxByForm = new HashMap<>();

  private Map<String, Integer> counterByForm = new HashMap<>();

  private Map<Integer, List<Integer>> inflectedLemmaByEntry = new HashMap<>();

  @Override
  public void export(Language language, OutputStream outputStream) throws ExportException {

    switch (language) {
      case HU:
        entries = loadHuEntries();
        maxByForm = countMaxByForm();
        writeEntries(outputStream, Language.HU);
        break;
      case NB:
        patterns = loadPatterns();
        entries = loadNbEntries();
        maxByForm = countMaxByForm();
        writeEntries(outputStream, Language.NB);
        break;
      default:
        break;
    }

  }

  private Map<String, Map<Integer, String>> loadPatterns() {

    Map<String, Map<Integer, String>> result = new HashMap<>();
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
        "SELECT PARADIGME_ID, BOY_NUMMER, BOY_UTTRYKK FROM PARADIGME_BOYING");
    while (rowSet.next()) {

      String paradigmeId = rowSet.getString(1);
      int boyNummer = rowSet.getInt(2);
      String boyUttrykk = rowSet.getString(3);

      Map<Integer, String> map = result.computeIfAbsent(paradigmeId, key -> new HashMap<>());
      map.put(boyNummer, boyUttrykk);

    }

    return result;

  }

  private List<Entry> loadHuEntries() {
    return loadEntries("HN_HU_ENTRY", "HN_HU_ENTRY_LEMMA", null, null,
        "HN_HU_LEMMA", null);
  }

  private List<Entry> loadNbEntries() {
    return loadEntries("HN_NB_ENTRY", "HN_NB_ENTRY_LEMMA", "LEMMA", "LEMMA_PARADIGME",
        "HN_NB_LEMMA", "HN_NB_LEMMA_PARADIGME");
  }

  private List<Entry> loadEntries(String entryTable, String entryLemmaTable,
      String lemmaTableOrdbank, String lemmaParadigmeTableOrdbank,
      String lemmaTableHn, String lemmaParadigmeTableHn) {

    List<Entry> result = new ArrayList<>();
    SqlRowSet entryRowSet = jdbcTemplate.queryForRowSet("SELECT ENTRY_ID, STATUS, POS,"
        + " CONCAT('', TRANSLATION) FROM " + entryTable + " WHERE STATUS > 0");
    while (entryRowSet.next()) {

      int entryId = entryRowSet.getInt(1);
      int status = entryRowSet.getInt(2);
      String pos = entryRowSet.getString(3);
      String translation = entryRowSet.getString(4);
      Entry entry = new Entry(entryId, status, pos, translation);
      result.add(entry);

      List<Lemma> lemmata = new ArrayList<>();
      SqlRowSet lemmaIdRowSet = jdbcTemplate.queryForRowSet("SELECT LEMMA_ID,"
          + " PARADIGME_ID, BOY_NUMMER, SOURCE, WEIGHT FROM " + entryLemmaTable
          + " WHERE ENTRY_ID = ? ORDER BY WEIGHT", entryId);
      while (lemmaIdRowSet.next()) {

        int lemmaId = lemmaIdRowSet.getInt(1);
        Lemma lemma = new Lemma();
        lemma.setId(lemmaId);

        int source = lemmaIdRowSet.getInt(4);
        String lemmaTable = null;
        String lemmaParadigmeTable = null;
        if (source == Lemma.SOURCE_ORDBANK) {
          lemmaTable = lemmaTableOrdbank;
          lemmaParadigmeTable = lemmaParadigmeTableOrdbank;
        } else {
          lemmaTable = lemmaTableHn;
          lemmaParadigmeTable = lemmaParadigmeTableHn;
        }

        String paradigmeId = lemmaIdRowSet.getString(2);
        int boyNummer = lemmaIdRowSet.getInt(3);

        int weight = lemmaIdRowSet.getInt(5);
        lemma.setWeight(weight);

        SqlRowSet lemmaRowSet = jdbcTemplate.queryForRowSet("SELECT GRUNNFORM FROM "
            + lemmaTable + " WHERE LEMMA_ID = ?", lemmaId);
        lemmaRowSet.first();
        String grunnform = lemmaRowSet.getString(1);
        String inflectedGrunnform = inflectGrunnform(grunnform, paradigmeId, boyNummer);
        lemma.setGrunnform(inflectedGrunnform);

        if (paradigmeId != null && boyNummer != 0) {
          inflectedLemmaByEntry
              .computeIfAbsent(entryId, k -> new ArrayList<Integer>()).add(lemmaId);
        }

        if (lemmaParadigmeTable != null) {
          List<String> paradigmeIdList = new ArrayList<>();
          SqlRowSet paradigmeRowSet = jdbcTemplate.queryForRowSet("SELECT PARADIGME_ID FROM "
              + lemmaParadigmeTable + " WHERE LEMMA_ID = ?", lemmaId);
          while (paradigmeRowSet.next()) {
            String id = paradigmeRowSet.getString(1);
            paradigmeIdList.add(id);
          }
          lemma.setParadigmeId(paradigmeIdList);
        }
        lemmata.add(lemma);

      }

      lemmata = lemmata.stream()
          .sorted(Comparator.comparing(Lemma::getWeight)
              .thenComparing(Lemma::getGrunnform)
              .thenComparing(Lemma::getId))
          .collect(Collectors.toList());
      entry.setLemmata(lemmata);

    }

    return result;

  }

  private String inflectGrunnform(String grunnform, String paradigmeId, int boyNummer) {
    String inflectedForm = grunnform;
    if (paradigmeId != null && boyNummer != 0) {
      Map<Integer, String> inflectionPatterns = patterns.get(paradigmeId);
      Map<Integer, String> inflectedForms =
          Inflections.getInflections(grunnform, inflectionPatterns);
      inflectedForm = inflectedForms.get(boyNummer);
    }
    return inflectedForm;
  }

  private Map<String, Integer> countMaxByForm() {

    Map<String, Integer> result = new HashMap<>();

    entries.stream().flatMap(entry -> entry.getLemmata().stream())
        .map(Lemma::getGrunnform).forEach(grunnform -> {

          if (result.get(grunnform) == null) {
            result.put(grunnform, 1);
          } else {
            result.put(grunnform, result.get(grunnform) + 1);
          }

        });

    return result;

  }

  private void writeEntries(OutputStream outputStream, Language language) throws ExportException {

    writerService.createWriter(outputStream);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeStartElement("hnDict");
    writerService.writeAttribute("updated", getDate());

    Map<String, List<Entry>> entriesByLetter = entries.stream()
        .filter(entry -> !entry.getLemmata().isEmpty())
        .collect(Collectors.groupingBy(entry -> entry.getFirstLetter(language)));

    Collator collator =  huCollator;
    if (Language.NB.equals(language)) {
      collator = nbCollator;
    }

    List<String> sortedEntries = entriesByLetter.keySet().stream()
        .sorted(collator).collect(Collectors.toList());
    for (String letter: sortedEntries) {
      writeEntriesForLetter(letter, entriesByLetter.get(letter));
    }

    writerService.writeEndElement();
    writerService.writeEndDocument();

    writerService.flush();
    writerService.close();

  }

  private void writeEntriesForLetter(String letter, List<Entry> entries)
      throws ExportException {

    writerService.writeStartElement("entryGrp");
    writerService.writeAttribute("head", letter.toUpperCase(Locale.getDefault()));
    List<Entry> sortedEntries = entries.stream()
        .sorted(Comparator.comparing(Entry::getSortKey))
        .collect(Collectors.toList());
    for (Entry entry: sortedEntries) {
      writeEntry(entry);
    }

    writerService.writeEndElement();

  }

  private void writeEntry(Entry entry) throws ExportException {

    writerService.writeStartElement("entry");
    writerService.writeAttribute("id", String.valueOf(entry.getId()));

    writerService.writeStartElement("formGrp");
    for (int i = 0; i < entry.getLemmata().size(); i++) {
      writeLemma(entry, i);
    }
    writerService.writeEndElement();

    writeTranslation(entry.getTranslation());

    writerService.writeEndElement();

  }

  private void writeLemma(Entry entry, int lemmaNumber)
      throws ExportException {

    writerService.writeStartElement("form");
    if (lemmaNumber == 0) {
      writerService.writeAttribute("primary", "yes");
    } else {
      writerService.writeAttribute("primary", "no");
    }

    Lemma lemma = entry.getLemmata().get(lemmaNumber);
    writerService.writeStartElement("orth");
    if (maxByForm.get(lemma.getGrunnform()) == 1) {
      writerService.writeAttribute("n", "0");
    } else {
      if (counterByForm.get(lemma.getGrunnform()) == null) {
        writerService.writeAttribute("n", "1");
        counterByForm.put(lemma.getGrunnform(), 1);
      } else {
        int counterOfCurrent = counterByForm.get(lemma.getGrunnform()) + 1;
        writerService.writeAttribute("n", String.valueOf(counterOfCurrent));
        counterByForm.put(lemma.getGrunnform(), counterOfCurrent);
      }
    }
    writerService.writeCharacters(lemma.getGrunnform());
    writerService.writeEndElement();

    if (lemmaNumber == 0) {
      writerService.writeStartElement("pos");
      writerService.writeCharacters(entry.getPos());
      writerService.writeEndElement();
    }

    String codes = Inflections.getCodes(lemma.getParadigmeId());
    if (codes != null) {
      writerService.writeStartElement("inflCode");
      writerService.writeAttribute("type", "bob");
      writerService.writeCharacters(codes);
      writerService.writeEndElement();
    }

    String suffixes = Inflections.getSuffixes(lemma.getParadigmeId());
    if (suffixes != null) {
      writerService.writeStartElement("inflCode");
      writerService.writeAttribute("type", "suff");
      writerService.writeCharacters(suffixes);
      writerService.writeEndElement();
    }

    List<Integer> lemmaIdList = inflectedLemmaByEntry.get(lemma.getId());
    if (lemmaIdList == null || !lemmaIdList.contains(lemma.getId())) {
      writeInflectionParadigms(entry, lemma);
    }

    writerService.writeEndElement();

  }

  private void writeInflectionParadigms(
      Entry entry, Lemma lemma) throws ExportException {

    int[] ids = getInflectedFormIds(entry);

    if (ids != null && !lemma.getParadigmeId().isEmpty()) {

      List<List<String>> uniqueFormSet = new ArrayList<>();

      for (int i = 0; i < lemma.getParadigmeId().size(); i++) {
        String paradigmeId = lemma.getParadigmeId().get(i);
        Map<Integer, String> patternMap = patterns.get(paradigmeId);
        if (patternMap != null) {
          Map<Integer, String> inflForms =
              Inflections.getInflections(lemma.getGrunnform(), patternMap);
          List<String> formSet = getInflectedFormsForPos(ids, inflForms);
          if (!uniqueFormSet.contains(formSet)) {
            uniqueFormSet.add(formSet);
          }
        }
      }

      for (int i = 0; i < uniqueFormSet.size(); i++) {
        List<String> formSet = uniqueFormSet.get(i);
        if (!formSet.isEmpty()) {
          writerService.writeStartElement("inflPar");
          for (int j = 0; j < formSet.size(); j++) {
            writerService.writeStartElement("inflSeq");
            writerService.writeAttribute("form", i + "-" + j);
            writerService.writeCharacters(formSet.get(j));
            writerService.writeEndElement();
          }
          writerService.writeEndElement();
        }
      }

    }

  }

  private void writeTranslation(String translation)
      throws ExportException {
    Document document = parserService.parse("<translation"
        + " xmlns=\"http://dict.hunnor.net\""
        + ">" + translation + "</translation>");
    Node documentNode = document.getDocumentElement();
    NodeList nodeList = documentNode.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      writeTranslationNode(nodeList.item(i));
    }
  }

  private void writeTranslationNode(Node node) throws ExportException {
    switch (node.getNodeType()) {
      case Node.ELEMENT_NODE:
        Element element = (Element) node;
        writerService.writeStartElement(element.getTagName());
        if (element.hasAttributes()) {
          NamedNodeMap attrNodes = element.getAttributes();
          for (int i = 0; i < attrNodes.getLength(); i++) {
            Attr attr = (Attr) attrNodes.item(i);
            writerService.writeAttribute(attr.getName(), attr.getValue());
          }
        }
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
          writeTranslationNode(nodeList.item(i));
        }
        writerService.writeEndElement();
        break;
      case Node.TEXT_NODE:
        writerService.writeCharacters(node.getTextContent());
        break;
      default:
        break;
    }
  }

  private int[] getInflectedFormIds(Entry entry) {

    int[] ids = null;

    switch (entry.getPos()) {
      case "adj":
        ids = new int[] {4, 3, 2};
        break;
      case "subst":
        ids = new int[] {2, 3, 4};
        break;
      case "verb":
        ids = new int[] {2, 4, 5};
        break;
      default:
        break;
    }

    return ids;

  }

  private List<String> getInflectedFormsForPos(
      int[] ids, Map<Integer, String> forms) {

    List<String> formSet = new ArrayList<>();
    for (int i = 0; i < ids.length; i++) {
      String form = forms.get(ids[i]);
      if (form != null) {
        formSet.add(form);
      }
    }

    return formSet;

  }

  private String getDate() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(Calendar.getInstance().getTime());
  }

}
