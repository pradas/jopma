package main.java.com.pradas.jopma.stubs.models;

public class Request {
    private String url;
    private String type;
    private String headers;
    private String parameters;

    public Request(String url, String type, String headers, String parameters) {
        super();

        setUrl(url);
        setType(type);
        setHeaders(headers);
        setParameters(parameters);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
