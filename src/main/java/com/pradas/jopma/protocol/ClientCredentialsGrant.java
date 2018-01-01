package main.java.com.pradas.jopma.protocol;

import main.java.com.pradas.jopma.artifacts.*;
import main.java.com.pradas.jopma.utils.MPILogicFilesPath;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientCredentialsGrant implements Grant {
    private String url;
    private String parameters;
    private String type;
    private String headers;
    private String body;
    private String result;

    private final MPILogicFilesPath filesPath;

    public String getResult() {
        return result;
    }

    public ClientCredentialsGrant(String url, String parameters, String type, String headers, String body) {
        super();

        filesPath = new MPILogicFilesPath(
                "src/definitions/clientcredentialsgrant/oauth-constraints.db",
                "src/definitions/clientcredentialsgrant/oauth-behaviour.db",
                "src/definitions/db-connection.txt",
                "src/definitions/clientcredentialsgrant/oauth-db-map.txt"
        );

        this.url = url;
        this.parameters = parameters;
        this.type = type;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String makeRequest() {
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

        ProcessModelImpl pmi = new MPILogicProcessModel(filesPath, flows, start, doRequest);

        ((MPILogicInputTask) start).addArguments(new Object[]{url, parameters, headers, type, body});

        pmi.run();

        result = pmi.getResult();
        return result;
    }

}
