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

import pro.komaru.tridot.common.compatibility.snakeyaml.DumperOptions;
import pro.komaru.tridot.common.compatibility.snakeyaml.events.ScalarEvent;
import pro.komaru.tridot.common.compatibility.snakeyaml.error.Mark;

/**
 * Represents a scalar node.
 * <p>
 * Scalar nodes form the leaves in the node graph.
 * </p>
 */
public class ScalarNode extends Node {

  private final DumperOptions.ScalarStyle style;
  private final String value;

  public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark,
                    DumperOptions.ScalarStyle style) {
    this(tag, true, value, startMark, endMark, style);
  }

  public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark,
                    DumperOptions.ScalarStyle style) {
    super(tag, startMark, endMark);
    if (value == null) {
      throw new NullPointerException("value in a Node is required.");
    }
    this.value = value;
    if (style == null) {
      throw new NullPointerException("Scalar style must be provided.");
    }
    this.style = style;
    this.resolved = resolved;
  }

  /**
   * Get scalar style of this node.
   *
   * @see ScalarEvent
   * @see <a href="http://yaml.org/spec/1.1/#id903915">Chapter 9. Scalar Styles</a>
   * @return style of this scalar node
   */
  public DumperOptions.ScalarStyle getScalarStyle() {
    return style;
  }

  @Override
  public NodeId getNodeId() {
    return NodeId.scalar;
  }

  /**
   * Value of this scalar.
   *
   * @return Scalar's value.
   */
  public String getValue() {
    return value;
  }

  public String toString() {
    return "<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
  }

  public boolean isPlain() {
    return style == DumperOptions.ScalarStyle.PLAIN;
  }
}
