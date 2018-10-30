package net.hunnor.dict.admin.export;

import org.junit.Test;

public class ExportExceptionTest {

  @Test(expected = ExportException.class)
  public void testException() throws ExportException {
    throw new ExportException(new Exception());
  }

}
