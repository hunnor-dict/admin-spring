package net.hunnor.dict.admin.edit;

import java.util.List;
import java.util.Map;
import net.hunnor.dict.admin.config.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

}
