package net.hunnor.dict.admin.export;

import org.w3c.dom.Document;

public interface ParserService {

  public Document parse(String content) throws ExportException;

}
