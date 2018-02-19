package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.operationexecutor.logicoperationexecutor.ProcessExecutor;

public class MPILogicTask extends NodeImpl {

    protected ProcessExecutor processExecutor;

    /**
     * Constructor of a node with the name.
     * @param methodName a string that have to match an operation name of the behaviour definition file
     */
    public MPILogicTask(String methodName) {
        super(methodName);
    }

    /**
     * Constructor of a node with the name and arguments.
     * @param methodName a string that have to match an operation name of the behaviour definition file
     * @param arguments an array of objects including the arguments of the node.
     */
    public MPILogicTask(String methodName, Object[] arguments) {
        super(methodName, arguments);
    }

    /**
     * Sets the processExecutor attached to the definitions file
     * @param processExecutor an initialized ProcessExecutor
     */
    public void setProcessExecutor(ProcessExecutor processExecutor) {
        this.processExecutor = processExecutor;
    }

    /**
     * Execute the node if the name of the node match a contract of the behaviour definition file
     */
    @Override
    public void execute() {
        try {
            result = processExecutor.executeOperation(name, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
