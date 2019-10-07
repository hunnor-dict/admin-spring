package net.hunnor.dict.admin.inflection;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

@Service
public final class InflectionServiceImpl implements InflectionService {

  private static final Logger logger = LoggerFactory.getLogger(InflectionServiceImpl.class);

  private Map<String, Inflection> inflectionMap = new HashMap<>();

  @Autowired
  private Unmarshaller unmarshaller;

  /**
   * Loads inflections from the XML resource file.
   */
  @PostConstruct
  public void setUp() {
    InputStream inputStream = getClass().getResourceAsStream("/inflections.xml");
    Source source = new StreamSource(inputStream);
    try {
      Inflections inflections = (Inflections) unmarshaller.unmarshal(source);
      for (Inflection inflection: inflections.getInflectionList()) {
        inflectionMap.put(inflection.getParadigms(), inflection);
      }
    } catch (IOException ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  /**
   * Return paradigm labels for a list of paradigm IDs.
   * @param paradigms the paradigm codes
   * @return the labels as a single string
   */
  public String getCodes(List<String> paradigms) {
    String key = concatParadigms(paradigms);
    Inflection inflection = inflectionMap.get(key);
    if (inflection != null) {
      return inflection.getCodes();
    }
    return null;
  }

  /**
   * Return suffixes for a list of paradigm IDs.
   * @param paradigms the paradigm codes
   * @return the suffixes as a single string
   */
  public String getSuffixes(List<String> paradigms) {
    String key = concatParadigms(paradigms);
    Inflection inflection = inflectionMap.get(key);
    if (inflection != null) {
      return inflection.getSuffixes();
    }
    return null;
  }

  /**
   * Generate inflected forms of a lemma.
   * @param lemma the base form
   * @param patterns the inflection patterns
   * @return inflected forms in a map by form number
   */
  public Map<Integer, String> getInflections(String lemma, Map<Integer, String> patterns) {
    Map<Integer, String> inflections = null;
    if (lemma != null && patterns != null) {
      inflections = new HashMap<>();
      Optional<Integer> basePatternKeyOpt = patterns.keySet().stream().sorted().findFirst();
      if (basePatternKeyOpt.isPresent()) {
        int basePatternKey = basePatternKeyOpt.get();
        String basePattern = patterns.get(basePatternKey);
        for (Entry<Integer, String> entry: patterns.entrySet()) {
          String pattern = patterns.get(entry.getKey());
          if (!"-".equals(pattern)) {
            String inflection = inflect(basePattern, pattern, lemma);
            inflections.put(entry.getKey(), inflection);
          }
        }
      }
    }
    return inflections;
  }

  private String concatParadigms(List<String> paradigmList) {
    String paradigms = null;
    if (paradigmList != null) {
      paradigms = paradigmList.stream().sorted().collect(Collectors.joining(":"));
    }
    return paradigms;
  }

  private String inflect(String basePattern, String pattern, String term) {

    String patternValue = Optional.ofNullable(pattern).orElse("");

    if (basePattern == null || basePattern.isEmpty()) {
      return term + patternValue;
    }

    if (basePattern.contains("+") || basePattern.contains("%")) {

      String percent;
      String plus;

      String inflected = term.substring(0, term.length() - basePattern.length()) + patternValue;

      int percentPos = basePattern.indexOf('%');
      int plusPos = basePattern.indexOf('+');

      if (percentPos != -1) {
        percent = String.valueOf(term.charAt(term.length() - basePattern.length() + percentPos));
        inflected = inflected.replace("%", String.valueOf(percent));
      }
      if (plusPos != -1) {
        plus = String.valueOf(term.charAt(term.length() - basePattern.length() + plusPos));
        inflected = inflected.replace("+", String.valueOf(plus));
      }

      return inflected;

    }

    return term.substring(0, term.length() - basePattern.length()) + patternValue;

  }

}
