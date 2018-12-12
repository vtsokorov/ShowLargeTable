package database;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;

import DataPresentationAPI.Table.Models.DAOInterface;

public class ExTableService implements DAOInterface<ExTable>
{
    private Session session = null;
    private String sortColumnName;
    private Integer sortIndexColumn;
    private boolean ascending;
    
    public ExTableService()
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


    public void save(ExTable item) throws SQLException, Exception {
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
    }

    public void update(ExTable item) throws SQLException, Exception {
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
    }
    
    
    public void delete(ExTable item) throws SQLException, Exception {
        session.beginTransaction();
        session.delete(item);
        session.getTransaction().commit();
    }
    

    public ExTable findById(Integer id) throws SQLException, Exception {
        return session.get(ExTable.class, id);
    }


    public List<ExTable> findAll() throws SQLException, Exception {   
         return (List<ExTable>)session.createQuery("from ExTable").list();  
    }
    
    public List<ExTable> findAll(Integer skip, Integer maxshow) throws SQLException, Exception {   
    	CriteriaBuilder builder = session.getCriteriaBuilder();
    	CriteriaQuery<ExTable> q = builder.createQuery(ExTable.class);
    	Root<ExTable> bigtable = q.from(ExTable.class);
    	if(ascending == true)
    		q.select(bigtable).orderBy(builder.asc(bigtable.get(sortColumnName)));
    	else
    		q.select(bigtable).orderBy(builder.desc(bigtable.get(sortColumnName)));
    	Query<ExTable> query = session.createQuery(q);
    	return query.setFirstResult(skip).setMaxResults(maxshow).getResultList();
    	//return session.createQuery("from BigTable as bt order by 1 desc").setFirstResult(skip).setMaxResults(maxshow).list();  
    }
    
    public Object[][] getRawData(Integer skip, Integer maxshow) throws SQLException, Exception
    {
    	CriteriaBuilder builder = session.getCriteriaBuilder();
    	CriteriaQuery<ExTable> criteria = builder.createQuery(ExTable.class);
    	Root<ExTable> extable = criteria.from(ExTable.class);
    	
    	Fetch<ExTable, BigTable> fetch = extable.fetch(ExTable.getFieldsNameAt(1), JoinType.LEFT);
    	Join<ExTable, BigTable> join = extable.join(ExTable.getFieldsNameAt(1));
    	
    	Expression<?> x = sortIndexColumn == 1 ? join.get(BigTable.getFieldsNameAt(2)) 
    			: extable.get(ExTable.getFieldsNameAt(sortIndexColumn));
    	
	    if(ascending == true)
	    	criteria.select(extable).orderBy(builder.asc(x));
	    else
	    	criteria.select(extable).orderBy(builder.desc(x));

    	Query<ExTable> query = session.createQuery(criteria).setFirstResult(skip).setMaxResults(maxshow);
    	
    	List<ExTable> rows = query.getResultList();

		int numRows = maxshow - skip;
		Object[][] data = new Object[numRows][ExTable.getColumnCount()]; 
		
		for (int i = 0; i < numRows; i++){
			ExTable row = rows.get(i);
			data[i][0] = row.getId();
			data[i][1] = row.getParent().getNameRow();
			data[i][2] = row.getNameRow();
		}
		rows.clear();
		
		return data;
    }
       
    public Integer count() throws SQLException, Exception
    {
        return ((Long)session.createQuery("select count(*) from ExTable").uniqueResult()).intValue();
    }
     
    public Integer deleteAll() 
    {
       session.beginTransaction();
       CriteriaBuilder builder = session.getCriteriaBuilder();
       CriteriaDelete<ExTable> q = builder.createCriteriaDelete(ExTable.class);
       Root<ExTable> bigtable = q.from(ExTable.class);
    	//q.where(/*some codition*/);
       int result = session.createQuery(q).executeUpdate();
       session.getTransaction().commit();
       return result;
    }
    
    public Integer getMaxId()
    {
        DetachedCriteria criteria = DetachedCriteria
                .forClass(ExTable.class).setProjection(Projections.max("id"));
        return (Integer) criteria.getExecutableCriteria(session).list().get(0);
    }
    
    public Integer getMinId()
    {
        DetachedCriteria criteria = DetachedCriteria
                .forClass(ExTable.class).setProjection(Projections.min("id"));
        return (Integer) criteria.getExecutableCriteria(session).list().get(0);
    }

 
    public ExTable getLastRow() {
               DetachedCriteria criteria = DetachedCriteria
                .forClass(ExTable.class).setProjection(Projections.max("id"));
        Integer id = (Integer) criteria.getExecutableCriteria(session).list().get(0);
        
        return session.get(ExTable.class, id);
    }
    
    public String getColumnNameAt(Integer indexColumn)
    {
    	return ExTable.getColumnNameAt(indexColumn);
    }
    
    public String getFieldsNameAt(Integer indexColumn)
    {
    	return ExTable.getFieldsNameAt(indexColumn);
    }
    
    public String [] getColumnNames()
    {
    	return ExTable.getColumnNames();
    }
    
    public Class<?> getColumnClassAt(int indexColumn)
    {
        return ExTable.getColumnClassAt(indexColumn);
    }
    
    public Class<?>[] getColumnClasses()
    {
        return ExTable.getColumnClasses();
    }
    
    public void setOrderParameters(Integer indexColumn, boolean ascending)
    {   
    	this.sortIndexColumn = indexColumn;
    	this.sortColumnName = this.getFieldsNameAt(sortIndexColumn);
    	this.ascending  = ascending;
    }
}
