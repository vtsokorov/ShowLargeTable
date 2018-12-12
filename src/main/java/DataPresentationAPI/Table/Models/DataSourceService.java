
package DataPresentationAPI.Table.Models;



//DistributedTableDescription getTableDescription() throws Exception;
//Object[][] retrieveRows(int from, int to) throws Exception;
//int[] sort(int sortColumn, boolean ascending, int[] selectedRows) throws Exception;
//void setSelectedRowsAndColumns(int[] selectedRows, int[] selectedColumns) throws Exception;
//int[] getSelectedRows() throws Exception;
//int[] getSelectedColumns() throws Exception;

public class DataSourceService<DAOService extends DAOInterface<?>> implements DistributedTableDataSource 
{

	//Cached locally for efficiency
	private DistributedTableDescription tableDescription;
	
	private DAOService service;
	
	private int[] selectedRows = null;

	/**
	 * Constructor for DemoTableDataSource.
	 */
	public DataSourceService(DAOService service) throws Exception {
		super();
		this.service = service;
		readTableDescription(); //fetch the TableDescription
	}

	/**
	 * Method from <code>DistributedTableDataSource</code>
	 */
	public DistributedTableDescription getTableDescription() throws Exception {
		return tableDescription;
	}

	/**
	 * Method from <code>DistributedTableDataSource</code>
	 */
	public Object[][] retrieveRows(int from, int to) throws Exception {
		return service.getRawData(from, to);
	}


	/**
	 * Method from <code>DistributedTableDataSource</code>
	 */
	public int[] sort(int sortColumn, boolean ascending, int[] selectedRows) throws Exception { 
		service.setOrderParameters(sortColumn, !ascending);
		return selectedRows;
	}

	/**
	 * Method from <code>DistributedTableDataSource</code>
	 */
	public void setSelectedRowsAndColumns(int[] selectedRows, int[] selectedColumns) throws Exception 
	{ 
		this.selectedRows = new int[selectedRows.length];
		//System.arraycopy(selectedRows, 0, this.selectedRows, 0, selectedRows.length);
		for(int i = 0; i < selectedRows.length; ++i)
			this.selectedRows[i] = selectedRows[i];
	}



	/**
	 * Method from <code>DistributedTableDataSource</code>
	 */
	public int[] getSelectedRows() throws Exception {
		return selectedRows;
	}

	/**
	 * Method from <code>DistributedTableDataSource</code>
	 */
	public int[] getSelectedColumns() {
		int[] cols = new int[tableDescription.getColumnCount()];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = i;
		}
		return cols;
	}


	/**
	 * Reads the table description data from a url and parses
	 * them up into a TableDescription object.
	 */
	private void readTableDescription() throws Exception {
		tableDescription = new DistributedTableDescription(
				service.getColumnNames(), service.getColumnClasses(), service.count());
	}
}
