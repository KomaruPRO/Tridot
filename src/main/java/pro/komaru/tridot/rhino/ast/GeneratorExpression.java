/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

import pro.komaru.tridot.rhino.*;

import java.util.*;

/**
 *
 */
public class GeneratorExpression extends Scope {

	private final List<GeneratorExpressionLoop> loops = new ArrayList<>();
	private AstNode result;
	private AstNode filter;
	private int ifPosition = -1;
	private int lp = -1;
	private int rp = -1;

	{
		type = Token.GENEXPR;
	}

	public GeneratorExpression() {
	}

	public GeneratorExpression(int pos) {
		super(pos);
	}

	public GeneratorExpression(int pos, int len) {
		super(pos, len);
	}

	/**
	 * Returns result expression node (just after opening bracket)
	 */
	public AstNode getResult() {
		return result;
	}

	/**
	 * Sets result expression, and sets its parent to this node.
	 *
	 * @throws IllegalArgumentException if result is {@code null}
	 */
	public void setResult(AstNode result) {
		assertNotNull(result);
		this.result = result;
		result.setParent(this);
	}

	/**
	 * Returns loop list
	 */
	public List<GeneratorExpressionLoop> getLoops() {
		return loops;
	}

	/**
	 * Sets loop list
	 *
	 * @throws IllegalArgumentException if loops is {@code null}
	 */
	public void setLoops(List<GeneratorExpressionLoop> loops) {
		assertNotNull(loops);
		this.loops.clear();
		for (GeneratorExpressionLoop acl : loops) {
			addLoop(acl);
		}
	}

	/**
	 * Adds a child loop node, and sets its parent to this node.
	 *
	 * @throws IllegalArgumentException if acl is {@code null}
	 */
	public void addLoop(GeneratorExpressionLoop acl) {
		assertNotNull(acl);
		loops.add(acl);
		acl.setParent(this);
	}

	/**
	 * Returns filter expression, or {@code null} if not present
	 */
	public AstNode getFilter() {
		return filter;
	}

	/**
	 * Sets filter expression, and sets its parent to this node.
	 * Can be {@code null}.
	 */
	public void setFilter(AstNode filter) {
		this.filter = filter;
		if (filter != null) {
			filter.setParent(this);
		}
	}

	/**
	 * Returns position of 'if' keyword, -1 if not present
	 */
	public int getIfPosition() {
		return ifPosition;
	}

	/**
	 * Sets position of 'if' keyword
	 */
	public void setIfPosition(int ifPosition) {
		this.ifPosition = ifPosition;
	}

	/**
	 * Returns filter left paren position, or -1 if no filter
	 */
	public int getFilterLp() {
		return lp;
	}

	/**
	 * Sets filter left paren position, or -1 if no filter
	 */
	public void setFilterLp(int lp) {
		this.lp = lp;
	}

	/**
	 * Returns filter right paren position, or -1 if no filter
	 */
	public int getFilterRp() {
		return rp;
	}

	/**
	 * Sets filter right paren position, or -1 if no filter
	 */
	public void setFilterRp(int rp) {
		this.rp = rp;
	}
}
