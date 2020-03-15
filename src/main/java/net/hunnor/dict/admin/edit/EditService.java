package net.hunnor.dict.admin.edit;

import java.util.List;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;

public interface EditService {

  public List<Map<String, String>> list(Language language, String letter);

}
