package net.hunnor.dict.admin.edit;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

@Service
public class EditServiceImpl implements EditService {

  private static final Logger logger = LoggerFactory.getLogger(EditServiceImpl.class);

  private static final String DEFAULT_TRANS = "<senseGrp>\n"
      + "  <sense>\n"
      + "    <trans></trans>\n"
      + "  </sense>\n"
      + "</senseGrp>\n";

  private static final String MISSING_ENTRY_ERROR =
      "A(z) '%s' nyelvben nincs '%s' azonosítójú szócikk.";

  private static final Map<String, String> LETTERS_HU = new HashMap<>();

  static {
    LETTERS_HU.put("a", "(orth LIKE 'a%' OR orth LIKE 'á%')");
    LETTERS_HU.put("b", "orth LIKE 'b%'");
    LETTERS_HU.put("c", "(orth LIKE 'c%' AND orth NOT LIKE 'cs%')");
    LETTERS_HU.put("cs", "orth LIKE 'cs%'");
    LETTERS_HU.put("d", "(orth LIKE 'd%' AND orth NOT LIKE 'dz%')");
    LETTERS_HU.put("dz", "(orth LIKE 'dz%' AND orth NOT LIKE 'dzs%')");
    LETTERS_HU.put("dzs", "orth LIKE 'dzs%'");
    LETTERS_HU.put("e", "(orth LIKE 'e%' OR orth LIKE 'é%')");
    LETTERS_HU.put("f", "orth LIKE 'f%'");
    LETTERS_HU.put("g", "(orth LIKE 'g%' AND orth NOT LIKE 'gy%')");
    LETTERS_HU.put("gy", "orth LIKE 'gy%'");
    LETTERS_HU.put("h", "orth LIKE 'h%'");
    LETTERS_HU.put("i", "(orth LIKE 'i%' OR orth LIKE 'í%')");
    LETTERS_HU.put("j", "orth LIKE 'j%'");
    LETTERS_HU.put("k", "orth LIKE 'k%'");
    LETTERS_HU.put("l", "(orth LIKE 'l%' AND orth NOT LIKE 'ly%')");
    LETTERS_HU.put("ly", "orth LIKE 'ly%'");
    LETTERS_HU.put("m", "orth LIKE 'm%'");
    LETTERS_HU.put("n", "(orth LIKE 'n%' AND orth NOT LIKE 'ny%')");
    LETTERS_HU.put("ny", "orth LIKE 'ny%'");
    LETTERS_HU.put("o", "(orth LIKE 'o%' OR orth LIKE 'ó%')");
    LETTERS_HU.put("oe", "(orth LIKE 'ö%' OR orth LIKE 'ő%')");
    LETTERS_HU.put("p", "orth LIKE 'p%'");
    LETTERS_HU.put("q", "orth LIKE 'q%'");
    LETTERS_HU.put("r", "orth LIKE 'r%'");
    LETTERS_HU.put("s", "(orth LIKE 's%' AND orth NOT LIKE 'sz%')");
    LETTERS_HU.put("sz", "orth LIKE 'sz%'");
    LETTERS_HU.put("t", "(orth LIKE 't%' AND orth NOT LIKE 'ty%')");
    LETTERS_HU.put("ty", "orth LIKE 'ty%'");
    LETTERS_HU.put("u", "(orth LIKE 'u%' OR orth LIKE 'ú%')");
    LETTERS_HU.put("ue", "(orth LIKE 'ü%' OR orth LIKE 'ű%')");
    LETTERS_HU.put("v", "orth LIKE 'v%'");
    LETTERS_HU.put("w", "orth LIKE 'w%'");
    LETTERS_HU.put("x", "orth LIKE 'x%'");
    LETTERS_HU.put("y", "orth LIKE 'y%'");
    LETTERS_HU.put("z", "(orth LIKE 'z%' AND orth NOT LIKE 'zs%')");
    LETTERS_HU.put("zs", "orth LIKE 'zs%'");
  }

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private LegacyExportService legacyExportService;

  @Autowired
  private SolrService solrService;

