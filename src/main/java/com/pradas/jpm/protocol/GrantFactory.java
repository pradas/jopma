package main.java.com.pradas.jpm.protocol;

public class GrantFactory {
    //use getShape method to get object of type shape
    public Grant getGrant(String grantType, String url, String parameters, String type, String headers, String body){
        if (grantType == null){
            return null;
        }
        if (grantType.equalsIgnoreCase("ClientCredentialsGrant")){
            return new ClientCredentialsGrant(url, parameters, type, headers, body);
        }

        return null;
    }
}
