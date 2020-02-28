package net.hunnor.dict.admin.config;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Serializer.Property;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializerConfig {

  /**
   * Create an XML serializer.
   * @return the serializer object
   */
  @Bean
  public Serializer serializer() {
    net.sf.saxon.Configuration config = new net.sf.saxon.Configuration();
    Processor processor = new Processor(config);
    Serializer serializer = processor.newSerializer();
    serializer.setOutputProperty(Property.METHOD, "xml");
    serializer.setOutputProperty(Property.INDENT, "yes");
    return serializer;
  }

}
