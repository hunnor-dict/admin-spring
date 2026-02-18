package net.hunnor.dict.admin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class Lemma {

  private static final Logger logger = LoggerFactory.getLogger(Lemma.class);

  public static final int SOURCE_ORDBANK = 0;

  public static final int SOURCE_HUNNOR = 1;

  private static final Map<String, String> normalizationMap = new HashMap<>();

  private static final Map<String, String[]> doubles = new HashMap<>();

  private int id;

  private String grunnform;

  private int weight;

  private List<String> paradigmeId;

  static {

    normalizationMap.put("Á", "A");
    normalizationMap.put("É", "E");
    normalizationMap.put("Í", "I");
    normalizationMap.put("Ó", "O");
    normalizationMap.put("Ő", "Ö");
    normalizationMap.put("Ú", "U");
    normalizationMap.put("Ű", "Ü");

    doubles.put("C", new String[] {"CS"});
    doubles.put("D", new String[] {"DZS", "DZ"});
    doubles.put("G", new String[] {"GY"});
    doubles.put("L", new String[] {"LY"});
    doubles.put("N", new String[] {"NY"});
    doubles.put("T", new String[] {"TY"});
    doubles.put("S", new String[] {"SZ"});
    doubles.put("Z", new String[] {"ZS"});

  }

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
    this.paradigmeId = copyParadigmeId(paradigmeId);
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

  /**
   * Returns the list of paradigm IDs.
   * The returned list is an unmodifiable copy of the internal list
   * to avoid exposing internal state.
   * @return an unmodifiable list of paradigm IDs, or {@code null} if no paradigm IDs are set
   */
  public List<String> getParadigmeId() {
    if (paradigmeId == null) {
      return null;
    }
    return Collections.unmodifiableList(new ArrayList<>(paradigmeId));
  }

  public void setParadigmeId(List<String> paradigmeId) {
    this.paradigmeId = copyParadigmeId(paradigmeId);
  }

  private List<String> copyParadigmeId(List<String> paradigmeId) {
    if (paradigmeId == null) {
      return null;
    }
    return new ArrayList<>(paradigmeId);
  }

  /**
   * Returns the letter the lemma belongs to.
   * @param language the language which alphabet to use
   * @return a letter from the alphabet of the specified language
   */
  public String getFirstLetter(Language language) {
    String firstLetter = null;
    if (StringUtils.hasText(grunnform)) {
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
    if (firstLetter == null) {
      logger.warn("Null first letter for {}", grunnform);
    }
    return firstLetter;
  }

  private String expandLetter(String firstLetter, String grunnform, int position) {
    String expandedLetter = firstLetter;
    if (doubles.containsKey(firstLetter)) {
      String[] candidates = doubles.get(firstLetter);
      for (String candidate : candidates) {
        if (grunnform.length() >= position + candidate.length() && grunnform.substring(
            position, position + candidate.length()).equalsIgnoreCase(candidate)) {
          expandedLetter = candidate;
          break;
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
    return normalizationMap.getOrDefault(letter, letter);
  }

  private String normalizeNb(String letter) {
    String normalizedLetter = letter;
    if ("À".equals(letter)) {
      normalizedLetter = "A";
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
