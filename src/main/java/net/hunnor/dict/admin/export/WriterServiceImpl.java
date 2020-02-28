package net.hunnor.dict.admin.export;

import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WriterServiceImpl implements WriterService {

  @Value("${net.hunnor.dict.admin.export.namespace}")
  private String namespace;

  @Autowired
  private Serializer serializer;

  private XMLStreamWriter writer = null;

  public void setWriter(XMLStreamWriter writer) {
    this.writer = writer;
  }

  /**
   * Create the XML writer.
   */
  public void createWriter(OutputStream outputStream) throws ExportException {
    serializer.setOutputStream(outputStream);
    try {
      writer = serializer.getXMLStreamWriter();
    } catch (SaxonApiException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Write the document start.
   * @param encoding the encoding
   * @param version the version
   * @throws ExportException if write fails
   */
  public void writeStartDocument(String encoding, String version) throws ExportException {
    try {
      writer.writeStartDocument(encoding, version);
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Write the document end.
   * @throws ExportException if write fails
   */
  public void writeEndDocument() throws ExportException {
    try {
      writer.writeEndDocument();
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Write a start element.
   * @param name the element name
   * @throws ExportException if write fails
   */
  public void writeStartElement(String name) throws ExportException {
    try {
      writer.writeStartElement(namespace, name);
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Write an end element.
   * @throws ExportException if write fails
   */
  public void writeEndElement() throws ExportException {
    try {
      writer.writeEndElement();
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Write an attribute.
   * @param name the attribute name
   * @param value the attribute value
   * @throws ExportException if write fails
   */
  public void writeAttribute(String name, String value) throws ExportException {
    try {
      writer.writeAttribute(name, value);
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Write characters.
   * @param characters the characters to write
   * @throws ExportException if write fails
   */
  public void writeCharacters(String characters) throws ExportException {
    try {
      writer.writeCharacters(characters);
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Flush the output.
   * @throws ExportException if flush fails
   */
  public void flush() throws ExportException {
    try {
      writer.flush();
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

  /**
   * Close the output.
   * @throws ExportException if close fails
   */
  public void close() throws ExportException {
    try {
      writer.close();
    } catch (XMLStreamException ex) {
      throw new ExportException(ex);
    }
  }

}