  /**
   * Lists entries by language and the provided filters.
   *
   * @param language the language code
   * @param letter first-letter filter
   * @param term term prefix filter
   * @param translatedOnly true if only entries with translation should be returned
   * @return matching legacy entries
   */
  @Override
  public List<LegacyEntry> list(
      String language, String letter, String term, boolean translatedOnly) {
    String normalizedLanguage = normalizeLanguage(language);
    TableNames tableNames = tableNames(normalizedLanguage);

    StringBuilder sql = new StringBuilder("SELECT DISTINCT id FROM ")
        .append(tableNames.formsTable)
        .append(" WHERE ");

    List<Object> params = new ArrayList<>();
    if (StringUtils.hasText(term)) {
      sql.append("orth LIKE ?");
      params.add(term.trim() + "%");
    } else {
      String letterCondition = letterCondition(normalizedLanguage, letter);
      if (!StringUtils.hasText(letterCondition)) {
        throw new IllegalArgumentException("Missing list query.");
      }
      sql.append(letterCondition);
    }

    if (translatedOnly) {
      sql.append(" AND status > 0");
    }
    sql.append(" AND seq = 1 ORDER BY orth");

    String listSql = Objects.requireNonNull(sql.toString());
    List<Integer> ids = jdbcTemplate.query(
        listSql,
        (resultSet, rowNum) -> resultSet.getInt("id"),
        params.toArray());
    List<LegacyEntry> entries = new ArrayList<>();
    for (Integer id : ids) {
      LegacyEntry entry = loadEntry(normalizedLanguage, String.valueOf(id), false, null);
      if (entry.isExists()) {
        entries.add(entry);
      }
    }
    return entries;
  }

  /**
   * Loads one entry for editing.
   *
   * @param language the language code
   * @param id the entry id or N for a new entry
   * @param term optional initial term when id is N
   * @return the loaded legacy entry
   */
  @Override
  public LegacyEntry entry(String language, String id, String term) {
    String normalizedLanguage = normalizeLanguage(language);
    return loadEntry(normalizedLanguage, id, true, term);
  }

