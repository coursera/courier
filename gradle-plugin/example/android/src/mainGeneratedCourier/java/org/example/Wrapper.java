package org.example;
import com.google.gson.annotations.JsonAdapter;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import org.coursera.courier.android.runtime.TypedDefinitionAdapterFactory;
import org.coursera.courier.android.runtime.UnionAdapterFactory;
import java.util.Objects;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Generated(value = "Wrapper", comments="Courier generated GSON data binding for Android")
public final class Wrapper {
  Wrapper() {
    fortune = null;
  }
  
  public Wrapper(org.example.Fortune fortune) {
    this.fortune = fortune;
  }
  
  public final org.example.Fortune fortune;
  
  public void validate() {
    if(fortune == null) {
    }
  }
  
  private transient volatile Integer hashCode = null;
  @Override
  public int hashCode() {
    if (hashCode != null) return hashCode.intValue();
    hashCode = Objects.hash(this.getClass(), fortune);
    return hashCode.intValue();
  }
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Wrapper)) return false;
    Wrapper other = (Wrapper)obj;
    if (other == this) return true;
    return  Objects.deepEquals(this.fortune, other.fortune) && true;
  }
  public static class Builder {
    public transient org.example.Fortune fortune;
    public Wrapper build() {
      return new Wrapper(fortune);
    }
  }
}
