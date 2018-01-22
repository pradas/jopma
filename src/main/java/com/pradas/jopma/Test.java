package main.java.com.pradas.jopma;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.pradas.jopma.protocol.*;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {

        GrantParser g = new GrantParser("ccg.json");

        if(g.needAuthentication()) {
            g.addUserCredentials("jane@roe.com", "Password12");
        }

        String result = g.makeRequest(
                "http://ec2-52-56-75-243.eu-west-2.compute.amazonaws.com/api/users",
                "",
                "GET",
                "",
                ""
        );
        System.out.println(result);

    }
}
