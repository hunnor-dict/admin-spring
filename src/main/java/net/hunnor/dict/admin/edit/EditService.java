package net.hunnor.dict.admin.edit;

import java.util.List;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;
import net.hunnor.dict.admin.model.Entry;

public interface EditService {

  public List<Map<String, String>> list(Language language, String letter);

  public Entry entry(Language language, int id);

}
