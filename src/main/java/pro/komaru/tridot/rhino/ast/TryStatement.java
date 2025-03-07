/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

import pro.komaru.tridot.rhino.*;

import java.util.*;

/**
 * Try/catch/finally statement.  Node type is {@link Token#TRY}.
 *
 * <pre><i>TryStatement</i> :
 *        <b>try</b> Block Catch
 *        <b>try</b> Block Finally
 *        <b>try</b> Block Catch Finally
 * <i>Catch</i> :
 *        <b>catch</b> ( <i><b>Identifier</b></i> ) Block
 * <i>Finally</i> :
 *        <b>finally</b> Block</pre>
 */
public class TryStatement extends AstNode {

	private static final List<CatchClause> NO_CATCHES = Collections.unmodifiableList(new ArrayList<>());

	private AstNode tryBlock;
	private List<CatchClause> catchClauses;
	private AstNode finallyBlock;
	private int finallyPosition = -1;

	{
		type = Token.TRY;
	}

	public TryStatement() {
	}

	public TryStatement(int pos) {
		super(pos);
	}

	public TryStatement(int pos, int len) {
		super(pos, len);
	}

	public AstNode getTryBlock() {
		return tryBlock;
	}

	/**
	 * Sets try block.  Also sets its parent to this node.
	 *
	 * @throws IllegalArgumentException} if {@code tryBlock} is {@code null}
	 */
	public void setTryBlock(AstNode tryBlock) {
		assertNotNull(tryBlock);
		this.tryBlock = tryBlock;
		tryBlock.setParent(this);
	}

	/**
	 * Returns list of {@link CatchClause} nodes.  If there are no catch
	 * clauses, returns an immutable empty list.
	 */
	public List<CatchClause> getCatchClauses() {
		return catchClauses != null ? catchClauses : NO_CATCHES;
	}

	/**
	 * Sets list of {@link CatchClause} nodes.  Also sets their parents
	 * to this node.  May be {@code null}.  Replaces any existing catch
	 * clauses for this node.
	 */
	public void setCatchClauses(List<CatchClause> catchClauses) {
		if (catchClauses == null) {
			this.catchClauses = null;
		} else {
			if (this.catchClauses != null) {
				this.catchClauses.clear();
			}
			for (CatchClause cc : catchClauses) {
				addCatchClause(cc);
			}
		}
	}

	/**
	 * Add a catch-clause to the end of the list, and sets its parent to
	 * this node.
	 *
	 * @throws IllegalArgumentException} if {@code clause} is {@code null}
	 */
	public void addCatchClause(CatchClause clause) {
		assertNotNull(clause);
		if (catchClauses == null) {
			catchClauses = new ArrayList<>();
		}
		catchClauses.add(clause);
		clause.setParent(this);
	}

	/**
	 * Returns finally block, or {@code null} if not present
	 */
	public AstNode getFinallyBlock() {
		return finallyBlock;
	}

	/**
	 * Sets finally block, and sets its parent to this node.
	 * May be {@code null}.
	 */
	public void setFinallyBlock(AstNode finallyBlock) {
		this.finallyBlock = finallyBlock;
		if (finallyBlock != null) {
			finallyBlock.setParent(this);
		}
	}

	/**
	 * Returns position of {@code finally} keyword, if present, or -1
	 */
	public int getFinallyPosition() {
		return finallyPosition;
	}

	/**
	 * Sets position of {@code finally} keyword, if present, or -1
	 */
	public void setFinallyPosition(int finallyPosition) {
		this.finallyPosition = finallyPosition;
	}
}
