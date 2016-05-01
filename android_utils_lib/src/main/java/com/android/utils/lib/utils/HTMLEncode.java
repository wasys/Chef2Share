package com.android.utils.lib.utils;

/* 
 2      *  Java HTML Tidy - JTidy 
 3      *  HTML parser and pretty printer 
 4      * 
 5      *  Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts 
 6      *  Institute of Technology, Institut National de Recherche en 
 7      *  Informatique et en Automatique, Keio University). All Rights 
 8      *  Reserved. 
 9      * 
 10      *  Contributing Author(s): 
 11      * 
 12      *     Dave Raggett <dsr@w3.org> 
 13      *     Andy Quick <ac.quick@sympatico.ca> (translation to Java) 
 14      *     Gary L Peskin <garyp@firstech.com> (Java development) 
 15      *     Sami Lempinen <sami@lempinen.net> (release management) 
 16      *     Fabrizio Giustina <fgiust at users.sourceforge.net> 
 17      *     Vlad Skarzhevskyy <vlads at users.sourceforge.net> (JTidy servlet  development) 
 18      * 
 19      *  The contributing author(s) would like to thank all those who 
 20      *  helped with testing, bug fixes, and patience.  This wouldn't 
 21      *  have been possible without all of you. 
 22      * 
 23      *  COPYRIGHT NOTICE: 
 24      * 
 25      *  This software and documentation is provided "as is," and 
 26      *  the copyright holders and contributing author(s) make no 
 27      *  representations or warranties, express or implied, including 
 28      *  but not limited to, warranties of merchantability or fitness 
 29      *  for any particular purpose or that the use of the software or 
 30      *  documentation will not infringe any third party patents, 
 31      *  copyrights, trademarks or other rights. 
 32      * 
 33      *  The copyright holders and contributing author(s) will not be 
 34      *  liable for any direct, indirect, special or consequential damages 
 35      *  arising out of any use of the software or documentation, even if 
 36      *  advised of the possibility of such damage. 
 37      * 
 38      *  Permission is hereby granted to use, copy, modify, and distribute 
 39      *  this source code, or portions hereof, documentation and executables, 
 40      *  for any purpose, without fee, subject to the following restrictions: 
 41      * 
 42      *  1. The origin of this source code must not be misrepresented. 
 43      *  2. Altered versions must be plainly marked as such and must 
 44      *     not be misrepresented as being the original source. 
 45      *  3. This Copyright notice may not be removed or altered from any 
 46      *     source or altered source distribution. 
 47      * 
 48      *  The copyright holders and contributing author(s) specifically 
 49      *  permit, without fee, and encourage the use of this source code 
 50      *  as a component for supporting the Hypertext Markup Language in 
 51      *  commercial products. If you use this source code in a product, 
 52      *  acknowledgment is not required but would be appreciated. 
 53      * 
 54      */

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Converts a String to HTML by converting all special characters to
 * HTML-entities.
 * 
 * @author Vlad Skarzhevskyy <a
 *         href="mailto:skarzhevskyy@gmail.com">skarzhevskyy@gmail.com </a>
 * @version $Revision: 1.2 $ ($Author: ricardo $)
 */

@SuppressWarnings("unchecked")
public class HTMLEncode {

	/**
	 * j2se 1.4 encode method, used by reflection if available.
	 */
	private static Method encodeMethod14;

	static {
		// URLEncoder.encode(String) has been deprecated in J2SE 1.4.
		// Take advantage of the new method URLEncoder.encode(String, enc) if
		// J2SE 1.4 is used.
		try

		{
			Class urlEncoderClass = Class.forName("java.net.URLEncoder");

			encodeMethod14 = urlEncoderClass.getMethod("encode", new Class[] {
					String.class, String.class });

		} catch (Throwable ex) {
			// encodeMethod14 will be null if exception
		}
	}

	/**
	 * Utility class, don't instantiate.
	 */
	private HTMLEncode() {
		// unused
	}

	private static final String[] ENTITIES = { ">", "&gt;", "<", "&lt;", "&",
			"&amp;", "\"", "&quot;", "'", "&#039;", "\\", "&#092;", "\u00a9",
			"&copy;", "\u00ae", "&reg;" };

