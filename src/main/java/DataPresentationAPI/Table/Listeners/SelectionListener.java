
package DataPresentationAPI.Table.Listeners;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import DataPresentationAPI.Table.Models.DistributedTableModel;

public class SelectionListener implements ListSelectionListener 
{ 

	private List<Integer> indexes;
	private DistributedTableModel model;

	public SelectionListener(JTable table)
	{
		indexes  = new ArrayList<Integer>();
		this.model = (DistributedTableModel) table.getModel();
	}
	

	public void valueChanged(ListSelectionEvent e) 
	{
    	  ListSelectionModel lsm = (ListSelectionModel) e.getSource();
	      if (!lsm.isSelectionEmpty() && !lsm.getValueIsAdjusting()) 
	      {
	        int minIndex = lsm.getMinSelectionIndex();
	        int maxIndex = lsm.getMaxSelectionIndex();
	        for (int i = minIndex, j = 0; i <= maxIndex; i++, j++) {
	          if (lsm.isSelectedIndex(i)) {
	        	  indexes.add(i);
	          }
	        }
	      }
	      else  
	    	  indexes.clear();
	      
	      try {
		    	int arr[] =  indexes.stream().mapToInt(i -> i).toArray();
				this.model.setSelectedRowsAndColumns(arr, null);
		  } catch (Exception ex) {ex.printStackTrace(); }
    }
  }


/*

class SharedListSelectionHandler implements ListSelectionListener {
  public void valueChanged(ListSelectionEvent e) {
  	 
  
  	  ListSelectionModel lsm = (ListSelectionModel) e.getSource();

	      int firstIndex = e.getFirstIndex();
	      int lastIndex = e.getLastIndex();
	      boolean isAdjusting = e.getValueIsAdjusting();
	      output.append("Event for indexes " + firstIndex + " - " + lastIndex
	          + "; isAdjusting is " + isAdjusting + "; selected indexes:");
	
	      if (lsm.isSelectionEmpty()) {
	        output.append(" <none>");
	      } else {
	        // Find out which indexes are selected.
	        int minIndex = lsm.getMinSelectionIndex();
	        int maxIndex = lsm.getMaxSelectionIndex();
	        for (int i = minIndex; i <= maxIndex; i++) {
	          if (lsm.isSelectedIndex(i)) {
	            output.append(" " + i);
	          }
	        }
	      }
	      output.append(newline);
	      output.setCaretPosition(output.getDocument().getLength());
  }
}

*/