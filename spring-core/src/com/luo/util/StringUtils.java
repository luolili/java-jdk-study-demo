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


}