  /**
   * Saves an entry against the old schema tables.
   *
   * @param language the language code
   * @param id the entry id or N for a new entry
   * @param entryValue the entry group identifier
   * @param formsYaml form data in YAML format
   * @param pos part-of-speech code
   * @param translation translation XML content
   * @param status translation status value
   * @param editorId editor identifier used in edit logs
   * @return save result containing errors and generated SQL
   */
  @Override
  public SaveResult save(
      String language,
      String id,
      String entryValue,
      String formsYaml,
      String pos,
      String translation,
      String status,
      String editorId) {

    logger.debug("EditService.save requested: "
        + "language='{}', id='{}', entryValue='{}', pos='{}', status='{}', editorId='{}'",
        language,
        id,
        entryValue,
        pos,
        status,
        editorId);

    String normalizedLanguage = normalizeLanguage(language);
    SaveResult result = new SaveResult();
    LegacyEntry current = loadEntry(normalizedLanguage, id, true, null);

    if (!current.isExists()) {
      logger.debug("EditService.save aborted: entry does not exist for language='{}', id='{}'",
          normalizedLanguage, id);
      result.addError(String.format(MISSING_ENTRY_ERROR, normalizedLanguage, id));
      return result;
    }

    if (!acceptsPos(normalizedLanguage, pos)) {
      logger.debug("EditService.save aborted: invalid part-of-speech '{}' for language='{}'",
          pos, normalizedLanguage);
      result.addError(
          "A(z) '" + normalizedLanguage + "' nyelvben nincs '" + pos + "' szófaj.");
      return result;
    }

    if (!acceptsStatus(status)) {
      logger.debug("EditService.save aborted: invalid status='{}'", status);
      result.addError("Érvénytelen státusz: '" + status + "'.");
      return result;
    }

    TableNames tableNames = tableNames(normalizedLanguage);
    Integer entryGroup = parseEntryGroup(entryValue, id);
    if (StringUtils.hasText(entryValue)
        && !acceptsEntry(normalizedLanguage, tableNames, entryGroup)) {
      logger.debug("EditService.save aborted: "
          + "invalid entry reference. language='{}', id='{}', entryValue='{}', parsedEntryGroup={}",
          normalizedLanguage,
          id,
          entryValue,
          entryGroup);
      result.addError(
          "A(z) '" + normalizedLanguage + "' nyelvben nem hivatkozhatsz '"
              + entryValue
              + "' azonosítójú szócikkre.");
      return result;
    }

    LinkedHashMap<String, LinkedHashMap<Integer, String>> newForms;
    try {
      newForms = parseForms(formsYaml);
    } catch (IllegalArgumentException ex) {
      logger.debug("EditService.save aborted: forms YAML validation failed: {}", ex.getMessage());
      result.addError(ex.getMessage());
      return result;
    }

    List<String> translationErrors = validateTrans(normalizedLanguage, translation);
    if (!translationErrors.isEmpty()) {
      logger.debug("EditService.save aborted: "
          + "translation validation failed with {} error(s)", translationErrors.size());
      result.addErrors(translationErrors);
      return result;
    }

    List<SqlCommand> commands = new ArrayList<>();
    String action;
    String effectiveId = id;
    Integer solrEntryGroup;
    Integer statusValue = Integer.valueOf(status);

    if ("N".equals(id)) {
      action = "insert";
      Integer maxId = jdbcTemplate.queryForObject(
          "SELECT MAX(id) FROM " + tableNames.formsTable,
          Integer.class);
      int newId = maxId == null ? 1 : maxId + 1;
      effectiveId = String.valueOf(newId);
      Integer insertEntryGroup = StringUtils.hasText(entryValue) ? entryGroup : newId;
      solrEntryGroup = insertEntryGroup;
      addInsertForms(
          commands,
          tableNames,
          effectiveId,
          insertEntryGroup,
          newForms,
          pos,
          statusValue);
      result.setAddition(effectiveId);
    } else {
      action = "update";
      solrEntryGroup = entryGroup;
      addUpdateForms(
          commands,
          tableNames,
          current,
          effectiveId,
          entryGroup,
          pos,
          statusValue,
          newForms);
    }

    if (translationChanged(current.getTrans(), translation)) {
      boolean hasTranslation = existsTranslation(tableNames, effectiveId);
      if (hasTranslation) {
        commands.add(sqlCommand(
            "UPDATE " + tableNames.transTable + " SET trans = ? WHERE id = ?",
            translation,
            effectiveId));
      } else {
        commands.add(sqlCommand(
            "INSERT INTO " + tableNames.transTable + " (id, trans) VALUES (?, ?)",
            effectiveId,
            translation));
      }
    }

    String safeEditorId = editorId != null && editorId.matches("\\d+") ? editorId : "0";
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
        .format(new Timestamp(System.currentTimeMillis()));
    commands.add(sqlCommand(
        "INSERT INTO hn_log_edit (editor_id, lang, entry_id, action, timestamp)"
            + " VALUES (?, ?, ?, ?, ?)",
        safeEditorId,
        normalizedLanguage,
        effectiveId,
        action,
        timestamp));

    for (SqlCommand command : commands) {
      String sqlQuery = Objects.requireNonNull(command.sql);
      jdbcTemplate.update(sqlQuery, command.params);
      result.addSql(command.rendered);
    }

    String xml = legacyExportService.exportEntryXml(normalizedLanguage, effectiveId);
    logger.debug("EditService.save invoking SolrService.save: "
        + "language='{}', effectiveId='{}', solrEntryGroup={}, xmlLength={}",
        normalizedLanguage,
        effectiveId,
        solrEntryGroup,
        xml == null ? 0 : xml.length());
    solrService.save(normalizedLanguage, effectiveId, solrEntryGroup, xml);
    logger.debug("EditService.save completed SolrService.save for effectiveId='{}'", effectiveId);

    result.setSuccess(true);
    logger.debug("EditService.save completed successfully: "
        + "language='{}', effectiveId='{}', action='{}'",
        normalizedLanguage, effectiveId, action);
    return result;
  }

