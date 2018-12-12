package database;

import java.sql.*;

//org.firebirdsql.jdbc.FBDriver
//jdbc:firebirdsql://localhost:3050/d:/vtsokorov/Firebird/STORE_DB.FDB
//?charSet=utf-8&roleName=yourRole charSet=utf-8&roleName=yourRole

public class SqlDataBase 
{    
    private Connection connect;
    private DatabaseMetaData meta;
    private FbProperty property;
    
    public SqlDataBase(FbProperty property) {
        System.setProperty("jdbc.drivers", property.driverName()); 
        this.property = property;
        connect    = null;
        meta       = null;   
    }
    
    public static boolean VerificationConnection(FbProperty property) throws SQLException
    {
        boolean fClose = false;
        Connection connectOpen = DriverManager.getConnection(property.url(), 
                            property.userName(), property.password());
        boolean fOpen = connectOpen != null; 
        if(fOpen) {
            connectOpen.close(); fClose = true;
        }
        return fOpen && fClose;
    }
    
    
    public boolean open() throws SQLException
    {
        boolean fOpen = false;
        connect = DriverManager.getConnection(property.url(), property.userName(), property.password());
        meta = connect.getMetaData();
        fOpen = connect != null;
        return fOpen;
    }
    
    public boolean isOpen() {
        return connect != null;
    }
    
    public boolean close() throws SQLException
    {
        if(connect != null) {
            connect.close();
            connect = null;
            return true;
        }

        return false;
    }
    
    public Connection getConnection(){
        return connect;
    }
    
    public void setAutoCommit(boolean flag) throws SQLException
    {
        connect.setAutoCommit(flag);
    }

    public ResultSet executeQuery(String query) throws SQLException
    {
        Statement st = connect.createStatement();
        return st.executeQuery(query);
    }

}

