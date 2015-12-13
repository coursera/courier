package org.coursera.courier.psi;

import com.google.common.base.Strings;
import com.intellij.openapi.util.text.StringUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A namespaced courier type name.
 */
public class TypeName implements Comparable<TypeName> {
  private final String fullname;

  public static TypeName escaped(String unescapedFullname) {
    return new TypeName(escape(unescapedFullname));
  }

  public static TypeName unescaped(String escapedFullname) {
    return new TypeName(unescape(escapedFullname));
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

  private static final Set<String> keywords;
  static {
    keywords = new HashSet<String>();
    keywords.add("namespace");
    keywords.add("import");
    keywords.add("record");
    keywords.add("enum");
    keywords.add("fixed");
    keywords.add("typeref");
    keywords.add("union");
    keywords.add("map");
    keywords.add("array");
    keywords.add("null");
    keywords.add("true");
    keywords.add("false");
  }

  public static String unescape(String name) {
    String[] parts = name.split("\\.");
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < parts.length; i++) {
      builder.append(unescapeIdentifier(parts[i]));
      if (i < parts.length - 1) {
        builder.append(".");
      }
    }
    return builder.toString();
  }

  private static String unescapeIdentifier(String name) {
    if (keywords.contains(name)) {
      return "`" + name + "`";
    } else {
      return name;
    }
  }

  public TypeName(String fullname) {
    this.fullname = fullname;
  }

  public TypeName(String namespace, String name) {
    if (namespace == null || namespace.isEmpty()) {
      this.fullname = name;
    } else {
      this.fullname = namespace + "." + name;
    }
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

  public List<String> getNamespaceParts() {
    return Arrays.asList(getNamespace().split("\\."));
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

  public String unescape() {
    return unescape(fullname);
  }

  @Override
  public int compareTo(TypeName o) {
    if (o == null) return 1;
    return this.fullname.compareTo(o.fullname);
  }
}
