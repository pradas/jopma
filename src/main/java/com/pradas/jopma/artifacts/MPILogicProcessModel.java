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
    private final String oauthClientInsert;         //insert client
    private ProcessExecutor processExecutor;

    public MPILogicProcessModel(MPILogicFilesPath filesPath, HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super(flows, startNode, endNode);

        umlSchema = new File(filesPath.getUmlSchema());        //Restriccions OCL del sistema
        behaviourSchema = new File(filesPath.getBehaviourSchema());    //Operacions el sistema
        dbConnection = new File(filesPath.getDbConnection());        //Connexió MySQL
        umlSchemaToDataMap = new File(filesPath.getUmlSchemaToDataMap());   //Mapping classes UML a taules SQL
        oauthClientInsert = filesPath.getOauthClientInsert();


        try {
            DataInitializerFinalizer dataInitializer = new DataInitializerFinalizer(){
                SQLDataController sqlDataController = new SQLDataController(dbConnection);

                @Override
                public void initData() throws Exception {
                    String sqlStatement="";
                    sqlStatement+="truncate table `client`;";
                    //sqlStatement+="truncate table `token`;";
                    sqlStatement+="truncate table `request`;";
                    sqlStatement+= oauthClientInsert;

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
    public Boolean hasValidToken() {
        NodeImpl validToken = null;
        for ( NodeImpl key : flows.keySet() ) {
            if (key.getName() == "hasvalidtoken") {
                validToken = key;
            }
        }
        ((MPILogicTask) validToken).setProcessExecutor(processExecutor);
        validToken.execute();
        return (Boolean) validToken.getResult();
    }

    @Override
    public Boolean hasToken() {
        NodeImpl hasToken = new MPILogicXORTask("gettoken");
        ((MPILogicTask) hasToken).setProcessExecutor(processExecutor);
        List<Atom> temp = null;
        try {
            temp = processExecutor.executeOperation("gettoken", new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        //hasToken.execute();
        return (Boolean) hasToken.getResult();
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

    protected void resourceRequest(List<Term> terms) { }

    protected void accessTokenRequest(List<Term> terms) { }

    private void sanitizeTerms(List<Term> terms) {
        for(Term t: terms){
            if (!StringUtils.containsOnlyNumbers(t.getName()))
                t.setName(t.getName().substring(1, t.getName().length()-1));
        }
    }

}
