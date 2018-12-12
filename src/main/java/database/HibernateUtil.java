/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;



import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;



public class HibernateUtil 
{
    private static SessionFactory sessionFactory = null;
    private static Configuration configuration = null;

    private HibernateUtil() { }
    
    public static boolean initConfiguration(FbProperty property) throws SQLException 
    {
        if(SqlDataBase.VerificationConnection(property)) {
            configuration = new Configuration().configure();
            configuration.setProperty("hibernate.connection.driver_class", property.driverName());
            configuration.setProperty("hibernate.dialect", property.dialectType());
            configuration.setProperty("hibernate.connection.url", property.url());
            configuration.setProperty("hibernate.connection.username", property.userName());
            configuration.setProperty("hibernate.connection.password", property.password());
            configuration.setProperty("hibernate.connection.autocommit", "false");
            
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(BigTable.class);
            configuration.addAnnotatedClass(ExTable.class);

            return true;
        }       
        return false;
    }

    public static SessionFactory getSessionFactory() 
    {
        if (sessionFactory == null) 
        {
            try { 
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } 
            catch (Exception  e) {
                System.out.println("ERROR : "+ e.getMessage());
            }
        }
        return sessionFactory;
    }
    
    public static void closeSession()
    {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}