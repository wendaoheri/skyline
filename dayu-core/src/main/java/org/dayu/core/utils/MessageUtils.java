package org.dayu.core.utils;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Component
public class MessageUtils {

  @Autowired
  private MessageSource messageSource;

  @Value("${spring.messages.default-lang:en_US}")
  private String defaultLang;

  public String getMessage(String code, String... args) {
    return this.getMessage(code, null, args);
  }

  public String getMessage(String code, Locale locale, String... args) {
    if (locale == null) {
      locale = getDefaultLocale();
    }
    return messageSource.getMessage(code, args, locale);
  }

  private Locale getDefaultLocale() {
    return new Locale(defaultLang);
  }

}
