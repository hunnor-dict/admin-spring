package net.hunnor.dict.admin.inflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InflectionsTest {

  @Test
  public void testConstructor() {
    Inflections inflections = new Inflections();
    assertNull(inflections.getInflectionList());
  }

  @Test
  public void testInflectionList() {
    Inflections inflections = new Inflections();
    List<Inflection> inflectionList = new ArrayList<>();
    inflections.setInflectionList(inflectionList);
    assertNotNull(inflections.getInflectionList());
    assertEquals(0, inflections.getInflectionList().size());
  }

}
