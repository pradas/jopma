package main.java.com.pradas.jopma.protocol;

public interface Grant {
    String makeRequest(String url, String parameters, String type, String headers, String body);
    Boolean needAuthentication();
}
