package net.hunnor.dict.admin.edit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@WebMvcTest(EditController.class)
public class EditControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EditService editService;

  @Test
  public void testIndexProtected() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(value = "admin")
  public void testIndex() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }

}
