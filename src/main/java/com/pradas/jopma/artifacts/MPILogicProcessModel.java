package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.logicschema.Atom;
import edu.upc.mpi.logicschema.Term;
import edu.upc.mpi.operationexecutor.Controller;
import edu.upc.mpi.operationexecutor.DataInitializerFinalizer;
import edu.upc.mpi.operationexecutor.dataservice.sql.SQLDataController;
import edu.upc.mpi.operationexecutor.logicoperationexecutor.ProcessExecutor;
import main.java.com.pradas.jopma.utils.JSONUtils;
import main.java.com.pradas.jopma.utils.MPILogicFilesPath;
import main.java.com.pradas.jopma.utils.Request;
import main.java.com.pradas.jopma.utils.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

public class MPILogicProcessModel extends ProcessModelImpl {

    private final File umlSchema;        //Restriccions OCL del sistema
    private final File behaviourSchema;    //Operacions el sistema
    private final File dbConnection;        //Connexió MySQL
    private final File umlSchemaToDataMap;   //Mapping classes UML a taules SQL
    private ProcessExecutor processExecutor;

    public MPILogicProcessModel(MPILogicFilesPath filesPath, HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super(flows, startNode, endNode);

        umlSchema = new File(filesPath.getUmlSchema());        //Restriccions OCL del sistema
        behaviourSchema = new File(filesPath.getBehaviourSchema());    //Operacions el sistema
        dbConnection = new File(filesPath.getDbConnection());        //Connexió MySQL
        umlSchemaToDataMap = new File(filesPath.getUmlSchemaToDataMap());   //Mapping classes UML a taules SQL


        try {
            DataInitializerFinalizer dataInitializer = new DataInitializerFinalizer(){
                SQLDataController sqlDataController = new SQLDataController(dbConnection);

                @Override
                public void initData() throws Exception {
                    String sqlStatement="";
                    sqlStatement+="truncate table `client`;";
                    //sqlStatement+="truncate table `token`;";
                    sqlStatement+="truncate table `request`;";
                    sqlStatement+="INSERT INTO `client` VALUES ('2','client_credentials'," +
                            "'meHv3ApE6Gbcow2TQjlijUHb23GKSWpHhL2fW9Bx'," +
                            "'http://ec2-52-56-227-247.eu-west-2.compute.amazonaws.com/oauth/token','');";

                    sqlDataController.establishConnection();
                    sqlDataController.executeStatement(sqlStatement);
                    sqlDataController.closeConnection();
                }

                @Override
                public void finalizeData() throws Exception {
                    String sqlStatement="";
                    sqlStatement+="truncate table `client`;";
                    //sqlStatement+="truncate table `token`;";
                    sqlStatement+="truncate table `request`;";

                    sqlDataController.establishConnection();
                    sqlDataController.executeStatement(sqlStatement);
                    sqlDataController.closeConnection();
                }
            };


            //Creating the controller. This method initializes the data according to the previous method
            Controller controller = new Controller(umlSchema, behaviourSchema, dbConnection, umlSchemaToDataMap, dataInitializer);

            //Creating the process executor. This method creates the artifact in the database
            processExecutor = controller.createProcessExecutor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void executeCurrentNode() {
        ((MPILogicTask) currentNode).setProcessExecutor(processExecutor);
        //Execute current node
        super.executeCurrentNode();

        String taskName = currentNode.getName();
        if (taskName.toLowerCase().contains("request")) {
            List<Term> terms = ((List<Atom>) currentNode.getResult()).get(0).getTerms();
            sanitizeTerms(terms);

            if (taskName.toLowerCase().contains("client")) {
                accessTokenRequest(terms);
            } else if(taskName.toLowerCase().contains("token")) {
                resourceRequest(terms);
            }
        }
    }

    private void resourceRequest(List<Term> terms) {
        Request r;
        Map<String, String> resultRequest;

        String url = terms.get(3).getName();
        if (terms.get(4).getName() != "")
            url.concat("?"+terms.get(4).getName());
        String type = terms.get(6).getName();
        String headers = terms.get(5).getName()+"Authorization="+terms.get(0).getName()+" "+terms.get(2).getName();
        String parameters = terms.get(7).getName();

        try {
            r = new Request(url,type,headers,parameters);
            result = r.doRequest();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void accessTokenRequest(List<Term> terms) {
        Request r;
        Map<String, String> resultRequest;
        String parameters = "grant_type="+terms.get(1).getName()+";"+
                "client_id="+terms.get(0).getName()+";"+
                "client_secret="+terms.get(2).getName()+";"+
                "scope="+terms.get(4).getName();

        try {
            r = new Request(terms.get(3).getName(),"POST","",parameters);
            resultRequest = JSONUtils.jsonStringToMap(r.doRequest());
            long newExpireDate = Long.parseLong(resultRequest.get("expires_in")) + System.currentTimeMillis()/1000;

            ((MPILogicInputTask) flows.get(currentNode).get(0)).addArguments(
                new Object[]{
                    resultRequest.get("token_type"),
                    newExpireDate,
                    resultRequest.get("access_token")
                }
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void sanitizeTerms(List<Term> terms) {
        for(Term t: terms){
            if (!StringUtils.containsOnlyNumbers(t.getName()))
                t.setName(t.getName().substring(1, t.getName().length()-1));
        }
    }

}
