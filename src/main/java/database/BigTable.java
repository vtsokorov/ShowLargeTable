
package database;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import javax.persistence.*;



@Entity
@Table (name = "BIGTABLE", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class BigTable implements Serializable 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, insertable = true, updatable = true)
    private Integer id;
    
    @Column(name = "DATE_ROW", nullable = true, insertable = true, updatable = true)
    private LocalDate dateRow;
    
    @Column(name = "NAME_ROW", nullable = true, insertable = true, updatable = true)
    private String nameRow;
    
    @Column(name = "VALUE_ROW", nullable = true, insertable = true, updatable = true)        
    private BigDecimal value;
    
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "parent")
    private Set<ExTable> exTableRecords = new HashSet<ExTable>(0);
  
     
    public BigTable() { }
    
    public BigTable(LocalDate date, String name, BigDecimal value) { 
        this.dateRow = date;  
        this.nameRow = name; 
        this.value = value; 
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer value) {
        this.id = value;
    }
    
    public LocalDate getDateRow() {
        return this.dateRow;
    }

    public void setDateRow(LocalDate date) {
        this.dateRow = date;
    }
    
    public String getNameRow() {
        return this.nameRow;
    }

    public void setNameRow(String value) {
        this.nameRow = value;
    }
    
    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
	public Set<ExTable> getStockDailyRecords() {
		return this.exTableRecords;
	}

	public void setStockDailyRecords(Set<ExTable> exTableRecords) {
		this.exTableRecords = exTableRecords;
	}
    
    @Transient 
    private static final String [] columnName = new String[]{"ID", "DATE", "NAME_ROW", "VALUE_ROW"};
    
    @Transient 
    private static final String [] fieldsName = new String[]{"id", "dateRow", "nameRow", "value"};
    
    @Transient 
    private static final Class<?>[] columnClass = new Class[] {
        Integer.class, LocalDate.class, String.class, BigDecimal.class };
    
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
}
