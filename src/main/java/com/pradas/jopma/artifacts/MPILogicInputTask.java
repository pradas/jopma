package main.java.com.pradas.jopma.artifacts;

public class MPILogicInputTask extends MPILogicTask implements InputNode {

    public MPILogicInputTask(String methodName) {
        super(methodName);
    }

    public MPILogicInputTask(String methodName, Object[] arguments) {
        super(methodName, arguments);
    }

    @Override
    public void addArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
