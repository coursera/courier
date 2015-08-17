package org.coursera.courier.android.customtypes;

public class CustomInt {

  private final int i;
  public CustomInt(int i) {
    this.i = i;
  }

  public int value() {
    return i;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CustomInt customInt = (CustomInt) o;

    if (i != customInt.i) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return i;
  }
}
