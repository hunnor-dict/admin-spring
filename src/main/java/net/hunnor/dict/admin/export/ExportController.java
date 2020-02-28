package net.hunnor.dict.admin.export;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import net.hunnor.dict.admin.config.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExportController {

  private static final Logger logger = LoggerFactory.getLogger(ExportController.class);

  @Autowired
  private ExportService exportService;

  /**
   * Exports the dictionary database as XML.
   * @param response the HTTP response object
   */
  @GetMapping("/export")
  public void export(HttpServletResponse response, @RequestParam(name = "lang") Language language) {
    try {
      OutputStream outputStream = response.getOutputStream();
      exportService.export(language, outputStream);
    } catch (IOException | ExportException ex) {
      logger.error(ex.getMessage(), ex);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}
