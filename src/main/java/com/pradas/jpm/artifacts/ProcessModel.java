package main.java.com.pradas.jpm.artifacts;

import java.util.ArrayList;
import java.util.HashMap;

public interface ProcessModel {
    void run();

    void forward();

    void executeCurrentNode();

    String getResult();
}
