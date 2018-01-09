package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.logicschema.Term;
import main.java.com.pradas.jopma.utils.JSONUtils;
import main.java.com.pradas.jopma.utils.MPILogicFilesPath;
import main.java.com.pradas.jopma.utils.Request;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPILogicCCGProcessModel extends MPILogicProcessModel {

    public MPILogicCCGProcessModel(MPILogicFilesPath filesPath, HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super(filesPath, flows, startNode, endNode);
    }

    @Override
    protected void accessTokenRequest(List<Term> terms) {
        Request r;
        Map<String, String> resultRequest;
        String parameters = "grant_type="+terms.get(1).getName()+";"+
                "client_id="+terms.get(0).getName()+";"+
                "client_secret="+terms.get(2).getName()+";"+
                "scope="+terms.get(4).getName();

        try {
            r = new Request(terms.get(3).getName(),"POST","",parameters);
            resultRequest = JSONUtils.jsonStringToMap(r.doRequest());
            long newExpireDate = Long.parseLong(resultRequest.get("expires_in")) + System.currentTimeMillis()/1000;

            ((MPILogicInputTask) flows.get(currentNode).get(0)).addArguments(
                    new Object[]{
                            resultRequest.get("token_type"),
                            newExpireDate,
                            resultRequest.get("access_token")
                    }
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void resourceRequest(List<Term> terms) {
        Request r;
        Map<String, String> resultRequest;

        String url = terms.get(3).getName();
        if (terms.get(4).getName() != "")
            url.concat("?"+terms.get(4).getName());
        String type = terms.get(6).getName();
        String headers = terms.get(5).getName()+"Authorization="+terms.get(0).getName()+" "+terms.get(2).getName();
        String parameters = terms.get(7).getName();

        try {
            r = new Request(url,type,headers,parameters);
            result = r.doRequest();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
