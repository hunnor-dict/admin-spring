package net.hunnor.dict.admin.edit;

import java.util.List;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;
import net.hunnor.dict.admin.model.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EditController {

  @Autowired
  private EditService editService;

  @GetMapping("/")
  public String index() {
    return "views/index";
  }

  /**
   * List lemma for a language and first letter.
   * @param language the language
   * @param letter the first letter
   * @return a list of matching lemma
   */
  @GetMapping(value = "/list", produces = {"application/json"})
  @ResponseBody
  public List<Map<String, String>> list(
      @RequestParam(name = "lang") Language language,
      @RequestParam(name = "letter") String letter) {
    return editService.list(language, letter);
  }

  /**
   * Details of a single lemma.
   * @param language the language
   * @param id the entry ID
   * @return the matching entry
   */
  @GetMapping(value = "/entry", produces = {"application/json"})
  @ResponseBody
  public Entry entry(
      @RequestParam(name = "lang") Language language,
      @RequestParam(name = "id") int id) {
    return editService.entry(language, id);
  }

  @PostMapping(value = "/save", produces = {"application/json"})
  @ResponseBody
  public void save(@RequestBody Entry entry) {
    editService.save(entry);
  }

}
