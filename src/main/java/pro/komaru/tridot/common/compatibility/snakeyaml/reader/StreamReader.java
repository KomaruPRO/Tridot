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
package pro.komaru.tridot.common.compatibility.snakeyaml.reader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import pro.komaru.tridot.common.compatibility.snakeyaml.error.Mark;
import pro.komaru.tridot.common.compatibility.snakeyaml.error.YAMLException;
import pro.komaru.tridot.common.compatibility.snakeyaml.scanner.Constant;

/**
 * Reader: checks if code points are in allowed range. Returns '\0' when end of data has been
 * reached.
 */
public class StreamReader {

  private String name;
  private final Reader stream;
  /**
   * Read data (as a moving window for input stream)
   */
  private int[] dataWindow;

  /**
   * Real length of the data in dataWindow
   */
  private int dataLength;

  /**
   * The variable points to the current position in the data array
   */
  private int pointer = 0;
  private boolean eof;
  private int index = 0; // in code points
  private int documentIndex = 0; // current document index in code points (only for limiting)
  private int line = 0;
  private int column = 0; // in code points
  private final char[] buffer; // temp buffer for one read operation (to avoid
  // creating the array in stack)

  public StreamReader(String stream) {
    this(new StringReader(stream));
    this.name = "'string'";
  }

  public StreamReader(Reader reader) {
    if (reader == null) {
      throw new NullPointerException("Reader must be provided.");
    }
    this.name = "'reader'";
    this.dataWindow = new int[0];
    this.dataLength = 0;
    this.stream = reader;
    this.eof = false;
    this.buffer = new char[1024];
  }

  public static boolean isPrintable(final String data) {
    final int length = data.length();
    for (int offset = 0; offset < length;) {
      final int codePoint = data.codePointAt(offset);

      if (!isPrintable(codePoint)) {
        return false;
      }

      offset += Character.charCount(codePoint);
    }

    return true;
  }

  public static boolean isPrintable(final int c) {
    return (c >= 0x20 && c <= 0x7E) || c == 0x9 || c == 0xA || c == 0xD || c == 0x85
        || (c >= 0xA0 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD)
        || (c >= 0x10000 && c <= 0x10FFFF);
  }

  public Mark getMark() {
    return new Mark(name, this.index, this.line, this.column, this.dataWindow, this.pointer);
  }

  public void forward() {
    forward(1);
  }

  /**
   * read the next length characters and move the pointer. if the last character is high surrogate
   * one more character will be read
   *
   * @param length amount of characters to move forward
   */
  public void forward(int length) {
    for (int i = 0; i < length && ensureEnoughData(); i++) {
      int c = dataWindow[pointer++];
      moveIndices(1);
      if (Constant.LINEBR.has(c)
          || (c == '\r' && (ensureEnoughData() && dataWindow[pointer] != '\n'))) {
        this.line++;
        this.column = 0;
      } else if (c != 0xFEFF) {
        this.column++;
      }
    }
  }

  public int peek() {
    return (ensureEnoughData()) ? dataWindow[pointer] : '\0';
  }

  /**
   * Peek the next index-th code point
   *
   * @param index to peek
   * @return the next index-th code point
   */
  public int peek(int index) {
    return (ensureEnoughData(index)) ? dataWindow[pointer + index] : '\0';
  }

  /**
   * peek the next length code points
   *
   * @param length amount of the characters to peek
   * @return the next length code points
   */
  public String prefix(int length) {
    if (length == 0) {
      return "";
    } else if (ensureEnoughData(length)) {
      return new String(this.dataWindow, pointer, length);
    } else {
      return new String(this.dataWindow, pointer, Math.min(length, dataLength - pointer));
    }
  }

  /**
   * prefix(length) immediately followed by forward(length)
   *
   * @param length amount of characters to get
   * @return the next length code points
   */
  public String prefixForward(int length) {
    final String prefix = prefix(length);
    this.pointer += length;
    moveIndices(length);
    // prefix never contains new line characters
    this.column += length;
    return prefix;
  }

  private boolean ensureEnoughData() {
    return ensureEnoughData(0);
  }

  private boolean ensureEnoughData(int size) {
    if (!eof && pointer + size >= dataLength) {
      update();
    }
    return (this.pointer + size) < dataLength;
  }

  private void update() {
    try {
      // read one less because the last char may be HighSurrogate
      int read = stream.read(buffer, 0, buffer.length - 1);
      if (read > 0) {
        int cpIndex = (dataLength - pointer);
        dataWindow = Arrays.copyOfRange(dataWindow, pointer, dataLength + read);
        if (Character.isHighSurrogate(buffer[read - 1])) {
          if (stream.read(buffer, read, 1) == -1) {
            throw new ReaderException(name, index + read, buffer[read - 1],
                "The last char is HighSurrogate (no LowSurrogate detected).");
          } else {
            read++;
          }
        }
        int nonPrintable = ' ';
        for (int i = 0; i < read; cpIndex++) {
          int codePoint = Character.codePointAt(buffer, i);
          dataWindow[cpIndex] = codePoint;
          if (isPrintable(codePoint)) {
            int charCount = Character.charCount(codePoint); // 2 for surrogates
            i += charCount;
          } else {
            nonPrintable = codePoint;
            i = read;
          }
        }

        dataLength = cpIndex;
        pointer = 0;
        if (nonPrintable != ' ') {
          throw new ReaderException(name, index + cpIndex - 1, nonPrintable,
              "special characters are not allowed");
        }
      } else {
        eof = true;
      }
    } catch (IOException ioe) {
      throw new YAMLException(ioe);
    }
  }


  public int getColumn() {
    return column;
  }

  private void moveIndices(int length) {
    this.index += length;
    this.documentIndex += length;
  }

  /**
   * Get the position of the current char in the current YAML document
   *
   * @return index of the current position from the beginning of the current document
   */
  public int getDocumentIndex() {
    return documentIndex;
  }

  /**
   * Reset the position to start (at the start of a new document in the stream)
   */
  public void resetDocumentIndex() {
    documentIndex = 0;
  }

  /**
   * @return current position as number (in characters) from the beginning of the stream
   */
  public int getIndex() {
    return index;
  }

  public int getLine() {
    return line;
  }
}
