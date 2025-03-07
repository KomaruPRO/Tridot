/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

import pro.komaru.tridot.rhino.*;

/**
 * While statement.  Node type is {@link Token#WHILE}.
 *
 * <pre><i>WhileStatement</i>:
 *     <b>while</b> <b>(</b> Expression <b>)</b> Statement</pre>
 */
public class WhileLoop extends Loop {

	private AstNode condition;

	{
		type = Token.WHILE;
	}

	public WhileLoop() {
	}

	public WhileLoop(int pos) {
		super(pos);
	}

	public WhileLoop(int pos, int len) {
		super(pos, len);
	}

	/**
	 * Returns loop condition
	 */
	public AstNode getCondition() {
		return condition;
	}

	/**
	 * Sets loop condition
	 *
	 * @throws IllegalArgumentException} if condition is {@code null}
	 */
	public void setCondition(AstNode condition) {
		assertNotNull(condition);
		this.condition = condition;
		condition.setParent(this);
	}
}
