package net.hunnor.dict.admin.inflection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class UnmarshallerConfig {

  /**
   * Create a JAXB marshaller for the inflection classes.
   * @return the marshaller object
   */
  @Bean
  public Unmarshaller unmarshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setClassesToBeBound(Inflections.class, Inflection.class);
    return jaxb2Marshaller;
  }

}
