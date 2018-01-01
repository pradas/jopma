package main.java.com.pradas.jopma;

import main.java.com.pradas.jopma.artifacts.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMPILogic {

    public static void main(String[] args) {
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

        ((MPILogicInputTask) start).addArguments(
            new Object[]{
                "http://ec2-52-56-227-247.eu-west-2.compute.amazonaws.com/api/users",
                "",
                "",
                "GET",
                ""
            }
        );

        pmi.run();

        pmi.getResult();
    }

}
