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

import pro.komaru.tridot.common.compatibility.snakeyaml.events.Event;

/**
 * Helper for {@link ParserImpl}. A grammar rule to apply given the symbols on top of its stack and
 * the next input token
 *
 * @see <a href="http://en.wikipedia.org/wiki/LL_parser">LL parser</a>
 */
interface Production {

  Event produce();
}
