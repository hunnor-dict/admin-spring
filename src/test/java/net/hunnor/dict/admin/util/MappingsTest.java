package net.hunnor.dict.admin.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the ID mappings.
 */
public class MappingsTest {

  @Test
  public void testMappings() {
    int id = Mappings.getIdMapping(2310);
    assertEquals(2522, id);
  }

}
