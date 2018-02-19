package main.java.com.pradas.jopma.artifacts;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ProcessModelImpl implements ProcessModel {
    protected HashMap<NodeImpl, ArrayList<NodeImpl>> flows;
    protected NodeImpl currentNode;
    protected NodeImpl endNode;
    protected String result;

    /**
     * Constructor for a ProcessModel
     * @param flows A HashMap including all the nodes and it's flows
     * @param startNode The first node to be executed
     * @param endNode The last node to be executed
     */
    public ProcessModelImpl(HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super();
        this.flows = flows;
        this.currentNode = startNode;
        this.endNode = endNode;
        this.result = "";
    }

    /**
     * The result of the execution of the process model
     * @return an empty string before the execution of the run method, and after the execution, returns the value
     */
    @Override
    public String getResult() {
        return result;
    }

    /**
     * The current node to be executed
     * @return
     */
    public NodeImpl getCurrentNode() {
        return currentNode;
    }

    /**
     * Method to run all the nodes
     */
    @Override
    public void run() {
        while (currentNode != endNode) {
            executeCurrentNode();
            forward();
        }
        executeCurrentNode();
    }

    /**
     * Method to select the next current node given the result of the execution of the current method.
     */
    @Override
    public void forward() {
        currentNode = (currentNode.getResult() instanceof Boolean && (Boolean) currentNode.getResult() == false) ?
                flows.get(currentNode).get(1) : flows.get(currentNode).get(0);
    }

    /**
     * Method to execute the curent node of the process model.
     */
    @Override
    public void executeCurrentNode() {
        currentNode.execute();
    }

    /**
     * Method to check if a process model need authentication
     * @return false, if a subclass need authentication, must override this method
     */
    public Boolean needAuthentication() {
        return false;
    }

    /**
     * Method to eneter the credentials for the authentication, if a subclass need authentication, must override this method
     * @param username a string containing the unsername of a protected resource
     * @param Password a string containing the passwrd of a protected resource
     */
    public void addCredentials(String username, String Password) { }
}
