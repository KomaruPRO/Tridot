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

import pro.komaru.tridot.common.compatibility.snakeyaml.error.Mark;

/**
 * Marks the end of a stream that might have contained multiple documents.
 * <p>
 * This event is the last event that a parser emits. Together with {@link StreamStartEvent} (which
 * is the first event a parser emits) they mark the beginning and the end of a stream of documents.
 * </p>
 * <p>
 * See {@link Event} for an exemplary output.
 * </p>
 */
public final class StreamEndEvent extends Event {

  public StreamEndEvent(Mark startMark, Mark endMark) {
    super(startMark, endMark);
  }

  @Override
  public ID getEventId() {
    return ID.StreamEnd;
  }
}
