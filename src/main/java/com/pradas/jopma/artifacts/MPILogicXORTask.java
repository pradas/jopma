package main.java.com.pradas.jopma.artifacts;

public class MPILogicXORTask extends MPILogicTask {

    /**
     * Constructor of a node with the name.
     * @param methodName a string that have to match an operation name of the behaviour definition file
     */
    public MPILogicXORTask(String methodName) {
        super(methodName);
    }

    /**
     * Constructor of a node with the name and arguments.
     * @param methodName a string that have to match an operation name of the behaviour definition file
     * @param arguments an array of objects including the arguments of the node.
     */
    public MPILogicXORTask(String methodName, Object[] arguments) {
        super(methodName, arguments);
    }

    /**
     * Depending on the result of the execution of the node, saves the result as a boolean showing the next node to execute.
     */
    @Override
    public void execute() {
        try {
            result = (processExecutor.executeOperation(name, arguments).isEmpty()) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
