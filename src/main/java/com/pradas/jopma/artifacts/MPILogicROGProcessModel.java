package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.logicschema.Term;
import main.java.com.pradas.jopma.utils.Request;
import main.java.com.pradas.jopma.utils.StringUtils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPILogicROGProcessModel extends MPILogicProcessModel {

    private String username;
    private String password;

    /**
     * Contructor that calls the superclass
     * @param filesPath A filePath instance containing a set of valid definitions
     * @param flows A HashMap including all the nodes and it's flows
     * @param startNode The first node to be executed
     * @param endNode The last node to be executed
     */
    public MPILogicROGProcessModel(MPILogicFilesPath filesPath, HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super(filesPath, flows, startNode, endNode);
    }

    /**
     * Method to eneter the credentials for the authentication
     * @param username a string containing the unsername of a protected resource
     * @param password a string containing the password of a protected resource
     */
    @Override
    public void addCredentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * Checks if authentication is needed searching if ther is a valid acces token or a refresh token saved.
     * @return Boolean indicatin if authentication is needed or not
     */
    @Override
    public Boolean needAuthentication() {
        MPILogicXORTask hasValidToken, hasToken;
        hasValidToken = new MPILogicXORTask("hasvalidtoken", new Object[]{System.currentTimeMillis()/1000});
        hasToken = new MPILogicXORTask("gettoken");
        hasValidToken.setProcessExecutor(processExecutor);
        hasToken.setProcessExecutor(processExecutor);
        hasValidToken.execute();
        hasToken.execute();

        if ((Boolean) hasValidToken.getResult() || (Boolean) hasToken.getResult())
            return false;
        return true;
    }

    /**
     * Method that given a set of valid terms in the correct order, does a request to get a refesh token and add the result as an arguement for the next task
     * @param terms The list of terms includin the necesary data to do the request
     */
    @Override
    protected void refreshTokenRequest(List<Term> terms) {
        //prepare the parameters for the request
        Request r;
        Map<String, String> resultRequest;
        String parameters = "grant_type=refresh_token;"+
                "client_id="+terms.get(0).getName()+";"+
                "client_secret="+terms.get(2).getName()+";"+
                "scope="+terms.get(4).getName()+";"+
                "refresh_token="+terms.get(8).getName();

        try {
            r = new Request(terms.get(3).getName(),"POST","",parameters);
            resultRequest = StringUtils.jsonStringToMap(r.doRequest());
            long newExpireDate = Long.parseLong(resultRequest.get("expires_in")) + System.currentTimeMillis()/1000;

            //Set the arguments for the next task
            ((MPILogicInputTask) flows.get(currentNode).get(0)).setArguments(
                    new Object[]{
                            resultRequest.get("token_type"),
                            newExpireDate,
                            resultRequest.get("access_token"),
                            resultRequest.get("refresh_token")
                    }
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
                "scope="+terms.get(4).getName()+";"+
                "username="+username+";password="+password;

        try {
            r = new Request(terms.get(3).getName(),"POST","",parameters);
            resultRequest = StringUtils.jsonStringToMap(r.doRequest());
            long newExpireDate = Long.parseLong(resultRequest.get("expires_in")) + System.currentTimeMillis()/1000;

            //Set the arguments for the next task
            ((MPILogicInputTask) flows.get(currentNode).get(0)).setArguments(
                    new Object[]{
                            resultRequest.get("token_type"),
                            newExpireDate,
                            resultRequest.get("access_token"),
                            resultRequest.get("refresh_token")
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

        String url = terms.get(4).getName();
        if (terms.get(5).getName() != "")
            url.concat("?"+terms.get(5).getName());
        String type = terms.get(7).getName();
        String headers = terms.get(6).getName()+"Authorization="+terms.get(0).getName()+" "+terms.get(2).getName();
        String parameters = terms.get(8).getName();

        try {
            //do the request an save the result
            r = new Request(url,type,headers,parameters);
            result = r.doRequest();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
