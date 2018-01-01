package main.java.com.pradas.jpm.stubs.models;

public class Client {
    private String grantType;
    private String clientId;
    private String cleintSecret;
    private String accesTokenEndpoint;
    private String scopes;

    public Client(String grantType, String clientId, String cleintSecret, String accesTokenEndpoint, String scopes) {
        super();

        setGrantType(grantType);
        setClientId(clientId);
        setCleintSecret(cleintSecret);
        setAccesTokenEndpoint(accesTokenEndpoint);
        setScopes(scopes);
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCleintSecret() {
        return cleintSecret;
    }

    public void setCleintSecret(String cleintSecret) {
        this.cleintSecret = cleintSecret;
    }

    public String getAccesTokenEndpoint() {
        return accesTokenEndpoint;
    }

    public void setAccesTokenEndpoint(String accesTokenEndpoint) {
        this.accesTokenEndpoint = accesTokenEndpoint;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
}
