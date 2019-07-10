package net.hunnor.dict.admin.inflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hunnor.dict.admin.inflection.InflectionService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InflectionServiceTest {

  @Autowired
  private InflectionService inflectionService;

  @Test
  public void testCodesNull() {
    List<String> paradigms = null;
    assertNull(inflectionService.getCodes(paradigms));
  }

  @Test
  public void testCodesEmpty() {
    List<String> paradigms = new ArrayList<>();
    assertNull(inflectionService.getCodes(paradigms));
  }

  @Test
  public void testCodes() {
    List<String> paradigms = Arrays.asList("700");
    assertEquals("m1", inflectionService.getCodes(paradigms));
  }

  @Test
  public void testSuffixesNull() {
    List<String> paradigms = null;
    assertNull(inflectionService.getSuffixes(paradigms));
  }

  @Test
  public void testSuffixesEmpty() {
    List<String> paradigms = new ArrayList<>();
    assertNull(inflectionService.getSuffixes(paradigms));
  }

  @Test
  public void testSuffixes() {
    List<String> paradigms = Arrays.asList("700");
    assertEquals("-en", inflectionService.getSuffixes(paradigms));
  }

  @Test
  public void testInflLemmaNull() {
    Map<Integer, String> inflections = inflectionService.getInflections(null, null);
    assertNull(inflections);
  }

  @Test
  public void testInflPatternsNull() {
    Map<Integer, String> inflections = inflectionService.getInflections("lemma", null);
    assertNull(inflections);
  }

  @Test
  public void testInflPatternsEmpty() {
    Map<Integer, String> inflections = inflectionService.getInflections("lemma", new HashMap<>());
    assertNotNull(inflections);
    assertEquals(0, inflections.size());
  }

  @Test
  public void testInflBasePatternEmpty() {
    Map<Integer, String> patterns = new HashMap<>();
    patterns.put(1, "");
    patterns.put(2, "en");
    patterns.put(3, "er");
    patterns.put(4, "ene");
    Map<Integer, String> inflections = inflectionService.getInflections("bil", patterns);
    assertNotNull(inflections);
    assertEquals(4, inflections.size());
    assertEquals("bil", inflections.get(1));
    assertEquals("bilen", inflections.get(2));
    assertEquals("biler", inflections.get(3));
    assertEquals("bilene", inflections.get(4));
  }

  @Test
  public void testInflBasePatternNull() {
    Map<Integer, String> patterns = new HashMap<>();
    patterns.put(1, null);
    patterns.put(2, "en");
    patterns.put(3, "er");
    patterns.put(4, "ene");
    Map<Integer, String> inflections = inflectionService.getInflections("bil", patterns);
    assertNotNull(inflections);
    assertEquals(4, inflections.size());
    assertEquals("bil", inflections.get(1));
    assertEquals("bilen", inflections.get(2));
    assertEquals("biler", inflections.get(3));
    assertEquals("bilene", inflections.get(4));
  }

  @Test
  public void testInflBasePatternNotEmpty() {
    Map<Integer, String> patterns = new HashMap<>();
    patterns.put(1, "e");
    patterns.put(2, "a");
    patterns.put(3, "er");
    patterns.put(4, "ene");
    Map<Integer, String> inflections = inflectionService.getInflections("jente", patterns);
    assertNotNull(inflections);
    assertEquals(4, inflections.size());
    assertEquals("jente", inflections.get(1));
    assertEquals("jenta", inflections.get(2));
    assertEquals("jenter", inflections.get(3));
    assertEquals("jentene", inflections.get(4));
  }

  @Test
  public void testInflPercent() {
    Map<Integer, String> patterns = new HashMap<>();
    patterns.put(1, "e%");
    patterns.put(2, "%a");
    patterns.put(3, "%er");
    patterns.put(4, "%ene");
    Map<Integer, String> inflections = inflectionService.getInflections("aksel", patterns);
    assertNotNull(inflections);
    assertEquals(4, inflections.size());
    assertEquals("aksel", inflections.get(1));
    assertEquals("aksla", inflections.get(2));
    assertEquals("aksler", inflections.get(3));
    assertEquals("akslene", inflections.get(4));
  }

  @Test
  public void testInflPlus() {
    Map<Integer, String> patterns = new HashMap<>();
    patterns.put(1, "+");
    patterns.put(2, "++en");
    patterns.put(3, "++er");
    patterns.put(4, "++ene");
    Map<Integer, String> inflections = inflectionService.getInflections("sum", patterns);
    assertNotNull(inflections);
    assertEquals(4, inflections.size());
    assertEquals("sum", inflections.get(1));
    assertEquals("summen", inflections.get(2));
    assertEquals("summer", inflections.get(3));
    assertEquals("summene", inflections.get(4));
  }

  @Test
  public void testInflPercentPlus() {
    Map<Integer, String> patterns = new HashMap<>();
    patterns.put(1, "++e%");
    patterns.put(2, "++e%en");
    patterns.put(3, "+%e");
    patterns.put(4, "+%ene");
    Map<Integer, String> inflections = inflectionService.getInflections("hammer", patterns);
    assertNotNull(inflections);
    assertEquals(4, inflections.size());
    assertEquals("hammer", inflections.get(1));
    assertEquals("hammeren", inflections.get(2));
    assertEquals("hamre", inflections.get(3));
    assertEquals("hamrene", inflections.get(4));
  }

}
