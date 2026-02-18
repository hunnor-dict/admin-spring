package net.hunnor.dict.admin.edit;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LegacyEntry {

  private String lang;

  private String id;

  private Integer entry;

  private String form;

  private String pos;

  private Integer status;

  private String trans;

  private boolean exists;

  private final LinkedHashMap<String, LinkedHashMap<Integer, String>> forms = new LinkedHashMap<>();

  /**
   * Returns language code.
   *
   * @return language code
   */
  public String getLang() {
    return lang;
  }

  /**
   * Sets language code.
   *
   * @param lang language code
   */
  public void setLang(String lang) {
    this.lang = lang;
  }

  /**
   * Returns entry id.
   *
   * @return entry id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets entry id.
   *
   * @param id entry id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns entry group id.
   *
   * @return entry group id
   */
  public Integer getEntry() {
    return entry;
  }

  /**
   * Sets entry group id.
   *
   * @param entry entry group id
   */
  public void setEntry(Integer entry) {
    this.entry = entry;
  }

  /**
   * Returns primary form value.
   *
   * @return primary form value
   */
  public String getForm() {
    return form;
  }

  /**
   * Sets primary form value.
   *
   * @param form primary form value
   */
  public void setForm(String form) {
    this.form = form;
  }

  /**
   * Returns part-of-speech code.
   *
   * @return part-of-speech code
   */
  public String getPos() {
    return pos;
  }

  /**
   * Sets part-of-speech code.
   *
   * @param pos part-of-speech code
   */
  public void setPos(String pos) {
    this.pos = pos;
  }

  /**
   * Returns status value.
   *
   * @return status value
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * Sets status value.
   *
   * @param status status value
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * Returns translation XML fragment.
   *
   * @return translation XML fragment
   */
  public String getTrans() {
    return trans;
  }

  /**
   * Sets translation XML fragment.
   *
   * @param trans translation XML fragment
   */
  public void setTrans(String trans) {
    this.trans = trans;
  }

  /**
   * Returns existence state.
   *
   * @return true if entry exists
   */
  public boolean isExists() {
    return exists;
  }

  /**
   * Sets existence state.
   *
   * @param exists true if entry exists
   */
  public void setExists(boolean exists) {
    this.exists = exists;
  }

  /**
   * Returns forms grouped by paradigm and sequence.
   *
   * @return forms map
   */
  public LinkedHashMap<String, LinkedHashMap<Integer, String>> getForms() {
    return copyForms(forms);
  }

  /**
   * Adds or updates one form value in the internal forms map.
   *
   * @param par paradigm key
   * @param seq sequence key
   * @param orth orthographic form value
   */
  public void putForm(String par, Integer seq, String orth) {
    forms.computeIfAbsent(par, key -> new LinkedHashMap<>()).put(seq, orth);
  }

  /**
   * Returns sorted form-group keys joined by colon.
   *
   * @return joined form-group keys
   */
  public String getFormsKeysJoined() {
    return forms.keySet().stream()
        .sorted(Comparator.naturalOrder())
        .collect(Collectors.joining(":"));
  }

  /**
   * Returns external dictionary link for the primary form.
   *
   * @return external dictionary URL
   */
  public String getBobLink() {
    String opp = form == null ? "" : URLEncoder.encode(form, StandardCharsets.UTF_8);
    return "http://ordbok.uib.no/perl/ordbok.cgi?OPP="
        + opp
        + "&ant_bokmaal=5&ant_nynorsk=5&bokmaal=+&ordbok=bokmaal";
  }

  /**
   * Serializes forms to YAML text used by the editor.
   *
   * @return YAML string representation of forms
   */
  public String formsAsYaml() {
    StringBuilder builder = new StringBuilder("---\n");
    for (Map.Entry<String, LinkedHashMap<Integer, String>> parEntry : forms.entrySet()) {
      builder
          .append('"')
          .append(escapeYaml(parEntry.getKey()))
          .append('"')
          .append(':')
          .append('\n');
      for (Map.Entry<Integer, String> seqEntry : parEntry.getValue().entrySet()) {
        builder
            .append("  ")
            .append(seqEntry.getKey())
            .append(": ")
            .append('"')
            .append(escapeYaml(seqEntry.getValue()))
            .append('"')
            .append('\n');
      }
    }
    return builder.toString();
  }

  private String escapeYaml(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private LinkedHashMap<String, LinkedHashMap<Integer, String>> copyForms(
      LinkedHashMap<String, LinkedHashMap<Integer, String>> source) {
    LinkedHashMap<String, LinkedHashMap<Integer, String>> copy = new LinkedHashMap<>();
    for (Map.Entry<String, LinkedHashMap<Integer, String>> parEntry : source.entrySet()) {
      copy.put(parEntry.getKey(), new LinkedHashMap<>(parEntry.getValue()));
    }
    return copy;
  }

}