  /**
   * Deletes an entry if there are no cross-entry references.
   *
   * @param language the language code
   * @param id the entry identifier
   * @return delete result with executed SQL or error message
   */
  @Override
  public DeleteResult delete(String language, String id) {
    String normalizedLanguage = normalizeLanguage(language);
    TableNames tableNames = tableNames(normalizedLanguage);
    DeleteResult result = new DeleteResult();

    LegacyEntry current = loadEntry(normalizedLanguage, id, false, null);
    if (!current.isExists()) {
      result.setError("A megadott szócikk (" + normalizedLanguage + ", " + id + ") nem létezik.");
      return result;
    }

    List<String> refs = referrers(tableNames, id);
    if (!refs.isEmpty()) {
      result.setError(
          "A megadott szócikk nem tötölhető, mert a következő szócikkek hivatkoznak rá: "
              + String.join(", ", refs));
      return result;
    }

    List<SqlCommand> commands = new ArrayList<>();
    commands.add(sqlCommand("DELETE FROM " + tableNames.formsTable + " WHERE id = ?", id));
    commands.add(sqlCommand("DELETE FROM " + tableNames.transTable + " WHERE id = ?", id));

    for (SqlCommand command : commands) {
      String sqlQuery = Objects.requireNonNull(command.sql);
      jdbcTemplate.update(sqlQuery, command.params);
      result.addSql(command.rendered);
    }

    solrService.delete(normalizedLanguage, id, current.getEntry());

    result.setDeleted(true);
    return result;
  }

  private LegacyEntry loadEntry(String language, String id, boolean withTranslation, String term) {
    LegacyEntry entry = new LegacyEntry();
    entry.setLang(language);
    entry.setId(id);

    if ("N".equals(id)) {
      entry.setExists(true);
      entry.setStatus(0);
      entry.setTrans(DEFAULT_TRANS);
      String initialForm = StringUtils.hasText(term) ? term.trim() : "szótári_alak";
      entry.putForm("0", 1, initialForm);
      entry.setForm(initialForm);
      return entry;
    }

    TableNames tableNames = tableNames(language);
    Integer numericId = parseId(id);
    SqlRowSet rows = jdbcTemplate.queryForRowSet(
        "SELECT id, entry, orth, pos, par, seq, status FROM " + tableNames.formsTable
            + " WHERE id = ? ORDER BY par, seq",
        numericId);

    boolean firstRow = true;
    while (rows.next()) {
      entry.setExists(true);
      String orth = rows.getString("orth");
      String par = rows.getString("par");
      Integer seq = rows.getInt("seq");

      entry.putForm(par, seq, orth);
      if (firstRow) {
        entry.setForm(orth);
        entry.setEntry(rows.getInt("entry"));
        entry.setPos(rows.getString("pos"));
        entry.setStatus(rows.getInt("status"));
        firstRow = false;
      }
    }

    if (!entry.isExists()) {
      return entry;
    }

    if (withTranslation) {
      String trans = DEFAULT_TRANS;
      SqlRowSet transRows = jdbcTemplate.queryForRowSet(
          "SELECT trans FROM " + tableNames.transTable + " WHERE id = ?",
          numericId);
      while (transRows.next()) {
        trans = transRows.getString("trans");
      }
      entry.setTrans(trans);
    }

    return entry;
  }

  private void addInsertForms(
      List<SqlCommand> commands,
      TableNames tableNames,
      String id,
      Integer entryGroup,
      LinkedHashMap<String, LinkedHashMap<Integer, String>> forms,
      String pos,
      Integer status) {
    for (Map.Entry<String, LinkedHashMap<Integer, String>> parEntry : forms.entrySet()) {
      for (Map.Entry<Integer, String> seqEntry : parEntry.getValue().entrySet()) {
        commands.add(sqlCommand(
            "INSERT INTO " + tableNames.formsTable
                + " (id, entry, orth, pos, par, seq, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
            id,
            entryGroup,
            seqEntry.getValue(),
            pos,
            parEntry.getKey(),
            seqEntry.getKey(),
            status));
      }
    }
  }

