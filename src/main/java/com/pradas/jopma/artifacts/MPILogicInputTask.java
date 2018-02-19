package main.java.com.pradas.jopma.artifacts;

public class MPILogicInputTask extends MPILogicTask {

    /**
     * Constructor of a node with the name.
     * @param methodName a string that have to match an operation name of the behaviour definition file
     */
    public MPILogicInputTask(String methodName) {
        super(methodName);
    }

    /**
     * Constructor of a node with the name and arguments.
     * @param methodName a string that have to match an operation name of the behaviour definition file
     * @param arguments an array of objects including the arguments of the node.
     */
    public MPILogicInputTask(String methodName, Object[] arguments) {
        super(methodName, arguments);
    }

    /**
     * Sets the arguments for the nodes that need the arguments added in runtime.
     * @param arguments An array of objects including all arguments.
     */
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
