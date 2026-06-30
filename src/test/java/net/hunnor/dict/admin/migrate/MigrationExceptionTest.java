package net.hunnor.dict.admin.migrate;

import org.junit.Test;

/**
 * Tests for {@link MigrationException}.
 */
public class MigrationExceptionTest {

  @Test(expected = MigrationException.class)
  public void testException() throws MigrationException {
    throw new MigrationException();
  }

}
