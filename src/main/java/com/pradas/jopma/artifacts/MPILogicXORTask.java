package main.java.com.pradas.jopma.artifacts;

public class MPILogicXORTask extends MPILogicTask {
    public MPILogicXORTask(String methodName) {
        super(methodName);
    }

    public MPILogicXORTask(String methodName, Object[] arguments) {
        super(methodName, arguments);
    }

    @Override
    public void execute() {
        try {
            result = (processExecutor.executeOperation(name, arguments).isEmpty()) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
