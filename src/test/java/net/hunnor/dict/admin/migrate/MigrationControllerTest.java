package net.hunnor.dict.admin.migrate;

import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MigrationController.class)
public class MigrationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MigrationService migrationService;

  @Test
  @WithMockUser(value = "admin")
  public void testMigration() throws Exception {
    mockMvc.perform(post("/migrate").with(csrf().asHeader()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(value = "admin")
  public void testMigrationError() throws Exception {
    doThrow(MigrationException.class).when(migrationService).migrate();
    mockMvc.perform(post("/migrate").with(csrf().asHeader()))
        .andExpect(status().is5xxServerError());
  }

}
