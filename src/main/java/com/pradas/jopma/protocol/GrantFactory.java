package main.java.com.pradas.jopma.protocol;

public class GrantFactory {
    //use getShape method to get object of type shape
    public Grant getGrant(String grantType){
        if (grantType == null){
            return null;
        }
        if (grantType.equalsIgnoreCase("ClientCredentialsGrant")){
            return new ClientCredentialsGrant();
        } else if (grantType.equalsIgnoreCase("ResourceOwnerGrant")) {
            return new ResourceOwnerGrant();
        } else if (grantType.equalsIgnoreCase("AuthorizationCodeGrant")) {
            return null;
        }

        return null;
    }
}
