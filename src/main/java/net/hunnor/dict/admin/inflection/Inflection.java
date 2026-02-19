package net.hunnor.dict.admin.inflection;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Inflection {

  @XmlElement(name = "paradigms")
  private String paradigms;

  @XmlElement(name = "codes")
  private String codes;

  @XmlElement(name = "suffixes")
  private String suffixes;

  public String getParadigms() {
    return paradigms;
  }

  public void setParadigms(String paradigms) {
    this.paradigms = paradigms;
  }

  public String getCodes() {
    return codes;
  }

  public void setCodes(String codes) {
    this.codes = codes;
  }

  public String getSuffixes() {
    return suffixes;
  }

  public void setSuffixes(String suffixes) {
    this.suffixes = suffixes;
  }

}
