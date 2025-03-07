/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino.regexp;

import pro.komaru.tridot.rhino.*;

/**
 * This class implements the RegExp native object.
 * <p>
 * Revision History:
 * Implementation in C by Brendan Eich
 * Initial port to Java by Norris Boyd from jsregexp.c version 1.36
 * Merged up to version 1.38, which included Unicode support.
 * Merged bug fixes in version 1.39.
 * Merged JSFUN13_BRANCH changes up to 1.32.2.13
 *
 * @author Brendan Eich
 * @author Norris Boyd
 */


public class NativeRegExp extends IdScriptableObject implements Function {
	public static final int JSREG_GLOB = 0x1;       // 'g' flag: global
	public static final int JSREG_FOLD = 0x2;       // 'i' flag: fold
	public static final int JSREG_MULTILINE = 0x4;  // 'm' flag: multiline
	//type of match to perform
	public static final int TEST = 0;
	public static final int MATCH = 1;
	public static final int PREFIX = 2;
	private static final Object REGEXP_TAG = new Object();
	private static final boolean debug = false;

	private static final byte REOP_SIMPLE_START = 1;  /* start of 'simple opcodes' */
	private static final byte REOP_EMPTY = 1;  /* match rest of input against rest of r.e. */
	private static final byte REOP_BOL = 2;  /* beginning of input (or line if multiline) */
	private static final byte REOP_EOL = 3;  /* end of input (or line if multiline) */
	private static final byte REOP_WBDRY = 4;  /* match "" at word boundary */
	private static final byte REOP_WNONBDRY = 5;  /* match "" at word non-boundary */
	private static final byte REOP_DOT = 6;  /* stands for any character */
	private static final byte REOP_DIGIT = 7;  /* match a digit char: [0-9] */
	private static final byte REOP_NONDIGIT = 8;  /* match a non-digit char: [^0-9] */
	private static final byte REOP_ALNUM = 9;  /* match an alphanumeric char: [0-9a-z_A-Z] */
	private static final byte REOP_NONALNUM = 10; /* match a non-alphanumeric char: [^0-9a-z_A-Z] */
	private static final byte REOP_SPACE = 11; /* match a whitespace char */
	private static final byte REOP_NONSPACE = 12; /* match a non-whitespace char */
	private static final byte REOP_BACKREF = 13; /* back-reference (e.g., \1) to a parenthetical */
	private static final byte REOP_FLAT = 14; /* match a flat string */
	private static final byte REOP_FLAT1 = 15; /* match a single char */
	private static final byte REOP_FLATi = 16; /* case-independent REOP_FLAT */
	private static final byte REOP_FLAT1i = 17; /* case-independent REOP_FLAT1 */
	private static final byte REOP_UCFLAT1 = 18; /* single Unicode char */
	private static final byte REOP_UCFLAT1i = 19; /* case-independent REOP_UCFLAT1 */
	//    private static final byte REOP_UCFLAT        = 20; /* flat Unicode string; len immediate counts chars */
	//    private static final byte REOP_UCFLATi       = 21; /* case-independent REOP_UCFLAT */
	private static final byte REOP_CLASS = 22; /* character class with index */
	private static final byte REOP_NCLASS = 23; /* negated character class with index */
	private static final byte REOP_SIMPLE_END = 23; /* end of 'simple opcodes' */
	private static final byte REOP_QUANT = 25; /* quantified atom: atom{1,2} */
	private static final byte REOP_STAR = 26; /* zero or more occurrences of kid */
	private static final byte REOP_PLUS = 27; /* one or more occurrences of kid */
	private static final byte REOP_OPT = 28; /* optional subexpression in kid */
	private static final byte REOP_LPAREN = 29; /* left paren bytecode: kid is u.num'th sub-regexp */
	private static final byte REOP_RPAREN = 30; /* right paren bytecode */
	private static final byte REOP_ALT = 31; /* alternative subexpressions in kid and next */
	private static final byte REOP_JUMP = 32; /* for deoptimized closure loops */
	//    private static final byte REOP_DOTSTAR       = 33; /* optimize .* to use a single opcode */
	//    private static final byte REOP_ANCHOR        = 34; /* like .* but skips left context to unanchored r.e. */
	//    private static final byte REOP_EOLONLY       = 35; /* $ not preceded by any pattern */
	//    private static final byte REOP_BACKREFi      = 37; /* case-independent REOP_BACKREF */
	//    private static final byte REOP_LPARENNON     = 40; /* non-capturing version of REOP_LPAREN */
	private static final byte REOP_ASSERT = 41; /* zero width positive lookahead assertion */
	private static final byte REOP_ASSERT_NOT = 42; /* zero width negative lookahead assertion */
	private static final byte REOP_ASSERTTEST = 43; /* sentinel at end of assertion child */
	private static final byte REOP_ASSERTNOTTEST = 44; /* sentinel at end of !assertion child */
	private static final byte REOP_MINIMALSTAR = 45; /* non-greedy version of * */
	private static final byte REOP_MINIMALPLUS = 46; /* non-greedy version of + */
	private static final byte REOP_MINIMALOPT = 47; /* non-greedy version of ? */
	private static final byte REOP_MINIMALQUANT = 48; /* non-greedy version of {} */
	private static final byte REOP_ENDCHILD = 49; /* sentinel at end of quantifier child */
	private static final byte REOP_REPEAT = 51; /* directs execution of greedy quantifier */
	private static final byte REOP_MINIMALREPEAT = 52; /* directs execution of non-greedy quantifier */
	private static final byte REOP_ALTPREREQ = 53; /* prerequisite for ALT, either of two chars */
	private static final byte REOP_ALTPREREQi = 54; /* case-independent REOP_ALTPREREQ */
	private static final byte REOP_ALTPREREQ2 = 55; /* prerequisite for ALT, a char or a class */
	//    private static final byte REOP_ENDALT        = 56; /* end of final alternate */
	private static final byte REOP_END = 57;

	private static final int ANCHOR_BOL = -2;
	private static final int INDEX_LEN = 2;
	private static final int Id_lastIndex = 1;
	private static final int Id_source = 2;
	private static final int Id_global = 3;
	private static final int Id_ignoreCase = 4;
	private static final int Id_multiline = 5;
	private static final int MAX_INSTANCE_ID = 5;
	private static final int Id_compile = 1;
	private static final int Id_toString = 2;
	private static final int Id_toSource = 3;
	private static final int Id_exec = 4;
	private static final int Id_test = 5;
	private static final int Id_prefix = 6;
	private static final int SymbolId_match = 7;
	private static final int SymbolId_search = 8;
	private static final int MAX_PROTOTYPE_ID = SymbolId_search;

	public static void init(Context cx, Scriptable scope, boolean sealed) {

		NativeRegExp proto = new NativeRegExp();
		proto.re = compileRE(cx, "", null, false);
		proto.activatePrototypeMap(MAX_PROTOTYPE_ID);
		proto.setParentScope(scope);
		proto.setPrototype(getObjectPrototype(scope, cx));

		NativeRegExpCtor ctor = new NativeRegExpCtor();
		// Bug #324006: ECMA-262 15.10.6.1 says "The initial value of
		// RegExp.prototype.constructor is the builtin RegExp constructor."
		proto.defineProperty(cx, "constructor", ctor, ScriptableObject.DONTENUM);

		ScriptRuntime.setFunctionProtoAndParent(cx, scope, ctor);

		ctor.setImmunePrototypeProperty(proto);

		if (sealed) {
			proto.sealObject(cx);
			ctor.sealObject(cx);
		}

		defineProperty(scope, "RegExp", ctor, ScriptableObject.DONTENUM, cx);
	}

	private static String escapeRegExp(Context cx, Object src) {
		String s = ScriptRuntime.toString(cx, src);
		// Escape any naked slashes in regexp source, see bug #510265
		StringBuilder sb = null; // instantiated only if necessary
		int start = 0;
		int slash = s.indexOf('/');
		while (slash > -1) {
			if (slash == start || s.charAt(slash - 1) != '\\') {
				if (sb == null) {
					sb = new StringBuilder();
				}
				sb.append(s, start, slash);
				sb.append("\\/");
				start = slash + 1;
			}
			slash = s.indexOf('/', slash + 1);
		}
		if (sb != null) {
			sb.append(s, start, s.length());
			s = sb.toString();
		}
		return s;
	}

