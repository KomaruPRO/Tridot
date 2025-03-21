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
package pro.komaru.tridot.common.compatibility.snakeyaml.events;

import pro.komaru.tridot.common.compatibility.snakeyaml.DumperOptions;
import pro.komaru.tridot.common.compatibility.snakeyaml.error.Mark;

/**
 * Marks the beginning of a mapping node.
 * <p>
 * This event is followed by a number of key value pairs. <br>
 * The pairs are not in any particular order. However, the value always directly follows the
 * corresponding key. <br>
 * After the key value pairs follows a {@link MappingEndEvent}.
 * </p>
 * <p>
 * There must be an even number of node events between the start and end event.
 * </p>
 *
 * @see MappingEndEvent
 */
public final class MappingStartEvent extends CollectionStartEvent {

  public MappingStartEvent(String anchor, String tag, boolean implicit, Mark startMark,
      Mark endMark, DumperOptions.FlowStyle flowStyle) {
    super(anchor, tag, implicit, startMark, endMark, flowStyle);
  }

  /**
   * getter
   *
   * @return its identity
   */
  @Override
  public ID getEventId() {
    return ID.MappingStart;
  }
}
