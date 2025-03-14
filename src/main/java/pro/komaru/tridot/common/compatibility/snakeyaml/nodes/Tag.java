/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package pro.komaru.tridot.common.compatibility.snakeyaml.nodes;

import pro.komaru.tridot.common.compatibility.snakeyaml.error.YAMLException;
import pro.komaru.tridot.common.compatibility.snakeyaml.util.UriEncoder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public final class Tag {

  public static final String PREFIX = "tag:yaml.org,2002:";
  public static final Tag YAML = new Tag(PREFIX + "yaml");
  public static final Tag MERGE = new Tag(PREFIX + "merge");
  public static final Tag SET = new Tag(PREFIX + "set");
  public static final Tag PAIRS = new Tag(PREFIX + "pairs");
  public static final Tag OMAP = new Tag(PREFIX + "omap");
  public static final Tag BINARY = new Tag(PREFIX + "binary");
  public static final Tag INT = new Tag(PREFIX + "int");
  public static final Tag FLOAT = new Tag(PREFIX + "float");
  public static final Tag TIMESTAMP = new Tag(PREFIX + "timestamp");
  public static final Tag BOOL = new Tag(PREFIX + "bool");
  public static final Tag NULL = new Tag(PREFIX + "null");
  public static final Tag STR = new Tag(PREFIX + "str");
  public static final Tag SEQ = new Tag(PREFIX + "seq");
  public static final Tag MAP = new Tag(PREFIX + "map");

  // https://yaml.org/type/index.html
  public static final Set<Tag> standardTags = new HashSet<>(15);

  static {
    standardTags.add(YAML);
    standardTags.add(MERGE);
    standardTags.add(SET);
    standardTags.add(PAIRS);
    standardTags.add(OMAP);
    standardTags.add(BINARY);
    standardTags.add(INT);
    standardTags.add(FLOAT);
    standardTags.add(TIMESTAMP);
    standardTags.add(BOOL);
    standardTags.add(NULL);
    standardTags.add(STR);
    standardTags.add(SEQ);
    standardTags.add(MAP);
  }

  // For use to indicate a DUMMY node that contains comments, when there is no other (empty
  // document)
  public static final Tag COMMENT = new Tag(PREFIX + "comment");
  private static final Map<Tag, Set<Class<?>>> COMPATIBILITY_MAP;

  static {
    COMPATIBILITY_MAP = new HashMap<Tag, Set<Class<?>>>();
    Set<Class<?>> floatSet = new HashSet<Class<?>>();
    floatSet.add(Double.class);
    floatSet.add(Float.class);
    floatSet.add(BigDecimal.class);
    COMPATIBILITY_MAP.put(FLOAT, floatSet);
    //
    Set<Class<?>> intSet = new HashSet<Class<?>>();
    intSet.add(Integer.class);
    intSet.add(Long.class);
    intSet.add(BigInteger.class);
    COMPATIBILITY_MAP.put(INT, intSet);
    //
    Set<Class<?>> timestampSet = new HashSet<Class<?>>();
    timestampSet.add(Date.class);

    // java.sql is a separate module since jigsaw was introduced in java9
    try {
      timestampSet.add(Class.forName("java.sql.Date"));
      timestampSet.add(Class.forName("java.sql.Timestamp"));
    } catch (ClassNotFoundException ignored) {
      // ignore - we are running in a module path without java.sql
    }

    COMPATIBILITY_MAP.put(TIMESTAMP, timestampSet);
  }

  private final String value;
  private boolean secondary = false; // see http://www.yaml.org/refcard.html

  public Tag(String tag) {
    if (tag == null) {
      throw new NullPointerException("Tag must be provided.");
    } else if (tag.isEmpty()) {
      throw new IllegalArgumentException("Tag must not be empty.");
    } else if (tag.trim().length() != tag.length()) {
      throw new IllegalArgumentException("Tag must not contain leading or trailing spaces.");
    }
    this.value = UriEncoder.encode(tag);
    this.secondary = !tag.startsWith(PREFIX);
  }

  public Tag(Class<?> clazz) {
    if (clazz == null) {
      throw new NullPointerException("Class for tag must be provided.");
    }
    this.value = Tag.PREFIX + UriEncoder.encode(clazz.getName());
  }

  public boolean isSecondary() {
    return secondary;
  }

  public String getValue() {
    return value;
  }

  public boolean startsWith(String prefix) {
    return value.startsWith(prefix);
  }

  public String getClassName() {
    if (secondary) {
      throw new YAMLException("Invalid tag: " + value);
    }
    return UriEncoder.decode(value.substring(Tag.PREFIX.length()));
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Tag) {
      return value.equals(((Tag) obj).getValue());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /**
   * Java has more than 1 class compatible with a language-independent tag (!!int, !!float,
   * !!timestamp etc)
   *
   * @param clazz - Class to check compatibility
   * @return true when the Class can be represented by this language-independent tag
   */
  public boolean isCompatible(Class<?> clazz) {
    Set<Class<?>> set = COMPATIBILITY_MAP.get(this);
    if (set != null) {
      return set.contains(clazz);
    } else {
      return false;
    }
  }

  /**
   * Check whether this tag matches the global tag for the Class
   *
   * @param clazz - Class to check
   * @return true when this tag can be used as a global tag for the Class during serialisation
   */
  public boolean matches(Class<?> clazz) {
    return value.equals(Tag.PREFIX + clazz.getName());
  }

  /**
   * Check if the that is global and not standard to provide it to TagInspector for verification.
   *
   * @return true when the tag must be verified to avoid remote code invocation
   */
  public boolean isCustomGlobal() {
    return !secondary && !standardTags.contains(this);
  }
}
