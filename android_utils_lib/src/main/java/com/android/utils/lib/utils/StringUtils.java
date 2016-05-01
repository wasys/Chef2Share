package com.android.utils.lib.utils;

import java.util.ArrayList;

/**
 * @author ricardo
 *
 */
public class StringUtils {
	
	/**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;
    
    /**
     * <p>An array of <code>String</code>s used for padding.</p>
     *
     * <p>Used for efficient space padding. The length of each String expands as needed.</p>
     */
    private static final String[] PADDING = new String[Character.MAX_VALUE];

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static boolean isNotEmpty(String s) {
		return ! isEmpty(s);
	}

	public static String[] split(String retorno,String expr) {
		return retorno.split(expr);
	}
	
	/**
	 * Similar ao split do JDK. Retorna um Vector com as Strings quebradas
	 * 
	 * @param s
	 * @param token
	 * @return
	 */
	public static ArrayList<String> split(String s, char token) {
		ArrayList<String> v = new ArrayList<String>();
		int srclength = s.length();

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < srclength; i++) {
			char currentChar = s.charAt(i);
			if (currentChar == token) {
				if (buf.length() > 0) {
					v.add(buf.toString());
					buf = new StringBuffer();
				}
			} else {
				buf.append(currentChar);
			}
		}
		if (buf.length() > 0) {
			v.add(buf.toString());
		}
		return v;
	}
	
	/**
	 * Similar ao split do JDK. Retorna um Array com as Strings quebradas
	 * 
	 * @param s
	 * @param token
	 * @return
	 */
	public static String[] splitArray(String s, char token) {
		if(StringUtils.isEmpty(s)){
			return null;
		}
		ArrayList<String> v = split(s, token);
		String array[] = new String[v.size()];
		v.toArray(array);
		v.clear();
		v = null;
		return array;
	}
	
	/**
     * <p>Left pad a String with a specified character.</p>
     *
     * <p>Pad to a size of <code>size</code>.</p>
     *
     * <pre>
     * StringUtils.leftPad(null, *, *)     = null
     * StringUtils.leftPad("", 3, 'z')     = "zzz"
     * StringUtils.leftPad("bat", 3, 'z')  = "bat"
     * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
     * StringUtils.leftPad("bat", 1, 'z')  = "bat"
     * StringUtils.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size  the size to pad to
     * @param padChar  the character to pad with
     * @return left padded String or original String if no padding is necessary,
     *  <code>null</code> if null String input
     * @since 2.0
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }
    
    /**
     * <p>Left pad a String with a specified String.</p>
     *
     * <p>Pad to a size of <code>size</code>.</p>
     *
     * <pre>
     * StringUtils.leftPad(null, *, *)      = null
     * StringUtils.leftPad("", 3, "z")      = "zzz"
     * StringUtils.leftPad("bat", 3, "yz")  = "bat"
     * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
     * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
     * StringUtils.leftPad("bat", 1, "yz")  = "bat"
     * StringUtils.leftPad("bat", -1, "yz") = "bat"
     * StringUtils.leftPad("bat", 5, null)  = "  bat"
     * StringUtils.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size  the size to pad to
     * @param padStr  the String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary,
     *  <code>null</code> if null String input
     */
    public static String leftPad(String str, int size, String padStr) {
    	if (str == null) {
    		return null;
    	}

    	if(str.length() > size) {
    		throw new RuntimeException("Texto [" + str + "] excedeu o tamanho: " + size);
    	}

    	if (isEmpty(padStr)) {
    		padStr = " ";
    	}

    	int padLen = padStr.length();
    	int strLen = str.length();
    	int pads = size - strLen;

    	if (pads <= 0) {
    		return str; // returns original String when possible
    	}
    	if (padLen == 1 && pads <= PAD_LIMIT) {
    		return leftPad(str, size, padStr.charAt(0));
    	}

    	if (pads == padLen) {
    		return padStr.concat(str);
    	} else if (pads < padLen) {
    		return padStr.substring(0, pads).concat(str);
    	} else {
    		char[] padding = new char[pads];
    		char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * <p>Right pad a String with a specified character.</p>
     *
     * <p>The String is padded to the size of <code>size</code>.</p>
     *
     * <pre>
     * StringUtils.rightPad(null, *, *)     = null
     * StringUtils.rightPad("", 3, 'z')     = "zzz"
     * StringUtils.rightPad("bat", 3, 'z')  = "bat"
     * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
     * StringUtils.rightPad("bat", 1, 'z')  = "bat"
     * StringUtils.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size  the size to pad to
     * @param padChar  the character to pad with
     * @return right padded String or original String if no padding is necessary,
     *  <code>null</code> if null String input
     * @since 2.0
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }

    /**
     * <p>Right pad a String with a specified String.</p>
     *
     * <p>The String is padded to the size of <code>size</code>.</p>
     *
     * <pre>
     * StringUtils.rightPad(null, *, *)      = null
     * StringUtils.rightPad("", 3, "z")      = "zzz"
     * StringUtils.rightPad("bat", 3, "yz")  = "bat"
     * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
     * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StringUtils.rightPad("bat", 1, "yz")  = "bat"
     * StringUtils.rightPad("bat", -1, "yz") = "bat"
     * StringUtils.rightPad("bat", 5, null)  = "bat  "
     * StringUtils.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str  the String to pad out, may be null
     * @param size  the size to pad to
     * @param padStr  the String to pad with, null or empty treated as single space
     * @return right padded String or original String if no padding is necessary,
     *  <code>null</code> if null String input
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    /**
     * <p>Returns padding using the specified delimiter repeated
     * to a given length.</p>
     *
     * <pre>
     * StringUtils.padding(0, 'e')  = ""
     * StringUtils.padding(3, 'e')  = "eee"
     * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
     * </pre>
     *
     * @param repeat  number of times to repeat delim
     * @param padChar  character to repeat
     * @return String with repeated character
     * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
     */
    private static String padding(int repeat, char padChar) {
        // be careful of synchronization in this method
        // we are assuming that get and set from an array index is atomic
        String pad = PADDING[padChar];
        if (pad == null) {
            pad = String.valueOf(padChar);
        }
        while (pad.length() < repeat) {
            pad = pad.concat(pad);
        }
        PADDING[padChar] = pad;
        return pad.substring(0, repeat);
    }
    
    public static boolean isDouble(String s) {
    	if(StringUtils.isEmpty(s)){
    		return false;
    	}
    	try {
    		Double d = Double.parseDouble(s);
    		d.doubleValue();
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    }

    public static boolean isInteger(String s) {
    	if(StringUtils.isEmpty(s)){
    		return false;
    	}
    	try {
    		Integer.parseInt(s);
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    }
    
    public static boolean isLong(String s) {
    	if(StringUtils.isEmpty(s)){
    		return false;
    	}
    	try {
    		Long.parseLong(s);
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    }
    
	public static boolean equals(String s1, String s2) {
		return isNotEmpty(s1) && s1.equals(s2);
	}

	public static boolean equalsAny(String s1, String strings[]) {
		if(isEmpty(s1) && (strings == null || strings.length == 0)) {
			return false;
		}
		for (String s2 : strings) {
        	boolean ok = equals(s1,s2);
        	if(ok) {
        		return true;
        	}
		}
		return false;
	}
	
	public static boolean equalsIgnoreCaseAny(String s1, String strings[]) {
		if(isEmpty(s1) && (strings == null || strings.length == 0)) {
			return false;
		}
		for (String s2 : strings) {
        	boolean ok = equalsIgnoreCase(s1,s2);
        	if(ok) {
        		return true;
        	}
		}
		return false;
	}
	
	public static boolean equalsIgnoreCase(String s1, String s2) {
		return isNotEmpty(s1) && s1.equalsIgnoreCase(s2);
	}
	
	public static boolean notEquals(String s1, String s2) {
		return isNotEmpty(s1) && !s1.equals(s2);
	}
	
	/**
	 * Faz um trim na string e retira espa�os a mais do meio da String. 
	 * 
	 * @param str
	 * @return
	 */
	public static String trimAll(String str) {
		return str != null ? str.trim().replace("      ", " ").replace("     ", " ").replace("    ", " ").replace("   ", " ").replace("  ", " ") : null;
	}
	
	/**
	 * Faz um trim na string
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str != null ? str.trim() : null;
	}

	/**
     * <p>Search a String to find the first index of any
     * character in the given set of characters.</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.
     * A <code>null</code> or zero length search array will return <code>-1</code>.</p>
     *
     * <pre>
     * StringUtils.indexOfAny(null, *)                = -1
     * StringUtils.indexOfAny("", *)                  = -1
     * StringUtils.indexOfAny(*, null)                = -1
     * StringUtils.indexOfAny(*, [])                  = -1
     * StringUtils.indexOfAny("zzabyycdxx",['z','a']) = 0
     * StringUtils.indexOfAny("zzabyycdxx",['b','y']) = 3
     * StringUtils.indexOfAny("aba", ['z'])           = -1
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param searchChars  the chars to search for, may be null
     * @return the index of any of the chars, -1 if no match or null input
     * @since 2.0
     */
	public static int indexOfAny(String str, char[] searchChars) {
		if (isEmpty(str) || searchChars == null || searchChars.length == 0) {
			return -1;
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < searchChars.length; j++) {
				if (searchChars[j] == ch) {
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 *  <p>Capitalizes a String changing the first letter to title case as
    * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
    *
    * <p>For a word based algorithm, see {@link WordUtils#capitalize(String)}.
    * A <code>null</code> input String returns <code>null</code>.</p>
    *
    * <pre>
    * StringUtils.capitalize(null)  = null
    * StringUtils.capitalize("")    = ""
    * StringUtils.capitalize("cat") = "Cat"
    * StringUtils.capitalize("cAt") = "CAt"
    * </pre>
    *
    * @param str  the String to capitalize, may be null
    * @return the capitalized String, <code>null</code> if null String input
    * @see WordUtils#capitalize(String)
    * @see #uncapitalize(String)
    * @since 2.0
    */
   public static String capitalize(String str) {
       int strLen;
       if (str == null || (strLen = str.length()) == 0) {
           return str;
       }
       return new StringBuffer(strLen)
           .append(Character.toTitleCase(str.charAt(0)))
           .append(str.substring(1))
           .toString();
   }

	public static boolean contains(String str, String query) {
		if(isNotEmpty(str) && isNotEmpty(query)) {
			if(str.contains(query)) {
				return true;
			}
		}
		return false;
	}
	
	// ContainsAny
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if the String contains any character in the given
     * set of characters.</p>
     *
     * <p>A <code>null</code> String will return <code>false</code>.
     * A <code>null</code> or zero length search array will return <code>false</code>.</p>
     *
     * <pre>
     * StringUtils.containsAny(null, *)                = false
     * StringUtils.containsAny("", *)                  = false
     * StringUtils.containsAny(*, null)                = false
     * StringUtils.containsAny(*, [])                  = false
     * StringUtils.containsAny("zzabyycdxx",['z','a']) = true
     * StringUtils.containsAny("zzabyycdxx",['b','y']) = true
     * StringUtils.containsAny("aba", ['z'])           = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param searchChars  the chars to search for, may be null
     * @return the <code>true</code> if any of the chars are found,
     * <code>false</code> if no match or null input
     * @since 2.4
     */
    public static boolean containsAny(String str, char[] searchChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < searchChars.length; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * Checks if the String contains any character in the given set of characters.
     * </p>
     * 
     * <p>
     * A <code>null</code> String will return <code>false</code>. A <code>null</code> search string will return
     * <code>false</code>.
     * </p>
     * 
     * <pre>
     * StringUtils.containsAny(null, *)            = false
     * StringUtils.containsAny("", *)              = false
     * StringUtils.containsAny(*, null)            = false
     * StringUtils.containsAny(*, "")              = false
     * StringUtils.containsAny("zzabyycdxx", "za") = true
     * StringUtils.containsAny("zzabyycdxx", "by") = true
     * StringUtils.containsAny("aba","z")          = false
     * </pre>
     * 
     * @param str
     *            the String to check, may be null
     * @param searchChars
     *            the chars to search for, may be null
     * @return the <code>true</code> if any of the chars are found, <code>false</code> if no match or null input
     * @since 2.4
     */
    public static boolean containsAny(String str, String searchChars) {
        if (searchChars == null) {
            return false;
        }
        return containsAny(str, searchChars.toCharArray());
    }
    
    public static boolean containsAny(String str, String[] searchChars) {
        if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            for (String searchChar : searchChars) {
            	boolean ok = containsAny(str, searchChar.toCharArray());
            	if(ok) {
            		return true;
            	}
			}
        }
        return false;
    }
	
	// Replacing
    //-----------------------------------------------------------------------
    /**
     * <p>Replaces a String with another String inside a larger String, once.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replaceOnce(null, *, *)        = null
     * StringUtils.replaceOnce("", *, *)          = ""
     * StringUtils.replaceOnce("any", null, *)    = "any"
     * StringUtils.replaceOnce("any", *, null)    = "any"
     * StringUtils.replaceOnce("any", "", *)      = "any"
     * StringUtils.replaceOnce("aba", "a", null)  = "aba"
     * StringUtils.replaceOnce("aba", "a", "")    = "ba"
     * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     *
     * @see #replace(String text, String repl, String with, int max)
     * @param text  text to search and replace in, may be null
     * @param repl  the String to search for, may be null
     * @param with  the String to replace with, may be null
     * @return the text with any replacements processed,
     *  <code>null</code> if null String input
     */
    public static String replaceOnce(String text, String repl, String with) {
        return replace(text, repl, with, 1);
    }

    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @see #replace(String text, String repl, String with, int max)
     * @param text  text to search and replace in, may be null
     * @param repl  the String to search for, may be null
     * @param with  the String to replace with, may be null
     * @return the text with any replacements processed,
     *  <code>null</code> if null String input
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first <code>max</code> values of the search String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param repl  the String to search for, may be null
     * @param with  the String to replace with, may be null
     * @param max  maximum number of values to replace, or <code>-1</code> if no maximum
     * @return the text with any replacements processed,
     *  <code>null</code> if null String input
     */
    public static String replace(String text, String repl, String with, int max) {
    	if (isEmpty(text) || repl == null || with == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(repl, start);
        if (end == -1) {
            return text;
        }
        int replLength = repl.length();
        int increase = with.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(repl, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
    public static String removerEspeciaisEspaco(String valor) {
        if (valor == null || valor == "")
            return "";

        return valor.replace("!", "").replace("\"", "").replace("#", "").replace("@", "").replace("$", "").replace("®", "")
                .replace("&", "").replace("*", "").replace("(", "").replace(")", "").replace("-", "").replace("_", "")
                .replace("+", "").replace("=", "").replace(".", "").replace(",", "").replace("}", "").replace("]", "")
                .replace("[", "").replace("{", "").replace("™", "").replace("'", "").replace("^", "").replace("~", "")
                .replace("?", "").replace("/", "").replace("∞", "").replace(";", "").replace(":", "").replace(">", "")
                .replace("<", "").replace("|", "").replace("ß", "");
    }
    public static boolean containCharacterEspecial(String valor) {
        if(StringUtils.isEmpty(valor)) {
            return false;
        }
        char []x = valor.toCharArray();
        for(char c : x){
            if(c == '!' || c == '"' || c == '#' || c == '@' || c == '$' || c == '®' || c == '&' || c == '*' || c == '(' ||
                    c == ')' || c == '-' || c == '_' || c == '+' || c == '=' || c == '.' || c == ',' ||
                    c == '}' || c == ']' || c == '[' || c == '{' || c == '™' || c == '\'' ||
                    c == '^' || c == '~' || c == '?' || c == '/' || c == '∞' ||
                    c == ';' || c == ':' || c == '>' || c == '<' || c == '|' || c == 'ß'){
                return true;
            }
        }
        return false;
    }
}
