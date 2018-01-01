package main.java.com.pradas.jpm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class JSONUtils {
    private JSONUtils() {
        super();
    }

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
