package net.hunnor.dict.admin.migrate;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MigrationController {

  private static final Logger logger = LoggerFactory.getLogger(MigrationController.class);

  @Autowired
  private MigrationService migrationService;

  /**
   * Start the migration to the new version of the HunNor database.
   */
  @PostMapping(value = "/migrate")
  public void migrate(HttpServletResponse response) {
    try {
      migrationService.migrate();
    } catch (MigrationException ex) {
      logger.error(ex.getMessage(), ex);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
  }

}
