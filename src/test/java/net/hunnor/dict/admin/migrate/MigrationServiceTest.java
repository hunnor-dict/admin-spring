package net.hunnor.dict.admin.migrate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import net.hunnor.dict.admin.model.Lemma;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {"classpath:sql/hunnor-v2-create.sql", "classpath:sql/hunnor-v2-insert-migrate.sql"})
@Sql(scripts = {"classpath:sql/hunnor-v3-create.sql"})
@Sql(scripts = {"classpath:sql/ordbank-create.sql", "classpath:sql/ordbank-insert-migrate.sql"})
public class MigrationServiceTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MigrationService migrationService;

  private static boolean migrated = false;

  private static final String HN_HU_ENTRY_SELECT =
      "SELECT ENTRY_ID, POS, STATUS, TRANSLATION FROM HN_HU_ENTRY WHERE ENTRY_ID = ?";

  private static final String HN_HU_LEMMA_SELECT =
      "SELECT LEMMA_ID, GRUNNFORM FROM HN_HU_LEMMA WHERE LEMMA_ID = ?";

  private static final String HN_HU_ENTRY_LEMMA_SELECT =
      "SELECT ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE"
          + " FROM HN_HU_ENTRY_LEMMA WHERE ENTRY_ID = ?";

  private static final String HN_NB_ENTRY_SELECT =
      "SELECT ENTRY_ID, POS, STATUS, TRANSLATION FROM HN_NB_ENTRY WHERE ENTRY_ID = ?";

  private static final String HN_NB_LEMMA_SELECT =
      "SELECT LEMMA_ID, GRUNNFORM FROM HN_NB_LEMMA WHERE LEMMA_ID = ?";

  private static final String HN_NB_ENTRY_LEMMA_SELECT =
      "SELECT ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE"
          + " FROM HN_NB_ENTRY_LEMMA WHERE ENTRY_ID = ?";

  private static final String HN_NB_LEMMA_PARADIGME_SELECT =
      "SELECT LEMMA_ID, PARADIGME_ID FROM HN_NB_LEMMA_PARADIGME WHERE LEMMA_ID = ?";

  /**
   * Migrate data before the tests.
   * @throws MigrationException if migration fails
   */
  @Before
  public void setUp() throws MigrationException {
    if (!migrated) {
      migrationService.migrate();
      migrated = true;
    }
  }

  @Test
  public void testHuSingleLemma() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_HU_ENTRY_SELECT, 1);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(1, id);
    String pos = rowSet.getString(2);
    assertEquals("subst", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_HU_ENTRY_LEMMA_SELECT, 1);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(1, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(1, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);
    assertFalse(rowSet.next());

    // Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_HU_LEMMA_SELECT, 1);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(1, lemmaId);
    String grunnform = rowSet.getString(2);
    assertEquals("alma", grunnform);
    assertFalse(rowSet.next());

  }

  @Test
  public void testHuSeveralLemma() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_HU_ENTRY_SELECT, 2);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(2, id);
    String pos = rowSet.getString(2);
    assertEquals("subst", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_HU_ENTRY_LEMMA_SELECT, 2);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(2, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(2, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);

    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(2, id);
    lemmaId = rowSet.getInt(2);
    assertEquals(3, lemmaId);
    paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    weight = rowSet.getInt(5);
    assertEquals(1, weight);
    source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);
    assertFalse(rowSet.next());

    // Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_HU_LEMMA_SELECT, 2);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(2, lemmaId);
    String grunnform = rowSet.getString(2);
    assertEquals("tejföl", grunnform);
    assertFalse(rowSet.next());

    rowSet = jdbcTemplate.queryForRowSet(HN_HU_LEMMA_SELECT, 3);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(3, lemmaId);
    grunnform = rowSet.getString(2);
    assertEquals("tejfel", grunnform);
    assertFalse(rowSet.next());

  }

  @Test
  public void testSingleMatch() throws MigrationException {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_SELECT, 1);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(1, id);
    String pos = rowSet.getString(2);
    assertEquals("subst", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_LEMMA_SELECT, 1);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(1, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(10, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_ORDBANK, source);
    assertFalse(rowSet.next());

    // Lemma (HN)
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 1);
    assertFalse(rowSet.next());

    // Lemma - Paradigm
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 1);
    assertFalse(rowSet.next());

  }

  @Test
  public void testAmbiguousWithoutMapping() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_SELECT, 2);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(2, id);
    String pos = rowSet.getString(2);
    assertEquals("subst", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_LEMMA_SELECT, 2);
    assertFalse(rowSet.next());

    // Lemma (HN)
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 2);
    assertFalse(rowSet.next());

    // Lemma - Paradigm
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 2);
    assertFalse(rowSet.next());

  }

  @Test
  public void testWithoutParadigm() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_SELECT, 3);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(3, id);
    String pos = rowSet.getString(2);
    assertEquals("subst", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_LEMMA_SELECT, 3);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(3, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(3, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);
    assertFalse(rowSet.next());

    // Lemma (HN)
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 3);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(3, lemmaId);
    String grunnform = rowSet.getString(2);
    assertEquals("lastebil", grunnform);
    assertFalse(rowSet.next());

    // Lemma - Paradigm
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 3);
    assertFalse(rowSet.next());

  }

  @Test
  public void testMixedPos() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_SELECT, 4);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(4, id);
    String pos = rowSet.getString(2);
    assertEquals("verb", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_LEMMA_SELECT, 4);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(4, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(4, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);
    assertFalse(rowSet.next());

    // Lemma (HN)
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 4);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(4, lemmaId);
    String grunnform = rowSet.getString(2);
    assertEquals("skrive", grunnform);
    assertFalse(rowSet.next());

    // Lemma - Paradigm
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 4);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(4, lemmaId);
    paradigmeId = rowSet.getString(2);
    assertEquals("001", paradigmeId);
    assertFalse(rowSet.next());

  }

  @Test
  public void testSeveralLemma() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_SELECT, 5);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(5, id);
    String pos = rowSet.getString(2);
    assertEquals("subst", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_LEMMA_SELECT, 5);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(5, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(5, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(5, id);
    lemmaId = rowSet.getInt(2);
    assertEquals(6, lemmaId);
    paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    weight = rowSet.getInt(5);
    assertEquals(1, weight);
    source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_HUNNOR, source);
    assertFalse(rowSet.next());

    // Lemma (HN)
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 5);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(5, lemmaId);
    String grunnform = rowSet.getString(2);
    assertEquals("band", grunnform);
    assertFalse(rowSet.next());

    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 6);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(6, lemmaId);
    grunnform = rowSet.getString(2);
    assertEquals("bånd", grunnform);
    assertFalse(rowSet.next());

    // Lemma - Paradigm
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 5);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(5, lemmaId);
    paradigmeId = rowSet.getString(2);
    assertEquals("800", paradigmeId);
    assertFalse(rowSet.next());

    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 6);
    assertTrue(rowSet.next());
    lemmaId = rowSet.getInt(1);
    assertEquals(6, lemmaId);
    paradigmeId = rowSet.getString(2);
    assertEquals("800", paradigmeId);
    assertFalse(rowSet.next());

  }

  @Test
  public void testAmbiguousWithMapping() {

    // Entry
    SqlRowSet rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_SELECT, 2310);
    assertTrue(rowSet.next());
    int id = rowSet.getInt(1);
    assertEquals(2310, id);
    String pos = rowSet.getString(2);
    assertEquals("adv", pos);
    int status = rowSet.getInt(3);
    assertEquals(1, status);
    String translation = rowSet.getString(4);
    assertEquals("<senseGrp/>", translation);
    assertFalse(rowSet.next());

    // Entry - Lemma
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_ENTRY_LEMMA_SELECT, 2310);
    assertTrue(rowSet.next());
    id = rowSet.getInt(1);
    assertEquals(2310, id);
    int lemmaId = rowSet.getInt(2);
    assertEquals(2522, lemmaId);
    String paradigmeId = rowSet.getString(3);
    assertNull(paradigmeId);
    int boyNummer = rowSet.getInt(4);
    assertEquals(0, boyNummer);
    int weight = rowSet.getInt(5);
    assertEquals(0, weight);
    int source = rowSet.getInt(6);
    assertEquals(Lemma.SOURCE_ORDBANK, source);
    assertFalse(rowSet.next());

    // Lemma (HN)
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_SELECT, 2310);
    assertFalse(rowSet.next());

    // Lemma - Paradigm
    rowSet = jdbcTemplate.queryForRowSet(HN_NB_LEMMA_PARADIGME_SELECT, 2310);
    assertFalse(rowSet.next());

  }

}
