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

import java.util.List;
import pro.komaru.tridot.common.compatibility.snakeyaml.DumperOptions;
import pro.komaru.tridot.common.compatibility.snakeyaml.error.Mark;

/**
 * Represents a map.
 * <p>
 * A map is a collection of unsorted key-value pairs.
 * </p>
 */
public class MappingNode extends CollectionNode<NodeTuple> {

  private List<NodeTuple> value;
  private boolean merged = false;

  public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark,
                     DumperOptions.FlowStyle flowStyle) {
    super(tag, startMark, endMark, flowStyle);
    if (value == null) {
      throw new NullPointerException("value in a Node is required.");
    }
    this.value = value;
    this.resolved = resolved;
  }

  public MappingNode(Tag tag, List<NodeTuple> value, DumperOptions.FlowStyle flowStyle) {
    this(tag, true, value, null, null, flowStyle);
  }

  @Override
  public NodeId getNodeId() {
    return NodeId.mapping;
  }

  /**
   * Returns the entries of this map.
   *
   * @return List of entries.
   */
  public List<NodeTuple> getValue() {
    return value;
  }

  public void setValue(List<NodeTuple> mergedValue) {
    value = mergedValue;
  }

  public void setOnlyKeyType(Class<?> keyType) {
    for (NodeTuple nodes : value) {
      nodes.getKeyNode().setType(keyType);
    }
  }

  public void setTypes(Class<?> keyType, Class<?> valueType) {
    for (NodeTuple nodes : value) {
      nodes.getValueNode().setType(valueType);
      nodes.getKeyNode().setType(keyType);
    }
  }

  @Override
  public String toString() {
    String values;
    StringBuilder buf = new StringBuilder();
    for (NodeTuple node : getValue()) {
      buf.append("{ key=");
      buf.append(node.getKeyNode());
      buf.append("; value=");
      if (node.getValueNode() instanceof CollectionNode) {
        // to avoid overflow in case of recursive structures
        buf.append(System.identityHashCode(node.getValueNode()));
      } else {
        buf.append(node);
      }
      buf.append(" }");
    }
    values = buf.toString();
    return "<" + this.getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
  }

  /**
   * @param merged - true if map contains merge node
   */
  public void setMerged(boolean merged) {
    this.merged = merged;
  }

  /**
   * @return true if map contains merge node
   */
  public boolean isMerged() {
    return merged;
  }
}
