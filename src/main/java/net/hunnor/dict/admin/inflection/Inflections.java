package net.hunnor.dict.admin.inflection;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Inflections {

  @XmlElement(name = "inflection")
  private List<Inflection> inflectionList;

  public List<Inflection> getInflectionList() {
    return inflectionList;
  }

  public void setInflectionList(List<Inflection> inflections) {
    this.inflectionList = inflections;
  }

}
