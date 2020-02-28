package net.hunnor.dict.admin.migrate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.hunnor.dict.admin.model.Entry;
import net.hunnor.dict.admin.model.Lemma;
import net.hunnor.dict.admin.util.Mappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MigrationServiceImpl implements MigrationService {

  private static final Logger logger = LoggerFactory.getLogger(MigrationServiceImpl.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private Map<String, List<Lemma>> ordbankByGrunnformAndParadigme = new HashMap<>();

  @Override
  public void migrate() throws MigrationException {

    clearTables();

    loadOrdbank();

    loadHunNor();

  }

  private void clearTables() {
    String[] tables = new String[] {"HN_HU_ENTRY", "HN_HU_LEMMA", "HN_HU_ENTRY_LEMMA",
        "HN_NB_ENTRY", "HN_NB_LEMMA", "HN_NB_ENTRY_LEMMA", "HN_NB_LEMMA_PARADIGME"};
    Arrays.stream(tables).forEach(table -> {
      String sql = "TRUNCATE TABLE " + table;
      jdbcTemplate.execute(sql);
    });
  }

  private void loadOrdbank() {

    List<Lemma> ordbank = new ArrayList<>();

    SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT LEMMA.LEMMA_ID, LEMMA.GRUNNFORM,"
        + " GROUP_CONCAT(PARADIGME_ID ORDER BY PARADIGME_ID) AS par"
        + " FROM LEMMA LEFT JOIN LEMMA_PARADIGME ON LEMMA.LEMMA_ID = LEMMA_PARADIGME.LEMMA_ID"
        + " GROUP BY LEMMA.LEMMA_ID");

    while (rowSet.next()) {

      int lemmaId = rowSet.getInt(1);
      String grunnform = rowSet.getString(2);
      List<String> paradigmeId = null;

      String parStr = rowSet.getString(3);
      if (!StringUtils.isEmpty(parStr)) {
        paradigmeId = Arrays.stream(parStr.split(",")).collect(Collectors.toList());
      }

      Lemma lemma = new Lemma(lemmaId, grunnform, paradigmeId);
      ordbank.add(lemma);

    }

    ordbankByGrunnformAndParadigme = ordbank.stream()
        .collect(Collectors.groupingBy(this::getKey));

  }

  private void loadHunNor() {

    loadEntries();

    loadLemma();

  }

  private void loadEntries() {

    // Hungarian

    Map<Integer, Entry> huEntries = new HashMap<>();

    SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT entry,"
        + " GROUP_CONCAT(DISTINCT status) AS status,"
        + " GROUP_CONCAT(DISTINCT pos) AS pos FROM hn_hun_segment"
        + " WHERE status > 0 AND entry = id"
        + " GROUP BY entry");

    while (rowSet.next()) {

      int entryId = rowSet.getInt(1);
      String rawStatus = rowSet.getString(2);
      String[] statusArray = new String[] {};
      if (rawStatus != null) {
        statusArray = rawStatus.split(",");
      }
      int status = Integer.parseInt(statusArray[0]);
      String pos = rowSet.getString(3);

      Entry entry = new Entry(entryId, status);
      entry.setPos(pos);
      huEntries.put(entryId, entry);

    }

    SqlRowSet translationRowSet = jdbcTemplate.queryForRowSet(
        "SELECT id, trans FROM hn_hun_tr_nob_tmp");

    while (translationRowSet.next()) {

      int entryId = translationRowSet.getInt(1);
      String translation = translationRowSet.getString(2);

      Entry entry = huEntries.get(entryId);
      if (entry != null) {
        entry.setTranslation(translation);
      }

    }

    String huSql =
        "INSERT INTO HN_HU_ENTRY (ENTRY_ID, STATUS, POS, TRANSLATION) VALUES (?, ?, ?, ?)";
    huEntries.values().forEach(entry -> {
      if (entry.getTranslation() == null) {
        logger.warn("Translation missing for entry HU/{}", entry.getId());
      }
      jdbcTemplate.update(
          huSql, entry.getId(), entry.getStatus(), entry.getPos(), entry.getTranslation());
    });

    // Norwegian

    Map<Integer, Entry> nbEntries = new HashMap<>();

    rowSet = jdbcTemplate.queryForRowSet("SELECT entry,"
        + " GROUP_CONCAT(DISTINCT status) AS status,"
        + " GROUP_CONCAT(DISTINCT pos) AS pos FROM hn_nob_segment"
        + " WHERE status > 0 AND entry = id"
        + " GROUP BY entry");

    while (rowSet.next()) {

      int entryId = rowSet.getInt(1);
      String rawStatus = rowSet.getString(2);
      String[] statusArray = new String[] {};
      if (rawStatus != null) {
        statusArray = rawStatus.split(",");
      }
      int status = Integer.parseInt(statusArray[0]);
      String pos = rowSet.getString(3);

      Entry entry = new Entry(entryId, status);
      if ("adj,verb".equals(pos)) {
        entry.setPos("verb");
      } else {
        entry.setPos(pos);
      }
      nbEntries.put(entryId, entry);

    }

    translationRowSet = jdbcTemplate.queryForRowSet(
        "SELECT id, trans FROM hn_nob_tr_hun_tmp");

    while (translationRowSet.next()) {

      int entryId = translationRowSet.getInt(1);
      String translation = translationRowSet.getString(2);

      Entry entry = nbEntries.get(entryId);
      if (entry != null) {
        entry.setTranslation(translation);
      }

    }

    String nbSql =
        "INSERT INTO HN_NB_ENTRY (ENTRY_ID, STATUS, POS, TRANSLATION) VALUES (?, ?, ?, ?)";
    nbEntries.values().forEach(entry -> {
      if (entry.getTranslation() == null) {
        logger.warn("Translation missing for entry NB/{}", entry.getId());
      }
      jdbcTemplate.update(
          nbSql, entry.getId(), entry.getStatus(), entry.getPos(), entry.getTranslation());
    });

  }

  private void loadLemma() {

    // Hungarian

    SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT id,"
        + " GROUP_CONCAT(DISTINCT entry) AS entry,"
        + " GROUP_CONCAT(orth ORDER BY par, seq SEPARATOR ';') AS orth,"
        + " GROUP_CONCAT(DISTINCT pos ORDER BY pos) AS pos,"
        + " GROUP_CONCAT(DISTINCT par ORDER BY par) AS par,"
        + " GROUP_CONCAT(DISTINCT status) AS status"
        + " FROM hn_hun_segment"
        + " WHERE status > 0"
        + " GROUP BY id");

    String lemmaSql = "INSERT INTO HN_HU_LEMMA"
        + " (LEMMA_ID, GRUNNFORM) VALUES (?, ?)";
    String entryLemmaSql = "INSERT INTO HN_HU_ENTRY_LEMMA"
        + " (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES"
        + " (?, ?, ?, ?, ?, ?)";

    while (rowSet.next()) {

      int lemmaId = rowSet.getInt(1);
      int entryId = rowSet.getInt(2);

      int weight = 0;
      if (lemmaId != entryId) {
        weight = 1;
      }

      String grunnformColumn = rowSet.getString(3);
      String[] grunnformArray = new String[] {};
      if (grunnformColumn != null) {
        grunnformArray = grunnformColumn.split(";");
      }
      String grunnform = grunnformArray[0];

      jdbcTemplate.update(lemmaSql, lemmaId, grunnform);
      jdbcTemplate.update(entryLemmaSql, entryId, lemmaId, null, 0, weight, Lemma.SOURCE_HUNNOR);

    }

    // Norwegian

    rowSet = jdbcTemplate.queryForRowSet("SELECT id,"
        + " GROUP_CONCAT(DISTINCT entry) AS entry,"
        + " GROUP_CONCAT(orth ORDER BY par, seq) AS orth,"
        + " GROUP_CONCAT(DISTINCT pos ORDER BY pos) AS pos,"
        + " GROUP_CONCAT(DISTINCT par ORDER BY par) AS par,"
        + " GROUP_CONCAT(DISTINCT status) AS status"
        + " FROM hn_nob_segment"
        + " WHERE status > 0"
        + " GROUP BY id");

    lemmaSql = "INSERT INTO HN_NB_LEMMA"
        + " (LEMMA_ID, GRUNNFORM) VALUES (?, ?)";
    String lemmaParadigmeSql = "INSERT INTO HN_NB_LEMMA_PARADIGME"
        + " (LEMMA_ID, PARADIGME_ID) VALUES (?, ?)";
    entryLemmaSql = "INSERT INTO HN_NB_ENTRY_LEMMA"
        + " (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES"
        + " (?, ?, ?, ?, ?, ?)";

    while (rowSet.next()) {

      int lemmaId = rowSet.getInt(1);
      int entryId = rowSet.getInt(2);

      int weight = 0;
      if (lemmaId != entryId) {
        weight = 1;
      }

      String grunnformColumn = rowSet.getString(3);
      String[] grunnformArray = new String[] {};
      if (grunnformColumn != null) {
        grunnformArray = grunnformColumn.split(",");
      }
      String grunnform = grunnformArray[0];

      String paradigmeIdColumn = rowSet.getString(5);
      String[] paradigmeIdArray = new String[] {};
      if (paradigmeIdColumn != null) {
        paradigmeIdArray = paradigmeIdColumn.split(",");
      }
      List<String> paradigmeId = Arrays.stream(paradigmeIdArray)
          .collect(Collectors.toList());

      Lemma lemma = new Lemma(lemmaId, grunnform, paradigmeId);

      String key = getKey(lemma);
      List<Lemma> matchFromOrdbank = ordbankByGrunnformAndParadigme.get(key);
      if (matchFromOrdbank == null) {

        jdbcTemplate.update(lemmaSql, lemmaId, grunnform);
        paradigmeId.stream()
            .filter(paradigme -> !"0".equals(paradigme))
            .forEach(paradigme -> 
              jdbcTemplate.update(lemmaParadigmeSql, lemmaId, paradigme));
        jdbcTemplate.update(entryLemmaSql, entryId, lemmaId, null, 0, weight, Lemma.SOURCE_HUNNOR);

      } else if (matchFromOrdbank.size() == 1) {
        jdbcTemplate.update(entryLemmaSql,
            entryId, matchFromOrdbank.get(0).getId(), null, 0, weight, Lemma.SOURCE_ORDBANK);
      } else {
        Integer id = Mappings.getIdMapping(lemmaId);
        if (id == null) {
          String pars = lemma.getParadigmeId().stream().collect(Collectors.joining(":"));
          logger.warn("Ambiguous: {} {} ({})", grunnform, pars, lemmaId);
          matchFromOrdbank.forEach(lemmaFromOrdbank -> logger.warn(
              "-> {} ({})", lemmaFromOrdbank.getGrunnform(), lemmaFromOrdbank.getId()));
        } else {
          jdbcTemplate.update(entryLemmaSql,
              entryId, id, null, 0, weight, Lemma.SOURCE_ORDBANK);
        }
      }

    }

  }

  private String getKey(Lemma lemma) {
    String key = lemma.getGrunnform();
    if (lemma.getParadigmeId() != null) {
      key = key + lemma.getParadigmeId().stream().collect(Collectors.joining());
    }
    return key;
  }

}
