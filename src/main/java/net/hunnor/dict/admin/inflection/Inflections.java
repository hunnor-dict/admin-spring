package net.hunnor.dict.admin.inflection;

import java.util.ArrayList;
import java.util.Collections;
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

  /**
   * Returns the list of inflections.
   * The returned list is an unmodifiable copy of the internal list
   * to avoid exposing internal state.
   * @return an unmodifiable list of inflections, or {@code null} if no inflections are set
   */
  public List<Inflection> getInflectionList() {
    if (inflectionList == null) {
      return null;
    }
    return Collections.unmodifiableList(new ArrayList<>(inflectionList));
  }

  public void setInflectionList(List<Inflection> inflections) {
    this.inflectionList = copyInflectionList(inflections);
  }

  private List<Inflection> copyInflectionList(List<Inflection> inflections) {
    if (inflections == null) {
      return null;
    }
    return new ArrayList<>(inflections);
  }

}
