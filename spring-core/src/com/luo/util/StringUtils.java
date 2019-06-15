package com.luo.util;

/**
 * it is abstract class too
 */
public abstract class StringUtils {

    //file attrs
    private static final String FOLDER_SEPARATOR = "/";//ctr+shift+u
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';


    /**
     * param is an Object
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(Object s) {
        return s == null || "".equals(s);
    }


    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasLength(String str) {
        //return (str!=null && str.length()>0);
        return (str != null && !str.isEmpty());
    }


    public static boolean containsText(CharSequence str) {
       /* if (str == null || str.length() == 0) {
            return false;
        }*/
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * has content in str:notice:it takes out the two conditions:str!=null and str.length()>0
     * do not put the two conditions into the containsText method
     *
     * @param str
     * @return
     */
    public static boolean hasText(CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    public static boolean containsWhiteSpace(CharSequence str) {
        //no white spacewhen str len is 0 or str is null
        if (!hasLength(str)) {
            return false;
        }

        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * convert String to CharSequence,then invoke containsWhiteSpace of CharSequence
     *
     * @param str
     * @return
     */
    public static boolean containsWhiteSpace(String str) {
        return containsWhiteSpace((CharSequence) str);
    }

    public static String trimWhiteSpace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        int beginIdex = 0;
        int endIndex = len - 1;

        while (beginIdex <= endIndex && Character.isWhitespace(str.charAt(beginIdex))) {
            beginIdex++;
        }
        while (endIndex > beginIdex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIdex, endIndex + 1);

    }

    public static String trimAllWhiteSpace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            /*if (!Character.isWhitespace(str.charAt(i))) {
                sb.append(str.charAt(i));
            }*/
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }

        return sb.toString();

    }

    //delete first whitespace of str
    public static String trimLeadingWhiteSpace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        if (str.length() > 0 && Character.isWhitespace(str.charAt(0))) {
            sb.deleteCharAt(0);
        }
        return sb.toString();

    }

    public static String trimTrailingWhiteSpace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        if (str.length() > 0 && Character.isWhitespace(str.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();

    }


    /**
     * use str.regionMarches to ignore case
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length()
                && str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return (str != null && suffix != null && str.length() >= suffix.length()
                && str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
    }

    public static boolean substringMatch(String str, int index, String substring) {
        if (index + substring.length() > str.length()) {
            return false;
        }

        //traverse the substring ,it is from index+i for the str
        for (int i = 0; i < substring.length(); i++) {
            if (str.charAt(index + i) != substring.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static int countOccurrencesOf(String str, String sub) {
        if (!hasLength(str) || !hasLength(sub)) {
            return 0;
        }

        int count = 0;
        int pos = 0;//start (dynamic) index pos
        int idx;

        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();//update to the next pos
        }
        return count;
    }

    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || hasLength(charsToDelete)) {
            return inString;
        }

        int len = inString.length();
        StringBuilder sb = new StringBuilder(inString.length());

        for (int i = 0; i < len; i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    //format string
    public static String quote(String str) {

        return (str != null ? "'" + str + "'" : null);
    }


    public static Object quoteIfString(Object object) {

        return ((object instanceof String) ? quote((String) object) : object);
    }

    //----unqualify:
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    public static String unqualify(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }

    //--capitalize

    public static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (!hasLength(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updateChar;

        if (capitalize) {
            updateChar = Character.toUpperCase(baseChar);
        } else {
            updateChar = Character.toLowerCase(baseChar);
        }

        if (updateChar == baseChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updateChar;

        return new String(chars, 0, chars.length);
    }

    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }


    //--file

    /**
     * get the file name from the path
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }

        int index = path.lastIndexOf(FOLDER_SEPARATOR);
        //-1 means path is the file name
        return (index != -1 ? path.substring(index + 1) : path);
    }

    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;//no extension
        }

        //--dot is before separator
        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }
        return (path.substring(extIndex + 1));
    }

    /**
     * strip the file extension for java resource path
     *
     * @param path
     * @return
     */
    public static String stripFilenameExtension(String path) {
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return path;
        }
        return path.substring(0, extIndex);
    }

    /**
     * use the relative path
     *
     * @param path
     * @param relativePath
     * @return
     */
    public static String applyRelativePath(String path, String relativePath) {

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex != -1) {
            String newPath = path.substring(0, folderIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }

    }

}
