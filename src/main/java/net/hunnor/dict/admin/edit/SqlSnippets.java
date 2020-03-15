package net.hunnor.dict.admin.edit;

import java.util.HashMap;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;

public class SqlSnippets {

  public static final String LIST_HU_LEMMA = ""

      + "SELECT "
      + "  HN_HU_ENTRY.ENTRY_ID AS ENTRY_ID, "
      + "  GROUP_CONCAT(DISTINCT HN_HU_LEMMA.LEMMA_ID) AS LEMMA_ID, "
      + "  GROUP_CONCAT(DISTINCT GRUNNFORM) AS GRUNNFORM, "
      + "  STATUS, "
      + "  HN_HU_ENTRY_LEMMA.PARADIGME_ID AS PARADIGME_ID, "
      + "  BOY_NUMMER, "
      + "  WEIGHT "
      + "FROM HN_HU_ENTRY "
      + "  LEFT JOIN HN_HU_ENTRY_LEMMA "
      + "    ON HN_HU_ENTRY.ENTRY_ID = HN_HU_ENTRY_LEMMA.ENTRY_ID AND HN_HU_ENTRY_LEMMA.SOURCE = 1 "
      + "  LEFT JOIN HN_HU_LEMMA "
      + "    ON HN_HU_ENTRY_LEMMA.LEMMA_ID = HN_HU_LEMMA.LEMMA_ID "
      + "  %1$s "
      + "GROUP BY HN_HU_LEMMA.LEMMA_ID "

      + "ORDER BY GRUNNFORM";

  public static final String LIST_NB_LEMMA = ""

      + "SELECT "
      + "  HN_NB_ENTRY.ENTRY_ID AS ENTRY_ID, "
      + "  GROUP_CONCAT(DISTINCT LEMMA.LEMMA_ID) AS LEMMA_ID, "
      + "  GROUP_CONCAT(DISTINCT GRUNNFORM) AS GRUNNFORM, "
      + "  GROUP_CONCAT(DISTINCT LEMMA_PARADIGME.PARADIGME_ID "
      + "    ORDER BY LEMMA_PARADIGME.PARADIGME_ID) AS PARADIGMER, "
      + "  STATUS, "
      + "  HN_NB_ENTRY_LEMMA.PARADIGME_ID AS PARADIGME_ID, "
      + "  BOY_NUMMER, "
      + "  WEIGHT "
      + "FROM HN_NB_ENTRY "
      + "  LEFT JOIN HN_NB_ENTRY_LEMMA "
      + "    ON HN_NB_ENTRY.ENTRY_ID = HN_NB_ENTRY_LEMMA.ENTRY_ID AND HN_NB_ENTRY_LEMMA.SOURCE = 0 "
      + "  LEFT JOIN LEMMA "
      + "    ON HN_NB_ENTRY_LEMMA.LEMMA_ID = LEMMA.LEMMA_ID "
      + "  LEFT JOIN LEMMA_PARADIGME "
      + "    ON LEMMA.LEMMA_ID = LEMMA_PARADIGME.LEMMA_ID "
      + "  %1$s "
      + "GROUP BY LEMMA.LEMMA_ID "

      + "UNION "

      + "SELECT "
      + "  HN_NB_ENTRY.ENTRY_ID AS ENTRY_ID, "
      + "  GROUP_CONCAT(DISTINCT HN_NB_LEMMA.LEMMA_ID) AS LEMMA_ID, "
      + "  GROUP_CONCAT(DISTINCT GRUNNFORM) AS GRUNNFORM, "
      + "  GROUP_CONCAT(DISTINCT HN_NB_LEMMA_PARADIGME.PARADIGME_ID "
      + "    ORDER BY HN_NB_LEMMA_PARADIGME.PARADIGME_ID) AS PARADIGMER, "
      + "  STATUS, "
      + "  HN_NB_ENTRY_LEMMA.PARADIGME_ID AS PARADIGME_ID, "
      + "  BOY_NUMMER, "
      + "  WEIGHT "
      + "FROM HN_NB_ENTRY "
      + "  LEFT JOIN HN_NB_ENTRY_LEMMA "
      + "    ON HN_NB_ENTRY.ENTRY_ID = HN_NB_ENTRY_LEMMA.ENTRY_ID AND HN_NB_ENTRY_LEMMA.SOURCE = 1 "
      + "  LEFT JOIN HN_NB_LEMMA "
      + "    ON HN_NB_ENTRY_LEMMA.LEMMA_ID = HN_NB_LEMMA.LEMMA_ID "
      + "  LEFT JOIN HN_NB_LEMMA_PARADIGME "
      + "    ON HN_NB_LEMMA.LEMMA_ID = HN_NB_LEMMA_PARADIGME.LEMMA_ID "
      + "  %1$s "
      + "GROUP BY HN_NB_LEMMA.LEMMA_ID "

      + "ORDER BY GRUNNFORM, PARADIGMER";

  private static final Map<String, String> LETTERS_HU = new HashMap<>();

