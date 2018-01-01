package main.java.com.pradas.jpm.artifacts;

import edu.upc.mpi.logicschema.Atom;

import java.lang.reflect.Method;
import java.util.List;

public abstract class NodeImpl implements Node {

    protected String name;
    protected Object result;
    protected Object[] arguments;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getResult() {
        return result;
    }

    public NodeImpl(String name) {
        super();
        this.name = name;
        this.arguments = new Object[]{};
    }

    public NodeImpl(String name, Object[] arguments) {
        this(name);
        this.arguments = arguments;
    }
}
