/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package pro.komaru.tridot.rhino;

import java.text.*;
import java.util.*;

/**
 * This class implements the Date native object.
 * See ECMA 15.9.
 *
 * @author Mike McCabe
 * <p>
 * Significant parts of this code are adapted from the venerable jsdate.cpp (also Mozilla):
 * https://dxr.mozilla.dev/latvian/repackaged/org/mozilla-central/source/js/src/jsdate.cpp
 */
final class NativeDate extends IdScriptableObject {
	private static final Object DATE_TAG = "Date";

	private static final String js_NaN_date_str = "Invalid Date";
	private static final double HalfTimeDomain = 8.64e15;
	private static final double HoursPerDay = 24.0;
	private static final double MinutesPerHour = 60.0;
	private static final double SecondsPerMinute = 60.0;
	private static final double msPerSecond = 1000.0;
	private static final double MinutesPerDay = (HoursPerDay * MinutesPerHour);
	private static final double SecondsPerDay = (MinutesPerDay * SecondsPerMinute);
	private static final double msPerDay = (SecondsPerDay * msPerSecond);

	/* ECMA helper functions */
	private static final double SecondsPerHour = (MinutesPerHour * SecondsPerMinute);
	private static final double msPerHour = (SecondsPerHour * msPerSecond);
	private static final double msPerMinute = (SecondsPerMinute * msPerSecond);
	/* compute the time in msec (unclipped) from the given args */
	private static final int MAXARGS = 7;
	private static final int ConstructorId_now = -3;
	private static final int ConstructorId_parse = -2;
	private static final int ConstructorId_UTC = -1;
	private static final int Id_constructor = 1;
	private static final int Id_toString = 2;
	private static final int Id_toTimeString = 3;
	private static final int Id_toDateString = 4;
	private static final int Id_toLocaleString = 5;
	private static final int Id_toLocaleTimeString = 6;
	private static final int Id_toLocaleDateString = 7;
	private static final int Id_toUTCString = 8;
	private static final int Id_toSource = 9;
	private static final int Id_valueOf = 10;
	private static final int Id_getTime = 11;
	private static final int Id_getYear = 12;
	private static final int Id_getFullYear = 13;
	private static final int Id_getUTCFullYear = 14;
	private static final int Id_getMonth = 15;
	private static final int Id_getUTCMonth = 16;
	private static final int Id_getDate = 17;
	private static final int Id_getUTCDate = 18;
	private static final int Id_getDay = 19;
	private static final int Id_getUTCDay = 20;
	private static final int Id_getHours = 21;
	private static final int Id_getUTCHours = 22;
	private static final int Id_getMinutes = 23;
	private static final int Id_getUTCMinutes = 24;
	private static final int Id_getSeconds = 25;
	private static final int Id_getUTCSeconds = 26;
	private static final int Id_getMilliseconds = 27;
	private static final int Id_getUTCMilliseconds = 28;
	private static final int Id_getTimezoneOffset = 29;

	/* end of ECMA helper functions */
	private static final int Id_setTime = 30;
	private static final int Id_setMilliseconds = 31;
	private static final int Id_setUTCMilliseconds = 32;
	private static final int Id_setSeconds = 33;
	private static final int Id_setUTCSeconds = 34;
	private static final int Id_setMinutes = 35;
	private static final int Id_setUTCMinutes = 36;
	private static final int Id_setHours = 37;
	private static final int Id_setUTCHours = 38;
	private static final int Id_setDate = 39;
	private static final int Id_setUTCDate = 40;
	private static final int Id_setMonth = 41;
	private static final int Id_setUTCMonth = 42;
	private static final int Id_setFullYear = 43;
	private static final int Id_setUTCFullYear = 44;
	private static final int Id_setYear = 45;
	private static final int Id_toISOString = 46;
	private static final int Id_toJSON = 47;
	private static final int MAX_PROTOTYPE_ID = Id_toJSON;
	private static final int Id_toGMTString = Id_toUTCString; // Alias, see Ecma B.2.6
	/* cached values */
	private static final TimeZone thisTimeZone = TimeZone.getDefault();
	private static final double LocalTZA = thisTimeZone.getRawOffset();
	//not thread safe
	private static final DateFormat timeZoneFormatter = new SimpleDateFormat("zzz");
	private static final DateFormat localeDateTimeFormatter = new SimpleDateFormat("MMMM d, yyyy h:mm:ss a z");
	private static final DateFormat localeDateFormatter = new SimpleDateFormat("MMMM d, yyyy");
	private static final DateFormat localeTimeFormatter = new SimpleDateFormat("h:mm:ss a z");

	static void init(Scriptable scope, boolean sealed, Context cx) {
		NativeDate obj = new NativeDate();
		// Set the value of the prototype Date to NaN ('invalid date');
		obj.date = ScriptRuntime.NaN;
		obj.exportAsJSClass(MAX_PROTOTYPE_ID, scope, sealed, cx);
	}

	private static double Day(double t) {
		return Math.floor(t / msPerDay);
	}

	private static double TimeWithinDay(double t) {
		double result;
		result = t % msPerDay;
		if (result < 0) {
			result += msPerDay;
		}
		return result;
	}

	private static boolean IsLeapYear(int year) {
		return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
	}

	/* math here has to be f.p, because we need
	 *  floor((1968 - 1969) / 4) == -1
	 */
	private static double DayFromYear(double y) {
		return ((365 * ((y) - 1970) + Math.floor(((y) - 1969) / 4.0) - Math.floor(((y) - 1901) / 100.0) + Math.floor(((y) - 1601) / 400.0)));
	}

	private static double TimeFromYear(double y) {
		return DayFromYear(y) * msPerDay;
	}

	private static int YearFromTime(double t) {
		if (Double.isInfinite(t) || Double.isNaN(t)) {
			return 0;
		}

		double y = Math.floor(t / (msPerDay * 365.2425)) + 1970;
		double t2 = TimeFromYear(y);

		/*
		 * Adjust the year if the approximation was wrong.  Since the year was
		 * computed using the average number of ms per year, it will usually
		 * be wrong for dates within several hours of a year transition.
		 */
		if (t2 > t) {
			y--;
		} else {
			if (t2 + msPerDay * DaysInYear(y) <= t) {
				y++;
			}
		}
		return (int) y;
	}

	private static double DayFromMonth(int m, int year) {
		int day = m * 30;

		if (m >= 7) {
			day += m / 2 - 1;
		} else if (m >= 2) {
			day += (m - 1) / 2 - 1;
		} else {
			day += m;
		}

		if (m >= 2 && IsLeapYear(year)) {
			++day;
		}

		return day;
	}

	private static double DaysInYear(double year) {
		if (Double.isInfinite(year) || Double.isNaN(year)) {
			return ScriptRuntime.NaN;
		}
		return IsLeapYear((int) year) ? 366.0 : 365.0;
	}

