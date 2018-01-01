package main.java.com.pradas.jopma.artifacts;

public interface ProcessModel {
    void run();

    void forward();

    void executeCurrentNode();

    String getResult();
}
