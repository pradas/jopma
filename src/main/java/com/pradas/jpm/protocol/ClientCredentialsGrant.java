package main.java.com.pradas.jpm.protocol;

import main.java.com.pradas.jpm.artifacts.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientCredentialsGrant implements Grant {
    private String url;
    private String parameters;
    private String type;
    private String headers;
    private String body;
    private String result;

    public String getResult() {
        return result;
    }

    public ClientCredentialsGrant(String url, String parameters, String type, String headers, String body) {
        super();

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

        ProcessModelImpl pmi = new MPILogicProcessModel(flows, start, doRequest);

        ((MPILogicInputTask) start).addArguments(new Object[]{url, parameters, headers, type, body});

        pmi.run();

        result = pmi.getResult();
        return result;
    }

}
