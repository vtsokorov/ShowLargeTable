
package DataPresentationAPI.Table.Models;

import DataPresentationAPI.Table.Listeners.ScrollListener;
import database.ExTable;
import database.ExTableService;

import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class DistributedTableModel implements TableModel, Serializable 
{

	//Used to retrieve table data
	private DistributedTableDataSource tableDataSource;

	//The cache of data that has been retrieved.
	private DistributedTableClientCache tableClientCache;

	//Contains the descriptive elements of the table
	private DistributedTableDescription tableDescription;
	
	protected EventListenerList listenerList = new EventListenerList();
	
	private boolean scrollFlag = true;
	
	private boolean cellEditable = true;


	/**
	 * Constructor for CachingTableModel.
	 * @param tableDataSource The object from which data should be retrieved.
	 */
	public DistributedTableModel(DistributedTableDataSource tableDataSource) throws Exception {
		this(tableDataSource, 200, 1000);//will set the two ints to their defaults in the constructor
	}
	
	/**
	 * Constructor for CachingTableModel.
	 * @param tableDataSource The object from which data should be retrieved.
	 * @param chunkSize The number of rows that should be retrieved from the DistributedTableDataSource at one time
	 * @param maximumCacheSize The number of rows that the DistributedTableModel should hold before overwriting data that's not required.
	 */
	public DistributedTableModel(DistributedTableDataSource tableDataSource, int chunkSize, int maximumCacheSize) throws Exception {
		this.tableDataSource = tableDataSource;
		this.tableDescription = tableDataSource.getTableDescription();
		this.tableClientCache = new DistributedTableClientCache(chunkSize, maximumCacheSize, tableDataSource);
	}
	
	public DistributedTableModel(DAOInterface<?> service) throws Exception
	{
		this(new DefaultDataSourceService<DAOInterface<?>>(service), 200, 1000);
	}
	
	public DistributedTableModel(DAOInterface<?> service, int chunkSize, int maximumCacheSize) throws Exception
	{
		this(new DefaultDataSourceService<DAOInterface<?>>(service), chunkSize, maximumCacheSize);
	}
	
	public DistributedTableModel() throws Exception
	{
		this.tableDataSource  = null;
		this.tableDescription = new DistributedTableDescription(new String[] {}, new Class<?>[] {}, 0);
		this.tableClientCache = null;
	}
	
	public void setDataSource(DistributedTableDataSource tableDataSource, int chunkSize, int maximumCacheSize) throws Exception
	{
		this.tableDataSource  = tableDataSource;
		this.tableDescription = tableDataSource.getTableDescription();
		this.tableClientCache = new DistributedTableClientCache(chunkSize, maximumCacheSize, tableDataSource);
	}
	
	public void setDataSource(DAOInterface<?> service, int chunkSize, int maximumCacheSize) throws Exception
	{
		this.tableDataSource  = new DefaultDataSourceService<DAOInterface<?>>(service);
		this.setDataSource(tableDataSource, chunkSize, maximumCacheSize);
		
		fireTableRowsUpdated(0, this.tableDescription.getRowCount()-1);
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return tableDescription.getRowCount();
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return tableDescription.getColumnCount();
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		if(columnIndex < tableDescription.getColumnCount()) {
			return tableDescription.getColumnNames()[columnIndex];
		}
		else {
			return null;
		}
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex < tableDescription.getColumnCount()) {
			return tableDescription.getColumnClasses()[columnIndex];
		}
		else {
			return null;
		}
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
    {
		if(ScrollListener.isScrollStop()) {
			return tableClientCache.retrieveRowFromCache(rowIndex)[columnIndex];
		}
		else 
			return null;
	}
	
	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return cellEditable;
	}
	
	/**
	 * @see javax.swing.table.TableModel#setCellEditable(boolean f)
	 */
	public void setTableEditable(boolean f) {
		cellEditable = f;
	}

	/**
	 * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
	 */	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) 
	{
		if(ScrollListener.isScrollStop()) {
			tableClientCache.retrieveRowFromCache(rowIndex)[columnIndex] = aValue;
			
	      	Integer id = Integer.valueOf(getValueAt(rowIndex, 0).toString());
	      	this.tableDataSource.getDAOService().findById(id);
//			try {
//				ExTable selectRow = service.findById(id);
//	        	String data = new String();
//	        	data += String.valueOf(selectRow.getId()) + "\n";
//	        	data += selectRow.getParent().getNameRow() + "\n";
//	        	data += selectRow.getNameRow() + "\n";
			
			
			fireTableCellUpdated(rowIndex, columnIndex);
		}

	}
	
	/**
	 * Add a TableModelListener.
	 * @param listener The listener to add.
	 */
	public void addTableModelListener(TableModelListener listener)
    {
	    listenerList.add (TableModelListener.class, listener);
	}

	/**
	 * Removes a TableModelListener.
	 * @param listener The listener to remove.
	 */
	public void removeTableModelListener(TableModelListener listener)
	{
	    listenerList.remove (TableModelListener.class, listener);
	}

	/**
	 * Return all registered TableModelListener objects.
	 * @return Array of TableModelListener objects.
	 * @since 1.4
	 */
	public TableModelListener[] getTableModelListeners()
	{
	    return (TableModelListener[])listenerList.getListeners (TableModelListener.class);
	}

	/**
	 * Initiates a sort by calling <code>sort</code> on the DistributedTableDataSource.
	 * @param sortColumn The column to sort on.
	 * @param ascending Whether the table should be sorted in an ascending or descending order.
	 * @param selectedRows The row indexes that are currently seleted in the table.
	 * @return An array of the indexes of the selected rows in the table after the sort.
	 */	
	public int[] sort(int sortColumn, boolean ascending, int[] selectedRows) throws Exception {
		tableClientCache.sortOccurred();
		return tableDataSource.sort(sortColumn, ascending, selectedRows);	
	}

	/**
	 * Sets the rows and columns that are selected by calling <code>setSelectedRowsAndColumns</code>
	 * on the DistributedTableDataSource.
	 * @param selectedRows An array of the selected row indexes.
	 * @param selectedColumns An array of the selected column indexes.
	 */
	public void setSelectedRowsAndColumns(int[] selectedRows, int[] selectedColumns) throws Exception {
		tableDataSource.setSelectedRowsAndColumns(selectedRows, selectedColumns);
	}
	
	/**
	 * Returns an array corresponding to the row indexes that are currently 
	 * selected.
	 */
	public int[] getSelectedRows() throws Exception {
		return tableDataSource.getSelectedRows();
	}

	
	/**
	 * Returns an array corresponding to the column indexes that are currently 
	 * selected.
	 */
	public int[] getSelectedColumns() throws Exception {
		return tableDataSource.getSelectedColumns();
	}
	
	
	  /**
	   * Return the index of the given name.
	   * @param columnName The name of the column.
	   * @return The index of the column, -1 if not found.
	   */
	  public int findColumn (String columnName) {
	    int count = getColumnCount();
	    for (int index = 0; index < count; index++) {
	        String name = getColumnName (index);
	        if (name.equals (columnName))
	          return index;
	    }
	    return -1;
	  }
	
	  /**
	   * fireTableDataChanged
	   */
	  public void fireTableDataChanged()
	  {
	    fireTableChanged (new TableModelEvent(this));
	  }

	  /**
	   * fireTableStructureChanged
	   */
	  public void fireTableStructureChanged()
	  {
	    fireTableChanged (new TableModelEvent (this, TableModelEvent.HEADER_ROW));
	  }

	  /**
	   * fireTableRowsInserted
	   * @param value0 TODO
	   * @param value1 TODO
	   */
	  public void fireTableRowsInserted (int firstRow, int lastRow)
	  {
	    fireTableChanged (new TableModelEvent (this, firstRow, lastRow,
	                                           TableModelEvent.ALL_COLUMNS,
	                                           TableModelEvent.INSERT));
	  }

	  /**
	   * fireTableRowsUpdated
	   * @param value0 TODO
	   * @param value1 TODO
	   */
	  public void fireTableRowsUpdated (int firstRow, int lastRow)
	  {
	    fireTableChanged (new TableModelEvent (this, firstRow, lastRow,
	                                           TableModelEvent.ALL_COLUMNS,
	                                           TableModelEvent.UPDATE));
	  }

	  /**
	   * fireTableRowsDeleted
	   * @param value0 TODO
	   * @param value1 TODO
	   */
	  public void fireTableRowsDeleted(int firstRow, int lastRow)
	  {
	    fireTableChanged (new TableModelEvent (this, firstRow, lastRow,
	                                           TableModelEvent.ALL_COLUMNS,
	                                           TableModelEvent.DELETE));
	  }

	  /**
	   * fireTableCellUpdated
	   * @param value0 TODO
	   * @param value1 TODO
	   */
	  public void fireTableCellUpdated (int row, int column)
	  {
	    fireTableChanged (new TableModelEvent(this, row, row, column));
	  }

	  /**
	   * fireTableChanged
	   * @param value0 TODO
	   */
	  public void fireTableChanged(TableModelEvent event)
	  {
	    int	index;
	    TableModelListener listener;
	    Object[] list = listenerList.getListenerList();
	 
	    for (index = 0; index < list.length; index += 2)
	      {
	        listener = (TableModelListener) list [index + 1];
	        listener.tableChanged (event);
	      }
	  }

	  /**
	   * getListeners
	   * @param value0 TODO
	   * @return EventListener[]
	   */
	  public EventListener[] getListeners (Class listenerType)
	  {
	    return listenerList.getListeners (listenerType);
	  }

}
