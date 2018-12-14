package DataPresentationAPI.Table.Listeners;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import DataPresentationAPI.Table.UI.SortButtonRenderer;


public class ScrollListener implements AdjustmentListener
{
	private JTable table;
	private static boolean scrollFlag = true;
	
	public ScrollListener(JTable table) {
		this.table = table;
	}
	
	public static boolean isScrollStop() {
		return scrollFlag;
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
	   // Check if user has done dragging the scroll bar
	   scrollFlag = !e.getValueIsAdjusting() ? true : false;
	   if(scrollFlag)
		   this.table.repaint();
	}

}
