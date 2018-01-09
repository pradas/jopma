package main.java.com.pradas.jopma.utils;

public class MPILogicFilesPath {
    protected String umlSchema;        //Restriccions OCL del sistema
    protected String behaviourSchema;    //Operacions el sistema
    protected String dbConnection;        //Connexió MySQL
    protected String umlSchemaToDataMap;//Mapping classes UML a taules SQL
    protected String oauthClientInsert;

    public MPILogicFilesPath(String umlSchema, String behaviourSchema, String dbConnection, String umlSchemaToDataMap, String oauthClientInsert) {
        super();

        this.umlSchema = umlSchema;
        this.behaviourSchema = behaviourSchema;
        this.dbConnection = dbConnection;
        this.umlSchemaToDataMap = umlSchemaToDataMap;
        this.oauthClientInsert = oauthClientInsert;
    }

    public String getUmlSchema() {
        return umlSchema;
    }

    public String getBehaviourSchema() {
        return behaviourSchema;
    }

    public String getDbConnection() {
        return dbConnection;
    }

    public String getUmlSchemaToDataMap() {
        return umlSchemaToDataMap;
    }

    public String getOauthClientInsert() {
        return oauthClientInsert;
    }
}
