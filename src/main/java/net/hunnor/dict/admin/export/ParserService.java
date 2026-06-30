package net.hunnor.dict.admin.export;

import org.w3c.dom.Document;

/**
 * Interface for parsing XML documents.
 */
public interface ParserService {

  public Document parse(String content) throws ExportException;

}
