/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;

import DataPresentationAPI.Table.Models.DAOInterface;


public class BigTableService implements DAOInterface<BigTable> 
{
    private Session session  = null;
    private String sortColumnName;
    private Integer sortIndexColumn;
    private boolean ascending;
    
    public BigTableService()
    {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        if(sf != null) {
            session = sf.openSession();
            sortIndexColumn = 0;
            sortColumnName = this.getFieldsNameAt(sortIndexColumn);
            ascending  = true;
        }
    }
    
    
    public boolean isOpenSession() {
        return session != null;
    }


    public void save(BigTable item) throws SQLException, Exception {
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
    }

    public void update(BigTable item) throws SQLException, Exception {
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
    }
    
    
    public void delete(BigTable item) throws SQLException, Exception {
        session.beginTransaction();
        session.delete(item);
        session.getTransaction().commit();
    }
    

    public BigTable findById(Integer id) throws SQLException, Exception {
        return session.get(BigTable.class, id);
    }


    public List<BigTable> findAll() throws SQLException, Exception {   
         return (List<BigTable>)session.createQuery("from BigTable").list();  
    }
    
    public List<BigTable> findAll(Integer skip, Integer maxshow) throws SQLException, Exception {   
    	CriteriaBuilder builder = session.getCriteriaBuilder();
    	CriteriaQuery<BigTable> q = builder.createQuery(BigTable.class);
    	Root<BigTable> bigtable = q.from(BigTable.class);
    	if(ascending == true)
    		q.select(bigtable).orderBy(builder.asc(bigtable.get(sortColumnName)));
    	else
    		q.select(bigtable).orderBy(builder.desc(bigtable.get(sortColumnName)));
    	Query<BigTable> query = session.createQuery(q);
    	return query.setFirstResult(skip).setMaxResults(maxshow).getResultList();
    	//return session.createQuery("from BigTable as bt order by 1 desc").setFirstResult(skip).setMaxResults(maxshow).list();  
    }
    
    public Object[][] getRawData(Integer skip, Integer maxshow) throws SQLException, Exception
    {
    	CriteriaBuilder builder = session.getCriteriaBuilder();
    	CriteriaQuery<BigTable> q = builder.createQuery(BigTable.class);
    	Root<BigTable> bigtable = q.from(BigTable.class);
    	if(ascending == true)
    		q.select(bigtable).orderBy(builder.asc(bigtable.get(sortColumnName)));
    	else
    		q.select(bigtable).orderBy(builder.desc(bigtable.get(sortColumnName)));
    	Query<BigTable> query = session.createQuery(q);
    	List<BigTable> rows = query.setFirstResult(skip).setMaxResults(maxshow).getResultList();

		int numRows = maxshow - skip;
		Object[][] data = new Object[numRows][BigTable.getColumnCount()]; 
		
		for (int i = 0; i < numRows; i++){
			BigTable row = rows.get(i);
			data[i][0] = row.getId();
			data[i][1] = row.getDateRow();
			data[i][2] = row.getNameRow();
			data[i][3] = row.getValue();
		}
		rows.clear();
		
		return data;
    }
       
    public Integer count() throws SQLException, Exception
    {
        return ((Long)session.createQuery("select count(*) from BigTable").uniqueResult()).intValue();
    }
     
    public Integer deleteAll() 
    {
       session.beginTransaction();
       CriteriaBuilder builder = session.getCriteriaBuilder();
       CriteriaDelete<BigTable> q = builder.createCriteriaDelete(BigTable.class);
       Root<BigTable> bigtable = q.from(BigTable.class);
    	//q.where(/*some codition*/);
       int result = session.createQuery(q).executeUpdate();
       session.getTransaction().commit();
       return result;
    }
    
    public Integer getMaxId()
    {
        DetachedCriteria criteria = DetachedCriteria
                .forClass(BigTable.class).setProjection(Projections.max("id"));
        return (Integer) criteria.getExecutableCriteria(session).list().get(0);
    }
    
    public Integer getMinId()
    {
        DetachedCriteria criteria = DetachedCriteria
                .forClass(BigTable.class).setProjection(Projections.min("id"));
        return (Integer) criteria.getExecutableCriteria(session).list().get(0);
    }

 
    public BigTable getLastRow() {
               DetachedCriteria criteria = DetachedCriteria
                .forClass(BigTable.class).setProjection(Projections.max("id"));
        Integer id = (Integer) criteria.getExecutableCriteria(session).list().get(0);
        
        return session.get(BigTable.class, id);
    }
    
    public String getColumnNameAt(Integer indexColumn)
    {
    	return BigTable.getColumnNameAt(indexColumn);
    }
    
    public String getFieldsNameAt(Integer indexColumn)
    {
    	return BigTable.getFieldsNameAt(indexColumn);
    }
    
    public String [] getColumnNames()
    {
    	return BigTable.getColumnNames();
    }
    
    public Class<?> getColumnClassAt(int indexColumn)
    {
        return BigTable.getColumnClassAt(indexColumn);
    }
    
    public Class<?>[] getColumnClasses()
    {
        return BigTable.getColumnClasses();
    }
    
    public void setOrderParameters(Integer indexColumn, boolean ascending)
    {
    	this.sortIndexColumn = indexColumn;
    	this.sortColumnName = this.getFieldsNameAt(sortIndexColumn);
    	this.ascending  = ascending;
    }

}
