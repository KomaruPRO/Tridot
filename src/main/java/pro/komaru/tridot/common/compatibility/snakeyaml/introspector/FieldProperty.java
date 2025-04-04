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
package pro.komaru.tridot.common.compatibility.snakeyaml.introspector;

import pro.komaru.tridot.common.compatibility.snakeyaml.error.YAMLException;
import pro.komaru.tridot.common.compatibility.snakeyaml.util.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 * A <code>FieldProperty</code> is a <code>Property</code> which is accessed as a field, without
 * going through accessor methods (setX, getX). The field may have any scope (public, package,
 * protected, private).
 * </p>
 */
public class FieldProperty extends GenericProperty {

  private final Field field;

  public FieldProperty(Field field) {
    super(field.getName(), field.getType(), field.getGenericType());
    this.field = field;
    field.setAccessible(true);
  }

  @Override
  public void set(Object object, Object value) throws Exception {
    field.set(object, value);
  }

  @Override
  public Object get(Object object) {
    try {
      return field.get(object);
    } catch (Exception e) {
      throw new YAMLException(
          "Unable to access field " + field.getName() + " on object " + object + " : " + e);
    }
  }

  @Override
  public List<Annotation> getAnnotations() {
    return ArrayUtils.toUnmodifiableList(field.getAnnotations());
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    return field.getAnnotation(annotationType);
  }

}
