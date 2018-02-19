package main.java.com.pradas.jopma.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.pradas.jopma.artifacts.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GrantParser extends GrantImpl {

    private String grantFile;
    private HashMap<Integer, NodeImpl> nodes;
    private HashMap<Integer, ArrayList<NodeImpl>> flows;

    /**
     * Contructor to initialize the grant
     * @param grantFile path to the description of the grant
     */
    public GrantParser(String grantFile) {
        super();
        this.grantFile = grantFile;
        nodes = new HashMap<>();
        flows = new HashMap<>();
        parseGrant();
    }

    /**
     * Configure a request and run the parsed grant.
     * @param url url of the request.
     * @param parameters parameters of the request.
     * @param type type of the request.
     * @param headers headers of the request.
     * @param body body of the request.
     * @return A string with the result of the execution of the grant.
     */
    @Override
    public String makeRequest(String url, String parameters, String type, String headers, String body) {
        ((MPILogicInputTask) pmi.getCurrentNode()).setArguments(new Object[]{url, parameters, headers, type, body});

        pmi.run();

        result = pmi.getResult();
        return result;
    }

    /**
     * Method to check if a grant need authentication.
     * @return Return true if the grant need authentication, otherwise return false.
     */
    @Override
    public Boolean needAuthentication() {
        return pmi.needAuthentication();
    }

    /**
     * Method to parse the file into a Grant
     */
    private void parseGrant() {
        GrantMapper gc = null;
        try {
            //Map the input json file into an instance of GrantMapper
            gc = new ObjectMapper().readValue(new File(grantFile), GrantMapper.class);

            setPath(gc.getDefinitionsPath());

            //Create all the nodes of the parsed grant
            ArrayList<HashMap<String, Object>> gcNodes = (ArrayList<HashMap<String, Object>>) gc.getProcesModel().get("nodes");
            for (HashMap<String, Object> node: gcNodes) {
                String name = (String) node.get("contract_name");

                //Configure the arguments of the node
                Object[] arguments = new Object[((ArrayList<String>) node.get("arguments")).size()];
                for (int i = 0; i < ((ArrayList<String>) node.get("arguments")).size(); i++) {
                    String arg = ((ArrayList<String>) node.get("arguments")).get(i);
                    if (arg.equals("currenttime")) {
                        arguments[i] = System.currentTimeMillis()/1000;
                    } else {
                        arguments[i] = arg;
                    }
                }

                //Configure the type of node
                NodeImpl n = null;
                if (node.get("node_type").equals("task")) {
                    n = new MPILogicTask(name, arguments);
                } else if (node.get("node_type").equals("input_task")) {
                    n = new MPILogicInputTask(name, arguments);
                } else if (node.get("node_type").equals("xor_task")) {
                    n = new MPILogicXORTask(name, arguments);
                }
                //Save the parsed node.
                nodes.put((Integer) node.get("id"),n);
            }

            //Create all the flows of the parsed grant.
            ArrayList<HashMap<String, Object>> gcFlows = (ArrayList<HashMap<String, Object>>) gc.getProcesModel().get("flows");
            for (HashMap<String, Object> flow: gcFlows) {
                ArrayList<NodeImpl> f = new ArrayList<>();
                ArrayList<HashMap<String, Integer>> flowDirections = (ArrayList<HashMap<String, Integer>>) flow.get("flow");
                for (HashMap<String, Integer> nextNode: flowDirections) {
                    f.add(nodes.get(nextNode.get("node_id")));
                }
                flows.put((Integer) flow.get("node_id"),f);
            }

            //Select the initial and finish node by id
            HashMap<NodeImpl, ArrayList<NodeImpl>> processFlows = new HashMap<>();
            int firstNodeId = -1;
            int lastNodeId = -1;
            for (Map.Entry<Integer, ArrayList<NodeImpl>> entry : flows.entrySet()) {
                processFlows.put(nodes.get(entry.getKey()), flows.get(entry.getKey()));
                if (firstNodeId == -1)
                    firstNodeId = entry.getKey();
                lastNodeId = entry.getKey();
            }

            //Select and create an instance of a ProcessModel depending on the type of the parsed grant
            if (gc.getProtocol().equals("clientcredentialsgrant")) {
                pmi = new MPILogicCCGProcessModel(filesPath, processFlows, nodes.get(firstNodeId), nodes.get(lastNodeId));
            } else if (gc.getProtocol().equals("resourceownergrant")) {
                pmi = new MPILogicROGProcessModel(filesPath, processFlows, nodes.get(firstNodeId), nodes.get(lastNodeId));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add user credentials to the grant
     * @param user username to consume the resource server, the username must be valid
     * @param pass password to consume the resource server, the password must be valid
     */
    public void addUserCredentials(String user, String pass) {
        pmi.addCredentials(user, pass);
    }

}
