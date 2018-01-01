package main.java.com.pradas.jpm.stubs.models;

public class Token {
    private String tokenType;
    private Integer expiresIn;
    private String accessToken;

    public Token(String tokenType, Integer expiresIn, String accessToken) {
        super();

        setTokenType(tokenType);
        setExpiresIn(expiresIn);
        setAccessToken(accessToken);
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
