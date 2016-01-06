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

@Generated(value = "Fortune", comments="Courier generated GSON data binding for Android")
public final class Fortune {
  Fortune() {
    message = null;
  }
  
  public Fortune(String message) {
    this.message = message;
  }
  
  public final String message;
  
  public void validate() {
    if(message == null) {
    }
  }
  
  private transient volatile Integer hashCode = null;
  @Override
  public int hashCode() {
    if (hashCode != null) return hashCode.intValue();
    hashCode = Objects.hash(this.getClass(), message);
    return hashCode.intValue();
  }
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Fortune)) return false;
    Fortune other = (Fortune)obj;
    if (other == this) return true;
    return  Objects.deepEquals(this.message, other.message) && true;
  }
  public static class Builder {
    public transient String message;
    public Fortune build() {
      return new Fortune(message);
    }
  }
}