	private static int DaysInMonth(int year, int month) {
		// month is 1-based for DaysInMonth!
		if (month == 2) {
			return IsLeapYear(year) ? 29 : 28;
		}
		return month >= 8 ? 31 - (month & 1) : 30 + (month & 1);
	}

	private static int MonthFromTime(double t) {
		int year = YearFromTime(t);
		int d = (int) (Day(t) - DayFromYear(year));

		d -= 31 + 28;
		if (d < 0) {
			return (d < -28) ? 0 : 1;
		}

		if (IsLeapYear(year)) {
			if (d == 0) {
				return 1; // 29 February
			}
			--d;
		}

		// d: date count from 1 March
		int estimate = d / 30; // approx number of month since March
		int mstart;
		switch (estimate) {
			case 0:
				return 2;
			case 1:
				mstart = 31;
				break;
			case 2:
				mstart = 31 + 30;
				break;
			case 3:
				mstart = 31 + 30 + 31;
				break;
			case 4:
				mstart = 31 + 30 + 31 + 30;
				break;
			case 5:
				mstart = 31 + 30 + 31 + 30 + 31;
				break;
			case 6:
				mstart = 31 + 30 + 31 + 30 + 31 + 31;
				break;
			case 7:
				mstart = 31 + 30 + 31 + 30 + 31 + 31 + 30;
				break;
			case 8:
				mstart = 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
				break;
			case 9:
				mstart = 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30;
				break;
			case 10:
				return 11; //Late december
			default:
				throw Kit.codeBug();
		}
		// if d < mstart then real month since March == estimate - 1
		return (d >= mstart) ? estimate + 2 : estimate + 1;
	}

	private static int DateFromTime(double t) {
		int year = YearFromTime(t);
		int d = (int) (Day(t) - DayFromYear(year));

		d -= 31 + 28;
		if (d < 0) {
			return (d < -28) ? d + 31 + 28 + 1 : d + 28 + 1;
		}

		if (IsLeapYear(year)) {
			if (d == 0) {
				return 29; // 29 February
			}
			--d;
		}

		// d: date count from 1 March
		int mdays, mstart;
		switch (d / 30) { // approx number of month since March
			case 0:
				return d + 1;
			case 1:
				mdays = 31;
				mstart = 31;
				break;
			case 2:
				mdays = 30;
				mstart = 31 + 30;
				break;
			case 3:
				mdays = 31;
				mstart = 31 + 30 + 31;
				break;
			case 4:
				mdays = 30;
				mstart = 31 + 30 + 31 + 30;
				break;
			case 5:
				mdays = 31;
				mstart = 31 + 30 + 31 + 30 + 31;
				break;
			case 6:
				mdays = 31;
				mstart = 31 + 30 + 31 + 30 + 31 + 31;
				break;
			case 7:
				mdays = 30;
				mstart = 31 + 30 + 31 + 30 + 31 + 31 + 30;
				break;
			case 8:
				mdays = 31;
				mstart = 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
				break;
			case 9:
				mdays = 30;
				mstart = 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30;
				break;
			case 10:
				return d - (31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30) + 1; //Late december
			default:
				throw Kit.codeBug();
		}
		d -= mstart;
		if (d < 0) {
			// wrong estimate: sfhift to previous month
			d += mdays;
		}
		return d + 1;
	}

	private static int WeekDay(double t) {
		double result;
		result = Day(t) + 4;
		result = result % 7;
		if (result < 0) {
			result += 7;
		}
		return (int) result;
	}

	private static double now() {
		return System.currentTimeMillis();
	}

	private static double DaylightSavingTA(double t) {
		// Another workaround!  The JRE doesn't seem to know about DST
		// before year 1 AD, so we map to equivalent dates for the
		// purposes of finding DST. To be safe, we do this for years
		// before 1970.
		if (t < 0.0) {
			int year = EquivalentYear(YearFromTime(t));
			double day = MakeDay(year, MonthFromTime(t), DateFromTime(t));
			t = MakeDate(day, TimeWithinDay(t));
		}
		Date date = new Date((long) t);
		if (thisTimeZone.inDaylightTime(date)) {
			return msPerHour;
		}
		return 0;
	}

	/*
	 * Find a year for which any given date will fall on the same weekday.
	 *
	 * This function should be used with caution when used other than
	 * for determining DST; it hasn't been proven not to produce an
	 * incorrect year for times near year boundaries.
	 */
	private static int EquivalentYear(int year) {
		int day = (int) DayFromYear(year) + 4;
		day = day % 7;
		if (day < 0) {
			day += 7;
		}
		// Years and leap years on which Jan 1 is a Sunday, Monday, etc.
		if (IsLeapYear(year)) {
			switch (day) {
				case 0:
					return 1984;
				case 1:
					return 1996;
				case 2:
					return 1980;
				case 3:
					return 1992;
				case 4:
					return 1976;
				case 5:
					return 1988;
				case 6:
					return 1972;
			}
		} else {
			switch (day) {
				case 0:
					return 1978;
				case 1:
					return 1973;
				case 2:
					return 1985;
				case 3:
					return 1986;
				case 4:
					return 1981;
				case 5:
					return 1971;
				case 6:
					return 1977;
			}
		}
		// Unreachable
		throw Kit.codeBug();
	}

	private static double LocalTime(double t) {
		return t + LocalTZA + DaylightSavingTA(t);
	}

	private static double internalUTC(double t) {
		return t - LocalTZA - DaylightSavingTA(t - LocalTZA);
	}

	private static int HourFromTime(double t) {
		double result;
		result = Math.floor(t / msPerHour) % HoursPerDay;
		if (result < 0) {
			result += HoursPerDay;
		}
		return (int) result;
	}

	private static int MinFromTime(double t) {
		double result;
		result = Math.floor(t / msPerMinute) % MinutesPerHour;
		if (result < 0) {
			result += MinutesPerHour;
		}
		return (int) result;
	}

	private static int SecFromTime(double t) {
		double result;
		result = Math.floor(t / msPerSecond) % SecondsPerMinute;
		if (result < 0) {
			result += SecondsPerMinute;
		}
		return (int) result;
	}

	private static int msFromTime(double t) {
		double result;
		result = t % msPerSecond;
		if (result < 0) {
			result += msPerSecond;
		}
		return (int) result;
	}

	private static double MakeTime(double hour, double min, double sec, double ms) {
		return ((hour * MinutesPerHour + min) * SecondsPerMinute + sec) * msPerSecond + ms;
	}

	private static double MakeDay(double year, double month, double date) {
		year += Math.floor(month / 12);

		month = month % 12;
		if (month < 0) {
			month += 12;
		}

		double yearday = Math.floor(TimeFromYear(year) / msPerDay);
		double monthday = DayFromMonth((int) month, (int) year);

		return yearday + monthday + date - 1;
	}

