package net.hunnor.dict.admin.export;

import java.io.OutputStream;

public interface WriterService {

  public void createWriter(OutputStream outputStream) throws ExportException;

  public void writeStartDocument(String string, String string2) throws ExportException;

  public void writeEndDocument() throws ExportException;

  public void writeStartElement(String string) throws ExportException;

  public void writeEndElement() throws ExportException;

  public void writeAttribute(String string, String string2) throws ExportException;

  public void writeCharacters(String string) throws ExportException;

  public void flush() throws ExportException;

  public void close() throws ExportException;

}
