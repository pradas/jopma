package main.java.com.pradas.jopma.protocol;

import java.util.HashMap;

public class GrantConfig {
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
