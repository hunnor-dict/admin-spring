package net.hunnor.dict.admin.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import net.hunnor.dict.admin.config.Language;
import org.junit.Test;

public class LemmaTest {

  @Test
  public void testDefaultConstructor() {
    Lemma lemma = new Lemma();
    assertEquals(0, lemma.getId());
    assertNull(lemma.getGrunnform());
    assertNull(lemma.getParadigmeId());
  }

  @Test
  public void testAllFieldsConstructor() {
    Lemma lemma = new Lemma(1, "grunnform", new ArrayList<>());
    assertEquals(1, lemma.getId());
    assertEquals("grunnform", lemma.getGrunnform());
    assertNotNull(lemma.getParadigmeId());
    assertEquals(0, lemma.getParadigmeId().size());
  }

  @Test
  public void testId() {
    Lemma lemma = new Lemma();
    lemma.setId(1);
    assertEquals(1, lemma.getId());
  }

  @Test
  public void testGrunnform() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("grunnform");
    assertEquals("grunnform", lemma.getGrunnform());
  }

  @Test
  public void testWeight() {
    Lemma lemma = new Lemma();
    lemma.setWeight(1);
    assertEquals(1, lemma.getWeight());
  }

  @Test
  public void testParadigmeId() {
    Lemma lemma = new Lemma();
    lemma.setParadigmeId(new ArrayList<>());
    assertNotNull(lemma.getParadigmeId());
    assertEquals(0, lemma.getParadigmeId().size());
  }

  @Test
  public void testParadigmeIdSetterDefensiveCopy() {
    Lemma lemma = new Lemma();
    List<String> paradigmeIds = new ArrayList<>();
    lemma.setParadigmeId(paradigmeIds);
    paradigmeIds.add("x");
    assertEquals(0, lemma.getParadigmeId().size());
  }

  @Test
  public void testParadigmeIdGetterDoesNotExposeInternalList() {
    Lemma lemma = new Lemma();
    List<String> paradigmeIds = new ArrayList<>();
    paradigmeIds.add("x");
    lemma.setParadigmeId(paradigmeIds);
    try {
      lemma.getParadigmeId().add("y");
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException expected) {
      assertEquals(1, lemma.getParadigmeId().size());
    }
  }

  @Test
  public void testFirstLetterDefaultLanguage() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("abc");
    assertEquals("A", lemma.getFirstLetter(Language.DEFAULT));
  }

  @Test
  public void firstLetterIsNull() {
    Lemma lemma = new Lemma();
    assertNull(lemma.getFirstLetter(Language.HU));
  }

  @Test
  public void firstLetterIsEmpty() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("");
    assertNull(lemma.getFirstLetter(Language.HU));
  }

  @Test
  public void firstLetterIsWithoutLetters() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("123");
    assertNull(lemma.getFirstLetter(Language.HU));
  }

  @Test
  public void firstLetterIsUppercase() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("abc");
    assertEquals("A", lemma.getFirstLetter(Language.HU));
  }

  @Test
  public void firstLetterIsNormalized() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("àbc");
    assertEquals("A", lemma.getFirstLetter(Language.NB));
  }

  @Test
  public void firstLetterIsLetter() {
    Lemma lemma = new Lemma();
    lemma.setGrunnform("1-abc");
    assertEquals("A", lemma.getFirstLetter(Language.HU));
  }

  @Test
  public void firstLetterIsHungarian() {

    Lemma lemma = new Lemma();

    lemma.setGrunnform("álom");
    assertEquals("A", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("cukor");
    assertEquals("C", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("csiga");
    assertEquals("CS", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("derűs");
    assertEquals("D", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("dz");
    assertEquals("DZ", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("dzxyz");
    assertEquals("DZ", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("dzs");
    assertEquals("DZS", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("dzsungel");
    assertEquals("DZS", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("élet");
    assertEquals("E", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("gitár");
    assertEquals("G", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("gyík");
    assertEquals("GY", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("í");
    assertEquals("I", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("lakás");
    assertEquals("L", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("lyuk");
    assertEquals("LY", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("nép");
    assertEquals("N", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("nyúl");
    assertEquals("NY", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("óra");
    assertEquals("O", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("öröm");
    assertEquals("Ö", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("ő");
    assertEquals("Ö", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("sereg");
    assertEquals("S", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("szél");
    assertEquals("SZ", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("tó");
    assertEquals("T", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("tyúk");
    assertEquals("TY", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("út");
    assertEquals("U", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("üres");
    assertEquals("Ü", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("űr");
    assertEquals("Ü", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("zene");
    assertEquals("Z", lemma.getFirstLetter(Language.HU));

    lemma.setGrunnform("zsiráf");
    assertEquals("ZS", lemma.getFirstLetter(Language.HU));

  }

}
