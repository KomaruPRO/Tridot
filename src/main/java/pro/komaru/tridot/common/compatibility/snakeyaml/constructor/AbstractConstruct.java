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
package pro.komaru.tridot.common.compatibility.snakeyaml.constructor;

import pro.komaru.tridot.common.compatibility.snakeyaml.error.YAMLException;
import pro.komaru.tridot.common.compatibility.snakeyaml.nodes.Node;

/**
 * Because recursive structures are not very common we provide a way to save some typing when
 * extending a constructor
 */
public abstract class AbstractConstruct implements Construct {

  /**
   * Fail with a reminder to provide the seconds step for a recursive structure
   *
   * @see Construct#construct2ndStep(Node,
   *      Object)
   */
  public void construct2ndStep(Node node, Object data) {
    if (node.isTwoStepsConstruction()) {
      throw new IllegalStateException("Not Implemented in " + getClass().getName());
    } else {
      throw new YAMLException("Unexpected recursive structure for Node: " + node);
    }
  }
}
