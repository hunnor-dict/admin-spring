package net.hunnor.dict.admin.config;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CollatorConfig {

  private static final String RULES_HU = "< a, A, á, Á < b, B < c, C < d, D < e, E, é, É  < f, F"
      + " < g, G < h, H < i, I, í, Í < j, J < k, K < l, L < m, M < n, N < o, O, ó, Ó < ö, Ö, ő, Ő"
      + " < p, P < q, Q < r, R < s, S < t, T < u, U, ú, Ú < ü, Ü, ű, Ű < v, V < w, W < x, X"
      + " < y, Y < z, Z";

  private static final String RULES_NB = "< a, A < b, B < c, C < d, D < e, E  < f, F < g, G < h, H"
      + " < i, I < j, J < k, K < l, L < m, M < n, N < o, O < p, P < q, Q < r, R < s, S"
      + " < t, T < u, U < v, V < w, W < x, X < y, Y < z, Z < æ, Æ < ø, Ø < å, Å";

  @Bean
  @Qualifier("HU")
  Collator hungarianCollator() throws ParseException {
    return new RuleBasedCollator(RULES_HU);
  }

  @Bean
  @Qualifier("NB")
  Collator norwegianCollator() throws ParseException {
    return new RuleBasedCollator(RULES_NB);
  }

}
