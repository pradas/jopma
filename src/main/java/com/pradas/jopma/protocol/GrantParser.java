package main.java.com.pradas.jopma.protocol;

import com.fasterxml.jackson.core.type.TypeReference;
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

    public GrantParser(String grantFile) {
        super();
        this.grantFile = grantFile;
        nodes = new HashMap<>();
        flows = new HashMap<>();
        parseGrant();
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
        return pmi.needAuthentication();
    }

    private void parseGrant() {
        GrantConfig gc = null;
        try {
            gc = new ObjectMapper().readValue(new File(grantFile), GrantConfig.class);

            setPath(gc.getDefinitionsPath());

            ArrayList<HashMap<String, Object>> gcNodes = (ArrayList<HashMap<String, Object>>) gc.getProcesModel().get("nodes");
            for (HashMap<String, Object> node: gcNodes) {
                String name = (String) node.get("contract_name");

                Object[] arguments = new Object[((ArrayList<String>) node.get("arguments")).size()];
                for (int i = 0; i < ((ArrayList<String>) node.get("arguments")).size(); i++) {
                    String arg = ((ArrayList<String>) node.get("arguments")).get(i);
                    if (arg.equals("currenttime")) {
                        arguments[i] = System.currentTimeMillis()/1000;
                    } else {
                        arguments[i] = arg;
                    }
                }

                NodeImpl n = null;
                if (node.get("node_type").equals("task")) {
                    n = new MPILogicTask(name, arguments);
                } else if (node.get("node_type").equals("input_task")) {
                    n = new MPILogicInputTask(name, arguments);
                } else if (node.get("node_type").equals("xor_task")) {
                    n = new MPILogicXORTask(name, arguments);
                }
                nodes.put((Integer) node.get("id"),n);
            }

            ArrayList<HashMap<String, Object>> gcFlows = (ArrayList<HashMap<String, Object>>) gc.getProcesModel().get("flows");
            for (HashMap<String, Object> flow: gcFlows) {
                ArrayList<NodeImpl> f = new ArrayList<>();
                ArrayList<HashMap<String, Integer>> flowDirections = (ArrayList<HashMap<String, Integer>>) flow.get("flow");
                for (HashMap<String, Integer> nextNode: flowDirections) {
                    f.add(nodes.get(nextNode.get("node_id")));
                }
                flows.put((Integer) flow.get("node_id"),f);
            }

            HashMap<NodeImpl, ArrayList<NodeImpl>> processFlows = new HashMap<>();
            int firstNodeId = -1;
            int lastNodeId = -1;
            for (Map.Entry<Integer, ArrayList<NodeImpl>> entry : flows.entrySet()) {
                processFlows.put(nodes.get(entry.getKey()), flows.get(entry.getKey()));
                if (firstNodeId == -1)
                    firstNodeId = entry.getKey();
                lastNodeId = entry.getKey();
            }

            if (gc.getProtocol().equals("clientcredentialsgrant")) {
                pmi = new MPILogicCCGProcessModel(filesPath, processFlows, nodes.get(firstNodeId), nodes.get(lastNodeId));
            } else if (gc.getProtocol().equals("resourceownergrant")) {
                pmi = new MPILogicROGProcessModel(filesPath, processFlows, nodes.get(firstNodeId), nodes.get(lastNodeId));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUserCredentials(String user, String pass) {
        pmi.addCredentials(user, pass);
    }

}
