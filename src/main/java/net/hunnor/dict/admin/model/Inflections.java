package net.hunnor.dict.admin.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class Inflections {

  private static Map<String, String> codesByParadigms = new HashMap<>();

  private static Map<String, String> suffixesByParadigms = new HashMap<>();

  static {

    codesByParadigms.put("700:900", "f1:m1");
    suffixesByParadigms.put("700:900", "-en/-a");

    codesByParadigms.put("702:902", "f1:m1");
    suffixesByParadigms.put("702:902", "-en/-a");

    codesByParadigms.put("700:800:810:900", "f1:m1:n1");
    suffixesByParadigms.put("700:800:810:900", "-en/-a/-et");

    codesByParadigms.put("702:800:810:902", "f1:m1:n1");
    suffixesByParadigms.put("702:800:810:902", "-en/-a/-et");

    codesByParadigms.put("700:801:811:900", "f1:m1:n2");
    suffixesByParadigms.put("700:801:811:900", "-en/-a/-et");

    codesByParadigms.put("702:802:812:902", "f1:m1:n2");
    suffixesByParadigms.put("702:802:812:902", "-en/-a/-et");

    codesByParadigms.put("700:800:801:810:811:900", "f1:m1:n3");
    suffixesByParadigms.put("700:800:801:810:811:900", "-en/-a/-et");

    codesByParadigms.put("700", "m1");
    suffixesByParadigms.put("700", "-en");

    codesByParadigms.put("700:703", "m1");
    suffixesByParadigms.put("700:703", "-en");

    codesByParadigms.put("702", "m1");
    suffixesByParadigms.put("702", "-en");

    codesByParadigms.put("700:800:810", "m1:n1");
    suffixesByParadigms.put("700:800:810", "-en/-et");

    codesByParadigms.put("700:801:811", "m1:n2");
    suffixesByParadigms.put("700:801:811", "-en/-et");

    codesByParadigms.put("702:802:812", "m1:n2");
    suffixesByParadigms.put("702:802:812", "-en/-et");

    codesByParadigms.put("700:703:800:801:810:811:874:875:876:877", "m1:n3");
    suffixesByParadigms.put("700:703:800:801:810:811:874:875:876:877", "-en/-et");

    codesByParadigms.put("700:800:801:810:811", "m1:n3");
    suffixesByParadigms.put("700:800:801:810:811", "-en/-et");

    codesByParadigms.put("711:712", "m2");
    suffixesByParadigms.put("711:712", "-eren, -ere, -erne");

    codesByParadigms.put("712", "m2");
    suffixesByParadigms.put("712", "-eren, -ere, -erne");

    codesByParadigms.put("711:712:720:724", "m3");
    suffixesByParadigms.put("711:712:720:724", "-eren, -ere/-re/-rer, -erne/-rene");

    codesByParadigms.put("711:712:721", "m3");
    suffixesByParadigms.put("711:712:721", "-eren, -ere/-re/-rer, -erne/-rene");

    codesByParadigms.put("712:720:724", "m3");
    suffixesByParadigms.put("712:720:724", "-eren, -ere/-re/-rer, -erne/-rene");

    codesByParadigms.put("712:717:724", "m3");
    suffixesByParadigms.put("712:717:724", "-eren, -ere/-re/-rer, -erne/-rene");

    codesByParadigms.put("712:721:742", "m3");
    suffixesByParadigms.put("712:721:742", "-eren, -ere/-re/-rer, -erne/-rene");

    codesByParadigms.put("800:810", "n1");
    suffixesByParadigms.put("800:810", "-et");

    codesByParadigms.put("801:811", "n2");
    suffixesByParadigms.put("801:811", "-et, -er, -ene/-a");

    codesByParadigms.put("802:812", "n2");
    suffixesByParadigms.put("802:812", "-et, -er, -ene/-a");

    codesByParadigms.put("800:801:810:811", "n3");
    suffixesByParadigms.put("800:801:810:811", "-et, -/-er, -ene/-a");

    codesByParadigms.put("800:801:810:811:874:875:876:877", "n3");
    suffixesByParadigms.put("800:801:810:811:874:875:876:877", "-et, -/-er, -ene/-a");

    codesByParadigms.put("802:804:812:814", "n3");
    suffixesByParadigms.put("802:804:812:814", "-et, -/-er, -ene/-a");

    codesByParadigms.put("700:715:900:915", "-a/-en, -/-er, -ene");
    suffixesByParadigms.put("700:715:900:915", "-a/-en, -/-er, -ene");

    codesByParadigms.put("715:915", "-a/-en, -, -ene");
    suffixesByParadigms.put("715:915", "-a/-en, -, -ene");

    codesByParadigms.put("790:990", "-a/-en, -");
    suffixesByParadigms.put("790:990", "-a/-en, -");

    codesByParadigms.put("715", "-en, -");
    suffixesByParadigms.put("715", "-en, -");

    codesByParadigms.put("716", "-en, -");
    suffixesByParadigms.put("716", "-en, -");

    codesByParadigms.put("720", "-en");
    suffixesByParadigms.put("720", "-en");

    codesByParadigms.put("721", "-en");
    suffixesByParadigms.put("721", "-en");

    codesByParadigms.put("725", "-men");
    suffixesByParadigms.put("725", "-men");

    codesByParadigms.put("780", "m:pl");
    suffixesByParadigms.put("780", "m:pl");

    codesByParadigms.put("790", "m:none");
    suffixesByParadigms.put("790", "m:none");

    codesByParadigms.put("791", "m:def");
    suffixesByParadigms.put("791", "m:def");

    codesByParadigms.put("700:890", "-en::n:none");
    suffixesByParadigms.put("700:890", "-en::n:none");

    codesByParadigms.put("715:800:810", "-en, - / n1");
    suffixesByParadigms.put("715:800:810", "-en, - / n1");

    codesByParadigms.put("790:890", "m:n:none");
    suffixesByParadigms.put("790:890", "m:n:none");

    codesByParadigms.put("810:811", "-et, -/-er, -ene");
    suffixesByParadigms.put("810:811", "-et, -/-er, -ene");

    codesByParadigms.put("801:811:850:851", "-um, -umet/-et, -umer/-er/-a, -uma/-umene/-a/-aene");
    suffixesByParadigms.put("801:811:850:851",
        "-um, -umet/-et, -umer/-er/-a, -uma/-umene/-a/-aene");

    codesByParadigms.put("880", "n:pl");
    suffixesByParadigms.put("880", "n:pl");

    codesByParadigms.put("815:816:826:827", "-er, -eret, -er/-re, -ra/-rene");
    suffixesByParadigms.put("815:816:826:827", "-er, -eret, -er/-re, -ra/-rene");

    codesByParadigms.put("890", "n:none");
    suffixesByParadigms.put("890", "n:none");

    codesByParadigms.put("895", "n:none");
    suffixesByParadigms.put("895", "n:none");

    codesByParadigms.put("830:831", "-met, -, -ma/-mene");
    suffixesByParadigms.put("830:831", "-met, -, -ma/-mene");

    codesByParadigms.put("815:816:824:825", "-d(e)ret, -der/-dre, -dra/-drene");
    suffixesByParadigms.put("815:816:824:825", "-d(e)ret, -der/-dre, -dra/-drene");

    codesByParadigms.put("835:836:837:838", "-d(de)let, -del/dler, -dla/-dlene");
    suffixesByParadigms.put("835:836:837:838", "-d(de)let, -del/dler, -dla/-dlene");

    codesByParadigms.put("800:828", "n");
    suffixesByParadigms.put("800:828", "-et");

    codesByParadigms.put("835:836:841:842", "n");
    suffixesByParadigms.put("835:836:841:842", "-et");

    codesByParadigms.put("830:831", "-met, -, -ma/-mene");
    suffixesByParadigms.put("830:831", "-met, -, -ma/-mene");

    codesByParadigms.put("830:831:832:833", "-met, -/-mer, -ma/-mene");
    suffixesByParadigms.put("830:831:832:833", "-met, -/-mer, -ma/-mene");

    codesByParadigms.put("001:007:010:011:017:019", "v1");
    suffixesByParadigms.put("001:007:010:011:017:019", "-et/-a");

    codesByParadigms.put("001:010:011", "v1");
    suffixesByParadigms.put("001:010:011", "-et/-a");

    codesByParadigms.put("001:010", "v1");
    suffixesByParadigms.put("001:010", "-et/-a");

    codesByParadigms.put("002:012:015", "v1");
    suffixesByParadigms.put("002:012:015", "-et/-a");

    codesByParadigms.put("001:010:011:020", "v1:v2");
    suffixesByParadigms.put("001:010:011:020", "-et/-a/-te");

    codesByParadigms.put("001:010:011:021", "v1:v2");
    suffixesByParadigms.put("001:010:011:021", "-et/-a/-te");

    codesByParadigms.put("001:010:011:020:030", "v1:v2:v3");
    suffixesByParadigms.put("001:010:011:020:030", "-et/-a/-te/-de");

    codesByParadigms.put("001:010:011:030", "v1:v3");
    suffixesByParadigms.put("001:010:011:030", "-et/-a/-de");

    codesByParadigms.put("001:010:011:043", "v1:v4");
    suffixesByParadigms.put("001:010:011:043", "-et/-a/-dde");

    codesByParadigms.put("020", "v2");
    suffixesByParadigms.put("020", "-te");

    codesByParadigms.put("020:030", "v2:v3");
    suffixesByParadigms.put("020:030", "-te/-de");

    codesByParadigms.put("030", "v3");
    suffixesByParadigms.put("030", "-de");

    codesByParadigms.put("040", "v4");
    suffixesByParadigms.put("040", "-dde");

    codesByParadigms.put("040:041", "v4");
    suffixesByParadigms.put("040:041", "-dde");

    codesByParadigms.put("040:042", "v4");
    suffixesByParadigms.put("040:042", "-dde");

    codesByParadigms.put("043", "v4");
    suffixesByParadigms.put("043", "-dde");

    codesByParadigms.put("500", "a1");
    suffixesByParadigms.put("500", "-t, -e");

    codesByParadigms.put("500:504", "a1:a2");
    suffixesByParadigms.put("500:504", "-t/-, -e");

    codesByParadigms.put("504", "a2");
    suffixesByParadigms.put("504", "-, -e");

    codesByParadigms.put("508", "a2");
    suffixesByParadigms.put("508", "-, -e");

    codesByParadigms.put("504:512", "a2:a3");
    suffixesByParadigms.put("504:512", "-, -/-e");

    codesByParadigms.put("501:503:504:512", "a2:a3");
    suffixesByParadigms.put("501:503:504:512", "-, -/-e");

    codesByParadigms.put("513", "a3");
    suffixesByParadigms.put("513", "-, -");

    codesByParadigms.put("514", "a3");
    suffixesByParadigms.put("514", "-, -");

    codesByParadigms.put("504:534", "a4");
    suffixesByParadigms.put("504:534", "-et, -ete/-ede");

    codesByParadigms.put("560", "a5");
    suffixesByParadigms.put("560", "-ent, -ne");

  }

  private Inflections() {
  }

  public static String getCodes(List<String> paradigms) {
    String key = concatParadigms(paradigms);
    return codesByParadigms.get(key);
  }

  public static String getSuffixes(List<String> paradigms) {
    String key = concatParadigms(paradigms);
    return suffixesByParadigms.get(key);
  }

  /**
   * Generate inflected forms of a lemma.
   * @param lemma the base form
   * @param patterns the inflection patterns
   * @return inflected forms in a map by form number
   */
  public static Map<Integer, String> getInflections(String lemma, Map<Integer, String> patterns) {
    Map<Integer, String> inflections = null;
    if (lemma != null && patterns != null) {
      inflections = new HashMap<>();
      Optional<Integer> basePatternKeyOpt = patterns.keySet().stream().sorted().findFirst();
      if (basePatternKeyOpt.isPresent()) {
        int basePatternKey = basePatternKeyOpt.get();
        String basePattern = patterns.get(basePatternKey);
        for (Entry<Integer, String> entry: patterns.entrySet()) {
          String pattern = patterns.get(entry.getKey());
          String inflection = inflect(basePattern, pattern, lemma);
          inflections.put(entry.getKey(), inflection);
        }
      }
    }
    return inflections;
  }

  private static String concatParadigms(List<String> paradigmList) {
    String paradigms = null;
    if (paradigmList != null) {
      paradigms = paradigmList.stream().sorted().collect(Collectors.joining(":"));
    }
    return paradigms;
  }

  private static String inflect(String basePattern, String pattern, String term) {

    String patternValue = Optional.ofNullable(pattern).orElse("");

    if (basePattern == null || basePattern.isEmpty()) {
      return term + patternValue;
    }

    if (basePattern.contains("+") || basePattern.contains("%")) {

      String percent = "%";
      String plus = "+";

      String inflected = term.substring(0, term.length() - basePattern.length()) + patternValue;

      int percentPos = basePattern.indexOf("%");
      int plusPos = basePattern.indexOf("+");

      if (percentPos != -1) {
        percent = String.valueOf(term.charAt(term.length() - basePattern.length() + percentPos));
        inflected = inflected.replaceAll("%", String.valueOf(percent));
      }
      if (plusPos != -1) {
        plus = String.valueOf(term.charAt(term.length() - basePattern.length() + plusPos));
        inflected = inflected.replaceAll("\\+", String.valueOf(plus));
      }

      return inflected;

    }

    return term.substring(0, term.length() - basePattern.length()) + patternValue;

  }

}
