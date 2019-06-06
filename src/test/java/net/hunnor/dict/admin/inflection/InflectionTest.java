package net.hunnor.dict.admin.inflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InflectionTest {

  @Test
  public void testConstructor() {
    Inflection inflection = new Inflection();
    assertNull(inflection.getParadigms());
    assertNull(inflection.getCodes());
    assertNull(inflection.getSuffixes());
  }

  @Test
  public void testParadigms() {
    Inflection inflection = new Inflection();
    inflection.setParadigms("paradigms");
    assertEquals("paradigms", inflection.getParadigms());
  }

  @Test
  public void testCodes() {
    Inflection inflection = new Inflection();
    inflection.setCodes("codes");
    assertEquals("codes", inflection.getCodes());
  }

  @Test
  public void testSuffixes() {
    Inflection inflection = new Inflection();
    inflection.setSuffixes("suffixes");
    assertEquals("suffixes", inflection.getSuffixes());
  }

}
