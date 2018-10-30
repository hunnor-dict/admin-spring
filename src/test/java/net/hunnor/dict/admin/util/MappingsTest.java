package net.hunnor.dict.admin.util;

import static org.junit.Assert.assertEquals;

import net.hunnor.dict.admin.util.Mappings;

import org.junit.Test;

public class MappingsTest {

  @Test
  public void testMappings() {
    int id = Mappings.getIdMapping(2310);
    assertEquals(2522, id);
  }

}