	private static double MakeDate(double day, double time) {
		return day * msPerDay + time;
	}

	private static double TimeClip(double d) {
		if (Double.isNaN(d) || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY || Math.abs(d) > HalfTimeDomain) {
			return ScriptRuntime.NaN;
		}
		if (d > 0.0) {
			return Math.floor(d + 0.);
		}
		return Math.ceil(d + 0.);
	}

	/* find UTC time from given date... no 1900 correction! */
	private static double date_msecFromDate(double year, double mon, double mday, double hour, double min, double sec, double msec) {
		double day;
		double time;
		double result;

		day = MakeDay(year, mon, mday);
		time = MakeTime(hour, min, sec, msec);
		result = MakeDate(day, time);
		return result;
	}

	private static double date_msecFromArgs(Object[] args, Context cx) {
		double[] array = new double[MAXARGS];
		int loop;
		double d;

		for (loop = 0; loop < MAXARGS; loop++) {
			if (loop < args.length) {
				d = ScriptRuntime.toNumber(cx, args[loop]);
				if (Double.isNaN(d) || Double.isInfinite(d)) {
					return ScriptRuntime.NaN;
				}
				array[loop] = ScriptRuntime.toInteger(cx, args[loop]);
			} else {
				if (loop == 2) {
					array[loop] = 1; /* Default the date argument to 1. */
				} else {
					array[loop] = 0;
				}
			}
		}

		/* adjust 2-digit years into the 20th century */
		if (array[0] >= 0 && array[0] <= 99) {
			array[0] += 1900;
		}

		return date_msecFromDate(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
	}

	private static double jsStaticFunction_UTC(Context cx, Object[] args) {
		if (args.length == 0) {
			return ScriptRuntime.NaN;
		}
		return TimeClip(date_msecFromArgs(args, cx));
	}

	/**
	 * 15.9.1.15 Date Time String Format<br>
	 * Parse input string according to simplified ISO-8601 Extended Format:
	 * <ul>
	 * <li><code>YYYY-MM-DD'T'HH:mm:ss.sss'Z'</code></li>
	 * <li>or <code>YYYY-MM-DD'T'HH:mm:ss.sss[+-]hh:mm</code></li>
	 * </ul>
	 */
	private static double parseISOString(String s) {
		// we use a simple state machine to parse the input string
		final int ERROR = -1;
		final int YEAR = 0, MONTH = 1, DAY = 2;
		final int HOUR = 3, MIN = 4, SEC = 5, MSEC = 6;
		final int TZHOUR = 7, TZMIN = 8;
		int state = YEAR;
		// default values per [15.9.1.15 Date Time String Format]
		int[] values = {1970, 1, 1, 0, 0, 0, 0, -1, -1};
		int yearlen = 4, yearmod = 1, tzmod = 1;
		int i = 0, len = s.length();
		if (len != 0) {
			char c = s.charAt(0);
			if (c == '+' || c == '-') {
				// 15.9.1.15.1 Extended years
				i += 1;
				yearlen = 6;
				yearmod = (c == '-') ? -1 : 1;
			} else if (c == 'T') {
				// time-only forms no longer in spec, but follow spidermonkey here
				i += 1;
				state = HOUR;
			}
		}
		loop:
		while (state != ERROR) {
			int m = i + (state == YEAR ? yearlen : state == MSEC ? 3 : 2);
			if (m > len) {
				state = ERROR;
				break;
			}

			int value = 0;
			for (; i < m; ++i) {
				char c = s.charAt(i);
				if (c < '0' || c > '9') {
					state = ERROR;
					break loop;
				}
				value = 10 * value + (c - '0');
			}
			values[state] = value;

			if (i == len) {
				// reached EOF, check for end state
				state = switch (state) {
					case HOUR, TZHOUR -> ERROR;
					default -> state;
				};
				break;
			}

			char c = s.charAt(i++);
			if (c == 'Z') {
				// handle abbrevation for UTC timezone
				values[TZHOUR] = 0;
				values[TZMIN] = 0;
				switch (state) {
					case MIN:
					case SEC:
					case MSEC:
						break;
					default:
						state = ERROR;
				}
				break;
			}

			// state transition
			switch (state) {
				case YEAR, MONTH -> state = (c == '-' ? state + 1 : c == 'T' ? HOUR : ERROR);
				case DAY -> state = (c == 'T' ? HOUR : ERROR);
				case HOUR -> state = (c == ':' ? MIN : ERROR);
				case TZHOUR -> {
					// state = (c == ':' ? state + 1 : ERROR);
					// Non-standard extension, https://bugzilla.mozilla.org/show_bug.cgi?id=682754
					if (c != ':') {
						// back off by one and try to read without ':' separator
						i -= 1;
					}
					state = TZMIN;
				}
				case MIN -> state = (c == ':' ? SEC : c == '+' || c == '-' ? TZHOUR : ERROR);
				case SEC -> state = (c == '.' ? MSEC : c == '+' || c == '-' ? TZHOUR : ERROR);
				case MSEC -> state = (c == '+' || c == '-' ? TZHOUR : ERROR);
				case TZMIN -> state = ERROR;
			}
			if (state == TZHOUR) {
				// save timezone modificator
				tzmod = (c == '-') ? -1 : 1;
			}
		}

		syntax:
		{
			// error or unparsed characters
			if (state == ERROR || i != len) {
				break syntax;
			}

			// check values
			int year = values[YEAR], month = values[MONTH], day = values[DAY];
			int hour = values[HOUR], min = values[MIN], sec = values[SEC], msec = values[MSEC];
			int tzhour = values[TZHOUR], tzmin = values[TZMIN];
			if (year > 275943 // ceil(1e8/365) + 1970 = 275943
					|| (month < 1 || month > 12) || (day < 1 || day > DaysInMonth(year, month)) || hour > 24 || (hour == 24 && (min > 0 || sec > 0 || msec > 0)) || min > 59 || sec > 59 || tzhour > 23 || tzmin > 59) {
				break syntax;
			}
			// valid ISO-8601 format, compute date in milliseconds
			double date = date_msecFromDate(year * yearmod, month - 1, day, hour, min, sec, msec);
			if (tzhour == -1) {
				// Spec says to use UTC timezone, the following bug report says
				// that local timezone was meant to be used. Stick with spec for now.
				// https://bugs.ecmascript.org/show_bug.cgi?id=112
				// date = internalUTC(date);
			} else {
				date -= (tzhour * 60 + tzmin) * msPerMinute * tzmod;
			}

			if (date < -HalfTimeDomain || date > HalfTimeDomain) {
				break syntax;
			}
			return date;
		}

		// invalid ISO-8601 format, return NaN
		return ScriptRuntime.NaN;
	}

	private static double date_parseString(String s) {
		double d = parseISOString(s);
		if (!Double.isNaN(d)) {
			return d;
		}

		int year = -1;
		int mon = -1;
		int mday = -1;
		int hour = -1;
		int min = -1;
		int sec = -1;
		char c = 0;
		char si = 0;
		int i = 0;
		int n = -1;
		double tzoffset = -1;
		char prevc = 0;
		int limit = 0;
		boolean seenplusminus = false;

		limit = s.length();
		while (i < limit) {
			c = s.charAt(i);
			i++;
			if (c <= ' ' || c == ',' || c == '-') {
				if (i < limit) {
					si = s.charAt(i);
					if (c == '-' && '0' <= si && si <= '9') {
						prevc = c;
					}
				}
				continue;
			}
			if (c == '(') { /* comments) */
				int depth = 1;
				while (i < limit) {
					c = s.charAt(i);
					i++;
					if (c == '(') {
						depth++;
					} else if (c == ')') {
						if (--depth <= 0) {
							break;
						}
					}
				}
				continue;
			}
			if ('0' <= c && c <= '9') {
				n = c - '0';
				while (i < limit && '0' <= (c = s.charAt(i)) && c <= '9') {
					n = n * 10 + c - '0';
					i++;
				}

				/* allow TZA before the year, so
				 * 'Wed Nov 05 21:49:11 GMT-0800 1997'
				 * works */

				/* uses of seenplusminus allow : in TZA, so Java
				 * no-timezone style of GMT+4:30 works
				 */
				if ((prevc == '+' || prevc == '-')/*  && year>=0 */) {
					/* make ':' case below change tzoffset */
					seenplusminus = true;

					/* offset */
					if (n < 24) {
						n = n * 60; /* EG. "GMT-3" */
					} else {
						n = n % 100 + n / 100 * 60; /* eg "GMT-0430" */
					}
					if (prevc == '+')       /* plus means east of GMT */ {
						n = -n;
					}
					if (tzoffset != 0 && tzoffset != -1) {
						return ScriptRuntime.NaN;
					}
					tzoffset = n;
				} else if (n >= 70 || (prevc == '/' && mon >= 0 && mday >= 0 && year < 0)) {
					if (year >= 0) {
						return ScriptRuntime.NaN;
					} else if (c <= ' ' || c == ',' || c == '/' || i >= limit) {
						year = n < 100 ? n + 1900 : n;
					} else {
						return ScriptRuntime.NaN;
					}
				} else if (c == ':') {
					if (hour < 0) {
						hour = /*byte*/ n;
					} else if (min < 0) {
						min = /*byte*/ n;
					} else {
						return ScriptRuntime.NaN;
					}
				} else if (c == '/') {
					if (mon < 0) {
						mon = /*byte*/ n - 1;
					} else if (mday < 0) {
						mday = /*byte*/ n;
					} else {
						return ScriptRuntime.NaN;
					}
				} else if (i < limit && c != ',' && c > ' ' && c != '-') {
					return ScriptRuntime.NaN;
				} else if (seenplusminus && n < 60) {  /* handle GMT-3:30 */
					if (tzoffset < 0) {
						tzoffset -= n;
					} else {
						tzoffset += n;
					}
				} else if (hour >= 0 && min < 0) {
					min = /*byte*/ n;
				} else if (min >= 0 && sec < 0) {
					sec = /*byte*/ n;
				} else if (mday < 0) {
					mday = /*byte*/ n;
				} else {
					return ScriptRuntime.NaN;
				}
				prevc = 0;
			} else if (c == '/' || c == ':' || c == '+' || c == '-') {
				prevc = c;
			} else {
				int st = i - 1;
				while (i < limit) {
					c = s.charAt(i);
					if (!(('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z'))) {
						break;
					}
					i++;
				}
				int letterCount = i - st;
				if (letterCount < 2) {
					return ScriptRuntime.NaN;
				}
				/*
				 * Use ported code from jsdate.c rather than the locale-specific
				 * date-parsing code from Java, to keep js and rhino consistent.
				 * Is this the right strategy?
				 */
				String wtb = "am;pm;" + "monday;tuesday;wednesday;thursday;friday;" + "saturday;sunday;" + "january;february;march;april;may;june;" + "july;august;september;october;november;december;" + "gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;";
				int index = 0;
				for (int wtbOffset = 0; ; ) {
					int wtbNext = wtb.indexOf(';', wtbOffset);
					if (wtbNext < 0) {
						return ScriptRuntime.NaN;
					}
					if (wtb.regionMatches(true, wtbOffset, s, st, letterCount)) {
						break;
					}
					wtbOffset = wtbNext + 1;
					++index;
				}
				if (index < 2) {
					/*
					 * AM/PM. Count 12:30 AM as 00:30, 12:30 PM as
					 * 12:30, instead of blindly adding 12 if PM.
					 */
					if (hour > 12 || hour < 0) {
						return ScriptRuntime.NaN;
					} else if (index == 0) {
						// AM
						if (hour == 12) {
							hour = 0;
						}
					} else {
						// PM
						if (hour != 12) {
							hour += 12;
						}
					}
				} else if ((index -= 2) < 7) {
					// ignore week days
				} else if ((index -= 7) < 12) {
					// month
					if (mon < 0) {
						mon = index;
					} else {
						return ScriptRuntime.NaN;
					}
				} else {
					index -= 12;
					// timezones
					switch (index) {
						case 0 /* gmt */ -> tzoffset = 0;
						case 1 /* ut */ -> tzoffset = 0;
						case 2 /* utc */ -> tzoffset = 0;
						case 3 /* est */ -> tzoffset = 5 * 60;
						case 4 /* edt */ -> tzoffset = 4 * 60;
						case 5 /* cst */ -> tzoffset = 6 * 60;
						case 6 /* cdt */ -> tzoffset = 5 * 60;
						case 7 /* mst */ -> tzoffset = 7 * 60;
						case 8 /* mdt */ -> tzoffset = 6 * 60;
						case 9 /* pst */ -> tzoffset = 8 * 60;
						case 10 /* pdt */ -> tzoffset = 7 * 60;
						default -> Kit.codeBug();
					}
				}
			}
		}
		if (year < 0 || mon < 0 || mday < 0) {
			return ScriptRuntime.NaN;
		}
		if (sec < 0) {
			sec = 0;
		}
		if (min < 0) {
			min = 0;
		}
		if (hour < 0) {
			hour = 0;
		}

		double msec = date_msecFromDate(year, mon, mday, hour, min, sec, 0);
		if (tzoffset == -1) { /* no time zone specified, have to use local */
			return internalUTC(msec);
		}
		return msec + tzoffset * msPerMinute;
	}

	private static String date_format(double t, int methodId) {
		StringBuilder result = new StringBuilder(60);
		double local = LocalTime(t);

		/* Tue Oct 31 09:41:40 GMT-0800 (PST) 2000 */
		/* Tue Oct 31 2000 */
		/* 09:41:40 GMT-0800 (PST) */

		if (methodId != Id_toTimeString) {
			appendWeekDayName(result, WeekDay(local));
			result.append(' ');
			appendMonthName(result, MonthFromTime(local));
			result.append(' ');
			append0PaddedUint(result, DateFromTime(local), 2);
			result.append(' ');
			int year = YearFromTime(local);
			if (year < 0) {
				result.append('-');
				year = -year;
			}
			append0PaddedUint(result, year, 4);
			if (methodId != Id_toDateString) {
				result.append(' ');
			}
		}

		if (methodId != Id_toDateString) {
			append0PaddedUint(result, HourFromTime(local), 2);
			result.append(':');
			append0PaddedUint(result, MinFromTime(local), 2);
			result.append(':');
			append0PaddedUint(result, SecFromTime(local), 2);

			// offset from GMT in minutes.  The offset includes daylight
			// savings, if it applies.
			int minutes = (int) Math.floor((LocalTZA + DaylightSavingTA(t)) / msPerMinute);
			// map 510 minutes to 0830 hours
			int offset = (minutes / 60) * 100 + minutes % 60;
			if (offset > 0) {
				result.append(" GMT+");
			} else {
				result.append(" GMT-");
				offset = -offset;
			}
			append0PaddedUint(result, offset, 4);

			// Find an equivalent year before getting the timezone
			// comment.  See DaylightSavingTA.
			if (t < 0.0) {
				int equiv = EquivalentYear(YearFromTime(local));
				double day = MakeDay(equiv, MonthFromTime(t), DateFromTime(t));
				t = MakeDate(day, TimeWithinDay(t));
			}
			result.append(" (");
			Date date = new Date((long) t);
			synchronized (timeZoneFormatter) {
				result.append(timeZoneFormatter.format(date));
			}
			result.append(')');
		}
		return result.toString();
	}

	/* the javascript constructor */
	private static Object jsConstructor(Object[] args, Context cx) {
		NativeDate obj = new NativeDate();

		// if called as a constructor with no args,
		// return a new Date with the current time.
		if (args.length == 0) {
			obj.date = now();
			return obj;
		}

		// if called with just one arg -
		if (args.length == 1) {
			Object arg0 = args[0];
			if (arg0 instanceof NativeDate) {
				obj.date = ((NativeDate) arg0).date;
				return obj;
			}
			if (arg0 instanceof Scriptable) {
				arg0 = ((Scriptable) arg0).getDefaultValue(cx, null);
			}
			double date;
			if (arg0 instanceof CharSequence) {
				// it's a string; parse it.
				date = date_parseString(arg0.toString());
			} else {
				// if it's not a string, use it as a millisecond date
				date = ScriptRuntime.toNumber(cx, arg0);
			}
			obj.date = TimeClip(date);
			return obj;
		}

		double time = date_msecFromArgs(args, cx);

		if (!Double.isNaN(time) && !Double.isInfinite(time)) {
			time = TimeClip(internalUTC(time));
		}

		obj.date = time;

		return obj;
	}

	private static String toLocale_helper(double t, int methodId) {
		DateFormat formatter = switch (methodId) {
			case Id_toLocaleString -> localeDateTimeFormatter;
			case Id_toLocaleTimeString -> localeTimeFormatter;
			case Id_toLocaleDateString -> localeDateFormatter;
			default -> throw new AssertionError(); // unreachable
		};

		synchronized (formatter) {
			return formatter.format(new Date((long) t));
		}
	}

	private static String js_toUTCString(double date) {
		StringBuilder result = new StringBuilder(60);

		appendWeekDayName(result, WeekDay(date));
		result.append(", ");
		append0PaddedUint(result, DateFromTime(date), 2);
		result.append(' ');
		appendMonthName(result, MonthFromTime(date));
		result.append(' ');
		int year = YearFromTime(date);
		if (year < 0) {
			result.append('-');
			year = -year;
		}
		append0PaddedUint(result, year, 4);
		result.append(' ');
		append0PaddedUint(result, HourFromTime(date), 2);
		result.append(':');
		append0PaddedUint(result, MinFromTime(date), 2);
		result.append(':');
		append0PaddedUint(result, SecFromTime(date), 2);
		result.append(" GMT");
		return result.toString();
	}

	private static String js_toISOString(double t) {
		StringBuilder result = new StringBuilder(27);

		int year = YearFromTime(t);
		if (year < 0) {
			result.append('-');
			append0PaddedUint(result, -year, 6);
		} else if (year > 9999) {
			append0PaddedUint(result, year, 6);
		} else {
			append0PaddedUint(result, year, 4);
		}
		result.append('-');
		append0PaddedUint(result, MonthFromTime(t) + 1, 2);
		result.append('-');
		append0PaddedUint(result, DateFromTime(t), 2);
		result.append('T');
		append0PaddedUint(result, HourFromTime(t), 2);
		result.append(':');
		append0PaddedUint(result, MinFromTime(t), 2);
		result.append(':');
		append0PaddedUint(result, SecFromTime(t), 2);
		result.append('.');
		append0PaddedUint(result, msFromTime(t), 3);
		result.append('Z');
		return result.toString();
	}

	private static void append0PaddedUint(StringBuilder sb, int i, int minWidth) {
		if (i < 0) {
			Kit.codeBug();
		}
		int scale = 1;
		--minWidth;
		if (i >= 10) {
			if (i < 1000 * 1000 * 1000) {
				for (; ; ) {
					int newScale = scale * 10;
					if (i < newScale) {
						break;
					}
					--minWidth;
					scale = newScale;
				}
			} else {
				// Separated case not to check against 10 * 10^9 overflow
				minWidth -= 9;
				scale = 1000 * 1000 * 1000;
			}
		}
		while (minWidth > 0) {
			sb.append('0');
			--minWidth;
		}
		while (scale != 1) {
			sb.append((char) ('0' + (i / scale)));
			i %= scale;
			scale /= 10;
		}
		sb.append((char) ('0' + i));
	}

	private static void appendMonthName(StringBuilder sb, int index) {
		// Take advantage of the fact that all month abbreviations
		// have the same length to minimize amount of strings runtime has
		// to keep in memory
		String months = "Jan" + "Feb" + "Mar" + "Apr" + "May" + "Jun" + "Jul" + "Aug" + "Sep" + "Oct" + "Nov" + "Dec";
		index *= 3;
		for (int i = 0; i != 3; ++i) {
			sb.append(months.charAt(index + i));
		}
	}

	private static void appendWeekDayName(StringBuilder sb, int index) {
		String days = "Sun" + "Mon" + "Tue" + "Wed" + "Thu" + "Fri" + "Sat";
		index *= 3;
		for (int i = 0; i != 3; ++i) {
			sb.append(days.charAt(index + i));
		}
	}

	private static double makeTime(double date, Object[] args, int methodId, Context cx) {
		if (args.length == 0) {
			/*
			 * Satisfy the ECMA rule that if a function is called with
			 * fewer arguments than the specified formal arguments, the
			 * remaining arguments are set to undefined.  Seems like all
			 * the Date.setWhatever functions in ECMA are only varargs
			 * beyond the first argument; this should be set to undefined
			 * if it's not given.  This means that "d = new Date();
			 * d.setMilliseconds()" returns NaN.  Blech.
			 */
			return ScriptRuntime.NaN;
		}

		int maxargs;
		boolean local = true;
		switch (methodId) {
			case Id_setUTCMilliseconds:
				local = false;
				// fallthrough
			case Id_setMilliseconds:
				maxargs = 1;
				break;

			case Id_setUTCSeconds:
				local = false;
				// fallthrough
			case Id_setSeconds:
				maxargs = 2;
				break;

			case Id_setUTCMinutes:
				local = false;
				// fallthrough
			case Id_setMinutes:
				maxargs = 3;
				break;

			case Id_setUTCHours:
				local = false;
				// fallthrough
			case Id_setHours:
				maxargs = 4;
				break;

			default:
				throw Kit.codeBug();
		}

		boolean hasNaN = false;
		int numNums = args.length < maxargs ? args.length : maxargs;
		assert numNums <= 4;
		double[] nums = new double[4];
		for (int i = 0; i < numNums; i++) {
			double d = ScriptRuntime.toNumber(cx, args[i]);
			if (Double.isNaN(d) || Double.isInfinite(d)) {
				hasNaN = true;
			} else {
				nums[i] = ScriptRuntime.toInteger(d);
			}
		}

		// just return NaN if the date is already NaN,
		// limit checks that happen in MakeTime in ECMA.
		if (hasNaN || Double.isNaN(date)) {
			return ScriptRuntime.NaN;
		}

		int i = 0, stop = numNums;
		double hour, min, sec, msec;
		double lorutime;  /* Local or UTC version of date */

		if (local) {
			lorutime = LocalTime(date);
		} else {
			lorutime = date;
		}

		if (maxargs >= 4 && i < stop) {
			hour = nums[i++];
		} else {
			hour = HourFromTime(lorutime);
		}

		if (maxargs >= 3 && i < stop) {
			min = nums[i++];
		} else {
			min = MinFromTime(lorutime);
		}

		if (maxargs >= 2 && i < stop) {
			sec = nums[i++];
		} else {
			sec = SecFromTime(lorutime);
		}

		if (maxargs >= 1 && i < stop) {
			msec = nums[i++];
		} else {
			msec = msFromTime(lorutime);
		}

		double time = MakeTime(hour, min, sec, msec);
		double result = MakeDate(Day(lorutime), time);

		if (local) {
			result = internalUTC(result);
		}

		return TimeClip(result);
	}

	private static double makeDate(double date, Object[] args, int methodId, Context cx) {
		/* see complaint about ECMA in date_MakeTime */
		if (args.length == 0) {
			return ScriptRuntime.NaN;
		}

		int maxargs;
		boolean local = true;
		switch (methodId) {
			case Id_setUTCDate:
				local = false;
				// fallthrough
			case Id_setDate:
				maxargs = 1;
				break;

			case Id_setUTCMonth:
				local = false;
				// fallthrough
			case Id_setMonth:
				maxargs = 2;
				break;

			case Id_setUTCFullYear:
				local = false;
				// fallthrough
			case Id_setFullYear:
				maxargs = 3;
				break;

			default:
				throw Kit.codeBug();
		}

		boolean hasNaN = false;
		int numNums = args.length < maxargs ? args.length : maxargs;
		assert 1 <= numNums && numNums <= 3;
		double[] nums = new double[3];
		for (int i = 0; i < numNums; i++) {
			double d = ScriptRuntime.toNumber(cx, args[i]);
			if (Double.isNaN(d) || Double.isInfinite(d)) {
				hasNaN = true;
			} else {
				nums[i] = ScriptRuntime.toInteger(d);
			}
		}

		// limit checks that happen in MakeTime in ECMA.
		if (hasNaN) {
			return ScriptRuntime.NaN;
		}

		int i = 0, stop = numNums;
		double year, month, day;
		double lorutime;  /* Local or UTC version of date */

		/* return NaN if date is NaN and we're not setting the year,
		 * If we are, use 0 as the time. */
		if (Double.isNaN(date)) {
			if (maxargs < 3) {
				return ScriptRuntime.NaN;
			}
			lorutime = 0;
		} else {
			if (local) {
				lorutime = LocalTime(date);
			} else {
				lorutime = date;
			}
		}

		if (maxargs >= 3 && i < stop) {
			year = nums[i++];
		} else {
			year = YearFromTime(lorutime);
		}

		if (maxargs >= 2 && i < stop) {
			month = nums[i++];
		} else {
			month = MonthFromTime(lorutime);
		}

		if (maxargs >= 1 && i < stop) {
			day = nums[i++];
		} else {
			day = DateFromTime(lorutime);
		}

		day = MakeDay(year, month, day); /* day within year */
		double result = MakeDate(day, TimeWithinDay(lorutime));

		if (local) {
			result = internalUTC(result);
		}

		return TimeClip(result);
	}

	private double date;

	private NativeDate() {
	}
	// #/string_id_map#

	@Override
	public String getClassName() {
		return "Date";
	}

	@Override
	public Object getDefaultValue(Context cx, Class<?> typeHint) {
		if (typeHint == null) {
			typeHint = ScriptRuntime.StringClass;
		}
		return super.getDefaultValue(cx, typeHint);
	}

	double getJSTimeValue() {
		return date;
	}

	@Override
	protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
		addIdFunctionProperty(ctor, DATE_TAG, ConstructorId_now, "now", 0, cx);
		addIdFunctionProperty(ctor, DATE_TAG, ConstructorId_parse, "parse", 1, cx);
		addIdFunctionProperty(ctor, DATE_TAG, ConstructorId_UTC, "UTC", 7, cx);
		super.fillConstructorProperties(ctor, cx);
	}