  private void addUpdateForms(
      List<SqlCommand> commands,
      TableNames tableNames,
      LegacyEntry current,
      String id,
      Integer entryGroup,
      String pos,
      Integer status,
      LinkedHashMap<String, LinkedHashMap<Integer, String>> newForms) {

    List<String> changes = new ArrayList<>();
    List<Object> changeParams = new ArrayList<>();

    Integer currentEntryGroup = current.getEntry() == null
        ? Integer.valueOf(0) : current.getEntry();
    if (!currentEntryGroup.equals(entryGroup)) {
      changes.add("entry = ?");
      changeParams.add(entryGroup);
    }
    if (!stringEquals(current.getPos(), pos)) {
      changes.add("pos = ?");
      changeParams.add(pos);
    }
    if (!integerEquals(current.getStatus(), status)) {
      changes.add("status = ?");
      changeParams.add(status);
    }

    if (!changes.isEmpty()) {
      String updateSql = "UPDATE " + tableNames.formsTable + " SET " + String.join(", ", changes)
          + " WHERE id = ?";
      changeParams.add(id);
      commands.add(sqlCommand(updateSql, changeParams.toArray()));
    }

    Set<String> oldTuples = formsToTupleSet(current.getForms());
    Set<String> newTuples = formsToTupleSet(newForms);

    Set<String> removed = new LinkedHashSet<>(oldTuples);
    removed.removeAll(newTuples);
    for (String tuple : removed) {
      Tuple parsed = Tuple.parse(tuple);
      commands.add(sqlCommand(
          "DELETE FROM " + tableNames.formsTable
              + " WHERE id = ? AND par = ? AND seq = ? AND orth = ?",
          id,
          parsed.par,
          parsed.seq,
          parsed.orth));
    }

    Set<String> added = new LinkedHashSet<>(newTuples);
    added.removeAll(oldTuples);
    for (String tuple : added) {
      Tuple parsed = Tuple.parse(tuple);
      commands.add(sqlCommand(
          "INSERT INTO " + tableNames.formsTable
              + " (id, entry, orth, pos, par, seq, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
          id,
          entryGroup,
          parsed.orth,
          pos,
          parsed.par,
          parsed.seq,
          status));
    }
  }

  private Set<String> formsToTupleSet(LinkedHashMap<String, LinkedHashMap<Integer, String>> forms) {
    Set<String> tuples = new LinkedHashSet<>();
    for (Map.Entry<String, LinkedHashMap<Integer, String>> parEntry : forms.entrySet()) {
      for (Map.Entry<Integer, String> seqEntry : parEntry.getValue().entrySet()) {
        tuples.add(parEntry.getKey() + ";" + seqEntry.getKey() + ";" + seqEntry.getValue());
      }
    }
    return tuples;
  }

  private LinkedHashMap<String, LinkedHashMap<Integer, String>> parseForms(String formsYaml) {
    if (!StringUtils.hasText(formsYaml)) {
      throw new IllegalArgumentException("A formák nem lehetnek üresek.");
    }

    Object loaded = new Yaml().load(formsYaml);
    if (!(loaded instanceof Map)) {
      throw new IllegalArgumentException("Hibás YAML a Formák mezőben.");
    }

    LinkedHashMap<String, LinkedHashMap<Integer, String>> forms = new LinkedHashMap<>();
    Map<?, ?> root = (Map<?, ?>) loaded;
    for (Map.Entry<?, ?> parEntry : root.entrySet()) {
      String parKey = String.valueOf(parEntry.getKey());
      Object parValue = parEntry.getValue();
      if (!(parValue instanceof Map)) {
        throw new IllegalArgumentException("Hibás YAML a Formák mezőben.");
      }
      LinkedHashMap<Integer, String> seqMap = new LinkedHashMap<>();
      Map<?, ?> seqRoot = (Map<?, ?>) parValue;
      for (Map.Entry<?, ?> seqEntry : seqRoot.entrySet()) {
        Integer seqKey;
        try {
          seqKey = Integer.valueOf(String.valueOf(seqEntry.getKey()));
        } catch (NumberFormatException ex) {
          throw new IllegalArgumentException("Hibás YAML a Formák mezőben.");
        }
        String orth = seqEntry.getValue() == null ? "" : String.valueOf(seqEntry.getValue());
        seqMap.put(seqKey, orth);
      }
      forms.put(parKey, seqMap);
    }

    if (forms.isEmpty()) {
      throw new IllegalArgumentException("A formák nem lehetnek üresek.");
    }

    sortForms(forms);
    return forms;
  }

