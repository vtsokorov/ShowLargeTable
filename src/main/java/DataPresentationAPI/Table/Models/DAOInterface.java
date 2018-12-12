/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataPresentationAPI.Table.Models;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


public interface DAOInterface<T> 
{
    public boolean isOpenSession();
    public void save(T item) throws SQLException, Exception;
    public void update(T item) throws SQLException, Exception;
    public void delete(T item) throws SQLException, Exception;
    public T findById(Integer id) throws SQLException, Exception;
    public List<T> findAll() throws SQLException, Exception;
    public List<T> findAll(Integer skip, Integer maxshow) throws SQLException, Exception;
    public Object[][] getRawData(Integer skip, Integer maxshow) throws SQLException, Exception;
    public Integer count() throws SQLException, Exception;
    public Integer deleteAll() throws SQLException, Exception;
    public Integer getMaxId();
    public Integer getMinId(); 
    public T getLastRow();
    
    
    public String getColumnNameAt(Integer indexColumn);
    public String getFieldsNameAt(Integer indexColumn);
    public String [] getColumnNames();  
    public Class<?> getColumnClassAt(int indexColumn); 
    public Class<?>[] getColumnClasses(); 
    
    default public void setOrderParameters(Integer indexColumn, boolean ascending) {/*EMPTY*/};
}