  static {

    LETTERS_HU.put("a", "WHERE GRUNNFORM LIKE 'a%' OR GRUNNFORM LIKE 'á%'");
    LETTERS_HU.put("b", "WHERE GRUNNFORM LIKE 'b%'");
    LETTERS_HU.put("c", "WHERE GRUNNFORM LIKE 'c%' AND GRUNNFORM NOT LIKE 'cs%'");
    LETTERS_HU.put("cs", "WHERE GRUNNFORM LIKE 'cs%'");
    LETTERS_HU.put("d", "WHERE GRUNNFORM LIKE 'd%' AND GRUNNFORM NOT LIKE 'dz%'");
    LETTERS_HU.put("dz", "WHERE GRUNNFORM LIKE 'dz%' AND GRUNNFORM NOT LIKE 'dzs%'");
    LETTERS_HU.put("dzs", "WHERE GRUNNFORM LIKE 'dzs%'");
    LETTERS_HU.put("e", "WHERE GRUNNFORM LIKE 'e%' OR GRUNNFORM LIKE 'é%'");
    LETTERS_HU.put("f", "WHERE GRUNNFORM LIKE 'f%'");
    LETTERS_HU.put("g", "WHERE GRUNNFORM LIKE 'g%' AND GRUNNFORM NOT LIKE 'gy%'");
    LETTERS_HU.put("gy", "WHERE GRUNNFORM LIKE 'gy%'");
    LETTERS_HU.put("h", "WHERE GRUNNFORM LIKE 'h%'");
    LETTERS_HU.put("i", "WHERE GRUNNFORM LIKE 'i%' OR GRUNNFORM LIKE 'í%'");
    LETTERS_HU.put("j", "WHERE GRUNNFORM LIKE 'j%'");
    LETTERS_HU.put("k", "WHERE GRUNNFORM LIKE 'k%'");
    LETTERS_HU.put("l", "WHERE GRUNNFORM LIKE 'l%' AND GRUNNFORM NOT LIKE 'ly%'");
    LETTERS_HU.put("ly", "WHERE GRUNNFORM LIKE 'ly%'");
    LETTERS_HU.put("m", "WHERE GRUNNFORM LIKE 'm%'");
    LETTERS_HU.put("n", "WHERE GRUNNFORM LIKE 'n%' AND GRUNNFORM NOT LIKE 'ny%'");
    LETTERS_HU.put("ny", "WHERE GRUNNFORM LIKE 'ny%'");
    LETTERS_HU.put("o", "WHERE GRUNNFORM LIKE 'o%' OR GRUNNFORM LIKE 'ó%'");
    LETTERS_HU.put("ö", "WHERE GRUNNFORM LIKE 'ö%' OR GRUNNFORM LIKE 'ő%'");
    LETTERS_HU.put("p", "WHERE GRUNNFORM LIKE 'p%'");
    LETTERS_HU.put("q", "WHERE GRUNNFORM LIKE 'q%'");
    LETTERS_HU.put("r", "WHERE GRUNNFORM LIKE 'r%'");
    LETTERS_HU.put("s", "WHERE GRUNNFORM LIKE 's%' AND GRUNNFORM NOT LIKE 'sz%'");
    LETTERS_HU.put("sz", "WHERE GRUNNFORM LIKE 'sz%'");
    LETTERS_HU.put("t", "WHERE GRUNNFORM LIKE 't%' AND GRUNNFORM NOT LIKE 'ty%'");
    LETTERS_HU.put("ty", "WHERE GRUNNFORM LIKE 'ty%'");
    LETTERS_HU.put("u", "WHERE GRUNNFORM LIKE 'u%' OR GRUNNFORM LIKE 'ú%'");
    LETTERS_HU.put("ü", "WHERE GRUNNFORM LIKE 'ü%' OR GRUNNFORM LIKE 'ű%'");
    LETTERS_HU.put("v", "WHERE GRUNNFORM LIKE 'v%'");
    LETTERS_HU.put("w", "WHERE GRUNNFORM LIKE 'w%'");
    LETTERS_HU.put("x", "WHERE GRUNNFORM LIKE 'x%'");
    LETTERS_HU.put("y", "WHERE GRUNNFORM LIKE 'y%'");
    LETTERS_HU.put("z", "WHERE GRUNNFORM LIKE 'z%' AND GRUNNFORM NOT LIKE 'zs%'");
    LETTERS_HU.put("zs", "WHERE GRUNNFORM LIKE 'zs%'");

  }

  /**
   * SQL snippet for selecting lemma for a language by first letter.
   * @param language the language
   * @param letter the first letter
   * @return the SQL condition as String
   */
  public static String listSnippet(Language language, String letter) {
    String snippet = null;
    if (Language.HU.equals(language)) {
      snippet = LETTERS_HU.get(letter);
    } else if (Language.NB.equals(language)) {
      snippet = "WHERE GRUNNFORM LIKE '" + letter + "%'";
    }
    return snippet;
  }

  private SqlSnippets() {
  }

}
