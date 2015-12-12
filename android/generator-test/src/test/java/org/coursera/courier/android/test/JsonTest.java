package org.coursera.courier.android.test;

import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

abstract class JsonTest {
  private static File jsonPath =
    new File(System.getProperty("referencesuite.srcdir") +
      File.separator + "main" + File.separator + "json");

  protected String load(String filename) {
    try {
      return FileUtils.readFileToString(new File(jsonPath, filename));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void assertJsonEquals(String expected, String actual) {
    JsonParser parser = new JsonParser();
    Assert.assertEquals(parser.parse(expected), parser.parse(actual));
  }
}
