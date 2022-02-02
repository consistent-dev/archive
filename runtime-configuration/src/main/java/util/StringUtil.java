package util;

public class StringUtil {

    private StringUtil(){}

    public static boolean isEmpty(String s){
        return s == null || s.length() == 0;
    }

    public static String trim(String s){
        return s == null ? null : s.trim();
    }
}
