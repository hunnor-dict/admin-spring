package net.hunnor.dict.admin.export;

import java.io.OutputStream;
import net.hunnor.dict.admin.config.Language;

public interface ExportService {

  public void export(Language language, OutputStream outputStream) throws ExportException;

}