	@Override
	protected void initPrototypeId(int id, Context cx) {
		String s;
		int arity;
		switch (id) {
			case Id_constructor -> {
				arity = 7;
				s = "constructor";
			}
			case Id_toString -> {
				arity = 0;
				s = "toString";
			}
			case Id_toTimeString -> {
				arity = 0;
				s = "toTimeString";
			}
			case Id_toDateString -> {
				arity = 0;
				s = "toDateString";
			}
			case Id_toLocaleString -> {
				arity = 0;
				s = "toLocaleString";
			}
			case Id_toLocaleTimeString -> {
				arity = 0;
				s = "toLocaleTimeString";
			}
			case Id_toLocaleDateString -> {
				arity = 0;
				s = "toLocaleDateString";
			}
			case Id_toUTCString -> {
				arity = 0;
				s = "toUTCString";
			}
			case Id_toSource -> {
				arity = 0;
				s = "toSource";
			}
			case Id_valueOf -> {
				arity = 0;
				s = "valueOf";
			}
			case Id_getTime -> {
				arity = 0;
				s = "getTime";
			}
			case Id_getYear -> {
				arity = 0;
				s = "getYear";
			}
			case Id_getFullYear -> {
				arity = 0;
				s = "getFullYear";
			}
			case Id_getUTCFullYear -> {
				arity = 0;
				s = "getUTCFullYear";
			}
			case Id_getMonth -> {
				arity = 0;
				s = "getMonth";
			}
			case Id_getUTCMonth -> {
				arity = 0;
				s = "getUTCMonth";
			}
			case Id_getDate -> {
				arity = 0;
				s = "getDate";
			}
			case Id_getUTCDate -> {
				arity = 0;
				s = "getUTCDate";
			}
			case Id_getDay -> {
				arity = 0;
				s = "getDay";
			}
			case Id_getUTCDay -> {
				arity = 0;
				s = "getUTCDay";
			}
			case Id_getHours -> {
				arity = 0;
				s = "getHours";
			}
			case Id_getUTCHours -> {
				arity = 0;
				s = "getUTCHours";
			}
			case Id_getMinutes -> {
				arity = 0;
				s = "getMinutes";
			}
			case Id_getUTCMinutes -> {
				arity = 0;
				s = "getUTCMinutes";
			}
			case Id_getSeconds -> {
				arity = 0;
				s = "getSeconds";
			}
			case Id_getUTCSeconds -> {
				arity = 0;
				s = "getUTCSeconds";
			}
			case Id_getMilliseconds -> {
				arity = 0;
				s = "getMilliseconds";
			}
			case Id_getUTCMilliseconds -> {
				arity = 0;
				s = "getUTCMilliseconds";
			}
			case Id_getTimezoneOffset -> {
				arity = 0;
				s = "getTimezoneOffset";
			}
			case Id_setTime -> {
				arity = 1;
				s = "setTime";
			}
			case Id_setMilliseconds -> {
				arity = 1;
				s = "setMilliseconds";
			}
			case Id_setUTCMilliseconds -> {
				arity = 1;
				s = "setUTCMilliseconds";
			}
			case Id_setSeconds -> {
				arity = 2;
				s = "setSeconds";
			}
			case Id_setUTCSeconds -> {
				arity = 2;
				s = "setUTCSeconds";
			}
			case Id_setMinutes -> {
				arity = 3;
				s = "setMinutes";
			}
			case Id_setUTCMinutes -> {
				arity = 3;
				s = "setUTCMinutes";
			}
			case Id_setHours -> {
				arity = 4;
				s = "setHours";
			}
			case Id_setUTCHours -> {
				arity = 4;
				s = "setUTCHours";
			}
			case Id_setDate -> {
				arity = 1;
				s = "setDate";
			}
			case Id_setUTCDate -> {
				arity = 1;
				s = "setUTCDate";
			}
			case Id_setMonth -> {
				arity = 2;
				s = "setMonth";
			}
			case Id_setUTCMonth -> {
				arity = 2;
				s = "setUTCMonth";
			}
			case Id_setFullYear -> {
				arity = 3;
				s = "setFullYear";
			}
			case Id_setUTCFullYear -> {
				arity = 3;
				s = "setUTCFullYear";
			}
			case Id_setYear -> {
				arity = 1;
				s = "setYear";
			}
			case Id_toISOString -> {
				arity = 0;
				s = "toISOString";
			}
			case Id_toJSON -> {
				arity = 1;
				s = "toJSON";
			}
			default -> throw new IllegalArgumentException(String.valueOf(id));
		}
		initPrototypeMethod(DATE_TAG, id, s, arity, cx);
	}

