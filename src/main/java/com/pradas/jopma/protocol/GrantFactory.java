package main.java.com.pradas.jopma.protocol;

public class GrantFactory {
    //use getShape method to get object of type shape
    public Grant getGrant(String grantType, String url, String parameters, String type, String headers, String body){
        if (grantType == null){
            return null;
        }
        if (grantType.equalsIgnoreCase("ClientCredentialsGrant")){
            return new ClientCredentialsGrant(url, parameters, type, headers, body);
        } else if (grantType.equalsIgnoreCase("ResourceOwnerGrant")) {
            return null;
        } else if (grantType.equalsIgnoreCase("AuthorizationCodeGrant")) {
            return null;
        }

        return null;
    }
}
