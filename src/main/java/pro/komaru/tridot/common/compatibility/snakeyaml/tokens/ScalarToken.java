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
package pro.komaru.tridot.common.compatibility.snakeyaml.tokens;

import pro.komaru.tridot.common.compatibility.snakeyaml.DumperOptions;
import pro.komaru.tridot.common.compatibility.snakeyaml.error.Mark;

public final class ScalarToken extends Token {

  private final String value;
  private final boolean plain;
  private final DumperOptions.ScalarStyle style;

  public ScalarToken(String value, Mark startMark, Mark endMark, boolean plain) {
    this(value, plain, startMark, endMark, DumperOptions.ScalarStyle.PLAIN);
  }

  public ScalarToken(String value, boolean plain, Mark startMark, Mark endMark,
      DumperOptions.ScalarStyle style) {
    super(startMark, endMark);
    this.value = value;
    this.plain = plain;
    if (style == null) {
      throw new NullPointerException("Style must be provided.");
    }
    this.style = style;
  }

  public boolean getPlain() {
    return this.plain;
  }

  public String getValue() {
    return this.value;
  }

  public DumperOptions.ScalarStyle getStyle() {
    return this.style;
  }

  @Override
  public ID getTokenId() {
    return ID.Scalar;
  }
}
