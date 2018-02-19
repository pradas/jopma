package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.logicschema.Term;
import main.java.com.pradas.jopma.utils.Request;
import main.java.com.pradas.jopma.utils.StringUtils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPILogicCCGProcessModel extends MPILogicProcessModel {

    /**
     * Contructor that calls the superclass
     * @param filesPath A filePath instance containing a set of valid definitions
     * @param flows A HashMap including all the nodes and it's flows
     * @param startNode The first node to be executed
     * @param endNode The last node to be executed
     */
    public MPILogicCCGProcessModel(MPILogicFilesPath filesPath, HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super(filesPath, flows, startNode, endNode);
    }

    /**
     * Method that given a set of valid terms in the correct order, does a request to get an acces token and add the result as an arguement for the next task
     * @param terms The list of terms includin the necesary data to do the request
     */
    @Override
    protected void accessTokenRequest(List<Term> terms) {
        //prepare the parameters for the request
        Request r;
        Map<String, String> resultRequest;
        String parameters = "grant_type="+terms.get(1).getName()+";"+
                "client_id="+terms.get(0).getName()+";"+
                "client_secret="+terms.get(2).getName()+";"+
                "scope="+terms.get(4).getName();

        try {
            r = new Request(terms.get(3).getName(),"POST","",parameters);
            resultRequest = StringUtils.jsonStringToMap(r.doRequest());
            long newExpireDate = Long.parseLong(resultRequest.get("expires_in")) + System.currentTimeMillis()/1000;

            //Set the arguments for the next task
            ((MPILogicInputTask) flows.get(currentNode).get(0)).setArguments(
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

    /**
     * Method that given a set of valid terms in the correct order, does a request to gent a resource ans saves it as a result
     * @param terms The list of terms includin the necesary data to do the request
     */
    @Override
    protected void resourceRequest(List<Term> terms) {
        //prepare the parameters for the request
        Request r;

        String url = terms.get(3).getName();
        if (terms.get(4).getName() != "")
            url.concat("?"+terms.get(4).getName());
        String type = terms.get(6).getName();
        String headers = terms.get(5).getName()+"Authorization="+terms.get(0).getName()+" "+terms.get(2).getName();
        String parameters = terms.get(7).getName();

        try {
            //do the request an save the result
            r = new Request(url,type,headers,parameters);
            result = r.doRequest();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
