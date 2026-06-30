package net.hunnor.dict.admin.export;

import org.junit.Test;

/**
 * Test for {@link ExportException}.
 */
public class ExportExceptionTest {

  @Test(expected = ExportException.class)
  public void testException() throws ExportException {
    throw new ExportException(new Exception());
  }

}
