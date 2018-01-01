package main.java.com.pradas.jpm;

import main.java.com.pradas.jpm.artifacts.NodeImpl;
import main.java.com.pradas.jpm.artifacts.StubTask;
import main.java.com.pradas.jpm.stubs.models.Client;
import main.java.com.pradas.jpm.stubs.StubClientCredentialsGrant;

import java.lang.reflect.Method;

public class MainStub {


    public static void main(String[] args) {

        Client client = new Client(
                "client_credentials",
                "2",
                "meHv3ApE6Gbcow2TQjlijUHb23GKSWpHhL2fW9Bx",
                "http://ec2-35-177-81-144.eu-west-2.compute.amazonaws.com/oauth/token",
                ""
        );
        StubClientCredentialsGrant sccg = new StubClientCredentialsGrant(client);
        Class cl = StubClientCredentialsGrant.class;

        NodeImpl enterURL = null;
        StubTask hasToken = null;
        StubTask getToken = null;
        StubTask saveToken = null;
        StubTask doRequest = null;

        Method m = null;
        Class[] paramsSaveURL = new Class[]{
                String.class,
                String.class,
                String.class,
                String.class
        };
        Class[] paramsSaveToken = new Class[]{
                String.class,
                Integer.class,
                String.class
        };

        Object[] varSaveURL = new Object[]{
                new String("http://url action"),
                new String("GET"),
                new String(""),
                new String("")
        };
        Object[] varSaveToken = new Object[]{
                new String("12341234123412341234"),
                4,
                new String("Bearer")
        };

        try {
            m = cl.getMethod("saveURL", paramsSaveURL);
            enterURL = new StubTask(m, varSaveURL, sccg);
            m = cl.getMethod("hasToken");
            hasToken = new StubTask(m,sccg);
            m = cl.getMethod("getTokenRequest");
            getToken = new StubTask(m,sccg);
            m = cl.getMethod("saveToken", paramsSaveToken);
            saveToken = new StubTask(m, varSaveToken, sccg);
            m = cl.getMethod("doGetRequest");
            doRequest = new StubTask(m,sccg);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
/*
        ProcessModelImpl p = new ProcessModelImpl();

        p.setCurrentNode(enterURL);
        p.setEndNode(doRequest);

        ArrayList<NodeImpl> temp = new ArrayList<>();
        temp.add(hasToken);
        p.addFlow(enterURL, temp);

        temp = new ArrayList<>();
        temp.add(getToken);
        p.addFlow(hasToken, temp);

        temp = new ArrayList<>();
        temp.add(saveToken);
        p.addFlow(getToken, temp);

        temp = new ArrayList<>();
        temp.add(doRequest);
        p.addFlow(saveToken, temp);

        //p.executeCurrentNode();
        //p.forward();
        p.run();

*/
    }
}
