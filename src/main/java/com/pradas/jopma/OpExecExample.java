/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.pradas.jopma;

import edu.upc.mpi.logicschema.Atom;
import edu.upc.mpi.logicschema.Term;
import edu.upc.mpi.operationexecutor.Controller;
import edu.upc.mpi.operationexecutor.DataInitializerFinalizer;
import edu.upc.mpi.operationexecutor.dataservice.sql.SQLDataController;
import edu.upc.mpi.operationexecutor.logicoperationexecutor.ProcessExecutor;
import java.io.File;
import java.util.List;

/**
 *
 * @author Xavier
 */
public class OpExecExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        final File umlSchema = new File("src/EXEMPLE/oauth-constraints.db");        //Restriccions OCL del sistema
        final File behaviourSchema = new File("src/EXEMPLE/oauth-behaviour.db");    //Operacions el sistema
        final File dbConnection = new File("src/EXEMPLE/db-connection.txt");        //Connexió MySQL
        final File umlSchemaToDataMap = new File("src/EXEMPLE/oauth-db-map.txt");   //Mapping classes UML a taules SQL

        //Implementing the method to initialize/finalize the data before/after executing the process
        DataInitializerFinalizer dataInitializer = new DataInitializerFinalizer(){
            SQLDataController sqlDataController = new SQLDataController(dbConnection);

            @Override
            public void initData() throws Exception {
                String sqlStatement="";
                sqlStatement+="truncate table `client`;";
                sqlStatement+="truncate table `token`;";
                sqlStatement+="truncate table `request`;";
                sqlStatement+="INSERT INTO `client` VALUES ('client-id','grant-type','client-secret','access-token-endpoing','scopte-list');";

                sqlDataController.establishConnection();
                sqlDataController.executeStatement(sqlStatement);
                sqlDataController.closeConnection();
            }

            @Override
            public void finalizeData() throws Exception {
                String sqlStatement="";
                sqlStatement+="truncate table `client`;";
                sqlStatement+="truncate table `token`;";
                sqlStatement+="truncate table `request`;";

                sqlDataController.establishConnection();
                sqlDataController.executeStatement(sqlStatement);
                sqlDataController.closeConnection();
            }
        };

        //Creating the controller. This method initializes the data according to the previous method
        Controller controller = new Controller(umlSchema, behaviourSchema, dbConnection, umlSchemaToDataMap, dataInitializer);

        //Creating the process executor. This method creates the artifact in the database
        ProcessExecutor processExecutor = controller.createProcessExecutor();

        //Invoking the operations throught the process executor. This method checks the constraints and persists the changes in the given database
        try{
            String opName;
            Object[] arguments;
            List<Atom> result;

            opName = "start";
            arguments = new String[]{"url","parameters","header","GET","body"};
            result = processExecutor.executeOperation(opName, arguments);
            if(result.isEmpty()){
                System.out.println("NO VALID TOKEN");
            } else System.out.println("VALID TOKEN FOUND");

            opName = "hasvalidtoken";
            arguments = new Object[1];
            arguments[0] = System.currentTimeMillis();
            result = processExecutor.executeOperation(opName, arguments); //Empty list means false, non empty list means true
            if(result.isEmpty()){
                System.out.println("NO VALID TOKEN");
            } else System.out.println("VALID TOKEN FOUND");

            opName = "getclientrequest";
            arguments = new Object[0];
            result = processExecutor.executeOperation(opName, arguments); //Empty list means false, non empty list means true
            System.out.println("CLIENT VALUES:");
            //for(Term t: result.get(0).getTerms()){
            //    System.out.println(t + "   " + t.getName());
            //}
            List<Term> tempo = result.get(0).getTerms();
            System.out.println((String) tempo.get(0).getName());
            System.out.println(tempo.get(1).getName().toString());
            System.out.println(tempo.get(2).getName());
            System.out.println(tempo.get(3).getName());
            System.out.println(tempo.get(4).getName());


            opName = "savetoken";
            arguments = new Object[]{"tokentype",System.currentTimeMillis()+10000,"access-token"};
            processExecutor.executeOperation(opName, arguments);

            opName = "hasvalidtoken";
            arguments = new Object[1];
            arguments[0] = System.currentTimeMillis();
            result = processExecutor.executeOperation(opName, arguments); //Empty list means false, non empty list means true
            if(result.isEmpty()){
                System.out.println("NO VALID TOKEN");
            } else System.out.println("VALID TOKEN FOUND");

            opName = "gettoken";
            arguments = new String[]{};
            result = processExecutor.executeOperation(opName, arguments);
            System.out.println("TOKEN VALUES:");
            for(Term t: result.get(0).getTerms()){
                System.out.println("   " + t.getName());
            }

            opName = "getrequest";
            arguments = new Object[]{};
            result = processExecutor.executeOperation(opName, arguments);
            System.out.println("REQUEST VALUES:");
            for(Term t: result.get(0).getTerms()){
                System.out.println("   " + t.getName());
            }

            opName = "removerequest";
            arguments = new Object[]{};
            processExecutor.executeOperation(opName, arguments);

        } finally{
            //Deleting the artifact from the database and closing the connection
            processExecutor.close();

            //Finalizing data
            controller.close();
        }
    }

}
