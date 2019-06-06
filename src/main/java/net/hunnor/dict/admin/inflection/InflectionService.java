package net.hunnor.dict.admin.inflection;

import java.util.List;
import java.util.Map;

public interface InflectionService {

  public String getCodes(List<String> paradigms);

  public String getSuffixes(List<String> paradigms);

  public Map<Integer, String> getInflections(String lemma, Map<Integer, String> patterns);

}
