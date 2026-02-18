package net.hunnor.dict.admin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.hunnor.dict.admin.config.Language;

public class Entry {

  private int id;

  private int status;

  private String pos;

  private String translation;

  private List<Lemma> lemmata;

  public Entry() {
  }

  public Entry(int id, int status) {
    this.id = id;
    this.status = status;
  }

  /**
   * Constructor with all fields.
   * @param id the id of the new Entry
   * @param status the translation status
   * @param pos the part of speech
   * @param translation the translation as XML
   */
  public Entry(int id, int status, String pos, String translation) {
    this.id = id;
    this.status = status;
    this.pos = pos;
    this.translation = translation;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getPos() {
    return pos;
  }

  public void setPos(String pos) {
    this.pos = pos;
  }

  public String getTranslation() {
    return translation;
  }

  public void setTranslation(String translation) {
    this.translation = translation;
  }

  /**
   * Returns the list of lemmata.
   * The returned list is an unmodifiable copy of the internal list
   * to avoid exposing internal state.
   * @return an unmodifiable list of lemmata, or {@code null} if no lemmata are set
   */
  public List<Lemma> getLemmata() {
    if (lemmata == null) {
      return null;
    }
    return Collections.unmodifiableList(new ArrayList<>(lemmata));
  }

  public void setLemmata(List<Lemma> lemmata) {
    this.lemmata = copyLemmata(lemmata);
  }

  private List<Lemma> copyLemmata(List<Lemma> lemmata) {
    if (lemmata == null) {
      return null;
    }
    return new ArrayList<>(lemmata);
  }

  /**
   * Returns the letter the entry belongs to.
   * @param language the language which alphabet to use
   * @return a letter from the alphabet of the specified language
   */
  public String getFirstLetter(Language language) {
    String firstLetter = null;
    if (lemmata != null && !lemmata.isEmpty()) {
      Lemma lemma = lemmata.get(0);
      firstLetter = lemma.getFirstLetter(language);
    }
    return firstLetter;
  }

  /**
   * Generate the key by which entries are sorted alphabetically.
   * @return the sort key
   */
  public String getSortKey() {
    String key = null;
    if (lemmata != null && !lemmata.isEmpty()) {
      Lemma lemma = lemmata.get(0);
      if (lemma.getGrunnform() != null) {
        key = lemma.getGrunnform().toLowerCase(Locale.getDefault());
      }
    }
    if (pos != null) {
      switch (pos) {
        case "fn":
        case "subst":
          key = key + "0";
          break;
        case "ige":
        case "verb":
          key = key + "1";
          break;
        case "mn":
        case "adj":
          key = key + "2";
          break;
        default:
          key = key + "9";
          break;
      }
      key = key + pos;
    }
    if (key != null) {
      key = key + String.format("%08d", id);
    }
    return key;
  }

}
