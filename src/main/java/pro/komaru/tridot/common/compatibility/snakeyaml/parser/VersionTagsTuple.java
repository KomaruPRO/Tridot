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
package pro.komaru.tridot.common.compatibility.snakeyaml.parser;

import java.util.Map;
import pro.komaru.tridot.common.compatibility.snakeyaml.DumperOptions.Version;

/**
 * Store the internal state for directives
 */
class VersionTagsTuple {

  private final Version version;
  private final Map<String, String> tags;

  public VersionTagsTuple(Version version, Map<String, String> tags) {
    this.version = version;
    this.tags = tags;
  }

  public Version getVersion() {
    return version;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  @Override
  public String toString() {
    return String.format("VersionTagsTuple<%s, %s>", version, tags);
  }
}
