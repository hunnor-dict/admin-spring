package net.hunnor.dict.admin.export;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ExportController.class)
public class ExportControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ExportService exportService;

  @Test
  public void testExportHu() throws Exception {
    mockMvc.perform(get("/export").param("lang", "HU"))
        .andExpect(status().isOk());
  }

  @Test
  public void testExportNb() throws Exception {
    mockMvc.perform(get("/export").param("lang", "NB"))
        .andExpect(status().isOk());
  }

  @Test
  public void testExportError() throws Exception {
    doThrow(ExportException.class).when(exportService).export(
        ArgumentMatchers.any(), ArgumentMatchers.any());
    mockMvc.perform(get("/export").param("lang", "NB"))
        .andExpect(status().is5xxServerError());
  }

}
