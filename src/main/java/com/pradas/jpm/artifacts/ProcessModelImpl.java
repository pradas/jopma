package main.java.com.pradas.jpm.artifacts;

import main.java.com.pradas.jpm.utils.Request;

import java.util.*;

public class ProcessModelImpl implements ProcessModel {
    protected HashMap<NodeImpl, ArrayList<NodeImpl>> flows;
    protected NodeImpl currentNode;
    protected NodeImpl endNode;
    protected String result;

    public ProcessModelImpl(HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super();
        this.flows = flows;
        this.currentNode = startNode;
        this.endNode = endNode;
        this.result = "";
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void run() {
        while (currentNode != endNode) {
            executeCurrentNode();
            forward();
        }
        executeCurrentNode();
    }

    @Override
    public void forward() {
        currentNode = (currentNode.getResult() instanceof Boolean && (Boolean) currentNode.getResult() == false) ?
                flows.get(currentNode).get(1) : flows.get(currentNode).get(0);
    }

    @Override
    public void executeCurrentNode() {
        currentNode.execute();
    }
}
