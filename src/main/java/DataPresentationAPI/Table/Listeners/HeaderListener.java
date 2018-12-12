
package DataPresentationAPI.Table.Listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import DataPresentationAPI.Table.Models.DistributedTableModel;
import DataPresentationAPI.Table.UI.SortButtonRenderer;

/**
 * Picks up clicks on the table header and calls the sort method
 * on the JTable's DistributedTableModel.
 */
public class HeaderListener extends MouseAdapter 
{
	private JTable table;
	private JTableHeader header;
	private SortButtonRenderer renderer;
	private boolean[] columnSorts; //uses these booleans to determine whether to sort in an ascending or descending order


	public HeaderListener(JTable table, SortButtonRenderer renderer) {
		this.table = table;
		this.header = table.getTableHeader();
		
		
		this.renderer = renderer;
		
		int columnCount = this.header.getColumnModel().getColumnCount();//((DistributedTableModel) table.getModel()).getColumnCount();
		this.columnSorts = new boolean[columnCount];
	}

	public void mousePressed(MouseEvent e) {
		int sortColumn = table.getColumnModel().getColumnIndexAtX(e.getX());
		
        renderer.setPressedColumn(sortColumn);
        renderer.setSelectedColumn(sortColumn);
        header.repaint();
        
        if (header.getTable().isEditing()) {
            header.getTable().getCellEditor().stopCellEditing();
          }
        
		if (SortButtonRenderer.DOWN == renderer.getState(sortColumn)) {
			columnSorts[sortColumn] = true;
		}
		else {
			columnSorts[sortColumn] = false;
		}
		
		boolean sortOrder = columnSorts[sortColumn];
		try {
			int[] selectedRows = table.getSelectedRows();
			int[] newSelectionRows = ((DistributedTableModel) table.getModel()).sort(sortColumn, sortOrder, selectedRows);
			ListSelectionModel sm = table.getSelectionModel();
			sm.clearSelection();
			for (int i = 0; i < newSelectionRows.length; i++) {
				sm.addSelectionInterval(newSelectionRows[i], newSelectionRows[i]);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
      public void mouseReleased(MouseEvent e) 
      {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setPressedColumn(-1);                // clear
        header.repaint();
      }
}

