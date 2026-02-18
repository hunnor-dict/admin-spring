package net.hunnor.dict.admin.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import net.hunnor.dict.admin.config.Language;
import org.junit.Test;

public class EntryTest {

  @Test
  public void testDefaultConstructor() {
    Entry entry = new Entry();
    assertEquals(0, entry.getId());
    assertEquals(0, entry.getStatus());
    assertNull(entry.getPos());
    assertNull(entry.getTranslation());
    assertNull(entry.getLemmata());
  }

  @Test
  public void testIdStatusConstructor() {
    Entry entry = new Entry(1, 2);
    assertEquals(1, entry.getId());
    assertEquals(2, entry.getStatus());
    assertNull(entry.getPos());
    assertNull(entry.getTranslation());
    assertNull(entry.getLemmata());
  }

  @Test
  public void testAllFieldsConstructor() {
    Entry entry = new Entry(1, 2, "pos", "translation");
    assertEquals(1, entry.getId());
    assertEquals(2, entry.getStatus());
    assertEquals("pos", entry.getPos());
    assertEquals("translation", entry.getTranslation());
    assertNull(entry.getLemmata());
  }

  @Test
  public void testId() {
    Entry entry = new Entry();
    entry.setId(1);
    assertEquals(1, entry.getId());
  }

  @Test
  public void testStatus() {
    Entry entry = new Entry();
    entry.setStatus(1);
    assertEquals(1, entry.getStatus());
  }

  @Test
  public void testPos() {
    Entry entry = new Entry();
    entry.setPos("pos");
    assertEquals("pos", entry.getPos());
  }

  @Test
  public void testTranslation() {
    Entry entry = new Entry();
    entry.setTranslation("translation");
    assertEquals("translation", entry.getTranslation());
  }

  @Test
  public void testLemmata() {
    Entry entry = new Entry();
    entry.setLemmata(new ArrayList<>());
    assertNotNull(entry.getLemmata());
    assertEquals(0, entry.getLemmata().size());
  }

  @Test
  public void testFirstLetterNull() {
    Entry entry = new Entry();
    assertNull(entry.getFirstLetter(Language.HU));
  }

  @Test
  public void testFirstLetterEmpty() {
    Entry entry = new Entry();
    entry.setLemmata(new ArrayList<>());
    assertNull(entry.getFirstLetter(Language.HU));
  }

  @Test
  public void testFirstLetter() {
    Entry entry = new Entry();
    List<Lemma> lemmata = new ArrayList<>();
    lemmata.add(new Lemma(1, "abc", null));
    entry.setLemmata(lemmata);
    assertEquals("A", entry.getFirstLetter(Language.HU));
  }

  @Test
  public void testSortKeyNull() {
    Entry entry = new Entry();
    assertNull(entry.getSortKey());
  }

  @Test
  public void testSortKeyEmpty() {
    Entry entry = new Entry();
    entry.setLemmata(new ArrayList<>());
    assertNull(entry.getSortKey());
  }

  @Test
  public void testSortKeyGrunnformNull() {
    Entry entry = new Entry();
    List<Lemma> lemmata = new ArrayList<>();
    lemmata.add(new Lemma());
    entry.setLemmata(lemmata);
    assertNull(entry.getSortKey());
  }

  @Test
  public void testSortKey() {
    Entry entry = new Entry();
    List<Lemma> lemmata = new ArrayList<>();
    lemmata.add(new Lemma(1, "abc", null));
    entry.setLemmata(lemmata);
    assertEquals("abc00000000", entry.getSortKey());
  }

  @Test
  public void testLemmataSetterDefensiveCopy() {
    Entry entry = new Entry();
    List<Lemma> lemmata = new ArrayList<>();
    entry.setLemmata(lemmata);
    lemmata.add(new Lemma(1, "abc", null));
    assertEquals(0, entry.getLemmata().size());
  }

  @Test
  public void testLemmataGetterDoesNotExposeInternalList() {
    Entry entry = new Entry();
    List<Lemma> lemmata = new ArrayList<>();
    lemmata.add(new Lemma(1, "abc", null));
    entry.setLemmata(lemmata);
    try {
      entry.getLemmata().add(new Lemma());
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(1, entry.getLemmata().size());
    }
  }

}
