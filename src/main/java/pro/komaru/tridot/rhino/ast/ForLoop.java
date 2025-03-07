/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

import pro.komaru.tridot.rhino.*;

/**
 * C-style for-loop statement.
 * Node type is {@link Token#FOR}.
 *
 * <pre><b>for</b> ( ExpressionNoInopt; Expressionopt ; Expressionopt ) Statement</pre>
 * <pre><b>for</b> ( <b>var</b> VariableDeclarationListNoIn; Expressionopt ; Expressionopt ) Statement</pre>
 */
public class ForLoop extends Loop {

	private AstNode initializer;
	private AstNode condition;
	private AstNode increment;

	{
		type = Token.FOR;
	}

	public ForLoop() {
	}

	public ForLoop(int pos) {
		super(pos);
	}

	public ForLoop(int pos, int len) {
		super(pos, len);
	}

	/**
	 * Returns loop initializer variable declaration list.
	 * This is either a {@link VariableDeclaration}, an
	 * {@link Assignment}, or an {@link InfixExpression} of
	 * type COMMA that chains multiple variable assignments.
	 */
	public AstNode getInitializer() {
		return initializer;
	}

	/**
	 * Sets loop initializer expression, and sets its parent
	 * to this node.  Virtually any expression can be in the initializer,
	 * so no error-checking is done other than a {@code null}-check.
	 *
	 * @param initializer loop initializer.  Pass an
	 *                    {@link EmptyExpression} if the initializer is not specified.
	 * @throws IllegalArgumentException if condition is {@code null}
	 */
	public void setInitializer(AstNode initializer) {
		assertNotNull(initializer);
		this.initializer = initializer;
		initializer.setParent(this);
	}

	/**
	 * Returns loop condition
	 */
	public AstNode getCondition() {
		return condition;
	}

	/**
	 * Sets loop condition, and sets its parent to this node.
	 *
	 * @param condition loop condition.  Pass an {@link EmptyExpression}
	 *                  if the condition is missing.
	 * @throws IllegalArgumentException} if condition is {@code null}
	 */
	public void setCondition(AstNode condition) {
		assertNotNull(condition);
		this.condition = condition;
		condition.setParent(this);
	}

	/**
	 * Returns loop increment expression
	 */
	public AstNode getIncrement() {
		return increment;
	}

	/**
	 * Sets loop increment expression, and sets its parent to
	 * this node.
	 *
	 * @param increment loop increment expression.  Pass an
	 *                  {@link EmptyExpression} if increment is {@code null}.
	 * @throws IllegalArgumentException} if increment is {@code null}
	 */
	public void setIncrement(AstNode increment) {
		assertNotNull(increment);
		this.increment = increment;
		increment.setParent(this);
	}
}
