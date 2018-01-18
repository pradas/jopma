package main.java.com.pradas.jopma.protocol;

import main.java.com.pradas.jopma.artifacts.ProcessModelImpl;
import main.java.com.pradas.jopma.utils.MPILogicFilesPath;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class GrantImpl implements Grant{
    protected String result;
    protected ProcessModelImpl pmi;
    protected final MPILogicFilesPath filesPath;

    public String getResult() {
        return result;
    }

    public GrantImpl(String path) {
        super();

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
        );
    }
}