  private void sortForms(LinkedHashMap<String, LinkedHashMap<Integer, String>> forms) {
    List<String> parKeys = new ArrayList<>(forms.keySet());
    Collections.sort(parKeys, Comparator.naturalOrder());

    LinkedHashMap<String, LinkedHashMap<Integer, String>> sorted = new LinkedHashMap<>();
    for (String parKey : parKeys) {
      LinkedHashMap<Integer, String> seqMap = forms.get(parKey);
      List<Integer> seqKeys = new ArrayList<>(seqMap.keySet());
      Collections.sort(seqKeys, Comparator.naturalOrder());
      LinkedHashMap<Integer, String> sortedSeq = new LinkedHashMap<>();
      for (Integer seqKey : seqKeys) {
        sortedSeq.put(seqKey, seqMap.get(seqKey));
      }
      sorted.put(parKey, sortedSeq);
    }

    forms.clear();
    forms.putAll(sorted);
  }

  private List<String> validateTrans(String language, String trans) {
    List<String> errors = new ArrayList<>();
    if (trans == null) {
      errors.add("Translation is NIL.");
      return errors;
    }

    String wrapped = "<hnDict updated=\"1970-01-01\" xmlns=\"http://dict.hunnor.net\">"
        + "<entryGrp head=\"A\"><entry id=\"1\"><formGrp><form><orth n=\"1\">A</orth>"
        + "</form></formGrp>"
        + trans
        + "</entry></entryGrp></hnDict>";

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.parse(new InputSource(new StringReader(wrapped)));
    } catch (ParserConfigurationException | SAXException | IOException e) {
      errors.add(e.getMessage());
      return errors;
    }