	private static Hashtable entityTableEncode = null;

	protected static synchronized void buildEntityTables() {
		entityTableEncode = new Hashtable(ENTITIES.length);

		for (int i = 0; i < ENTITIES.length; i += 2) {
			if (!entityTableEncode.containsKey(ENTITIES[i]))

			{
				entityTableEncode.put(ENTITIES[i], ENTITIES[i + 1]);

			}
		}
	}

	/**
	 * Converts a String to HTML by converting all special characters to
	 * HTML-entities.
	 */
	public final static String encode(String s)

	{
		return encode(s, "\n");

	}

	/**
	 * Converts a String to HTML by converting all special characters to
	 * HTML-entities.
	 */
	public final static String encode(String s, String cr)

	{
		if (entityTableEncode == null)

		{
			buildEntityTables();

		}
		if (s == null)

		{
			return "";

		}
		StringBuffer sb = new StringBuffer(s.length() * 2);

		char ch;

		for (int i = 0; i < s.length(); ++i)

		{
			ch = s.charAt(i);

			if ((ch >= 63 && ch <= 90) || (ch >= 97 && ch <= 122)
					|| (ch == ' '))

			{
				sb.append(ch);

			} else if (ch == '\n')

			{
				sb.append(cr);

			} else {
				String chEnc = encodeSingleChar(String.valueOf(ch));

				if (chEnc != null)

				{
					sb.append(chEnc);

				} else {
					// Not 7 Bit use the unicode system
					sb.append("&#");
					sb.append(new Integer(ch).toString());

					sb.append(';');

				}
			}
		}
		return sb.toString();

	}

	/**
	 * Converts a single character to HTML
	 */
	private static String encodeSingleChar(String ch)

	{
		return (String) entityTableEncode.get(ch);

	}

	/**
	 * Converts a String to valid HTML HREF by converting all special characters
	 * to HTML-entities.
	 * 
	 * @param url
	 *            url to be encoded
	 * @return encoded url.
	 */
	protected static String encodeHREFParam(String url)

	{
		if (encodeMethod14 != null)

		{
			Object[] methodArgs = new Object[2];

			methodArgs[0] = url;

			methodArgs[1] = "UTF8";

			try

			{
				return (String) encodeMethod14.invoke(null, methodArgs);

			} catch (Throwable e) {
				throw new RuntimeException(
						"Error invoking 1.4 URLEncoder.encode with reflection: "
								+ e.getMessage());

			}
		}

		// must use J2SE 1.3 version
		return URLEncoder.encode(url);

	}

	protected static String encodeHREFParamJava13(String value)

	{
		return URLEncoder.encode(value);

	}

	public static String encodeQuery(String url, String[] args)

	{
		return encodeHREFQuery(url, args, false);

	}

	public static String encodeHREFQuery(String url, String[] args)

	{
		return encodeHREFQuery(url, args, true);

	}

	public static String encodeHREFQuery(String url, String[] args,
			boolean forHtml)

	{
		StringBuffer out = new StringBuffer(128);

		out.append(url);

		if ((args != null) && (args.length > 0))

		{
			out.append("?");

			for (int i = 0; i < (args.length + 1) / 2; i++)

			{
				int k = i * 2;

				if (k != 0)

				{
					if (forHtml)

					{
						out.append("&amp;");

					} else {
						out.append("&");

					}
				}
				out.append(encodeHREFParam(args[k]));

				if (k + 1 < args.length)

				{
					out.append("=");

					out.append(encodeHREFParam(args[k + 1]));

				}
			}
		}
		return out.toString();

	}

	public static String encodeHREFQuery(String url, Map args, boolean forHtml)

	{
		StringBuffer out = new StringBuffer(128);

		out.append(url);

		if ((args != null) && (args.size() > 0))

		{
			out.append("?");

			int k = 0;

			for (Iterator i = args.keySet().iterator(); i.hasNext();)

			{
				if (k != 0)

				{
					if (forHtml)

					{
						out.append("&amp;");

					} else {
						out.append("&");

					}
				}
				String name = (String) i.next();

				out.append(encodeHREFParam(name));

				out.append("=");

				out.append(encodeHREFParam((String) args.get(name)));

				k++;

			}
		}
		return out.toString();

	}
}
