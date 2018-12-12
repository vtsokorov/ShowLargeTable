package database;

import java.nio.file.*;
import java.io.*;
import java.util.*;

//org.firebirdsql.jdbc.FBDriver
//jdbc:firebirdsql://localhost:3050/d:/vtsokorov/Firebird/STORE_DB.FDB
//?charSet=utf-8&roleName=yourRole charSet=utf-8&roleName=yourRole

public class FbProperty 
{
    private static final String driver = "org.firebirdsql.jdbc.FBDriver";
    private static final String dialect = "org.hibernate.dialect.FirebirdDialect";
    private String path;
    private String username;
    private String password; 
    private String host;
    private Integer port;
    
    private String encoding;
    private String roleName;
    
    public FbProperty() { }
    
    public void setDataBase(String path) { this.path = path; };
    public void setUserName(String username) { this.username = username; };
    public void setPassword(String password) { this.password = password; };
    public void setHost(String host) { this.host = host; };
    public void setPort(Integer port) { this.port = port; };
    public void setEncoding(String encoding) { this.encoding = encoding; };
    public void setRoleName(String roleName) { this.roleName = roleName; };
    
    public String driverName() { return FbProperty.driver; };
    public String dialectType() { return FbProperty.dialect; }
    public String dataBase() { return this.path; };
    public String userName() { return this.username; };
    public String password() { return this.password; };
    public String host() { return this.host; };
    public Integer port() { return this.port; };
    public String encoding() { return this.encoding; };
    public String roleName() { return this.roleName; };
    
    public String url() {
        return new StringBuilder().append("jdbc:firebirdsql://").append(host)
                    .append(":").append(String.valueOf(port)).append("/").append(path)
                .append(encoding.isEmpty() ? "" : "?encoding="+encoding)
                .append(roleName.isEmpty() ? "" : "&roleName="+roleName).toString();
    }
 

    public boolean readPropertyFromFile(String filename) throws IOException
    {
        Properties pf = new Properties();
        InputStream in = Files.newInputStream(Paths.get(filename));
        pf.load(in);

        path     = pf.getProperty("PATH");
        host     = pf.getProperty("HOST");
        port     = Integer.valueOf(pf.getProperty("PORT"));
        encoding = pf.getProperty("ENCODING");
        username = pf.getProperty("USERNAME");
        roleName = pf.getProperty("ROLE");
                   
        return !pf.isEmpty();
    }
    
    public void showProperty()
    {
        System.out.println(path);
        System.out.println(host);
        System.out.println(port);
        System.out.println(encoding);
        System.out.println(username);
        System.out.println(roleName);
        System.out.println(url());
    }
    
}
