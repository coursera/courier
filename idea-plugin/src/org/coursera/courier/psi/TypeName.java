package org.coursera.courier.psi;

/**
 * A namespaced courier type name.
 */
public class TypeName {
  private final String fullname;

  public static TypeName escaped(String unescapedFullname) {
    return new TypeName(escape(unescapedFullname));
  }

  public static TypeName escaped(String unescapedNamespace, String unescapedName) {
    return new TypeName(escape(unescapedNamespace), escape(unescapedName));
  }

  public static boolean isPrimitive(String name) {
    return (CourierTokenType.PRIMITIVE_TYPES.contains(escape(name)));
  }

  public static String escape(String name) {
    return name.replaceAll("`", "");
  }

  public TypeName(String fullname) {
    this.fullname = fullname;
  }

  public TypeName(String namespace, String name) {
    this.fullname = namespace + "." + name;
  }

  public String getName() {
    int idx = fullname.lastIndexOf('.');
    if (idx > 0 && idx < fullname.length() - 2) {
      return fullname.substring(idx + 1);
    } else {
      return fullname;
    }
  }

  public String getNamespace() {
    int idx = fullname.lastIndexOf('.');
    if (idx > 1) {
      return fullname.substring(0, idx);
    } else {
      return "";
    }
  }

  public boolean isPrimitive() {
    return (CourierTokenType.PRIMITIVE_TYPES.contains(fullname));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TypeName typeName = (TypeName) o;

    return !(fullname != null ? !fullname.equals(typeName.fullname) : typeName.fullname != null);
  }

  @Override
  public int hashCode() {
    return fullname != null ? fullname.hashCode() : 0;
  }

  @Override
  public String toString() {
    return fullname;
  }
}
