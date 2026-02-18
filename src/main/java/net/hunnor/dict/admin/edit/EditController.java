package net.hunnor.dict.admin.edit;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class EditController {

  @Autowired
  private EditService editService;

  /**
   * Displays the main editor page.
   *
   * @param model the model used by the view
   * @param principal the authenticated user principal
   * @return the editor index template name
   */
  @GetMapping({"/", "/priv"})
  public String index(Model model, Principal principal) {
    String principalName = principal == null ? "" : principal.getName();
    model.addAttribute("name", principalName);
    model.addAttribute("uid", principalName);
    model.addAttribute("provider", "local");
    return "views/index";
  }

  /**
   * Renders the filtered entry list fragment.
   *
   * @param language the selected language code
   * @param letter the selected first-letter filter
   * @param term the free-text search prefix
   * @param stat translation status filter value
   * @param model the model used by the view
   * @return the list fragment template name
   */
  @GetMapping("/priv/list")
  public String list(
      @RequestParam(name = "lang") String language,
      @RequestParam(name = "letter", required = false) String letter,
      @RequestParam(name = "term", required = false) String term,
      @RequestParam(name = "stat", required = false) String stat,
      Model model) {
    try {
      model.addAttribute("lang", normalizeLanguage(language));
      model.addAttribute("entries", editService.list(language, letter, term, "1".equals(stat)));
      return "views/priv/list";
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  /**
   * Renders the edit form fragment for one entry.
   *
   * @param language the selected language code
   * @param id the entry identifier or N for a new entry
   * @param term optional initial term for a new entry
   * @param model the model used by the view
   * @return the edit fragment template name
   */
  @GetMapping("/priv/edit")
  public String edit(
      @RequestParam(name = "lang") String language,
      @RequestParam(name = "id") String id,
      @RequestParam(name = "term", required = false) String term,
      Model model) {
    try {
      LegacyEntry entry = editService.entry(language, id, term);
      model.addAttribute("entry", entry);
      model.addAttribute("formsYaml", entry.formsAsYaml());
      model.addAttribute("posOptions", posOptions(entry.getLang()));
      return "views/priv/edit";
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  /**
   * Saves an entry and returns the save result fragment.
   *
   * @param language the entry language code
   * @param id the entry identifier or N for a new entry
   * @param entry the entry group identifier
   * @param formsYaml the forms content in YAML format
   * @param pos the part-of-speech code
   * @param translation the translation XML content
   * @param status the translation status value
   * @param principal the authenticated user principal
   * @param model the model used by the view
   * @return the save fragment template name
   */
  @PostMapping("/priv/save")
  public String save(
      @RequestParam(name = "entrylang") String language,
      @RequestParam(name = "id") String id,
      @RequestParam(name = "entry", required = false) String entry,
      @RequestParam(name = "forms") String formsYaml,
      @RequestParam(name = "pos") String pos,
      @RequestParam(name = "trans") String translation,
      @RequestParam(name = "status") String status,
      Principal principal,
      Model model) {
    try {
      String editorId = principal == null ? "0" : principal.getName();
      SaveResult result = editService.save(
          language,
          id,
          entry,
          formsYaml,
          pos,
          translation,
          status,
          editorId);
      model.addAttribute("result", result);
      return "views/priv/save";
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  /**
   * Deletes an entry and returns the delete result fragment.
   *
   * @param language the entry language code
   * @param id the entry identifier
   * @param model the model used by the view
   * @return the delete fragment template name
   */
  @PostMapping("/priv/delete")
  public String delete(
      @RequestParam(name = "entrylang") String language,
      @RequestParam(name = "id") String id,
      Model model) {
    try {
      DeleteResult result = editService.delete(language, id);
      model.addAttribute("result", result);
      return "views/priv/delete";
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  private String normalizeLanguage(String language) {
    if (language == null) {
      throw new IllegalArgumentException("Invalid language.");
    }
    String normalized = language.toLowerCase(Locale.ROOT);
    if (!"hu".equals(normalized) && !"nb".equals(normalized)) {
      throw new IllegalArgumentException("Invalid language.");
    }
    return normalized;
  }

  private Map<String, String> posOptions(String language) {
    Map<String, String> options = new LinkedHashMap<>();
    if ("hu".equals(language)) {
      options.put("ige", "ige");
      options.put("fn", "főnév");
      options.put("hsz", "határozószó");
      options.put("nével", "névelő");
      options.put("röv", "rovidítés");
      options.put("névm", "névmás");
      options.put("mn", "melléknév");
      options.put("ksz", "kötőszó");
      options.put("módsz", "módosítószó");
      options.put("névut", "névutó");
      options.put("isz", "indulatszó");
      options.put("szn", "számnév");
      options.put("igek", "igekötő");
      return options;
    }
    options.put("subst", "substantiv");
    options.put("verb", "verb");
    options.put("adj", "adjektiv");
    options.put("adv", "adverb");
    options.put("prep", "preposisjon");
    options.put("pron", "pronomen");
    options.put("tall", "tallord");
    options.put("konj", "konjunksjon");
    options.put("subj", "subjunksjon");
    options.put("interj", "interjeksjon");
    options.put("fork", "forkortelse");
    options.put("inf", "infinitivsmerke");
    options.put("art", "artikkel");
    return options;
  }

}
