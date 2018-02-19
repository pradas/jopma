package main.java.com.pradas.jopma.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public final class StringUtils {

    private StringUtils(){
        super();
    }

    /**
     * Converts a HashMap<String, String> into a string.
     * @param params HashMap to convert to string.
     * @param keyDelimeter Delimeter between key and value.
     * @param entryDelimeter Delimeter between entries.
     * @return String containing all the keys and values from the input HashMap.
     */
    public static String hashMapToString(HashMap<String, String> params, String keyDelimeter, String entryDelimeter) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append(keyDelimeter);
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append(entryDelimeter);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    /**
     * Converts a String into a HashMap<String>.
     * @param params String to convert to HashMap.
     * @param keyDelimeter Delimiter between key and value.
     * @param entryDelimeter Delimeter between entries.
     * @return HashMap containing all entries marked with the delimiters in the input string.
     */
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

    /**
     * Check that a string contain only numbers.
     * @param str Input string.
     * @return True if all characters are numbers, otherwise return false.
     */
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

    /**
     * Converts a strict first level JSON String into a Map <String,String>.
     * @param jsonInput Input string.
     * @return The parsed map of the input string.
     */
    public static Map<String, String> jsonStringToMap(String jsonInput) {
        Map<String, String> map = null;
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        try {
            map = mapper.readValue(jsonInput, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
