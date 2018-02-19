package main.java.com.pradas.jopma.artifacts;

public abstract class NodeImpl implements Node {

    protected String name;
    protected Object result;
    protected Object[] arguments;

    /**
     * The name is a string that have to match an operation name of the behaviour definition file
     * @return The name of the node.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The result may vary depending on the type of node
     * @return the result of the execution of the node.
     */
    @Override
    public Object getResult() {
        return result;
    }

    /**
     * Constructor of a node with the name.
     * @param name a string with the given name to the node
     */
    public NodeImpl(String name) {
        super();
        this.name = name;
        this.arguments = new Object[]{};
    }

    /**
     * Constructor of a node with the name and arguments.
     * @param name a string with the given name to the node
     * @param arguments an array of objects including the arguments of the node.
     */
    public NodeImpl(String name, Object[] arguments) {
        super();
        this.name = name;
        this.arguments = arguments;
    }
}
