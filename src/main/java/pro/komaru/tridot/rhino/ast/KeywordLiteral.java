/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

import pro.komaru.tridot.rhino.*;

/**
 * AST node for keyword literals:  currently, {@code this},
 * {@code null}, {@code true}, {@code false}, and {@code debugger}.
 * Node type is one of
 * {@link Token#THIS},
 * {@link Token#NULL},
 * {@link Token#TRUE},
 * {@link Token#FALSE}.
 */
public class KeywordLiteral extends AstNode {

	public KeywordLiteral() {
	}

	public KeywordLiteral(int pos) {
		super(pos);
	}

	public KeywordLiteral(int pos, int len) {
		super(pos, len);
	}

	/**
	 * Constructs a new KeywordLiteral
	 *
	 * @param nodeType the token type
	 */
	public KeywordLiteral(int pos, int len, int nodeType) {
		super(pos, len);
		setType(nodeType);
	}

	/**
	 * Sets node token type
	 *
	 * @throws IllegalArgumentException if {@code nodeType} is unsupported
	 */
	@Override
	public KeywordLiteral setType(int nodeType) {
		if (!(nodeType == Token.THIS || nodeType == Token.NULL || nodeType == Token.TRUE || nodeType == Token.FALSE)) {
			throw new IllegalArgumentException("Invalid node type: " + nodeType);
		}
		type = nodeType;
		return this;
	}

	/**
	 * Returns true if the token type is {@link Token#TRUE} or
	 * {@link Token#FALSE}.
	 */
	public boolean isBooleanLiteral() {
		return type == Token.TRUE || type == Token.FALSE;
	}
}
