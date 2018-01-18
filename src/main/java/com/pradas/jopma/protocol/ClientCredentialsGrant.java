package main.java.com.pradas.jopma.protocol;

import main.java.com.pradas.jopma.artifacts.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientCredentialsGrant extends GrantImpl {

    public ClientCredentialsGrant() {
        super("src/definitions/clientcredentialsgrant");
        configureGrant();
    }

    @Override
    public String makeRequest(String url, String parameters, String type, String headers, String body) {
        ((MPILogicInputTask) pmi.getCurrentNode()).addArguments(new Object[]{url, parameters, headers, type, body});

        pmi.run();

        result = pmi.getResult();
        return result;
    }

    @Override
    public Boolean needAuthentication() {
        return false;
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