	@Override
	public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (!f.hasTag(DATE_TAG)) {
			return super.execIdCall(f, cx, scope, thisObj, args);
		}
		int id = f.methodId();
		switch (id) {
			case ConstructorId_now:
				return ScriptRuntime.wrapNumber(now());

			case ConstructorId_parse: {
				String dataStr = ScriptRuntime.toString(cx, args, 0);
				return ScriptRuntime.wrapNumber(date_parseString(dataStr));
			}

			case ConstructorId_UTC:
				return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(cx, args));

			case Id_constructor: {
				// if called as a function, just return a string
				// representing the current time.
				if (thisObj != null) {
					return date_format(now(), Id_toString);
				}
				return jsConstructor(args, cx);
			}

			case Id_toJSON: {
				final String toISOString = "toISOString";

				Scriptable o = ScriptRuntime.toObject(cx, scope, thisObj);
				Object tv = ScriptRuntime.toPrimitive(cx, o, ScriptRuntime.NumberClass);
				if (tv instanceof Number) {
					double d = ((Number) tv).doubleValue();
					if (Double.isNaN(d) || Double.isInfinite(d)) {
						return null;
					}
				}
				Object toISO = getProperty(o, toISOString, cx);
				if (toISO == NOT_FOUND) {
					throw ScriptRuntime.typeError2(cx, "msg.function.not.found.in", toISOString, ScriptRuntime.toString(cx, o));
				}
				if (!(toISO instanceof Callable)) {
					throw ScriptRuntime.typeError3(cx, "msg.isnt.function.in", toISOString, ScriptRuntime.toString(cx, o), ScriptRuntime.toString(cx, toISO));
				}
				Object result = ((Callable) toISO).call(cx, scope, o, ScriptRuntime.EMPTY_OBJECTS);
				if (!ScriptRuntime.isPrimitive(result)) {
					throw ScriptRuntime.typeError1(cx, "msg.toisostring.must.return.primitive", ScriptRuntime.toString(cx, result));
				}
				return result;
			}

		}

		// The rest of Date.prototype methods require thisObj to be Date

		if (!(thisObj instanceof NativeDate realThis)) {
			throw incompatibleCallError(f, cx);
		}
		double t = realThis.date;

		switch (id) {

			case Id_toString:
			case Id_toTimeString:
			case Id_toDateString:
				if (!Double.isNaN(t)) {
					return date_format(t, id);
				}
				return js_NaN_date_str;

			case Id_toLocaleString:
			case Id_toLocaleTimeString:
			case Id_toLocaleDateString:
				if (!Double.isNaN(t)) {
					return toLocale_helper(t, id);
				}
				return js_NaN_date_str;

			case Id_toUTCString:
				if (!Double.isNaN(t)) {
					return js_toUTCString(t);
				}
				return js_NaN_date_str;

			case Id_toSource:
				return "not_supported";

			case Id_valueOf:
			case Id_getTime:
				return ScriptRuntime.wrapNumber(t);

			case Id_getYear:
			case Id_getFullYear:
			case Id_getUTCFullYear:
				if (!Double.isNaN(t)) {
					if (id != Id_getUTCFullYear) {
						t = LocalTime(t);
					}
					t = YearFromTime(t);
					if (id == Id_getYear) {
						t -= 1900;
					}
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getMonth:
			case Id_getUTCMonth:
				if (!Double.isNaN(t)) {
					if (id == Id_getMonth) {
						t = LocalTime(t);
					}
					t = MonthFromTime(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getDate:
			case Id_getUTCDate:
				if (!Double.isNaN(t)) {
					if (id == Id_getDate) {
						t = LocalTime(t);
					}
					t = DateFromTime(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getDay:
			case Id_getUTCDay:
				if (!Double.isNaN(t)) {
					if (id == Id_getDay) {
						t = LocalTime(t);
					}
					t = WeekDay(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getHours:
			case Id_getUTCHours:
				if (!Double.isNaN(t)) {
					if (id == Id_getHours) {
						t = LocalTime(t);
					}
					t = HourFromTime(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getMinutes:
			case Id_getUTCMinutes:
				if (!Double.isNaN(t)) {
					if (id == Id_getMinutes) {
						t = LocalTime(t);
					}
					t = MinFromTime(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getSeconds:
			case Id_getUTCSeconds:
				if (!Double.isNaN(t)) {
					if (id == Id_getSeconds) {
						t = LocalTime(t);
					}
					t = SecFromTime(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getMilliseconds:
			case Id_getUTCMilliseconds:
				if (!Double.isNaN(t)) {
					if (id == Id_getMilliseconds) {
						t = LocalTime(t);
					}
					t = msFromTime(t);
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_getTimezoneOffset:
				if (!Double.isNaN(t)) {
					t = (t - LocalTime(t)) / msPerMinute;
				}
				return ScriptRuntime.wrapNumber(t);

			case Id_setTime:
				t = TimeClip(ScriptRuntime.toNumber(cx, args, 0));
				realThis.date = t;
				return ScriptRuntime.wrapNumber(t);

			case Id_setMilliseconds:
			case Id_setUTCMilliseconds:
			case Id_setSeconds:
			case Id_setUTCSeconds:
			case Id_setMinutes:
			case Id_setUTCMinutes:
			case Id_setHours:
			case Id_setUTCHours:
				t = makeTime(t, args, id, cx);
				realThis.date = t;
				return ScriptRuntime.wrapNumber(t);

			case Id_setDate:
			case Id_setUTCDate:
			case Id_setMonth:
			case Id_setUTCMonth:
			case Id_setFullYear:
			case Id_setUTCFullYear:
				t = makeDate(t, args, id, cx);
				realThis.date = t;
				return ScriptRuntime.wrapNumber(t);

			case Id_setYear: {
				double year = ScriptRuntime.toNumber(cx, args, 0);

				if (Double.isNaN(year) || Double.isInfinite(year)) {
					t = ScriptRuntime.NaN;
				} else {
					if (Double.isNaN(t)) {
						t = 0;
					} else {
						t = LocalTime(t);
					}

					if (year >= 0 && year <= 99) {
						year += 1900;
					}

					double day = MakeDay(year, MonthFromTime(t), DateFromTime(t));
					t = MakeDate(day, TimeWithinDay(t));
					t = internalUTC(t);
					t = TimeClip(t);
				}
			}
			realThis.date = t;
			return ScriptRuntime.wrapNumber(t);

			case Id_toISOString:
				if (!Double.isNaN(t)) {
					return js_toISOString(t);
				}
				String msg = ScriptRuntime.getMessage0("msg.invalid.date");
				throw ScriptRuntime.rangeError(cx, msg);

			default:
				throw new IllegalArgumentException(String.valueOf(id));
		}

	}

	@Override
	protected int findPrototypeId(String s) {
		return switch (s) {
			case "constructor" -> Id_constructor;
			case "toString" -> Id_toString;
			case "toTimeString" -> Id_toTimeString;
			case "toDateString" -> Id_toDateString;
			case "toLocaleString" -> Id_toLocaleString;
			case "toLocaleTimeString" -> Id_toLocaleTimeString;
			case "toLocaleDateString" -> Id_toLocaleDateString;
			case "toUTCString" -> Id_toUTCString;
			case "toSource" -> Id_toSource;
			case "valueOf" -> Id_valueOf;
			case "getTime" -> Id_getTime;
			case "getYear" -> Id_getYear;
			case "getFullYear" -> Id_getFullYear;
			case "getUTCFullYear" -> Id_getUTCFullYear;
			case "getMonth" -> Id_getMonth;
			case "getUTCMonth" -> Id_getUTCMonth;
			case "getDate" -> Id_getDate;
			case "getUTCDate" -> Id_getUTCDate;
			case "getDay" -> Id_getDay;
			case "getUTCDay" -> Id_getUTCDay;
			case "getHours" -> Id_getHours;
			case "getUTCHours" -> Id_getUTCHours;
			case "getMinutes" -> Id_getMinutes;
			case "getUTCMinutes" -> Id_getUTCMinutes;
			case "getSeconds" -> Id_getSeconds;
			case "getUTCSeconds" -> Id_getUTCSeconds;
			case "getMilliseconds" -> Id_getMilliseconds;
			case "getUTCMilliseconds" -> Id_getUTCMilliseconds;
			case "getTimezoneOffset" -> Id_getTimezoneOffset;
			case "setTime" -> Id_setTime;
			case "setMilliseconds" -> Id_setMilliseconds;
			case "setUTCMilliseconds" -> Id_setUTCMilliseconds;
			case "setSeconds" -> Id_setSeconds;
			case "setUTCSeconds" -> Id_setUTCSeconds;
			case "setMinutes" -> Id_setMinutes;
			case "setUTCMinutes" -> Id_setUTCMinutes;
			case "setHours" -> Id_setHours;
			case "setUTCHours" -> Id_setUTCHours;
			case "setDate" -> Id_setDate;
			case "setUTCDate" -> Id_setUTCDate;
			case "setMonth" -> Id_setMonth;
			case "setUTCMonth" -> Id_setUTCMonth;
			case "setFullYear" -> Id_setFullYear;
			case "setUTCFullYear" -> Id_setUTCFullYear;
			case "setYear" -> Id_setYear;
			case "toISOString" -> Id_toISOString;
			case "toJSON" -> Id_toJSON;
			default -> 0;
		};
	}
}

