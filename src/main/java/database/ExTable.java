package database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;

import DataPresentationAPI.Table.Models.CellInterface;


@Entity
@Table (name = "EXTABLE", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class ExTable implements Serializable, CellInterface  {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)//optional = false, 
    @JoinColumn(name="ID_PARENT", nullable = false, insertable = true, updatable = true)
    private BigTable parent;
    
    @Column(name = "TEXT_NAME", nullable = true, insertable = true, updatable = true)
    private String name;
    
    public ExTable() { }
    
    public ExTable(BigTable parent, String name)
    {
    	this.parent = parent;
    	this.name = name;
    }
    
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer value) {
        this.id = value;
    }
    
    public BigTable getParent() {
        return this.parent;
    }

    public void setParent(BigTable value) {
        this.parent = value;
    }
    
    public String getNameRow() {
        return this.name;
    }

    public void setNameRow(String value) {
        this.name = value;
    }
    
    @Transient 
    private static final String [] columnName = new String[]{"ID", "ID_PARENT", "TEXT_NAME"};

    @Transient 
    private static final String [] fieldsName = new String[]{"id", "parent", "name"};
    
    @Transient 
    private static final Class<?>[] columnClass = new Class[] {
        Integer.class, String.class, String.class };
    
    public static Class<?> getColumnClassAt(int indexColumn)
    {
        return columnClass[indexColumn];
    }
    
    public static Class<?>[] getColumnClasses()
    {
        return columnClass;
    }
    
    public static String getColumnNameAt(int indexColumn)
    {
        return columnName[indexColumn];
    }
    
    public static String [] getColumnNames()
    {
        return columnName;
    }
    
    public static int getColumnCount()
    {
        return columnName.length;
    }
    
    public static String getFieldsNameAt(int indexColumn)
    {
        return fieldsName[indexColumn];
    }

	@Override
	public void set(Integer columnIndex, Object data) {
		switch(columnIndex)
		{
			case 0: {this.setId((Integer)data); break;}
		    case 1: break;
		    case 2: {this.setNameRow((String)data); break;}
		}
		
	}

	@Override
	public Object get(Integer columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
   
}
