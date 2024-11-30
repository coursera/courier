package org.coursera.courier;

import com.intellij.lang.Language;

public class CourierLanguage extends Language {
  public static final CourierLanguage INSTANCE = new CourierLanguage();

  private CourierLanguage() {
    super("Courier");
  }
}
