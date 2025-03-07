/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.ast;

import pro.komaru.tridot.rhino.*;

/**
 * AST node for Template Literal Characters.
 * <p>Node type is {@link Token#TEMPLATE_CHARS}.</p>
 */
public class TemplateCharacters extends AstNode {
	private String value;
	private String rawValue;

	{
		type = Token.TEMPLATE_CHARS;
	}

	public TemplateCharacters() {
	}

	public TemplateCharacters(int pos) {
		super(pos);
	}

	public TemplateCharacters(int pos, int len) {
		super(pos, len);
	}

	/**
	 * Returns the node's value: the parsed template-literal-value (QV)
	 *
	 * @return the node's value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the node's value.
	 *
	 * @param value the node's value
	 * @throws IllegalArgumentException} if value is {@code null}
	 */
	public void setValue(String value) {
		assertNotNull(value);
		this.value = value;
	}

	/**
	 * Returns the node's raw-value: the parsed template-literal-raw-value (QRV)
	 *
	 * @return the node's raw-value
	 */
	public String getRawValue() {
		return rawValue;
	}

	/**
	 * Sets the node's raw-value.
	 *
	 * @param rawValue the node's raw-value
	 * @throws IllegalArgumentException} if rawValue is {@code null}
	 */
	public void setRawValue(String rawValue) {
		assertNotNull(rawValue);
		this.rawValue = rawValue;
	}
}