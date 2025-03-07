/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

/**
 * Encapsulates information for a JavaScript parse error or warning.
 */
public class ParseProblem {

	private Type type;
	private String message;
	private String sourceName;
	private int offset;
	private int length;

	/**
	 * Constructs a new ParseProblem.
	 */
	public ParseProblem(Type type, String message, String sourceName, int offset, int length) {
		setType(type);
		setMessage(message);
		setSourceName(sourceName);
		setFileOffset(offset);
		setLength(length);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String name) {
		this.sourceName = name;
	}

	public int getFileOffset() {
		return offset;
	}

	public void setFileOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		String sb = sourceName + ":" +
				"offset=" + offset + "," +
				"length=" + length + "," +
				(type == Type.Error ? "error: " : "warning: ") +
				message;
		return sb;
	}

	public enum Type {
		Error, Warning
	}
}
