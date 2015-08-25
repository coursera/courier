package org.coursera.courier.android.test;

import com.google.gson.JsonParser;
import org.junit.Assert;

import java.util.Scanner;

abstract class JsonTest {
  protected String load(String filename) {
    String fullpath = "/test/records/" + filename;
    return new Scanner(getClass().getResourceAsStream(fullpath), "UTF-8").useDelimiter("\\A").next();
  }

  protected void assertJsonEquals(String expected, String actual) {
    JsonParser parser = new JsonParser();
    Assert.assertEquals(parser.parse(expected), parser.parse(actual));
  }
}
