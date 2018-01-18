package main.java.com.pradas.jopma.artifacts;

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
        super();
        this.name = name;
        this.arguments = arguments;
    }
}