	static RECompiled compileRE(Context cx, String str, String global, boolean flat) {
		RECompiled regexp = new RECompiled(str);
		int length = str.length();
		int flags = 0;
		if (global != null) {
			for (int i = 0; i < global.length(); i++) {
				char c = global.charAt(i);
				int f = 0;
				if (c == 'g') {
					f = JSREG_GLOB;
				} else if (c == 'i') {
					f = JSREG_FOLD;
				} else if (c == 'm') {
					f = JSREG_MULTILINE;
				} else {
					reportError("msg.invalid.re.flag", String.valueOf(c), cx);
				}
				if ((flags & f) != 0) {
					reportError("msg.invalid.re.flag", String.valueOf(c), cx);
				}
				flags |= f;
			}
		}
		regexp.flags = flags;

		CompilerState state = new CompilerState(cx, regexp.source, length, flags);
		if (flat && length > 0) {
			if (debug) {
				System.out.println("flat = \"" + str + "\"");
			}
			state.result = new RENode(REOP_FLAT);
			state.result.chr = state.cpbegin[0];
			state.result.length = length;
			state.result.flatIndex = 0;
			state.progLength += 5;
		} else {
			if (!parseDisjunction(state, cx)) {
				return null;
			}
			// Need to reparse if pattern contains invalid backreferences:
			// "Note: if the number of left parentheses is less than the number
			// specified in \#, the \# is taken as an octal escape"
			if (state.maxBackReference > state.parenCount) {
				state = new CompilerState(cx, regexp.source, length, flags);
				state.backReferenceLimit = state.parenCount;
				if (!parseDisjunction(state, cx)) {
					return null;
				}
			}
		}

		regexp.program = new byte[state.progLength + 1];
		if (state.classCount != 0) {
			regexp.classList = new RECharSet[state.classCount];
			regexp.classCount = state.classCount;
		}
		int endPC = emitREBytecode(state, regexp, 0, state.result, cx);
		regexp.program[endPC++] = REOP_END;

		if (debug) {
			System.out.println("Prog. length = " + endPC);
			for (int i = 0; i < endPC; i++) {
				System.out.print(regexp.program[i]);
				if (i < (endPC - 1)) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
		regexp.parenCount = state.parenCount;

		// If re starts with literal, init anchorCh accordingly
		switch (regexp.program[0]) {
			case REOP_UCFLAT1, REOP_UCFLAT1i -> regexp.anchorCh = (char) getIndex(regexp.program, 1);
			case REOP_FLAT1, REOP_FLAT1i -> regexp.anchorCh = (char) (regexp.program[1] & 0xFF);
			case REOP_FLAT, REOP_FLATi -> {
				int k = getIndex(regexp.program, 1);
				regexp.anchorCh = regexp.source[k];
			}
			case REOP_BOL -> regexp.anchorCh = ANCHOR_BOL;
			case REOP_ALT -> {
				RENode n = state.result;
				if (n.kid.op == REOP_BOL && n.kid2.op == REOP_BOL) {
					regexp.anchorCh = ANCHOR_BOL;
				}
			}
		}

		if (debug) {
			if (regexp.anchorCh >= 0) {
				System.out.println("Anchor ch = '" + (char) regexp.anchorCh + "'");
			}
		}
		return regexp;
	}

	static boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	private static boolean isWord(char c) {
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || isDigit(c) || c == '_';
	}

	private static boolean isControlLetter(char c) {
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}

	private static boolean isLineTerm(char c) {
		return ScriptRuntime.isJSLineTerminator(c);
	}

	/*
	 *  item:       assertion               An item is either an assertion or
	 *              quantatom               a quantified atom.
	 *
	 *  assertion:  '^'                     Assertions match beginning of string
	 *                                      (or line if the class static property
	 *                                      RegExp.multiline is true).
	 *              '$'                     End of string (or line if the class
	 *                                      static property RegExp.multiline is
	 *                                      true).
	 *              '\b'                    Word boundary (between \w and \W).
	 *              '\B'                    Word non-boundary.
	 *
	 *  quantatom:  atom                    An unquantified atom.
	 *              quantatom '{' n ',' m '}'
	 *                                      Atom must occur between n and m times.
	 *              quantatom '{' n ',' '}' Atom must occur at least n times.
	 *              quantatom '{' n '}'     Atom must occur exactly n times.
	 *              quantatom '*'           Zero or more times (same as {0,}).
	 *              quantatom '+'           One or more times (same as {1,}).
	 *              quantatom '?'           Zero or one time (same as {0,1}).
	 *
	 *              any of which can be optionally followed by '?' for ungreedy
	 *
	 *  atom:       '(' regexp ')'          A parenthesized regexp (what matched
	 *                                      can be addressed using a backreference,
	 *                                      see '\' n below).
	 *              '.'                     Matches any char except '\n'.
	 *              '[' classlist ']'       A character class.
	 *              '[' '^' classlist ']'   A negated character class.
	 *              '\f'                    Form Feed.
	 *              '\n'                    Newline (Line Feed).
	 *              '\r'                    Carriage Return.
	 *              '\t'                    Horizontal Tab.
	 *              '\v'                    Vertical Tab.
	 *              '\d'                    A digit (same as [0-9]).
	 *              '\D'                    A non-digit.
	 *              '\w'                    A word character, [0-9a-z_A-Z].
	 *              '\W'                    A non-word character.
	 *              '\s'                    A whitespace character, [ \b\f\n\r\t\v].
	 *              '\S'                    A non-whitespace character.
	 *              '\' n                   A backreference to the nth (n decimal
	 *                                      and positive) parenthesized expression.
	 *              '\' octal               An octal escape sequence (octal must be
	 *                                      two or three digits long, unless it is
	 *                                      0 for the null character).
	 *              '\x' hex                A hex escape (hex must be two digits).
	 *              '\c' ctrl               A control character, ctrl is a letter.
	 *              '\' literalatomchar     Any character except one of the above
	 *                                      that follow '\' in an atom.
	 *              otheratomchar           Any character not first among the other
	 *                                      atom right-hand sides.
	 */

	private static boolean isREWhiteSpace(int c) {
		return ScriptRuntime.isJSWhitespaceOrLineTerminator(c);
	}

	/*
	 *
	 * 1. If IgnoreCase is false, return ch.
	 * 2. Let u be ch converted to upper case as if by calling
	 *    String.prototype.toUpperCase on the one-character string ch.
	 * 3. If u does not consist of a single character, return ch.
	 * 4. Let cu be u's character.
	 * 5. If ch's code point value is greater than or equal to decimal 128 and cu's
	 *    code point value is less than decimal 128, then return ch.
	 * 6. Return cu.
	 */
	private static char upcase(char ch) {
		if (ch < 128) {
			if ('a' <= ch && ch <= 'z') {
				return (char) (ch + ('A' - 'a'));
			}
			return ch;
		}
		char cu = Character.toUpperCase(ch);
		return (cu < 128) ? ch : cu;
	}

	private static char downcase(char ch) {
		if (ch < 128) {
			if ('A' <= ch && ch <= 'Z') {
				return (char) (ch + ('a' - 'A'));
			}
			return ch;
		}
		char cl = Character.toLowerCase(ch);
		return (cl < 128) ? ch : cl;

	}

	/*
	 * Validates and converts hex ascii value.
	 */
	private static int toASCIIHexDigit(int c) {
		if (c < '0') {
			return -1;
		}
		if (c <= '9') {
			return c - '0';
		}
		c |= 0x20;
		if ('a' <= c && c <= 'f') {
			return c - 'a' + 10;
		}
		return -1;
	}

	/*
	 * Top-down regular expression grammar, based closely on Perl4.
	 *
	 *  regexp:     altern                  A regular expression is one or more
	 *              altern '|' regexp       alternatives separated by vertical bar.
	 */
	private static boolean parseDisjunction(CompilerState state, Context cx) {
		if (!parseAlternative(state, cx)) {
			return false;
		}
		char[] source = state.cpbegin;
		int index = state.cp;
		if (index != source.length && source[index] == '|') {
			RENode result;
			++state.cp;
			result = new RENode(REOP_ALT);
			result.kid = state.result;
			if (!parseDisjunction(state, cx)) {
				return false;
			}
			result.kid2 = state.result;
			state.result = result;
			/*
			 * Look at both alternates to see if there's a FLAT or a CLASS at
			 * the start of each. If so, use a prerequisite match.
			 */
			if (result.kid.op == REOP_FLAT && result.kid2.op == REOP_FLAT) {
				result.op = (state.flags & JSREG_FOLD) == 0 ? REOP_ALTPREREQ : REOP_ALTPREREQi;
				result.chr = result.kid.chr;
				result.index = result.kid2.chr;
                /* ALTPREREQ, uch1, uch2, <next>, ...,
                                            JUMP, <end> ... JUMP, <end> */
				state.progLength += 13;
			} else if (result.kid.op == REOP_CLASS && result.kid.index < 256 && result.kid2.op == REOP_FLAT && (state.flags & JSREG_FOLD) == 0) {
				result.op = REOP_ALTPREREQ2;
				result.chr = result.kid2.chr;
				result.index = result.kid.index;
                /* ALTPREREQ2, uch1, uch2, <next>, ...,
                                            JUMP, <end> ... JUMP, <end> */
				state.progLength += 13;
			} else if (result.kid.op == REOP_FLAT && result.kid2.op == REOP_CLASS && result.kid2.index < 256 && (state.flags & JSREG_FOLD) == 0) {
				result.op = REOP_ALTPREREQ2;
				result.chr = result.kid.chr;
				result.index = result.kid2.index;
                /* ALTPREREQ2, uch1, uch2, <next>, ...,
                                            JUMP, <end> ... JUMP, <end> */
				state.progLength += 13;
			} else {
				/* ALT, <next>, ..., JUMP, <end> ... JUMP, <end> */
				state.progLength += 9;
			}
		}
		return true;
	}

	/*
	 *  altern:     item                    An alternative is one or more items,
	 *              item altern             concatenated together.
	 */
	private static boolean parseAlternative(CompilerState state, Context cx) {
		RENode headTerm = null;
		RENode tailTerm = null;
		char[] source = state.cpbegin;
		while (true) {
			if (state.cp == state.cpend || source[state.cp] == '|' || (state.parenNesting != 0 && source[state.cp] == ')')) {
				if (headTerm == null) {
					state.result = new RENode(REOP_EMPTY);
				} else {
					state.result = headTerm;
				}
				return true;
			}
			if (!parseTerm(state, cx)) {
				return false;
			}
			if (headTerm == null) {
				headTerm = state.result;
				tailTerm = headTerm;
			} else {
				tailTerm.next = state.result;
			}
			while (tailTerm.next != null) {
				tailTerm = tailTerm.next;
			}
		}
	}

	/* calculate the total size of the bitmap required for a class expression */
	private static boolean calculateBitmapSize(CompilerState state, RENode target, char[] src, int index, int end, Context cx) {
		char rangeStart = 0;
		char c;
		int n;
		int nDigits;
		int i;
		int max = 0;
		boolean inRange = false;

		target.bmsize = 0;
		target.sense = true;

		if (index == end) {
			return true;
		}

		if (src[index] == '^') {
			++index;
			target.sense = false;
		}

		while (index != end) {
			int localMax = 0;
			nDigits = 2;
			if (src[index] == '\\') {
				++index;
				c = src[index++];
				switch (c) {
					case 'b':
						localMax = 0x8;
						break;
					case 'f':
						localMax = 0xC;
						break;
					case 'n':
						localMax = 0xA;
						break;
					case 'r':
						localMax = 0xD;
						break;
					case 't':
						localMax = 0x9;
						break;
					case 'v':
						localMax = 0xB;
						break;
					case 'c':
						if ((index < end) && isControlLetter(src[index])) {
							localMax = (char) (src[index++] & 0x1F);
						} else {
							--index;
						}
						localMax = '\\';
						break;
					case 'u':
						nDigits += 2;
						// fall through
					case 'x':
						n = 0;
						for (i = 0; (i < nDigits) && (index < end); i++) {
							c = src[index++];
							n = Kit.xDigitToInt(c, n);
							if (n < 0) {
								// Back off to accepting the original
								// '\' as a literal
								index -= (i + 1);
								n = '\\';
								break;
							}
						}
						localMax = n;
						break;
					case 'd':
						if (inRange) {
							target.bmsize = 65536;
							return true;
						}
						localMax = '9';
						break;
					case 'D':
					case 'w':
					case 'W':
					case 'S':
					case 's':
						target.bmsize = 65536;
						return true;

					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
						/*
						 *  This is a non-ECMA extension - decimal escapes (in this
						 *  case, octal!) are supposed to be an error inside class
						 *  ranges, but supported here for backwards compatibility.
						 *
						 */
						n = (c - '0');
						c = src[index];
						if ('0' <= c && c <= '7') {
							index++;
							n = 8 * n + (c - '0');
							c = src[index];
							if ('0' <= c && c <= '7') {
								index++;
								i = 8 * n + (c - '0');
								if (i <= 0377) {
									n = i;
								} else {
									index--;
								}
							}
						}
						localMax = n;
						break;

					default:
						localMax = c;
						break;
				}
			} else {
				localMax = src[index++];
			}
			if (inRange) {
				if (rangeStart > localMax) {
					reportError("msg.bad.range", "", cx);
					return false;
				}
				inRange = false;
			} else {
				if (index < (end - 1)) {
					if (src[index] == '-') {
						++index;
						inRange = true;
						rangeStart = (char) localMax;
						continue;
					}
				}
			}
			if ((state.flags & JSREG_FOLD) != 0) {
				char cu = upcase((char) localMax);
				char cd = downcase((char) localMax);
				localMax = (cu >= cd) ? cu : cd;
			}
			if (localMax > max) {
				max = localMax;
			}
		}
		target.bmsize = max + 1;
		return true;
	}

	private static void doFlat(CompilerState state, char c) {
		state.result = new RENode(REOP_FLAT);
		state.result.chr = c;
		state.result.length = 1;
		state.result.flatIndex = -1;
		state.progLength += 3;
	}

	private static int getDecimalValue(char c, CompilerState state, int maxValue, String overflowMessageId, Context cx) {
		boolean overflow = false;
		int start = state.cp;
		char[] src = state.cpbegin;
		int value = c - '0';
		for (; state.cp != state.cpend; ++state.cp) {
			c = src[state.cp];
			if (!isDigit(c)) {
				break;
			}
			if (!overflow) {
				int v = value * 10 + (c - '0');
				if (v < maxValue) {
					value = v;
				} else {
					overflow = true;
					value = maxValue;
				}
			}
		}
		if (overflow) {
			reportError(overflowMessageId, String.valueOf(src, start, state.cp - start), cx);
		}
		return value;
	}

	private static boolean parseTerm(CompilerState state, Context cx) {
		char[] src = state.cpbegin;
		char c = src[state.cp++];
		int nDigits = 2;
		int parenBaseCount = state.parenCount;
		int num;
		RENode term;
		int termStart;

		switch (c) {
			/* assertions and atoms */
			case '^' -> {
				state.result = new RENode(REOP_BOL);
				state.progLength++;
				return true;
			}
			case '$' -> {
				state.result = new RENode(REOP_EOL);
				state.progLength++;
				return true;
			}
			case '\\' -> {
				if (state.cp < state.cpend) {
					c = src[state.cp++];
					switch (c) {
						/* assertion escapes */
						case 'b':
							state.result = new RENode(REOP_WBDRY);
							state.progLength++;
							return true;
						case 'B':
							state.result = new RENode(REOP_WNONBDRY);
							state.progLength++;
							return true;
						/* Decimal escape */
						case '0':
							/*
							 * We're deliberately violating the ECMA 5.1 specification and allow octal
							 * escapes to follow spidermonkey and general 'web reality':
							 * http://wiki.ecmascript.org/doku.php?id=harmony:regexp_match_web_reality
							 * http://wiki.ecmascript.org/doku.php?id=strawman:match_web_reality_spec
							 */
							/* octal escape */
							num = 0;
							// follow spidermonkey and allow multiple leading zeros,
							// e.g. let /\0000/ match the string "\0"
							while (num < 040 && state.cp < state.cpend) {
								c = src[state.cp];
								if ((c >= '0') && (c <= '7')) {
									state.cp++;
									num = 8 * num + (c - '0');
								} else {
									break;
								}
							}
							c = (char) (num);
							doFlat(state, c);
							break;
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							termStart = state.cp - 1;
							num = getDecimalValue(c, state, 0xFFFF, "msg.overlarge.backref", cx);
							/*
							 * n > count of parentheses, then treat as octal instead.
							 * Also see note above concerning 'web reality'
							 */
							if (num > state.backReferenceLimit) {
								state.cp = termStart;
								if (c >= '8') {
									// invalid octal escape, follow spidermonkey and
									// treat as \\8 resp. \\9
									c = '\\';
									doFlat(state, c);
									break;
								}
								state.cp++;
								num = c - '0';
								while (num < 040 && state.cp < state.cpend) {
									c = src[state.cp];
									if ((c >= '0') && (c <= '7')) {
										state.cp++;
										num = 8 * num + (c - '0');
									} else {
										break;
									}
								}
								c = (char) (num);
								doFlat(state, c);
								break;
							}
							/* otherwise, it's a back-reference */
							state.result = new RENode(REOP_BACKREF);
							state.result.parenIndex = num - 1;
							state.progLength += 3;
							if (state.maxBackReference < num) {
								state.maxBackReference = num;
							}
							break;
						/* Control escape */
						case 'f':
							c = 0xC;
							doFlat(state, c);
							break;
						case 'n':
							c = 0xA;
							doFlat(state, c);
							break;
						case 'r':
							c = 0xD;
							doFlat(state, c);
							break;
						case 't':
							c = 0x9;
							doFlat(state, c);
							break;
						case 'v':
							c = 0xB;
							doFlat(state, c);
							break;
						/* Control letter */
						case 'c':
							if ((state.cp < state.cpend) && isControlLetter(src[state.cp])) {
								c = (char) (src[state.cp++] & 0x1F);
							} else {
								/* back off to accepting the original '\' as a literal */
								--state.cp;
								c = '\\';
							}
							doFlat(state, c);
							break;
						/* UnicodeEscapeSequence */
						case 'u':
							nDigits += 2;
							/* fall through */
						case 'x':  /* HexEscapeSequence */ {
							int n = 0;
							int i;
							for (i = 0; (i < nDigits) && (state.cp < state.cpend); i++) {
								c = src[state.cp++];
								n = Kit.xDigitToInt(c, n);
								if (n < 0) {
									// Back off to accepting the original
									// 'u' or 'x' as a literal
									state.cp -= (i + 2);
									n = src[state.cp++];
									break;
								}
							}
							c = (char) (n);
						}
						doFlat(state, c);
						break;
						/* Character class escapes */
						case 'd':
							state.result = new RENode(REOP_DIGIT);
							state.progLength++;
							break;
						case 'D':
							state.result = new RENode(REOP_NONDIGIT);
							state.progLength++;
							break;
						case 's':
							state.result = new RENode(REOP_SPACE);
							state.progLength++;
							break;
						case 'S':
							state.result = new RENode(REOP_NONSPACE);
							state.progLength++;
							break;
						case 'w':
							state.result = new RENode(REOP_ALNUM);
							state.progLength++;
							break;
						case 'W':
							state.result = new RENode(REOP_NONALNUM);
							state.progLength++;
							break;
						/* IdentityEscape */
						default:
							state.result = new RENode(REOP_FLAT);
							state.result.chr = c;
							state.result.length = 1;
							state.result.flatIndex = state.cp - 1;
							state.progLength += 3;
							break;
					}
					break;
				}
				/* a trailing '\' is an error */
				reportError("msg.trail.backslash", "", cx);
				return false;
			}
			case '(' -> {
				RENode result = null;
				termStart = state.cp;
				if (state.cp + 1 < state.cpend && src[state.cp] == '?' && ((c = src[state.cp + 1]) == '=' || c == '!' || c == ':')) {
					state.cp += 2;
					if (c == '=') {
						result = new RENode(REOP_ASSERT);
						/* ASSERT, <next>, ... ASSERTTEST */
						state.progLength += 4;
					} else if (c == '!') {
						result = new RENode(REOP_ASSERT_NOT);
						/* ASSERTNOT, <next>, ... ASSERTNOTTEST */
						state.progLength += 4;
					}
				} else {
					result = new RENode(REOP_LPAREN);
					/* LPAREN, <index>, ... RPAREN, <index> */
					state.progLength += 6;
					result.parenIndex = state.parenCount++;
				}
				++state.parenNesting;
				if (!parseDisjunction(state, cx)) {
					return false;
				}
				if (state.cp == state.cpend || src[state.cp] != ')') {
					reportError("msg.unterm.paren", "", cx);
					return false;
				}
				++state.cp;
				--state.parenNesting;
				if (result != null) {
					result.kid = state.result;
					state.result = result;
				}
				break;
			}
			case ')' -> {
				reportError("msg.re.unmatched.right.paren", "", cx);
				return false;
			}
			case '[' -> {
				state.result = new RENode(REOP_CLASS);
				termStart = state.cp;
				state.result.startIndex = termStart;
				while (true) {
					if (state.cp == state.cpend) {
						reportError("msg.unterm.class", "", cx);
						return false;
					}
					if (src[state.cp] == '\\') {
						state.cp++;
					} else {
						if (src[state.cp] == ']') {
							state.result.kidlen = state.cp - termStart;
							break;
						}
					}
					state.cp++;
				}
				state.result.index = state.classCount++;
				/*
				 * Call calculateBitmapSize now as we want any errors it finds
				 * to be reported during the parse phase, not at execution.
				 */
				if (!calculateBitmapSize(state, state.result, src, termStart, state.cp++, cx)) {
					return false;
				}
				state.progLength += 3; /* CLASS, <index> */
			}
			case '.' -> {
				state.result = new RENode(REOP_DOT);
				state.progLength++;
			}
			case '*', '+', '?' -> {
				reportError("msg.bad.quant", String.valueOf(src[state.cp - 1]), cx);
				return false;
			}
			default -> {
				state.result = new RENode(REOP_FLAT);
				state.result.chr = c;
				state.result.length = 1;
				state.result.flatIndex = state.cp - 1;
				state.progLength += 3;
			}
		}

		term = state.result;
		if (state.cp == state.cpend) {
			return true;
		}
		boolean hasQ = false;
		switch (src[state.cp]) {
			case '+' -> {
				state.result = new RENode(REOP_QUANT);
				state.result.min = 1;
				state.result.max = -1;
				/* <PLUS>, <parencount>, <parenindex>, <next> ... <ENDCHILD> */
				state.progLength += 8;
				hasQ = true;
			}
			case '*' -> {
				state.result = new RENode(REOP_QUANT);
				state.result.min = 0;
				state.result.max = -1;
				/* <STAR>, <parencount>, <parenindex>, <next> ... <ENDCHILD> */
				state.progLength += 8;
				hasQ = true;
			}
			case '?' -> {
				state.result = new RENode(REOP_QUANT);
				state.result.min = 0;
				state.result.max = 1;
				/* <OPT>, <parencount>, <parenindex>, <next> ... <ENDCHILD> */
				state.progLength += 8;
				hasQ = true;
			}
			case '{' ->  /* balance '}' */ {
				int min = 0;
				int max = -1;
				int leftCurl = state.cp;

				/* For Perl etc. compatibility, if quntifier does not match
				 * \{\d+(,\d*)?\} exactly back off from it
				 * being a quantifier, and chew it up as a literal
				 * atom next time instead.
				 */

				if (++state.cp < src.length && isDigit(c = src[state.cp])) {
					++state.cp;
					min = getDecimalValue(c, state, 0xFFFF, "msg.overlarge.min", cx);
					if (state.cp < src.length) {
						c = src[state.cp];
						if (c == ',' && ++state.cp < src.length) {
							c = src[state.cp];
							if (isDigit(c) && ++state.cp < src.length) {
								max = getDecimalValue(c, state, 0xFFFF, "msg.overlarge.max", cx);
								c = src[state.cp];
								if (min > max) {
									reportError("msg.max.lt.min", String.valueOf(src[state.cp]), cx);
									return false;
								}
							}
						} else {
							max = min;
						}
						/* balance '{' */
						if (c == '}') {
							state.result = new RENode(REOP_QUANT);
							state.result.min = min;
							state.result.max = max;
							// QUANT, <min>, <max>, <parencount>,
							// <parenindex>, <next> ... <ENDCHILD>
							state.progLength += 12;
							hasQ = true;
						}
					}
				}
				if (!hasQ) {
					state.cp = leftCurl;
				}
				break;
			}
		}
		if (!hasQ) {
			return true;
		}

		++state.cp;
		state.result.kid = term;
		state.result.parenIndex = parenBaseCount;
		state.result.parenCount = state.parenCount - parenBaseCount;
		if ((state.cp < state.cpend) && (src[state.cp] == '?')) {
			++state.cp;
			state.result.greedy = false;
		} else {
			state.result.greedy = true;
		}
		return true;
	}

	private static void resolveForwardJump(byte[] array, int from, int pc, Context cx) {
		if (from > pc) {
			throw Kit.codeBug();
		}
		addIndex(array, from, pc - from, cx);
	}

	private static int getOffset(byte[] array, int pc) {
		return getIndex(array, pc);
	}

	private static int addIndex(byte[] array, int pc, int index, Context cx) {
		if (index < 0) {
			throw Kit.codeBug();
		}
		if (index > 0xFFFF) {
			throw Context.reportRuntimeError("Too complex regexp", cx);
		}
		array[pc] = (byte) (index >> 8);
		array[pc + 1] = (byte) (index);
		return pc + 2;
	}

	private static int getIndex(byte[] array, int pc) {
		return ((array[pc] & 0xFF) << 8) | (array[pc + 1] & 0xFF);
	}

	private static int emitREBytecode(CompilerState state, RECompiled re, int pc, RENode t, Context cx) {
		RENode nextAlt;
		int nextAltFixup, nextTermFixup;
		byte[] program = re.program;

		while (t != null) {
			program[pc++] = t.op;
			switch (t.op) {
				case REOP_EMPTY:
					--pc;
					break;
				case REOP_ALTPREREQ:
				case REOP_ALTPREREQi:
				case REOP_ALTPREREQ2:
					boolean ignoreCase = t.op == REOP_ALTPREREQi;
					addIndex(program, pc, ignoreCase ? upcase(t.chr) : t.chr, cx);
					pc += INDEX_LEN;
					addIndex(program, pc, ignoreCase ? upcase((char) t.index) : t.index, cx);
					pc += INDEX_LEN;
					// fall through to REOP_ALT
				case REOP_ALT:
					nextAlt = t.kid2;
					nextAltFixup = pc;    /* address of next alternate */
					pc += INDEX_LEN;
					pc = emitREBytecode(state, re, pc, t.kid, cx);
					program[pc++] = REOP_JUMP;
					nextTermFixup = pc;    /* address of following term */
					pc += INDEX_LEN;
					resolveForwardJump(program, nextAltFixup, pc, cx);
					pc = emitREBytecode(state, re, pc, nextAlt, cx);

					program[pc++] = REOP_JUMP;
					nextAltFixup = pc;
					pc += INDEX_LEN;

					resolveForwardJump(program, nextTermFixup, pc, cx);
					resolveForwardJump(program, nextAltFixup, pc, cx);
					break;
				case REOP_FLAT:
					/*
					 * Consecutize FLAT's if possible.
					 */
					if (t.flatIndex != -1) {
						while ((t.next != null) && (t.next.op == REOP_FLAT) && ((t.flatIndex + t.length) == t.next.flatIndex)) {
							t.length += t.next.length;
							t.next = t.next.next;
						}
					}
					if ((t.flatIndex != -1) && (t.length > 1)) {
						if ((state.flags & JSREG_FOLD) != 0) {
							program[pc - 1] = REOP_FLATi;
						} else {
							program[pc - 1] = REOP_FLAT;
						}
						pc = addIndex(program, pc, t.flatIndex, cx);
						pc = addIndex(program, pc, t.length, cx);
					} else {
						if (t.chr < 256) {
							if ((state.flags & JSREG_FOLD) != 0) {
								program[pc - 1] = REOP_FLAT1i;
							} else {
								program[pc - 1] = REOP_FLAT1;
							}
							program[pc++] = (byte) (t.chr);
						} else {
							if ((state.flags & JSREG_FOLD) != 0) {
								program[pc - 1] = REOP_UCFLAT1i;
							} else {
								program[pc - 1] = REOP_UCFLAT1;
							}
							pc = addIndex(program, pc, t.chr, cx);
						}
					}
					break;
				case REOP_LPAREN:
					pc = addIndex(program, pc, t.parenIndex, cx);
					pc = emitREBytecode(state, re, pc, t.kid, cx);
					program[pc++] = REOP_RPAREN;
					pc = addIndex(program, pc, t.parenIndex, cx);
					break;
				case REOP_BACKREF:
					pc = addIndex(program, pc, t.parenIndex, cx);
					break;
				case REOP_ASSERT:
					nextTermFixup = pc;
					pc += INDEX_LEN;
					pc = emitREBytecode(state, re, pc, t.kid, cx);
					program[pc++] = REOP_ASSERTTEST;
					resolveForwardJump(program, nextTermFixup, pc, cx);
					break;
				case REOP_ASSERT_NOT:
					nextTermFixup = pc;
					pc += INDEX_LEN;
					pc = emitREBytecode(state, re, pc, t.kid, cx);
					program[pc++] = REOP_ASSERTNOTTEST;
					resolveForwardJump(program, nextTermFixup, pc, cx);
					break;
				case REOP_QUANT:
					if ((t.min == 0) && (t.max == -1)) {
						program[pc - 1] = (t.greedy) ? REOP_STAR : REOP_MINIMALSTAR;
					} else if ((t.min == 0) && (t.max == 1)) {
						program[pc - 1] = (t.greedy) ? REOP_OPT : REOP_MINIMALOPT;
					} else if ((t.min == 1) && (t.max == -1)) {
						program[pc - 1] = (t.greedy) ? REOP_PLUS : REOP_MINIMALPLUS;
					} else {
						if (!t.greedy) {
							program[pc - 1] = REOP_MINIMALQUANT;
						}
						pc = addIndex(program, pc, t.min, cx);
						// max can be -1 which addIndex does not accept
						pc = addIndex(program, pc, t.max + 1, cx);
					}
					pc = addIndex(program, pc, t.parenCount, cx);
					pc = addIndex(program, pc, t.parenIndex, cx);
					nextTermFixup = pc;
					pc += INDEX_LEN;
					pc = emitREBytecode(state, re, pc, t.kid, cx);
					program[pc++] = REOP_ENDCHILD;
					resolveForwardJump(program, nextTermFixup, pc, cx);
					break;
				case REOP_CLASS:
					if (!t.sense) {
						program[pc - 1] = REOP_NCLASS;
					}
					pc = addIndex(program, pc, t.index, cx);
					re.classList[t.index] = new RECharSet(t.bmsize, t.startIndex, t.kidlen, t.sense);
					break;
				default:
					break;
			}
			t = t.next;
		}
		return pc;
	}

	private static void pushProgState(REGlobalData gData, int min, int max, int cp, REBackTrackData backTrackLastToSave, int continuationOp, int continuationPc) {
		gData.stateStackTop = new REProgState(gData.stateStackTop, min, max, cp, backTrackLastToSave, continuationOp, continuationPc);
	}

	private static REProgState popProgState(REGlobalData gData) {
		REProgState state = gData.stateStackTop;
		gData.stateStackTop = state.previous;
		return state;
	}

	private static void pushBackTrackState(REGlobalData gData, byte op, int pc) {
		REProgState state = gData.stateStackTop;
		gData.backTrackStackTop = new REBackTrackData(gData, op, pc, gData.cp, state.continuationOp, state.continuationPc);
	}

	private static void pushBackTrackState(REGlobalData gData, byte op, int pc, int cp, int continuationOp, int continuationPc) {
		gData.backTrackStackTop = new REBackTrackData(gData, op, pc, cp, continuationOp, continuationPc);
	}

	/*
	 *   Consecutive literal characters.
	 */
	private static boolean flatNMatcher(REGlobalData gData, int matchChars, int length, String input, int end) {
		if ((gData.cp + length) > end) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (gData.regexp.source[matchChars + i] != input.charAt(gData.cp + i)) {
				return false;
			}
		}
		gData.cp += length;
		return true;
	}

	private static boolean flatNIMatcher(REGlobalData gData, int matchChars, int length, String input, int end) {
		if ((gData.cp + length) > end) {
			return false;
		}
		char[] source = gData.regexp.source;
		for (int i = 0; i < length; i++) {
			char c1 = source[matchChars + i];
			char c2 = input.charAt(gData.cp + i);
			if (c1 != c2 && upcase(c1) != upcase(c2)) {
				return false;
			}
		}
		gData.cp += length;
		return true;
	}

	/*
    1. Evaluate DecimalEscape to obtain an EscapeValue E.
    2. If E is not a character then go to step 6.
    3. Let ch be E's character.
    4. Let A be a one-element RECharSet containing the character ch.
    5. Call CharacterSetMatcher(A, false) and return its Matcher result.
    6. E must be an integer. Let n be that integer.
    7. If n=0 or n>NCapturingParens then throw a SyntaxError exception.
    8. Return an internal Matcher closure that takes two arguments, a State x
       and a Continuation c, and performs the following:
        1. Let cap be x's captures internal array.
        2. Let s be cap[n].
        3. If s is undefined, then call c(x) and return its result.
        4. Let e be x's endIndex.
        5. Let len be s's length.
        6. Let f be e+len.
        7. If f>InputLength, return failure.
        8. If there exists an integer i between 0 (inclusive) and len (exclusive)
           such that Canonicalize(s[i]) is not the same character as
           Canonicalize(Input [e+i]), then return failure.
        9. Let y be the State (f, cap).
        10. Call c(y) and return its result.
    */
	private static boolean backrefMatcher(REGlobalData gData, int parenIndex, String input, int end) {
		int len;
		int i;
		if (gData.parens == null || parenIndex >= gData.parens.length) {
			return false;
		}
		int parenContent = gData.parensIndex(parenIndex);
		if (parenContent == -1) {
			return true;
		}

		len = gData.parensLength(parenIndex);
		if ((gData.cp + len) > end) {
			return false;
		}

		if ((gData.regexp.flags & JSREG_FOLD) != 0) {
			for (i = 0; i < len; i++) {
				char c1 = input.charAt(parenContent + i);
				char c2 = input.charAt(gData.cp + i);
				if (c1 != c2 && upcase(c1) != upcase(c2)) {
					return false;
				}
			}
		} else if (!input.regionMatches(parenContent, input, gData.cp, len)) {
			return false;
		}
		gData.cp += len;
		return true;
	}

	/* Add a single character to the RECharSet */
	private static void addCharacterToCharSet(RECharSet cs, char c, Context cx) {
		int byteIndex = (c / 8);
		if (c >= cs.length) {
			throw ScriptRuntime.constructError(cx, "SyntaxError", "invalid range in character class");
		}
		cs.bits[byteIndex] |= 1 << (c & 0x7);
	}

	/* Add a character range, c1 to c2 (inclusive) to the RECharSet */
	private static void addCharacterRangeToCharSet(RECharSet cs, char c1, char c2, Context cx) {
		int i;

		int byteIndex1 = (c1 / 8);
		int byteIndex2 = (c2 / 8);

		if ((c2 >= cs.length) || (c1 > c2)) {
			throw ScriptRuntime.constructError(cx, "SyntaxError", "invalid range in character class");
		}

		c1 &= 0x7;
		c2 &= 0x7;

		if (byteIndex1 == byteIndex2) {
			cs.bits[byteIndex1] |= ((0xFF) >> (7 - (c2 - c1))) << c1;
		} else {
			cs.bits[byteIndex1] |= 0xFF << c1;
			for (i = byteIndex1 + 1; i < byteIndex2; i++) {
				cs.bits[i] = (byte) 0xFF;
			}
			cs.bits[byteIndex2] |= (0xFF) >> (7 - c2);
		}
	}

	/* Compile the source of the class into a RECharSet */
	private static void processCharSet(REGlobalData gData, RECharSet charSet, Context cx) {
		synchronized (charSet) {
			if (!charSet.converted) {
				processCharSetImpl(gData, charSet, cx);
				charSet.converted = true;
			}
		}
	}

	private static void processCharSetImpl(REGlobalData gData, RECharSet charSet, Context cx) {
		int src = charSet.startIndex;
		int end = src + charSet.strlength;

		char rangeStart = 0, thisCh;
		int byteLength;
		char c;
		int n;
		int nDigits;
		int i;
		boolean inRange = false;

		byteLength = (charSet.length + 7) / 8;
		charSet.bits = new byte[byteLength];

		if (src == end) {
			return;
		}

		if (gData.regexp.source[src] == '^') {
			assert (!charSet.sense);
			++src;
		} else {
			assert (charSet.sense);
		}

		while (src != end) {
			nDigits = 2;
			if (gData.regexp.source[src] == '\\') {
				++src;
				c = gData.regexp.source[src++];
				switch (c) {
					case 'b':
						thisCh = 0x8;
						break;
					case 'f':
						thisCh = 0xC;
						break;
					case 'n':
						thisCh = 0xA;
						break;
					case 'r':
						thisCh = 0xD;
						break;
					case 't':
						thisCh = 0x9;
						break;
					case 'v':
						thisCh = 0xB;
						break;
					case 'c':
						if ((src < end) && isControlLetter(gData.regexp.source[src])) {
							thisCh = (char) (gData.regexp.source[src++] & 0x1F);
						} else {
							--src;
							thisCh = '\\';
						}
						break;
					case 'u':
						nDigits += 2;
						// fall through
					case 'x':
						n = 0;
						for (i = 0; (i < nDigits) && (src < end); i++) {
							c = gData.regexp.source[src++];
							int digit = toASCIIHexDigit(c);
							if (digit < 0) {
								/* back off to accepting the original '\'
								 * as a literal
								 */
								src -= (i + 1);
								n = '\\';
								break;
							}
							n = (n << 4) | digit;
						}
						thisCh = (char) (n);
						break;
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
						/*
						 *  This is a non-ECMA extension - decimal escapes (in this
						 *  case, octal!) are supposed to be an error inside class
						 *  ranges, but supported here for backwards compatibility.
						 *
						 */
						n = (c - '0');
						c = gData.regexp.source[src];
						if ('0' <= c && c <= '7') {
							src++;
							n = 8 * n + (c - '0');
							c = gData.regexp.source[src];
							if ('0' <= c && c <= '7') {
								src++;
								i = 8 * n + (c - '0');
								if (i <= 0377) {
									n = i;
								} else {
									src--;
								}
							}
						}
						thisCh = (char) (n);
						break;

					case 'd':
						if (inRange) {
							addCharacterToCharSet(charSet, '-', cx);
							inRange = false;
						}
						addCharacterRangeToCharSet(charSet, '0', '9', cx);
						continue;   /* don't need range processing */
					case 'D':
						if (inRange) {
							addCharacterToCharSet(charSet, '-', cx);
							inRange = false;
						}
						addCharacterRangeToCharSet(charSet, (char) 0, (char) ('0' - 1), cx);
						addCharacterRangeToCharSet(charSet, (char) ('9' + 1), (char) (charSet.length - 1), cx);
						continue;
					case 's':
						if (inRange) {
							addCharacterToCharSet(charSet, '-', cx);
							inRange = false;
						}
						for (i = (charSet.length - 1); i >= 0; i--) {
							if (isREWhiteSpace(i)) {
								addCharacterToCharSet(charSet, (char) (i), cx);
							}
						}
						continue;
					case 'S':
						if (inRange) {
							addCharacterToCharSet(charSet, '-', cx);
							inRange = false;
						}
						for (i = (charSet.length - 1); i >= 0; i--) {
							if (!isREWhiteSpace(i)) {
								addCharacterToCharSet(charSet, (char) (i), cx);
							}
						}
						continue;
					case 'w':
						if (inRange) {
							addCharacterToCharSet(charSet, '-', cx);
							inRange = false;
						}
						for (i = (charSet.length - 1); i >= 0; i--) {
							if (isWord((char) i)) {
								addCharacterToCharSet(charSet, (char) (i), cx);
							}
						}
						continue;
					case 'W':
						if (inRange) {
							addCharacterToCharSet(charSet, '-', cx);
							inRange = false;
						}
						for (i = (charSet.length - 1); i >= 0; i--) {
							if (!isWord((char) i)) {
								addCharacterToCharSet(charSet, (char) (i), cx);
							}
						}
						continue;
					default:
						thisCh = c;
						break;
				}
			} else {
				thisCh = gData.regexp.source[src++];
			}
			if (inRange) {
				if ((gData.regexp.flags & JSREG_FOLD) != 0) {
					assert (rangeStart <= thisCh);
					for (c = rangeStart; c <= thisCh; ) {
						addCharacterToCharSet(charSet, c, cx);
						char uch = upcase(c);
						char dch = downcase(c);
						if (c != uch) {
							addCharacterToCharSet(charSet, uch, cx);
						}
						if (c != dch) {
							addCharacterToCharSet(charSet, dch, cx);
						}
						if (++c == 0) {
							break; // overflow
						}
					}
				} else {
					addCharacterRangeToCharSet(charSet, rangeStart, thisCh, cx);
				}
				inRange = false;
			} else {
				if ((gData.regexp.flags & JSREG_FOLD) != 0) {
					addCharacterToCharSet(charSet, upcase(thisCh), cx);
					addCharacterToCharSet(charSet, downcase(thisCh), cx);
				} else {
					addCharacterToCharSet(charSet, thisCh, cx);
				}
				if (src < (end - 1)) {
					if (gData.regexp.source[src] == '-') {
						++src;
						inRange = true;
						rangeStart = thisCh;
					}
				}
			}
		}
	}

	/*
	 *   Initialize the character set if it this is the first call.
	 *   Test the bit - if the ^ flag was specified, non-inclusion is a success
	 */
	private static boolean classMatcher(REGlobalData gData, RECharSet charSet, char ch, Context cx) {
		if (!charSet.converted) {
			processCharSet(gData, charSet, cx);
		}

		int byteIndex = ch >> 3;
		return (charSet.length == 0 || ch >= charSet.length || (charSet.bits[byteIndex] & (1 << (ch & 0x7))) == 0) ^ charSet.sense;
	}

	private static boolean reopIsSimple(int op) {
		return op >= REOP_SIMPLE_START && op <= REOP_SIMPLE_END;
	}

	// #string_id_map#

	/*
	 *   Apply the current op against the given input to see if
	 *   it's going to match or fail. Return false if we don't
	 *   get a match, true if we do and update the state of the
	 *   input and pc if the update flag is true.
	 */
	private static int simpleMatch(REGlobalData gData, String input, int op, byte[] program, int pc, int end, boolean updatecp, Context cx) {
		boolean result = false;
		char matchCh;
		int parenIndex;
		int offset, length, index;
		int startcp = gData.cp;

		switch (op) {
			case REOP_EMPTY:
				result = true;
				break;
			case REOP_BOL:
				if (gData.cp != 0) {
					if (!gData.multiline || !isLineTerm(input.charAt(gData.cp - 1))) {
						break;
					}
				}
				result = true;
				break;
			case REOP_EOL:
				if (gData.cp != end) {
					if (!gData.multiline || !isLineTerm(input.charAt(gData.cp))) {
						break;
					}
				}
				result = true;
				break;
			case REOP_WBDRY:
				result = ((gData.cp == 0 || !isWord(input.charAt(gData.cp - 1))) ^ !((gData.cp < end) && isWord(input.charAt(gData.cp))));
				break;
			case REOP_WNONBDRY:
				result = ((gData.cp == 0 || !isWord(input.charAt(gData.cp - 1))) ^ ((gData.cp < end) && isWord(input.charAt(gData.cp))));
				break;
			case REOP_DOT:
				if (gData.cp != end && !isLineTerm(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_DIGIT:
				if (gData.cp != end && isDigit(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_NONDIGIT:
				if (gData.cp != end && !isDigit(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_ALNUM:
				if (gData.cp != end && isWord(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_NONALNUM:
				if (gData.cp != end && !isWord(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_SPACE:
				if (gData.cp != end && isREWhiteSpace(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_NONSPACE:
				if (gData.cp != end && !isREWhiteSpace(input.charAt(gData.cp))) {
					result = true;
					gData.cp++;
				}
				break;
			case REOP_BACKREF: {
				parenIndex = getIndex(program, pc);
				pc += INDEX_LEN;
				result = backrefMatcher(gData, parenIndex, input, end);
			}
			break;
			case REOP_FLAT: {
				offset = getIndex(program, pc);
				pc += INDEX_LEN;
				length = getIndex(program, pc);
				pc += INDEX_LEN;
				result = flatNMatcher(gData, offset, length, input, end);
			}
			break;
			case REOP_FLAT1: {
				matchCh = (char) (program[pc++] & 0xFF);
				if (gData.cp != end && input.charAt(gData.cp) == matchCh) {
					result = true;
					gData.cp++;
				}
			}
			break;
			case REOP_FLATi: {
				offset = getIndex(program, pc);
				pc += INDEX_LEN;
				length = getIndex(program, pc);
				pc += INDEX_LEN;
				result = flatNIMatcher(gData, offset, length, input, end);
			}
			break;
			case REOP_FLAT1i: {
				matchCh = (char) (program[pc++] & 0xFF);
				if (gData.cp != end) {
					char c = input.charAt(gData.cp);
					if (matchCh == c || upcase(matchCh) == upcase(c)) {
						result = true;
						gData.cp++;
					}
				}
			}
			break;
			case REOP_UCFLAT1: {
				matchCh = (char) getIndex(program, pc);
				pc += INDEX_LEN;
				if (gData.cp != end && input.charAt(gData.cp) == matchCh) {
					result = true;
					gData.cp++;
				}
			}
			break;
			case REOP_UCFLAT1i: {
				matchCh = (char) getIndex(program, pc);
				pc += INDEX_LEN;
				if (gData.cp != end) {
					char c = input.charAt(gData.cp);
					if (matchCh == c || upcase(matchCh) == upcase(c)) {
						result = true;
						gData.cp++;
					}
				}
			}
			break;

			case REOP_CLASS:
			case REOP_NCLASS: {
				index = getIndex(program, pc);
				pc += INDEX_LEN;
				if (gData.cp != end) {
					if (classMatcher(gData, gData.regexp.classList[index], input.charAt(gData.cp), cx)) {
						gData.cp++;
						result = true;
						break;
					}
				}
			}
			break;

			default:
				throw Kit.codeBug();
		}
		if (result) {
			if (!updatecp) {
				gData.cp = startcp;
			}
			return pc;
		}
		gData.cp = startcp;
		return -1;
	}

	private static boolean executeREBytecode(REGlobalData gData, String input, int end, Context cx) {
		int pc = 0;
		byte[] program = gData.regexp.program;
		int continuationOp = REOP_END;
		int continuationPc = 0;
		boolean result = false;

		int op = program[pc++];

		/*
		 * If the first node is a simple match, step the index into the string
		 * until that match is made, or fail if it can't be found at all.
		 */
		if (gData.regexp.anchorCh < 0 && reopIsSimple(op)) {
			boolean anchor = false;
			while (gData.cp <= end) {
				int match = simpleMatch(gData, input, op, program, pc, end, true, cx);
				if (match >= 0) {
					anchor = true;
					pc = match;    /* accept skip to next opcode */
					op = program[pc++];
					break;
				}
				gData.skipped++;
				gData.cp++;
			}
			if (!anchor) {
				return false;
			}
		}

		for (; ; ) {

			if (reopIsSimple(op)) {
				int match = simpleMatch(gData, input, op, program, pc, end, true, cx);
				result = match >= 0;
				if (result) {
					pc = match;    /* accept skip to next opcode */
				}
			} else {
				switchStatement:
				switch (op) {
					case REOP_ALTPREREQ:
					case REOP_ALTPREREQi:
					case REOP_ALTPREREQ2: {
						char matchCh1 = (char) getIndex(program, pc);
						pc += INDEX_LEN;
						char matchCh2 = (char) getIndex(program, pc);
						pc += INDEX_LEN;

						if (gData.cp == end) {
							result = false;
							break;
						}
						char c = input.charAt(gData.cp);
						if (op == REOP_ALTPREREQ2) {
							if (c != matchCh1 && !classMatcher(gData, gData.regexp.classList[matchCh2], c, cx)) {
								result = false;
								break;
							}
						} else {
							if (op == REOP_ALTPREREQi) {
								c = upcase(c);
							}
							if (c != matchCh1 && c != matchCh2) {
								result = false;
								break;
							}
						}
					}
					/* else false thru... */
					// fall through
					case REOP_ALT: {
						int nextpc = pc + getOffset(program, pc);
						pc += INDEX_LEN;
						op = program[pc++];
						int startcp = gData.cp;
						if (reopIsSimple(op)) {
							int match = simpleMatch(gData, input, op, program, pc, end, true, cx);
							if (match < 0) {
								op = program[nextpc++];
								pc = nextpc;
								continue;
							}
							result = true;
							pc = match;
							op = program[pc++];
						}
						byte nextop = program[nextpc++];
						pushBackTrackState(gData, nextop, nextpc, startcp, continuationOp, continuationPc);
					}
					continue;

					case REOP_JUMP: {
						int offset = getOffset(program, pc);
						pc += offset;
						op = program[pc++];
					}
					continue;


					case REOP_LPAREN: {
						int parenIndex = getIndex(program, pc);
						pc += INDEX_LEN;
						gData.setParens(parenIndex, gData.cp, 0);
						op = program[pc++];
					}
					continue;
					case REOP_RPAREN: {
						int parenIndex = getIndex(program, pc);
						pc += INDEX_LEN;
						int cap_index = gData.parensIndex(parenIndex);
						gData.setParens(parenIndex, cap_index, gData.cp - cap_index);
						op = program[pc++];
					}
					continue;

					case REOP_ASSERT: {
						int nextpc = pc + getIndex(program, pc); /* start of term after ASSERT */
						pc += INDEX_LEN;                         /* start of ASSERT child */
						op = program[pc++];
						if (reopIsSimple(op) && simpleMatch(gData, input, op, program, pc, end, false, cx) < 0) {
							result = false;
							break;
						}
						pushProgState(gData, 0, 0, gData.cp, gData.backTrackStackTop, continuationOp, continuationPc);
						pushBackTrackState(gData, REOP_ASSERTTEST, nextpc);
					}
					continue;
					case REOP_ASSERT_NOT: {
						int nextpc = pc + getIndex(program, pc); /* start of term after ASSERT */
						pc += INDEX_LEN;                         /* start of ASSERT child */
						op = program[pc++];
						if (reopIsSimple(op)) {
							int match = simpleMatch(gData, input, op, program, pc, end, false, cx);
							if (match >= 0 && program[match] == REOP_ASSERTNOTTEST) {
								result = false;
								break;
							}
						}
						pushProgState(gData, 0, 0, gData.cp, gData.backTrackStackTop, continuationOp, continuationPc);
						pushBackTrackState(gData, REOP_ASSERTNOTTEST, nextpc);
					}
					continue;

					case REOP_ASSERTTEST:
					case REOP_ASSERTNOTTEST: {
						REProgState state = popProgState(gData);
						gData.cp = state.index;
						gData.backTrackStackTop = state.backTrack;
						continuationPc = state.continuationPc;
						continuationOp = state.continuationOp;
						if (op == REOP_ASSERTNOTTEST) {
							result = !result;
						}
					}
					break;

					case REOP_STAR:
					case REOP_PLUS:
					case REOP_OPT:
					case REOP_QUANT:
					case REOP_MINIMALSTAR:
					case REOP_MINIMALPLUS:
					case REOP_MINIMALOPT:
					case REOP_MINIMALQUANT: {
						int min, max;
						boolean greedy = false;
						switch (op) {
							case REOP_STAR:
								greedy = true;
								// fallthrough
							case REOP_MINIMALSTAR:
								min = 0;
								max = -1;
								break;
							case REOP_PLUS:
								greedy = true;
								// fallthrough
							case REOP_MINIMALPLUS:
								min = 1;
								max = -1;
								break;
							case REOP_OPT:
								greedy = true;
								// fallthrough
							case REOP_MINIMALOPT:
								min = 0;
								max = 1;
								break;
							case REOP_QUANT:
								greedy = true;
								// fallthrough
							case REOP_MINIMALQUANT:
								min = getOffset(program, pc);
								pc += INDEX_LEN;
								// See comments in emitREBytecode for " - 1" reason
								max = getOffset(program, pc) - 1;
								pc += INDEX_LEN;
								break;
							default:
								throw Kit.codeBug();
						}
						pushProgState(gData, min, max, gData.cp, null, continuationOp, continuationPc);
						if (greedy) {
							pushBackTrackState(gData, REOP_REPEAT, pc);
							continuationOp = REOP_REPEAT;
							continuationPc = pc;
							/* Step over <parencount>, <parenindex> & <next> */
							pc += 3 * INDEX_LEN;
							op = program[pc++];
						} else {
							if (min != 0) {
								continuationOp = REOP_MINIMALREPEAT;
								continuationPc = pc;
								/* <parencount> <parenindex> & <next> */
								pc += 3 * INDEX_LEN;
								op = program[pc++];
							} else {
								pushBackTrackState(gData, REOP_MINIMALREPEAT, pc);
								popProgState(gData);
								pc += 2 * INDEX_LEN;  // <parencount> & <parenindex>
								pc = pc + getOffset(program, pc);
								op = program[pc++];
							}
						}
					}
					continue;

					case REOP_ENDCHILD: /* marks the end of a quantifier child */
						// If we have not gotten a result here, it is because of an
						// empty match.  Do the same thing REOP_EMPTY would do.
						result = true;
						// Use the current continuation.
						pc = continuationPc;
						op = continuationOp;
						continue;

					case REOP_REPEAT: {
						int nextpc, nextop;
						do {
							REProgState state = popProgState(gData);
							if (!result) {
								// Failed, see if we have enough children.
								if (state.min == 0) {
									result = true;
								}
								continuationPc = state.continuationPc;
								continuationOp = state.continuationOp;
								pc += 2 * INDEX_LEN;  /* <parencount> & <parenindex> */
								pc += getOffset(program, pc);
								break switchStatement;
							}
							if (state.min == 0 && gData.cp == state.index) {
								// matched an empty string, that'll get us nowhere
								result = false;
								continuationPc = state.continuationPc;
								continuationOp = state.continuationOp;
								pc += 2 * INDEX_LEN;
								pc += getOffset(program, pc);
								break switchStatement;
							}
							int new_min = state.min, new_max = state.max;
							if (new_min != 0) {
								new_min--;
							}
							if (new_max != -1) {
								new_max--;
							}
							if (new_max == 0) {
								result = true;
								continuationPc = state.continuationPc;
								continuationOp = state.continuationOp;
								pc += 2 * INDEX_LEN;
								pc += getOffset(program, pc);
								break switchStatement;
							}
							nextpc = pc + 3 * INDEX_LEN;
							nextop = program[nextpc];
							int startcp = gData.cp;
							if (reopIsSimple(nextop)) {
								nextpc++;
								int match = simpleMatch(gData, input, nextop, program, nextpc, end, true, cx);
								if (match < 0) {
									result = (new_min == 0);
									continuationPc = state.continuationPc;
									continuationOp = state.continuationOp;
									pc += 2 * INDEX_LEN;  /* <parencount> & <parenindex> */
									pc += getOffset(program, pc);
									break switchStatement;
								}
								result = true;
								nextpc = match;
							}
							continuationOp = REOP_REPEAT;
							continuationPc = pc;
							pushProgState(gData, new_min, new_max, startcp, null, state.continuationOp, state.continuationPc);
							if (new_min == 0) {
								pushBackTrackState(gData, REOP_REPEAT, pc, startcp, state.continuationOp, state.continuationPc);
								int parenCount = getIndex(program, pc);
								int parenIndex = getIndex(program, pc + INDEX_LEN);
								for (int k = 0; k < parenCount; k++) {
									gData.setParens(parenIndex + k, -1, 0);
								}
							}
						} while (program[nextpc] == REOP_ENDCHILD);

						pc = nextpc;
						op = program[pc++];
					}
					continue;

					case REOP_MINIMALREPEAT: {
						REProgState state = popProgState(gData);
						if (!result) {
							//
							// Non-greedy failure - try to consume another child.
							//
							if (state.max == -1 || state.max > 0) {
								pushProgState(gData, state.min, state.max, gData.cp, null, state.continuationOp, state.continuationPc);
								continuationOp = REOP_MINIMALREPEAT;
								continuationPc = pc;
								int parenCount = getIndex(program, pc);
								pc += INDEX_LEN;
								int parenIndex = getIndex(program, pc);
								pc += 2 * INDEX_LEN;
								for (int k = 0; k < parenCount; k++) {
									gData.setParens(parenIndex + k, -1, 0);
								}
								op = program[pc++];
								continue;
							}
							// Don't need to adjust pc since we're going to pop.
							continuationPc = state.continuationPc;
							continuationOp = state.continuationOp;
							break;
						}
						if (state.min == 0 && gData.cp == state.index) {
							// Matched an empty string, that'll get us nowhere.
							result = false;
							continuationPc = state.continuationPc;
							continuationOp = state.continuationOp;
							break;
						}
						int new_min = state.min, new_max = state.max;
						if (new_min != 0) {
							new_min--;
						}
						if (new_max != -1) {
							new_max--;
						}
						pushProgState(gData, new_min, new_max, gData.cp, null, state.continuationOp, state.continuationPc);
						if (new_min != 0) {
							continuationOp = REOP_MINIMALREPEAT;
							continuationPc = pc;
							int parenCount = getIndex(program, pc);
							pc += INDEX_LEN;
							int parenIndex = getIndex(program, pc);
							pc += 2 * INDEX_LEN;
							for (int k = 0; k < parenCount; k++) {
								gData.setParens(parenIndex + k, -1, 0);
							}
							op = program[pc++];
						} else {
							continuationPc = state.continuationPc;
							continuationOp = state.continuationOp;
							pushBackTrackState(gData, REOP_MINIMALREPEAT, pc);
							popProgState(gData);
							pc += 2 * INDEX_LEN;
							pc = pc + getOffset(program, pc);
							op = program[pc++];
						}
						continue;
					}

					case REOP_END:
						return true;

					default:
						throw Kit.codeBug("invalid bytecode");

				}
			}
			/*
			 *  If the match failed and there's a backtrack option, take it.
			 *  Otherwise this is a complete and utter failure.
			 */
			if (!result) {
				REBackTrackData backTrackData = gData.backTrackStackTop;
				if (backTrackData != null) {
					gData.backTrackStackTop = backTrackData.previous;
					gData.parens = backTrackData.parens;
					gData.cp = backTrackData.cp;
					gData.stateStackTop = backTrackData.stateStackTop;
					continuationOp = backTrackData.continuationOp;
					continuationPc = backTrackData.continuationPc;
					pc = backTrackData.pc;
					op = backTrackData.op;
					continue;
				}
				return false;
			}

			op = program[pc++];
		}

	}

	private static boolean matchRegExp(REGlobalData gData, RECompiled re, String input, int start, int end, boolean multiline, Context cx) {
		if (re.parenCount != 0) {
			gData.parens = new long[re.parenCount];
		} else {
			gData.parens = null;
		}

		gData.backTrackStackTop = null;
		gData.stateStackTop = null;

		gData.multiline = multiline || (re.flags & JSREG_MULTILINE) != 0;
		gData.regexp = re;

		int anchorCh = gData.regexp.anchorCh;
		//
		// have to include the position beyond the last character
		//  in order to detect end-of-input/line condition
		//
		for (int i = start; i <= end; ++i) {
			//
			// If the first node is a literal match, step the index into
			// the string until that match is made, or fail if it can't be
			// found at all.
			//
			if (anchorCh >= 0) {
				for (; ; ) {
					if (i == end) {
						return false;
					}
					char matchCh = input.charAt(i);
					if (matchCh == anchorCh || ((gData.regexp.flags & JSREG_FOLD) != 0 && upcase(matchCh) == upcase((char) anchorCh))) {
						break;
					}
					++i;
				}
			}
			gData.cp = i;
			gData.skipped = i - start;
			for (int j = 0; j < re.parenCount; j++) {
				gData.parens[j] = -1L;
			}
			boolean result = executeREBytecode(gData, input, end, cx);

			gData.backTrackStackTop = null;
			gData.stateStackTop = null;
			if (result) {
				return true;
			}
			if (anchorCh == ANCHOR_BOL && !gData.multiline) {
				gData.skipped = end;
				return false;
			}
			i = start + gData.skipped;
		}
		return false;
	}

	private static void reportError(String messageId, String arg, Context cx) {
		String msg = ScriptRuntime.getMessage1(messageId, arg);
		throw ScriptRuntime.constructError(cx, "SyntaxError", msg);
	}

	private static NativeRegExp realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
		if (!(thisObj instanceof NativeRegExp)) {
			throw incompatibleCallError(f, cx);
		}
		return (NativeRegExp) thisObj;
	}

	Object lastIndex = ScriptRuntime.zeroObj;     /* index after last match, for //g iterator */
	private RECompiled re;
	private int lastIndexAttr = DONTENUM | PERMANENT;

	NativeRegExp(Scriptable scope, RECompiled regexpCompiled, Context cx) {
		this.re = regexpCompiled;
		setLastIndex(ScriptRuntime.zeroObj, cx);
		ScriptRuntime.setBuiltinProtoAndParent(cx, scope, this, TopLevel.Builtins.RegExp);
	}

	NativeRegExp() {
	}

	@Override
	public String getClassName() {
		return "RegExp";
	}

	/**
	 * Gets the value to be returned by the typeof operator called on this object.
	 *
	 * @return "object"
	 * @see ScriptableObject#getTypeOf()
	 */
	@Override
	public MemberType getTypeOf() {
		return MemberType.OBJECT;
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		throw ScriptRuntime.notFunctionError(cx, thisObj);
	}

	@Override
	public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
		throw ScriptRuntime.notFunctionError(cx, this);
	}

	Scriptable compile(Context cx, Scriptable scope, Object[] args) {
		if (args.length > 0 && args[0] instanceof NativeRegExp thatObj) {
			if (args.length > 1 && args[1] != Undefined.instance) {
				// report error
				throw ScriptRuntime.typeError0(cx, "msg.bad.regexp.compile");
			}
			this.re = thatObj.re;
			setLastIndex(thatObj.lastIndex, cx);
			return this;
		}
		String s = args.length == 0 || args[0] instanceof Undefined ? "" : escapeRegExp(cx, args[0]);
		String global = args.length > 1 && args[1] != Undefined.instance ? ScriptRuntime.toString(cx, args[1]) : null;
		this.re = compileRE(cx, s, global, false);
		setLastIndex(ScriptRuntime.zeroObj, cx);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append('/');
		if (re.source.length != 0) {
			buf.append(re.source);
		} else {
			// See bugzilla 226045
			buf.append("(?:)");
		}
		buf.append('/');
		if ((re.flags & JSREG_GLOB) != 0) {
			buf.append('g');
		}
		if ((re.flags & JSREG_FOLD) != 0) {
			buf.append('i');
		}
		if ((re.flags & JSREG_MULTILINE) != 0) {
			buf.append('m');
		}
		return buf.toString();
	}

	private Object execSub(Context cx, Scriptable scopeObj, Object[] args, int matchType) {
		RegExp reImpl = cx.getRegExp();
		String str;
		if (args.length == 0) {
			str = reImpl.input;
			if (str == null) {
				str = ScriptRuntime.toString(cx, Undefined.instance);
			}
		} else {
			str = ScriptRuntime.toString(cx, args[0]);
		}
		double d = 0;
		if ((re.flags & JSREG_GLOB) != 0) {
			d = ScriptRuntime.toInteger(cx, lastIndex);
		}

		Object rval;
		if (d < 0 || str.length() < d) {
			setLastIndex(ScriptRuntime.zeroObj, cx);
			rval = null;
		} else {
			int[] indexp = {(int) d};
			rval = executeRegExp(cx, scopeObj, reImpl, str, indexp, matchType);
			if ((re.flags & JSREG_GLOB) != 0) {
				if (rval == null || rval == Undefined.instance) {
					setLastIndex(ScriptRuntime.zeroObj, cx);
				} else {
					setLastIndex((double) indexp[0], cx);
				}
			}
		}
		return rval;
	}

	/*
	 * indexp is assumed to be an array of length 1
	 */
	Object executeRegExp(Context cx, Scriptable scope, RegExp res, String str, int[] indexp, int matchType) {
		REGlobalData gData = new REGlobalData();

		int start = indexp[0];
		int end = str.length();
		if (start > end) {
			start = end;
		}
		//
		// Call the recursive matcher to do the real work.
		//
		boolean matches = matchRegExp(gData, re, str, start, end, res.multiline, cx);
		if (!matches) {
			if (matchType != PREFIX) {
				return null;
			}
			return Undefined.instance;
		}
		int index = gData.cp;
		int ep = indexp[0] = index;
		int matchlen = ep - (start + gData.skipped);
		index -= matchlen;
		Object result;
		Scriptable obj;

		if (matchType == TEST) {
			/*
			 * Testing for a match and updating cx.regExpImpl: don't allocate
			 * an array object, do return true.
			 */
			result = Boolean.TRUE;
			obj = null;
		} else {
			/*
			 * The array returned on match has element 0 bound to the matched
			 * string, elements 1 through re.parenCount bound to the paren
			 * matches, an index property telling the length of the left context,
			 * and an input property referring to the input string.
			 */
			result = cx.newArray(scope, 0);
			obj = (Scriptable) result;

			String matchstr = str.substring(index, index + matchlen);
			obj.put(cx, 0, obj, matchstr);
		}

		if (re.parenCount == 0) {
			res.parens = null;
			res.lastParen = new SubString();
		} else {
			SubString parsub = null;
			int num;
			res.parens = new SubString[re.parenCount];
			for (num = 0; num < re.parenCount; num++) {
				int cap_index = gData.parensIndex(num);
				if (cap_index != -1) {
					int cap_length = gData.parensLength(num);
					parsub = new SubString(str, cap_index, cap_length);
					res.parens[num] = parsub;
					if (matchType != TEST) {
						obj.put(cx, num + 1, obj, parsub.toString());
					}
				} else {
					if (matchType != TEST) {
						obj.put(cx, num + 1, obj, Undefined.instance);
					}
				}
			}
			res.lastParen = parsub;
		}

		if (!(matchType == TEST)) {
			/*
			 * Define the index and input properties last for better for/in loop
			 * order (so they come after the elements).
			 */
			obj.put(cx, "index", obj, start + gData.skipped);
			obj.put(cx, "input", obj, str);
		}

		if (res.lastMatch == null) {
			res.lastMatch = new SubString();
			res.leftContext = new SubString();
			res.rightContext = new SubString();
		}
		res.lastMatch.str = str;
		res.lastMatch.index = index;
		res.lastMatch.length = matchlen;

		res.leftContext.str = str;
		res.leftContext.index = 0;
		res.leftContext.length = start + gData.skipped;

		res.rightContext.str = str;
		res.rightContext.index = ep;
		res.rightContext.length = end - ep;

		return result;
	}

	int getFlags() {
		return re.flags;
	}

	@Override
	protected int getMaxInstanceId() {
		return MAX_INSTANCE_ID;
	}

	@Override
	protected int findInstanceIdInfo(String s, Context cx) {
		int id = switch (s) {
			case "lastIndex" -> Id_lastIndex;
			case "source" -> Id_source;
			case "global" -> Id_global;
			case "ignoreCase" -> Id_ignoreCase;
			case "multiline" -> Id_multiline;
			default -> 0;
		};

		if (id == 0) {
			return super.findInstanceIdInfo(s, cx);
		}

		int attr = switch (id) {
			case Id_lastIndex -> lastIndexAttr;
			case Id_source, Id_global, Id_ignoreCase, Id_multiline -> PERMANENT | READONLY | DONTENUM;
			default -> throw new IllegalStateException();
		};
		return instanceIdInfo(attr, id);
	}

	@Override
	protected String getInstanceIdName(int id) {
		return switch (id) {
			case Id_lastIndex -> "lastIndex";
			case Id_source -> "source";
			case Id_global -> "global";
			case Id_ignoreCase -> "ignoreCase";
			case Id_multiline -> "multiline";
			default -> super.getInstanceIdName(id);
		};
	}

	@Override
	protected Object getInstanceIdValue(int id, Context cx) {
		return switch (id) {
			case Id_lastIndex -> lastIndex;
			case Id_source -> new String(re.source);
			case Id_global -> (re.flags & JSREG_GLOB) != 0;
			case Id_ignoreCase -> (re.flags & JSREG_FOLD) != 0;
			case Id_multiline -> (re.flags & JSREG_MULTILINE) != 0;
			default -> super.getInstanceIdValue(id, cx);
		};
	}

	private void setLastIndex(Object value, Context cx) {
		if ((lastIndexAttr & READONLY) != 0) {
			throw ScriptRuntime.typeError1(cx, "msg.modify.readonly", "lastIndex");
		}
		lastIndex = value;
	}

	@Override
	protected void setInstanceIdValue(int id, Object value, Context cx) {
		switch (id) {
			case Id_lastIndex:
				setLastIndex(value, cx);
				return;
			case Id_source:
			case Id_global:
			case Id_ignoreCase:
			case Id_multiline:
				return;
		}
		super.setInstanceIdValue(id, value, cx);
	}

	@Override
	protected void setInstanceIdAttributes(int id, int attr, Context cx) {
		if (id == Id_lastIndex) {
			lastIndexAttr = attr;
			return;
		}
		super.setInstanceIdAttributes(id, attr, cx);
	}

	@Override
	protected void initPrototypeId(int id, Context cx) {
		if (id == SymbolId_match) {
			initPrototypeMethod(REGEXP_TAG, id, SymbolKey.MATCH, "[Symbol.match]", 1, cx);
			return;
		}
		if (id == SymbolId_search) {
			initPrototypeMethod(REGEXP_TAG, id, SymbolKey.SEARCH, "[Symbol.search]", 1, cx);
			return;
		}

		String s;
		int arity;
		switch (id) {
			case Id_compile -> {
				arity = 2;
				s = "compile";
			}
			case Id_toString -> {
				arity = 0;
				s = "toString";
			}
			case Id_toSource -> {
				arity = 0;
				s = "toSource";
			}
			case Id_exec -> {
				arity = 1;
				s = "exec";
			}
			case Id_test -> {
				arity = 1;
				s = "test";
			}
			case Id_prefix -> {
				arity = 1;
				s = "prefix";
			}
			default -> throw new IllegalArgumentException(String.valueOf(id));
		}
		initPrototypeMethod(REGEXP_TAG, id, s, arity, cx);
	}

	// #/string_id_map#

	@Override
	public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (!f.hasTag(REGEXP_TAG)) {
			return super.execIdCall(f, cx, scope, thisObj, args);
		}
		int id = f.methodId();
		switch (id) {
			case Id_compile:
				return realThis(thisObj, f, cx).compile(cx, scope, args);

			case Id_toString:
			case Id_toSource:
				return realThis(thisObj, f, cx).toString();

			case Id_exec:
				return realThis(thisObj, f, cx).execSub(cx, scope, args, MATCH);

			case Id_test: {
				Object x = realThis(thisObj, f, cx).execSub(cx, scope, args, TEST);
				return Boolean.TRUE.equals(x) ? Boolean.TRUE : Boolean.FALSE;
			}

			case Id_prefix:
				return realThis(thisObj, f, cx).execSub(cx, scope, args, PREFIX);

			case SymbolId_match:
				return realThis(thisObj, f, cx).execSub(cx, scope, args, MATCH);

			case SymbolId_search:
				Scriptable scriptable = (Scriptable) realThis(thisObj, f, cx).execSub(cx, scope, args, MATCH);
				return scriptable.get(cx, "index", scriptable);
		}
		throw new IllegalArgumentException(String.valueOf(id));
	}

	@Override
	protected int findPrototypeId(Symbol k) {
		if (SymbolKey.MATCH.equals(k)) {
			return SymbolId_match;
		}
		if (SymbolKey.SEARCH.equals(k)) {
			return SymbolId_search;
		}
		return 0;
	}

	// #string_id_map#
	@Override
	protected int findPrototypeId(String s) {
		return switch (s) {
			case "compile" -> Id_compile;
			case "toString" -> Id_toString;
			case "toSource" -> Id_toSource;
			case "exec" -> Id_exec;
			case "test" -> Id_test;
			case "prefix" -> Id_prefix;
			default -> 0;
		};
	}

}       // class NativeRegExp

class RECompiled {
	final char[] source;    /* locked source string, sans // */
	int parenCount;         /* number of parenthesized submatches */
	int flags;              /* flags  */
	byte[] program;         /* regular expression bytecode */
	int classCount;         /* count [...] bitmaps */
	RECharSet[] classList;  /* list of [...] bitmaps */
	int anchorCh = -1;      /* if >= 0, then re starts with this literal char */

	RECompiled(String str) {
		this.source = str.toCharArray();
	}
}

class RENode {

	byte op;         /* r.e. op bytecode */
	RENode next;       /* next in concatenation order */
	RENode kid;        /* first operand */
	RENode kid2;       /* second operand */
	int parenIndex; /* or a parenthesis index */
	/* or a range */ int min;
	int max;
	int parenCount;
	boolean greedy;
	/* or a character class */ int startIndex;
	int kidlen;     /* length of string at kid, in chars */
	int bmsize;     /* bitmap size, based on max char code */
	int index;      /* index into class list */
	boolean sense;
	/* or a literal sequence */ char chr;        /* of one character */
	int length;     /* or many (via the index) */
	int flatIndex;  /* which is -1 if not sourced */

	RENode(byte op) {
		this.op = op;
	}

}

class CompilerState {

	Context cx;
	char[] cpbegin;
	int cpend;
	int cp;
	int flags;
	int backReferenceLimit;
	int maxBackReference;
	int parenCount;
	int parenNesting;
	int classCount;   /* number of [] encountered */
	int progLength;   /* estimated bytecode length */
	RENode result;

	CompilerState(Context cx, char[] source, int length, int flags) {
		this.cx = cx;
		this.cpbegin = source;
		this.cp = 0;
		this.cpend = length;
		this.flags = flags;
		this.backReferenceLimit = Integer.MAX_VALUE;
		this.maxBackReference = 0;
		this.parenCount = 0;
		this.classCount = 0;
		this.progLength = 0;
	}
}

class REProgState {
	final REProgState previous; // previous state in stack
	final int min;                      /* current quantifier min */
	final int max;                      /* current quantifier max */
	final int index;                    /* progress in text */
	final int continuationOp;
	final int continuationPc;
	final REBackTrackData backTrack; // used by ASSERT_  to recover state

	REProgState(REProgState previous, int min, int max, int index, REBackTrackData backTrack, int continuationOp, int continuationPc) {
		this.previous = previous;
		this.min = min;
		this.max = max;
		this.index = index;
		this.continuationOp = continuationOp;
		this.continuationPc = continuationPc;
		this.backTrack = backTrack;
	}
}

class REBackTrackData {

	final REBackTrackData previous;
	final int op;                             /* operator */
	final int pc;                             /* bytecode pointer */
	final int cp;                             /* char buffer index */
	final int continuationOp;                 /* continuation op */
	final int continuationPc;                 /* continuation pc */
	final long[] parens;                      /* parenthesis captures */
	final REProgState stateStackTop;          /* state of op that backtracked */

	REBackTrackData(REGlobalData gData, int op, int pc, int cp, int continuationOp, int continuationPc) {
		previous = gData.backTrackStackTop;
		this.op = op;
		this.pc = pc;
		this.cp = cp;
		this.continuationOp = continuationOp;
		this.continuationPc = continuationPc;
		parens = gData.parens;
		stateStackTop = gData.stateStackTop;
	}
}

class REGlobalData {
	boolean multiline;
	RECompiled regexp;              /* the RE in execution */
	int skipped;                    /* chars skipped anchoring this r.e. */

	int cp;                         /* char buffer index */
	long[] parens;                  /* parens captures */

	REProgState stateStackTop;       /* stack of state of current ancestors */

	REBackTrackData backTrackStackTop;  /* last matched-so-far position */


	/**
	 * Get start of parenthesis capture contents, -1 for empty.
	 */
	int parensIndex(int i) {
		return (int) (parens[i]);
	}

	/**
	 * Get length of parenthesis capture contents.
	 */
	int parensLength(int i) {
		return (int) (parens[i] >>> 32);
	}

	void setParens(int i, int index, int length) {
		// clone parens array if it is shared with backtrack state
		if (backTrackStackTop != null && backTrackStackTop.parens == parens) {
			parens = parens.clone();
		}
		parens[i] = (index & 0xffffffffL) | ((long) length << 32);
	}

}

/*
 * This struct holds a bitmap representation of a class from a regexp.
 * There's a list of these referenced by the classList field in the NativeRegExp
 * struct below. The initial state has startIndex set to the offset in the
 * original regexp source of the beginning of the class contents. The first
 * use of the class converts the source representation into a bitmap.
 *
 */
final class RECharSet {
	final int length;
	final int startIndex;
	final int strlength;
	final boolean sense;
	volatile transient boolean converted;
	volatile transient byte[] bits;

	RECharSet(int length, int startIndex, int strlength, boolean sense) {
		this.length = length;
		this.startIndex = startIndex;
		this.strlength = strlength;
		this.sense = sense;
	}
}


