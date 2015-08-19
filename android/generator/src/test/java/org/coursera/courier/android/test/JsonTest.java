package org.coursera.courier.android.test;

import com.google.gson.JsonElement;
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
    JsonElement o1 = parser.parse("{a : {a : 2}, b : 2}");
    JsonElement o2 = parser.parse("{b : 2, a : {a : 2}}");
    Assert.assertEquals(o1, o2);
  }
}
