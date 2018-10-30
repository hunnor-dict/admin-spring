package net.hunnor.dict.admin.model;

import net.hunnor.dict.admin.config.Language;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

public class Lemma {

  public static final int SOURCE_ORDBANK = 0;

  public static final int SOURCE_HUNNOR = 1;

  private int id;

  private String grunnform;

  private int weight;

  private List<String> paradigmeId;

  public Lemma() {
  }

  /**
   * Constructor with all fields.
   * @param id the id of the new Lemma
   * @param grunnform the spelling of the lemma
   * @param paradigmeId a list of paradigm IDs
   */
  public Lemma(int id, String grunnform, List<String> paradigmeId) {
    this.id = id;
    this.grunnform = grunnform;
    this.paradigmeId = paradigmeId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGrunnform() {
    return grunnform;
  }

  public void setGrunnform(String grunnform) {
    this.grunnform = grunnform;
  }

  public List<String> getParadigmeId() {
    return paradigmeId;
  }

  public void setParadigmeId(List<String> paradigmeId) {
    this.paradigmeId = paradigmeId;
  }

  /**
   * Returns the letter the lemma belongs to.
   * @param language the language which alphabet to use
   * @return a letter from the alphabet of the specified language
   */
  public String getFirstLetter(Language language) {
    String firstLetter = null;
    if (!StringUtils.isEmpty(grunnform)) {
      int position = 0;
      while (firstLetter == null && position < grunnform.length()) {
        String letter = grunnform.substring(position, position + 1);
        String normalizedLetter = normalizeLetter(letter, language);
        if (normalizedLetter != null) {
          firstLetter = normalizedLetter;
          if (Language.HU.equals(language)) {
            firstLetter = expandLetter(firstLetter, grunnform, position);
          }
        }
        position++;
      }
    }
    return firstLetter;
  }

  private String expandLetter(String firstLetter, String grunnform, int position) {
    String expandedLetter = firstLetter;
    if (grunnform.length() > position + 1) {
      if ("C".equals(firstLetter)) {
        String secondLetter = grunnform.substring(position + 1, position + 2);
        if ("S".equalsIgnoreCase(secondLetter)) {
          expandedLetter = "CS";
        }
      } else if ("D".equals(firstLetter)) {
        String secondLetter = grunnform.substring(position + 1, position + 2);
        if ("Z".equalsIgnoreCase(secondLetter)) {
          expandedLetter = "DZ";
          if (grunnform.length() > position + 2) {
            String thirdLetter = grunnform.substring(position + 2, position + 3);
            if ("S".equalsIgnoreCase(thirdLetter)) {
              expandedLetter = "DZS";
            }
          }
        }
      } else if ("G".equals(firstLetter) || "L".equals(firstLetter)
          || "N".equals(firstLetter) || "T".equals(firstLetter)) {
        String secondLetter = grunnform.substring(position + 1, position + 2);
        if ("Y".equalsIgnoreCase(secondLetter)) {
          expandedLetter = firstLetter + "Y";
        }
      } else if ("S".equals(firstLetter)) {
        String secondLetter = grunnform.substring(position + 1, position + 2);
        if ("Z".equalsIgnoreCase(secondLetter)) {
          expandedLetter = "SZ";
        }
      } else if ("Z".equals(firstLetter)) {
        String secondLetter = grunnform.substring(position + 1, position + 2);
        if ("S".equalsIgnoreCase(secondLetter)) {
          expandedLetter = "ZS";
        }
      }
    }
    return expandedLetter;
  }

  private String normalizeLetter(String letter,Language language) {
    String normalizedLetter = letter.toUpperCase(Locale.getDefault());
    String pattern = "[A-Z]";
    switch (language) {
      case HU:
        normalizedLetter = normalizeHu(normalizedLetter);
        pattern = "[A-ZÖÜ]";
        break;
      case NB:
        normalizedLetter = normalizeNb(normalizedLetter);
        pattern = "[A-ZÆØÅ]";
        break;
      default:
        break;
    }
    if (normalizedLetter.matches(pattern)) {
      return normalizedLetter;
    }
    return null;
  }

  private String normalizeHu(String letter) {
    String normalizedLetter = letter;
    switch (letter) {
      case "Á":
        normalizedLetter = "A";
        break;
      case "É":
        normalizedLetter = "E";
        break;
      case "Í":
        normalizedLetter = "I";
        break;
      case "Ó":
        normalizedLetter = "O";
        break;
      case "Ő":
        normalizedLetter = "Ö";
        break;
      case "Ú":
        normalizedLetter = "U";
        break;
      case "Ű":
        normalizedLetter = "Ü";
        break;
      default:
        break;
    }
    return normalizedLetter;
  }

  private String normalizeNb(String letter) {
    String normalizedLetter = letter;
    switch (letter) {
      case "À":
        normalizedLetter = "A";
        break;
      default:
        break;
    }
    return normalizedLetter;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

}
