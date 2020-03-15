package net.hunnor.dict.admin.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

@Service
public class EditServiceImpl implements EditService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Map<String, String>> list(Language language, String letter) {

    String snippet = SqlSnippets.LIST_HU_LEMMA;
    if (Language.NB.equals(language)) {
      snippet = SqlSnippets.LIST_NB_LEMMA;
    }
    String condition = SqlSnippets.listSnippet(language, letter);
    String sql = String.format(snippet, condition);

    SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

    List<Map<String, String>> elements = new ArrayList<>();

    while (results.next()) {

      Map<String, String> element = new HashMap<>();

      String entryId = results.getString("ENTRY_ID");
      element.put("entryId", entryId);
      String lemmaId = results.getString("LEMMA_ID");
      element.put("lemmaId", lemmaId);
      String grunnform = results.getString("GRUNNFORM");
      element.put("grunnform", grunnform);
      String status = results.getString("STATUS");
      element.put("status", status);
      if (Language.NB.equals(language)) {
        String paradigmer = results.getString("PARADIGMER");
        element.put("paradigmer", paradigmer);
      }

      elements.add(element);

    }

    return elements;

  }

}
