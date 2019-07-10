package net.hunnor.dict.admin.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class ParserServiceImpl implements ParserService {

  private static final Logger logger = LoggerFactory.getLogger(ParserServiceImpl.class);

  @Value("${net.hunnor.dict.admin.export.schema.location}")
  private String schemaLocation;

  private DocumentBuilderFactory factory = null;

  /**
   * Set up the schema and the factory used by the parse method.
   * @throws SAXException if an error occurs while parsing the schema
   * @throws IOException if an error occurs while reading the schema
   */
  @PostConstruct
  public void setUp() throws SAXException, IOException {

    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    InputStream inputStream = ResourceUtils.getURL(schemaLocation).openStream();
    Source streamSource = new StreamSource(inputStream);
    Schema schema = schemaFactory.newSchema(streamSource);

    factory = DocumentBuilderFactory.newInstance();

    try {
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    } catch (ParserConfigurationException ex) {
      logger.error(ex.getMessage(), ex);
    }

    factory.setSchema(schema);
    factory.setIgnoringElementContentWhitespace(true);
    factory.setNamespaceAware(true);

  }

  @Override
  public Document parse(String content) throws ExportException {

    Document document = null;

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      StringReader reader = new StringReader(content);
      InputSource source = new InputSource(reader);
      document = builder.parse(source);
    } catch (ParserConfigurationException | SAXException | IOException ex) {
      throw new ExportException(ex);
    }

    return document;

  }

}
