package net.hunnor.dict.admin.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;
import net.hunnor.dict.admin.model.Entry;
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

  @Override
  public Entry entry(Language language, int id) {

    Entry entry = new Entry();

    String snippet = SqlSnippets.ENTRY_HU;
    if (Language.NB.equals(language)) {
      snippet = SqlSnippets.ENTRY_NB;
    }

    SqlRowSet results = jdbcTemplate.queryForRowSet(snippet, id);

    while (results.next()) {
      String pos = results.getString("POS");
      entry.setPos(pos);
      String status = results.getString("STATUS");
      if (status != null) {
        entry.setStatus(Integer.parseInt(status));
      }
      String translation = results.getString("TRANSLATION");
      entry.setTranslation(translation);
    }

    return entry;

  }

}
