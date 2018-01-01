package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.operationexecutor.logicoperationexecutor.ProcessExecutor;

public class MPILogicTask extends NodeImpl {

    protected ProcessExecutor processExecutor;

    public MPILogicTask(String methodName) {
        super(methodName);
    }

    public MPILogicTask(String methodName, Object[] arguments) {
        super(methodName, arguments);
    }

    public void setProcessExecutor(ProcessExecutor processExecutor) {
        this.processExecutor = processExecutor;
    }

    @Override
    public void execute() {
        try {
            result = processExecutor.executeOperation(name, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
