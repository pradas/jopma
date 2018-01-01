package main.java.com.pradas.jpm.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public final class StringUtils {

    private StringUtils(){
        super();
    }

    public static String getParamsString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public static HashMap<String, String> stringToHashMap(String params, String keyDelimeter, String entryDelimeter) {
        HashMap<String, String> result = new HashMap<>();

        String[] entries = params.split(entryDelimeter);
        if (entries[0] != "") {
            for (String entry : entries) {
                String[] keyValue = entry.split(keyDelimeter);
                if (keyValue.length == 2)
                    result.put(keyValue[0], keyValue[1]);
                else
                    result.put(keyValue[0], "");
            }
        }
        return result;
    }

    public static boolean containsOnlyNumbers(String str) {

        //It can't contain only numbers if it's null or empty...
        if (str == null || str.length() == 0)
            return false;

        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }

        return true;
    }
}
