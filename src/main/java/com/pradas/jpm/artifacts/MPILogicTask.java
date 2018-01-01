package main.java.com.pradas.jpm.artifacts;

import edu.upc.mpi.logicschema.Atom;
import edu.upc.mpi.operationexecutor.logicoperationexecutor.ProcessExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
