package main.java.com.pradas.jopma.protocol;

import main.java.com.pradas.jopma.artifacts.*;
import main.java.com.pradas.jopma.utils.MPILogicFilesPath;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientCredentialsGrant implements Grant {
    private String result;
    private ProcessModelImpl pmi;
    private final MPILogicFilesPath filesPath;

    public String getResult() {
        return result;
    }

    public ClientCredentialsGrant() {
        super();

        List<String> list = null;
        try {
            list = Files.readAllLines(Paths.get("src/definitions/clientcredentialsgrant/oauth-client.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        filesPath = new MPILogicFilesPath(
                "src/definitions/clientcredentialsgrant/oauth-constraints.db",
                "src/definitions/clientcredentialsgrant/oauth-behaviour.db",
                "src/definitions/clientcredentialsgrant/db-connection.txt",
                "src/definitions/clientcredentialsgrant/oauth-db-map.txt",
                list.get(0)
        );

        configureGrant();
    }

    @Override
    public String makeRequest(String url, String parameters, String type, String headers, String body) {
        ((MPILogicInputTask) pmi.getCurrentNode()).addArguments(new Object[]{url, parameters, headers, type, body});

        pmi.run();

        result = pmi.getResult();
        return result;
    }

    private void configureGrant() {
        NodeImpl start, hasValidToken, getClientRequest, saveToken, doRequest;
        start = new MPILogicInputTask("start");
        hasValidToken = new MPILogicXORTask("hasvalidtoken", new Object[]{System.currentTimeMillis()/1000});
        getClientRequest = new MPILogicTask("getclientrequest");
        saveToken = new MPILogicInputTask("savetoken");
        doRequest = new MPILogicTask("gettokenandrequest");

        HashMap<NodeImpl, ArrayList<NodeImpl>> flows = new HashMap<>();
        ArrayList<NodeImpl> flowsStart, flowsHasValidToken, flowsGetClientRequest, flowsSaveToken, flowsDoRequest;

        flowsStart = new ArrayList<>();
        flowsStart.add(hasValidToken);
        flowsHasValidToken = new ArrayList<>();
        flowsHasValidToken.add(doRequest);
        flowsHasValidToken.add(getClientRequest);
        flowsGetClientRequest = new ArrayList<>();
        flowsGetClientRequest.add(saveToken);
        flowsSaveToken = new ArrayList<>();
        flowsSaveToken.add(doRequest);
        flowsDoRequest = new ArrayList<>();

        flows.put(start, flowsStart);
        flows.put(hasValidToken, flowsHasValidToken);
        flows.put(getClientRequest, flowsGetClientRequest);
        flows.put(saveToken, flowsSaveToken);
        flows.put(doRequest, flowsDoRequest);

        pmi = new MPILogicCCGProcessModel(filesPath, flows, start, doRequest);
    }

}
