package main;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.hibernate.HibernateException;

import DataPresentationAPI.Table.Models.DefaultDataSourceService;
import DataPresentationAPI.Table.Models.DistributedTableModel;
import DataPresentationAPI.Table.UI.SortButtonRenderer;
import DataPresentationAPI.Table.Listeners.SelectionListener;
import DataPresentationAPI.Table.Listeners.HeaderListener;
import DataPresentationAPI.Table.Listeners.ScrollListener;
import database.FbProperty;
import database.HibernateUtil;
import database.BigTableService;
import database.ExTable;
import database.ExTableService;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.Box;

public class MainWindow extends JFrame {
	private JTextField textField;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel labelStatus;


	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 594, 524);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menuDatabase = new JMenu("Database");
		menuBar.add(menuDatabase);
		JMenuItem menuItemConnect = new JMenuItem("Connect...");
		menuDatabase.add(menuItemConnect);
		
		menuItemConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connection(arg0);
			}
		});
		
		JMenu menuOperations = new JMenu("Operations");
		menuBar.add(menuOperations);
		
		JMenuItem mwnuItemDeleteAll = new JMenuItem("Delete all...");
		mwnuItemDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteAll(arg0);
			}
		});
		menuOperations.add(mwnuItemDeleteAll);
		
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel filterPanel = new JPanel();
		mainPanel.add(filterPanel);
		
		JLabel label1 = new JLabel("Filter: ");
		
		textField = new JTextField();
		textField.setColumns(10);
		GroupLayout filterPanelGLayout = new GroupLayout(filterPanel);
		filterPanelGLayout.setHorizontalGroup(
			filterPanelGLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(filterPanelGLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(label1)
					.addGap(5)
					.addComponent(textField))
		);
		filterPanelGLayout.setVerticalGroup(
			filterPanelGLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(filterPanelGLayout.createSequentialGroup()
					.addGap(8)
					.addComponent(label1))
				.addGroup(filterPanelGLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		filterPanel.setLayout(filterPanelGLayout);
		
		JPanel tablePanel = new JPanel();
		mainPanel.add(tablePanel);
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
		
		scrollPane = new JScrollPane();
		tablePanel.add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
	
		
//		scrollPane.getViewport().addMouseWheelListener(new MouseWheelListener() 
//		{
//			private int row = 5;
//			public void mouseWheelMoved(MouseWheelEvent e) 
//			{
//			   if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) 
//			   {
//				     if (table.getParent() instanceof JViewport) {
//				        row += e.getUnitsToScroll();
//					    JViewport viewport = (JViewport)table.getParent();
//					    Rectangle rect = table.getCellRect(row, 0, true);
//					    Point pt = viewport.getViewPosition();
//					    rect.setLocation(rect.x-pt.x, rect.y-pt.y);
//					    viewport.scrollRectToVisible(rect);
//				     }
//			   }
//			}
//        });

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		labelStatus = new JLabel("Row count: ");
		Component horizontalGlue = Box.createHorizontalGlue();
		statusPanel.add(labelStatus);
		statusPanel.add(horizontalGlue);
		mainPanel.add(statusPanel);
		
		
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable target = (JTable)mouseEvent.getSource();
		        int row = target.getSelectedRow();
		        if (mouseEvent.getClickCount() == 2 && row != -1) {
		        	Integer id = Integer.valueOf(table.getModel().getValueAt(row, 0).toString());
		        	ExTableService service = new ExTableService();
					try {
						ExTable selectRow = service.findById(id);
			        	String data = new String();
			        	data += String.valueOf(selectRow.getId()) + "\n";
			        	data += selectRow.getParent().getNameRow() + "\n";
			        	data += selectRow.getNameRow() + "\n";
			        	showWarningMsg(data);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

		        }
		    }
		});	
	}
	
	private void tableInit(TableModel model)
	{	
		table.setModel(model);
		table.setColumnSelectionAllowed(false);
		
	    SortButtonRenderer renderer = new SortButtonRenderer();
	    TableColumnModel columnModel = table.getColumnModel();
	    for (int i = 0; i < columnModel.getColumnCount(); i++) {
	    	columnModel.getColumn(i).setHeaderRenderer(renderer);
	    	//columnModel.getColumn(i).setPreferredWidth(columnWidth[i]);
	    }
		
		table.getTableHeader().addMouseListener(new HeaderListener(table, renderer));
		table.getSelectionModel().addListSelectionListener(new SelectionListener(table));
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new ScrollListener(table));
	}
	
	private void connection(ActionEvent arg0)
	{
        final FbProperty property = new FbProperty();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
        	private boolean flag = false;
            @Override
            protected Void doInBackground() throws Exception 
            {
                try 
                {
                    if(property.readPropertyFromFile("settings.ini"))
                    {
                      property.setUserName("SYSDBA");
                      property.setPassword("masterkey");
                      if(HibernateUtil.initConfiguration(property)) 
                              flag = true;
                    }
                } catch (HibernateException | SQLException | IOException ex) 
                {
                	flag = false;
                    showWarningMsg("Error: "+ex.getMessage()); 
                } 
                return null;
            }
            
            protected void done()
            {
                if(flag == true)
                {
                	showWarningMsg("CONNECTION OK!!!"); 
					try {
						
						DistributedTableModel m = new DistributedTableModel();
						m.setDataSource(new ExTableService(), 200, 1000);
						tableInit(m);
						labelStatus.setText("Row count: "+String.valueOf(m.getRowCount()));
					}
					catch (Exception e) {
						e.printStackTrace();
					}

                } 
            }
         };

        worker.execute();
	}
	
	private void deleteAll(ActionEvent arg0)
	{
		BigTableService service = new BigTableService();
		service.deleteAll();
        table.clearSelection();
        table.revalidate();
        table.repaint();
		
	}
	
    public static void showWarningMsg(String text)
    {
        JOptionPane optionPane = new JOptionPane(text, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
