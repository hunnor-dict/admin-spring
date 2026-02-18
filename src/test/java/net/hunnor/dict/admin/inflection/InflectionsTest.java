package net.hunnor.dict.admin.inflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

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

  @Test
  public void testInflectionListSetterDefensiveCopy() {
    Inflections inflections = new Inflections();
    List<Inflection> inflectionList = new ArrayList<>();
    inflections.setInflectionList(inflectionList);
    inflectionList.add(new Inflection());
    assertEquals(0, inflections.getInflectionList().size());
  }

  @Test
  public void testInflectionListGetterDoesNotExposeInternalList() {
    Inflections inflections = new Inflections();
    List<Inflection> inflectionList = new ArrayList<>();
    inflectionList.add(new Inflection());
    inflections.setInflectionList(inflectionList);
    try {
      inflections.getInflectionList().add(new Inflection());
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(1, inflections.getInflectionList().size());
    }
  }

}
