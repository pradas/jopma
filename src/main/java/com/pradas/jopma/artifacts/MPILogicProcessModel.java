package main.java.com.pradas.jopma.artifacts;

import edu.upc.mpi.logicschema.Atom;
import edu.upc.mpi.logicschema.Term;
import edu.upc.mpi.operationexecutor.Controller;
import edu.upc.mpi.operationexecutor.DataInitializerFinalizer;
import edu.upc.mpi.operationexecutor.dataservice.sql.SQLDataController;
import edu.upc.mpi.operationexecutor.logicoperationexecutor.ProcessExecutor;
import main.java.com.pradas.jopma.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MPILogicProcessModel extends ProcessModelImpl {
    protected ProcessExecutor processExecutor;

    /**
     * Contructor for the MPILogicProcessModel in charge of the creation of the processExecutor
     * @param filesPath A filePath instance containing a set of valid definitions
     * @param flows A HashMap including all the nodes and it's flows
     * @param startNode The first node to be executed
     * @param endNode The last node to be executed
     */
    public MPILogicProcessModel(MPILogicFilesPath filesPath, HashMap<NodeImpl, ArrayList<NodeImpl>> flows, NodeImpl startNode, NodeImpl endNode) {
        super(flows, startNode, endNode);

        File umlSchema = new File(filesPath.getUmlSchema());        //Restriccions OCL del sistema
        File behaviourSchema = new File(filesPath.getBehaviourSchema());    //Operacions el sistema
        File dbConnection = new File(filesPath.getDbConnection());        //Connexió MySQL
        File umlSchemaToDataMap = new File(filesPath.getUmlSchemaToDataMap());   //Mapping classes UML a taules SQL
        String oauthClientInsert = filesPath.getOauthClientInsert();


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

    /**
     * Execute the current task an does a request if needed
     */
    @Override
    public void executeCurrentNode() {
        // Set the processExecutor to the task
        ((MPILogicTask) currentNode).setProcessExecutor(processExecutor);
        //Execute current node
        super.executeCurrentNode();

        //If the task name contains the request keyword, then we have to know which kind of request invoke.
        String taskName = currentNode.getName();
        if (taskName.toLowerCase().contains("request")) {
            List<Term> terms = ((List<Atom>) currentNode.getResult()).get(0).getTerms();
            sanitizeTerms(terms);

            if (taskName.toLowerCase().contains("client")) {
                accessTokenRequest(terms);
            } else if(taskName.toLowerCase().contains("refresh")) {
                refreshTokenRequest(terms);
            } else if(taskName.toLowerCase().contains("token")) {
                resourceRequest(terms);
            }
        }
    }

    /**
     * Method to overide to do the request of an refreh token
     * @param terms The list of terms includin the necesary data to do the request
     */
    protected void refreshTokenRequest(List<Term> terms) {}

    /**
     * Method to overide to do the request to a resource
     * @param terms The list of terms includin the necesary data to do the request
     */
    protected void resourceRequest(List<Term> terms){}

    /**
     * Method to overide to do the request of an access token
     * @param terms The list of terms includin the necesary data to do the request
     */
    protected void accessTokenRequest(List<Term> terms){}

    /**
     * The returned list of terms contains a blank space when the term contain characters, this methos erase this blank space.
     * @param terms The list of terms to sanitize
     */
    private void sanitizeTerms(List<Term> terms) {
        for(Term t: terms){
            if (!StringUtils.containsOnlyNumbers(t.getName()))
                t.setName(t.getName().substring(1, t.getName().length()-1));
        }
    }

}
