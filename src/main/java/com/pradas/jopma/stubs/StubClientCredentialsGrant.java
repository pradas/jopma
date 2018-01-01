package main.java.com.pradas.jopma.stubs;

import javafx.util.Pair;
import main.java.com.pradas.jopma.stubs.models.Client;
import main.java.com.pradas.jopma.stubs.models.Request;
import main.java.com.pradas.jopma.stubs.models.Token;

public class StubClientCredentialsGrant {

    private Client client;
    private Token token;
    private Request request;

    public StubClientCredentialsGrant(Client client) {
        super();
        this.client = client;
    }

    public void saveURL(String url, String type, String headers, String parameters) {
        request = new Request(url, type, headers, parameters);
    }

    public Boolean hasToken(){
        if (token != null)
            return true;
        return false;
    }

    public Client getTokenRequest() {
        return client;
    }

    public void saveToken(String accessToken, Integer expiresIn, String tokenType) {
        token = new Token(accessToken, expiresIn, tokenType);
    }

    public Pair<Request, Token> doGetRequest() {
        return new Pair<>(request, token);
    }
}
