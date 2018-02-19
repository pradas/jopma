package main.java.com.pradas.jopma.protocol;

import java.util.HashMap;

/**
 * Skeleton class to parse the json file
 */
public class GrantMapper {
    public String protocol;
    public String definitions_path;
    public HashMap<String, Object> proces_model;

    public String getProtocol() {
        return protocol;
    }

    public String getDefinitionsPath() {
        return definitions_path;
    }

    public HashMap<String, Object> getProcesModel() {
        return proces_model;
    }
}
