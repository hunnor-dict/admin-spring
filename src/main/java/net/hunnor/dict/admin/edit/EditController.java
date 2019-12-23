package net.hunnor.dict.admin.edit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditController {

  @GetMapping("/")
  public String index() {
    return "views/index";
  }

}
