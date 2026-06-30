package net.hunnor.dict.admin.export;

/**
 * Exception thrown when an error occurs during export.
 */
public class ExportException extends Exception {

  private static final long serialVersionUID = -2807508159837048883L;

  public ExportException(Exception ex) {
    super(ex);
  }

}
