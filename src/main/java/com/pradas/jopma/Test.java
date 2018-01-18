package main.java.com.pradas.jopma;

import main.java.com.pradas.jopma.protocol.Grant;
import main.java.com.pradas.jopma.protocol.GrantFactory;
import main.java.com.pradas.jopma.protocol.ResourceOwnerGrant;

public class Test {
    public static void main(String[] args) {

        GrantFactory gf = new GrantFactory();
        //Grant g = gf.getGrant("ClientCredentialsGrant");
        Grant g = gf.getGrant("ResourceOwnerGrant");
        if(g.needAuthentication()) {
            ((ResourceOwnerGrant) g).addUserCredentials("jane@roe.com", "Password12");
        }

        String result = g.makeRequest(
                "http://ec2-35-177-110-173.eu-west-2.compute.amazonaws.com/api/user",
                "",
                "GET",
                "",
                ""
        );
        System.out.println(result);

    }
}
