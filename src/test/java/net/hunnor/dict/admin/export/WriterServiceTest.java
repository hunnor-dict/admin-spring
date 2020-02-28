package net.hunnor.dict.admin.export;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WriterServiceTest {

  @Autowired
  private WriterService writerService;

  @Mock
  private XMLStreamWriter mockWriter;

  @SpyBean
  private Serializer mockSerializer;

  @Test
  public void testWriter() throws ExportException {

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    writerService.createWriter(outputStream);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeStartElement("document");
    writerService.writeAttribute("type", "1");
    writerService.writeCharacters("content");
    writerService.writeEndElement();
    writerService.writeEndDocument();

    writerService.flush();
    writerService.close();

    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<document xmlns=\"http://dict.hunnor.net\" type=\"1\">content</document>\n",
        outputStream.toString(StandardCharsets.UTF_8));

  }

  @Test(expected = ExportException.class)
  public void testStartDocumentError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .writeStartDocument(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");

  }

  @Test(expected = ExportException.class)
  public void testEndDocumentError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .writeEndDocument();
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeEndDocument();

  }

  @Test(expected = ExportException.class)
  public void testStartElementError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .writeStartElement(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeStartElement("root");

  }

  @Test(expected = ExportException.class)
  public void testEndElementError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .writeEndElement();
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeStartElement("root");
    writerService.writeEndElement();

  }

  @Test(expected = ExportException.class)
  public void testAttributeError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .writeAttribute(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeStartElement("root");
    writerService.writeAttribute("type", "1");

  }

  @Test(expected = ExportException.class)
  public void testCharactersError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .writeCharacters(ArgumentMatchers.anyString());
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeStartElement("root");
    writerService.writeCharacters("content");

  }

  @Test(expected = ExportException.class)
  public void testFlushError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .flush();
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeEndDocument();

    writerService.flush();

  }

  @Test(expected = ExportException.class)
  public void testCloseError() throws ExportException, XMLStreamException {

    writerService.createWriter(new ByteArrayOutputStream());

    doThrow(XMLStreamException.class).when(mockWriter)
        .close();
    writerService.setWriter(mockWriter);

    writerService.writeStartDocument("UTF-8", "1.0");
    writerService.writeEndDocument();

    writerService.flush();
    writerService.close();

  }

  @Test(expected = ExportException.class)
  public void testSerializerError() throws ExportException, SaxonApiException {

    doThrow(new SaxonApiException(new Exception())).when(mockSerializer).getXMLStreamWriter();

    writerService.createWriter(new ByteArrayOutputStream());

  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

}
