package net.hunnor.dict.admin.export;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParserServiceTest {

  @Autowired
  private ParserService parserService;

  @Test
  public void testParse() throws ExportException {
    Document document = parserService.parse("<root/>");
    assertNotNull(document);
  }

  @Test(expected = ExportException.class)
  public void testParseError() throws ExportException {
    parserService.parse("<root>");
  }

}
