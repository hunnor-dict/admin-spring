package net.hunnor.dict.admin.export;

import net.hunnor.dict.admin.config.Language;

import java.io.OutputStream;

public interface ExportService {

  public void export(Language language, OutputStream outputStream) throws ExportException;

}
