package net.hunnor.dict.admin.edit;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.hunnor.dict.admin.inflection.InflectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class LegacyExportServiceImpl implements LegacyExportService {

  private static final String DICT_NAMESPACE = "http://dict.hunnor.net";

  private static final String DEFAULT_TRANS = "<senseGrp>\n"
      + "  <sense>\n"
      + "    <trans></trans>\n"
      + "  </sense>\n"
      + "</senseGrp>\n";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private InflectionService inflectionService;

  @Override
  public String exportEntryXml(String language, String id) {

    Document document = createDocument();
    Element root = document.createElementNS(DICT_NAMESPACE, "hnDict");
    document.appendChild(root);

    Element entryGroup = document.createElementNS(DICT_NAMESPACE, "entryGrp");
    root.appendChild(entryGroup);

    String normalizedLanguage = normalizeLanguage(language);
    EntryData entryData = loadEntry(normalizedLanguage, id);
    Element entry = document.createElementNS(DICT_NAMESPACE, "entry");
    entry.setAttribute("id", entryData.id);
    entryGroup.appendChild(entry);

    Element formGroup = document.createElementNS(DICT_NAMESPACE, "formGrp");
    entry.appendChild(formGroup);

    appendForm(document, formGroup, true, entryData.pos, entryData.forms, entryData.primaryOrth);

    List<String> additionalIds = loadAdditionalIds(normalizedLanguage, entryData.entryGroup);
    for (String additionalId : additionalIds) {
      EntryData additional = loadEntry(normalizedLanguage, additionalId);
      appendForm(
          document,
          formGroup,
          false,
          entryData.pos,
          additional.forms,
          additional.primaryOrth);
    }

    appendTranslations(document, entry, entryData.translation);
    return toXml(document);
  }

  private void appendForm(
      Document document,
      Element formGroup,
      boolean primary,
      String pos,
      LinkedHashMap<String, LinkedHashMap<Integer, String>> forms,
      String orthValue) {
    Element form = document.createElementNS(DICT_NAMESPACE, "form");
    form.setAttribute("primary", primary ? "yes" : "no");
    formGroup.appendChild(form);

    Element orth = document.createElementNS(DICT_NAMESPACE, "orth");
    orth.setTextContent(orthValue);
    form.appendChild(orth);

    if (primary) {
      Element posTag = document.createElementNS(DICT_NAMESPACE, "pos");
      posTag.setTextContent(pos);
      form.appendChild(posTag);
    }

    if ("adj".equals(pos) || "subst".equals(pos) || "verb".equals(pos)) {
      List<String> paradigms = new ArrayList<>(forms.keySet());
      Collections.sort(paradigms);
      String codes = inflectionService.getCodes(paradigms);
      String suffixes = inflectionService.getSuffixes(paradigms);

      if (StringUtils.hasText(codes)) {
        Element inflCode = document.createElementNS(DICT_NAMESPACE, "inflCode");
        inflCode.setAttribute("type", "bob");
        inflCode.setTextContent(codes);
        form.appendChild(inflCode);
      }
      if (StringUtils.hasText(suffixes)) {
        Element inflCode = document.createElementNS(DICT_NAMESPACE, "inflCode");
        inflCode.setAttribute("type", "suff");
        inflCode.setTextContent(suffixes);
        form.appendChild(inflCode);
      }

      List<List<String>> formsArray = getFormsArray(pos, forms);
      for (int formIndex = 0; formIndex < formsArray.size(); formIndex++) {
        List<String> inflected = formsArray.get(formIndex);
        if (inflected.isEmpty()) {
          continue;
        }
        Element inflPar = document.createElementNS(DICT_NAMESPACE, "inflPar");
        form.appendChild(inflPar);
        for (int seqIndex = 0; seqIndex < inflected.size(); seqIndex++) {
          Element inflSeq = document.createElementNS(DICT_NAMESPACE, "inflSeq");
          inflSeq.setAttribute("form", formIndex + "-" + seqIndex);
          inflSeq.setTextContent(inflected.get(seqIndex));
          inflPar.appendChild(inflSeq);
        }
      }
    }
  }

  private List<List<String>> getFormsArray(
      String pos, LinkedHashMap<String, LinkedHashMap<Integer, String>> forms) {
    List<Integer> keys = new ArrayList<>();
    if ("adj".equals(pos)) {
      keys.add(5);
      keys.add(6);
    } else if ("subst".equals(pos)) {
      keys.add(2);
      keys.add(3);
      keys.add(4);
    } else if ("verb".equals(pos)) {
      keys.add(4);
      keys.add(6);
      keys.add(7);
    }

    List<List<String>> values = new ArrayList<>();
    Set<String> uniqueSignatures = new LinkedHashSet<>();
    for (Map<Integer, String> seqMap : forms.values()) {
      List<String> parValues = new ArrayList<>();
      for (Integer key : keys) {
        String value = seqMap.get(key);
        if (StringUtils.hasText(value)) {
          parValues.add(value);
        }
      }
      String signature = String.join("\u0000", parValues);
      if (!uniqueSignatures.contains(signature)) {
        uniqueSignatures.add(signature);
        values.add(parValues);
      }
    }
    return values;
  }

  private void appendTranslations(Document document, Element entry, String translation) {
    String compact = removeSpaces(translation);
    Document translationDocument;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      translationDocument = builder.parse(new InputSource(
          new StringReader("<foo>" + compact + "</foo>")));
    } catch (ParserConfigurationException | SAXException | java.io.IOException ex) {
      throw new IllegalStateException("Failed to parse translation XML", ex);
    }

    Element wrapper = translationDocument.getDocumentElement();
    for (int index = 0; index < wrapper.getChildNodes().getLength(); index++) {
      if (wrapper.getChildNodes().item(index) instanceof Element) {
        Element child = (Element) wrapper.getChildNodes().item(index);
        if ("senseGrp".equals(child.getLocalName()) || "senseGrp".equals(child.getNodeName())) {
          entry.appendChild(importNodeWithNamespace(document, child));
        }
      }
    }
  }

  private Element importNodeWithNamespace(Document targetDocument, Element source) {
    String localName = source.getLocalName() == null ? source.getNodeName() : source.getLocalName();
    Element target = targetDocument.createElementNS(DICT_NAMESPACE, localName);

    for (int index = 0; index < source.getAttributes().getLength(); index++) {
      org.w3c.dom.Node attribute = source.getAttributes().item(index);
      target.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
    }

    for (int index = 0; index < source.getChildNodes().getLength(); index++) {
      org.w3c.dom.Node node = source.getChildNodes().item(index);
      if (node instanceof Element) {
        target.appendChild(importNodeWithNamespace(targetDocument, (Element) node));
      } else {
        target.appendChild(targetDocument.importNode(node, true));
      }
    }

    return target;
  }

  private EntryData loadEntry(String language, String id) {
    String formsTable = formsTable(language);
    LinkedHashMap<String, LinkedHashMap<Integer, String>> forms = new LinkedHashMap<>();

    SqlRowSet rows = jdbcTemplate.queryForRowSet(
        "SELECT id, entry, orth, pos, par, seq FROM " + formsTable
            + " WHERE id = ? ORDER BY par, seq", Integer.valueOf(id));

    String entryGroup = null;
    String pos = null;
    String primaryOrth = null;
    while (rows.next()) {
      if (entryGroup == null) {
        entryGroup = String.valueOf(rows.getInt("entry"));
      }
      if (pos == null) {
        pos = rows.getString("pos");
      }
      if (primaryOrth == null) {
        primaryOrth = rows.getString("orth");
      }
      String par = rows.getString("par");
      Integer seq = rows.getInt("seq");
      String orth = rows.getString("orth");
      forms.computeIfAbsent(par, key -> new LinkedHashMap<>()).put(seq, orth);
    }

    if (entryGroup == null || pos == null || primaryOrth == null) {
      throw new IllegalArgumentException(
          "Entry not found for legacy export: " + language + "/" + id);
    }

    String transTable = transTable(language);
    String translation = DEFAULT_TRANS;
    SqlRowSet transRows = jdbcTemplate.queryForRowSet(
        "SELECT trans FROM " + transTable + " WHERE id = ?",
        Integer.valueOf(id));
    while (transRows.next()) {
      translation = transRows.getString("trans");
    }

    return new EntryData(id, entryGroup, pos, primaryOrth, forms, translation);
  }

  private List<String> loadAdditionalIds(String language, String entryGroup) {
    String formsTable = formsTable(language);
    SqlRowSet rows = jdbcTemplate.queryForRowSet(
        "SELECT DISTINCT id FROM " + formsTable + " WHERE entry = ? ORDER BY orth",
        Integer.valueOf(entryGroup));
    List<String> ids = new ArrayList<>();
    while (rows.next()) {
      String candidate = String.valueOf(rows.getInt("id"));
      if (!candidate.equals(entryGroup)) {
        ids.add(candidate);
      }
    }
    return ids;
  }

  private String formsTable(String language) {
    if ("hu".equals(language)) {
      return "hn_hun_segment";
    }
    return "hn_nob_segment";
  }

  private String transTable(String language) {
    if ("hu".equals(language)) {
      return "hn_hun_tr_nob_tmp";
    }
    return "hn_nob_tr_hun_tmp";
  }

  private String normalizeLanguage(String language) {
    if (!StringUtils.hasText(language)) {
      throw new IllegalArgumentException("Invalid language for legacy export");
    }
    String normalized = language.toLowerCase(Locale.ROOT);
    if (!"hu".equals(normalized) && !"nb".equals(normalized)) {
      throw new IllegalArgumentException("Invalid language for legacy export");
    }
    return normalized;
  }

  private Document createDocument() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.newDocument();
    } catch (ParserConfigurationException ex) {
      throw new IllegalStateException("Failed to create XML document", ex);
    }
  }

  private String toXml(Document document) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(document), new StreamResult(writer));
      return writer.toString();
    } catch (TransformerException ex) {
      throw new IllegalStateException("Failed to serialize XML", ex);
    }
  }

  private String removeSpaces(String value) {
    if (value == null) {
      return "";
    }
    return value.replaceAll(">[ \\n\\r\\t]+<", "><");
  }

  private static final class EntryData {

    private final String id;

    private final String entryGroup;

    private final String pos;

    private final String primaryOrth;

    private final LinkedHashMap<String, LinkedHashMap<Integer, String>> forms;

    private final String translation;

    private EntryData(
        String id,
        String entryGroup,
        String pos,
        String primaryOrth,
        LinkedHashMap<String, LinkedHashMap<Integer, String>> forms,
        String translation) {
      this.id = id;
      this.entryGroup = entryGroup;
      this.pos = pos;
      this.primaryOrth = primaryOrth;
      this.forms = forms;
      this.translation = translation;
    }
  }

}
