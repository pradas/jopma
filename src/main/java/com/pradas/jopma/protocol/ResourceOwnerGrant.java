package main.java.com.pradas.jopma.protocol;

import main.java.com.pradas.jopma.artifacts.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceOwnerGrant extends GrantImpl {

    public ResourceOwnerGrant() {
        super();
        setPath("src/definitions/resourceownergrant");
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
        return ((MPILogicROGProcessModel) pmi).needAuthentication();
    }

    public void addUserCredentials(String user, String pass) {
        ((MPILogicROGProcessModel) pmi).addCredentials(user, pass);
    }

    private void configureGrant() {
        NodeImpl start, hasValidToken, hasToken, getRefreshRequest, getClientRequest, saveToken, doRequest;
        start = new MPILogicInputTask("start");
        hasValidToken = new MPILogicXORTask("hasvalidtoken", new Object[]{System.currentTimeMillis()/1000});
        hasToken = new MPILogicXORTask("gettoken");
        getRefreshRequest = new MPILogicTask("getrefreshrequest");
        getClientRequest = new MPILogicTask("getclientrequest");
        saveToken = new MPILogicInputTask("savetoken");
        doRequest = new MPILogicTask("gettokenandrequest");

        HashMap<NodeImpl, ArrayList<NodeImpl>> flows = new HashMap<>();
        ArrayList<NodeImpl> flowsStart, flowsHasValidToken, flowsHasToken, flowsRefreshRequest,
                flowsGetClientRequest, flowsSaveToken, flowsDoRequest;

        flowsStart = new ArrayList<>();
        flowsStart.add(hasValidToken);
        flowsHasValidToken = new ArrayList<>();
        flowsHasValidToken.add(doRequest);
        flowsHasValidToken.add(hasToken);
        flowsHasToken = new ArrayList<>();
        flowsHasToken.add(getRefreshRequest);
        flowsHasToken.add(getClientRequest);
        flowsRefreshRequest = new ArrayList<>();
        flowsRefreshRequest.add(saveToken);
        flowsGetClientRequest = new ArrayList<>();
        flowsGetClientRequest.add(saveToken);
        flowsSaveToken = new ArrayList<>();
        flowsSaveToken.add(doRequest);
        flowsDoRequest = new ArrayList<>();

        flows.put(start, flowsStart);
        flows.put(hasValidToken, flowsHasValidToken);
        flows.put(hasToken, flowsHasToken);
        flows.put(getRefreshRequest, flowsRefreshRequest);
        flows.put(getClientRequest, flowsGetClientRequest);
        flows.put(saveToken, flowsSaveToken);
        flows.put(doRequest, flowsDoRequest);

        pmi = new MPILogicROGProcessModel(filesPath, flows, start, doRequest);
    }

}