    try {
      Resource schemaResource = resourceLoader.getResource("classpath:"
          + ("hu".equals(language) ? "hunnor.net.Schema.HN.xsd" : "hunnor.net.Schema.NH.xsd"));
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      javax.xml.validation.Schema schema = schemaFactory.newSchema(schemaResource.getURL());
      javax.xml.validation.Validator validator = schema.newValidator();
      validator.validate(new StreamSource(new StringReader(wrapped)));
    } catch (SAXException | IOException e) {
      errors.add(e.getMessage());
    }
    return errors;
  }

  private boolean existsTranslation(TableNames tableNames, String id) {
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM " + tableNames.transTable + " WHERE id = ?",
        Integer.class,
        parseId(id));
    return count != null && count > 0;
  }

  private List<String> referrers(TableNames tableNames, String id) {
    List<String> refs = new ArrayList<>();
    SqlRowSet rows = jdbcTemplate.queryForRowSet(
        "SELECT DISTINCT id, entry, orth FROM " + tableNames.formsTable
            + " WHERE entry = ? AND id <> ? AND seq = 1",
        parseId(id),
        parseId(id));
    while (rows.next()) {
      refs.add(rows.getString("orth") + " (" + rows.getString("id") + ")");
    }
    return refs;
  }

  private boolean acceptsEntry(String language, TableNames tableNames, Integer entryId) {
    if (entryId == null) {
      return false;
    }
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM " + tableNames.formsTable + " WHERE entry = ?",
        Integer.class,
        entryId);
    logger.debug("Entry validation for language {} and id {}: {}", language, entryId, count);
    return count != null && count > 0;
  }

  private boolean acceptsPos(String language, String pos) {
    if ("hu".equals(language)) {
      return List.of(
          "ige", "fn", "hsz", "nével", "röv", "névm", "mn", "ksz", "módsz", "névut", "isz",
          "szn", "igek").contains(pos);
    }
    return List.of(
        "subst", "verb", "adj", "adv", "prep", "pron", "tall", "konj", "subj", "interj",
        "fork", "inf", "art").contains(pos);
  }

  private boolean acceptsStatus(String status) {
    return List.of("0", "1", "2").contains(status);
  }

  private String normalizeLanguage(String language) {
    if (language == null) {
      throw new IllegalArgumentException("Invalid language.");
    }
    String normalized = language.toLowerCase(Locale.ROOT);
    if (!"hu".equals(normalized) && !"nb".equals(normalized)) {
      throw new IllegalArgumentException("Invalid language.");
    }
    return normalized;
  }

  private String letterCondition(String language, String letter) {
    if (!StringUtils.hasText(letter)) {
      return null;
    }
    String normalizedLetter = letter.toLowerCase(Locale.ROOT);
    if ("hu".equals(language)) {
      return LETTERS_HU.get(normalizedLetter);
    }
    return "orth LIKE '" + normalizedLetter + "%'";
  }

  private TableNames tableNames(String language) {
    if ("hu".equals(language)) {
      return new TableNames("hn_hun_segment", "hn_hun_tr_nob_tmp");
    }
    return new TableNames("hn_nob_segment", "hn_nob_tr_hun_tmp");
  }

  private Integer parseId(String id) {
    if (!StringUtils.hasText(id)) {
      throw new IllegalArgumentException("Invalid id.");
    }
    try {
      Integer parsed = Integer.valueOf(id);
      if (parsed < 1) {
        throw new IllegalArgumentException("Invalid id.");
      }
      return parsed;
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid id.");
    }
  }

  private Integer parseEntryGroup(String entryValue, String id) {
    if (!StringUtils.hasText(entryValue)) {
      return "N".equals(id) ? null : 0;
    }
    try {
      Integer parsed = Integer.valueOf(entryValue);
      if (parsed < 1) {
        throw new IllegalArgumentException("Invalid entry group.");
      }
      return parsed;
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid entry group.");
    }
  }

  private boolean translationChanged(String current, String input) {
    return !stringEquals(current, input);
  }

  private boolean integerEquals(Integer left, Integer right) {
    if (left == null && right == null) {
      return true;
    }
    if (left == null || right == null) {
      return false;
    }
    return left.equals(right);
  }

  private boolean stringEquals(String left, String right) {
    if (left == null && right == null) {
      return true;
    }
    if (left == null || right == null) {
      return false;
    }
    return left.equals(right);
  }

  private SqlCommand sqlCommand(String sql, Object... params) {
    return new SqlCommand(sql, params, renderSql(sql, params));
  }

  private String renderSql(String sql, Object... params) {
    String rendered = sql;
    for (Object param : params) {
      String replacement;
      if (param == null) {
        replacement = "NULL";
      } else if (param instanceof Number) {
        replacement = String.valueOf(param);
      } else {
        replacement = "'" + String.valueOf(param).replace("'", "''") + "'";
      }
      rendered = rendered.replaceFirst("\\?", replacement);
    }
    return rendered;
  }

  private static final class TableNames {

    private final String formsTable;

    private final String transTable;

    private TableNames(String formsTable, String transTable) {
      this.formsTable = formsTable;
      this.transTable = transTable;
    }
  }

  private static final class SqlCommand {

    private final String sql;

    private final Object[] params;

    private final String rendered;

    private SqlCommand(String sql, Object[] params, String rendered) {
      this.sql = sql;
      this.params = params;
      this.rendered = rendered;
    }
  }

  private static final class Tuple {

    private final String par;

    private final Integer seq;

    private final String orth;

    private Tuple(String par, Integer seq, String orth) {
      this.par = par;
      this.seq = seq;
      this.orth = orth;
    }

    private static Tuple parse(String tuple) {
      String[] split = tuple.split(";", 3);
      return new Tuple(split[0], Integer.valueOf(split[1]), split[2]);
    }
  }

}
