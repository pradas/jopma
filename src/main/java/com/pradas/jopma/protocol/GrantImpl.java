package main.java.com.pradas.jopma.protocol;

import main.java.com.pradas.jopma.artifacts.ProcessModelImpl;
import main.java.com.pradas.jopma.artifacts.MPILogicFilesPath;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class GrantImpl implements Grant{
    protected String result;
    protected ProcessModelImpl pmi;
    protected MPILogicFilesPath filesPath;

    /**
     * Constructor to set the attributes
     */
    public GrantImpl() {
        super();
        result = "";
        pmi = null;
        filesPath = null;
    }

    /**
     * Getter of the result of the execution of the grant
     * @return String containing the result of the grant
     */
    public String getResult() {
        return result;
    }

    /**
     * Setter of the path
     * @param path The path to the folder conaining the configuration files.
     */
    protected void setPath(String path) {
        List<String> list = null;
        try {
            list = Files.readAllLines(Paths.get(path+"/oauth-client.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        filesPath = new MPILogicFilesPath(
                path+"/oauth-constraints.db",
                path+"/oauth-behaviour.db",
                path+"/db-connection.txt",
                path+"/oauth-db-map.txt",
                list.get(0)
        );    }
}
