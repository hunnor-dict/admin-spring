package net.hunnor.dict.admin.migrate;

import org.junit.Test;

public class MigrationExceptionTest {

  @Test(expected = MigrationException.class)
  public void testException() throws MigrationException {
    throw new MigrationException();
  }

}
