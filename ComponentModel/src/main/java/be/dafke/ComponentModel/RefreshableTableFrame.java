package be.dafke.ComponentModel;

import javax.swing.*;
import java.awt.*;

/**
 * Uitbreiding op RefreshableFrame, refresh() herlaad de gegevens van de tabel
 * @author David Danneels
 * @since 01/10/2010
 * @see #refresh()
 */
public abstract class RefreshableTableFrame<BusinessObject> extends RefreshableFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected RefreshableTable<BusinessObject> tabel;
	protected JPanel contentPanel;
	protected int selectedRow;
	protected int selectedColumn;

	/**
	 * Constructor
	 * @param title titel van het RefreshableFrame
	 * @param model het TableModel van de tabel
	 * @see RefreshableFrame#RefreshableFrame(java.lang.String) RefreshableFrame(String)
	 * @see javax.swing.table.AbstractTableModel
	 */
	public RefreshableTableFrame(String title, RefreshableTableModel<BusinessObject> model) {
		super(title);
		tabel = new RefreshableTable<BusinessObject>(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		JScrollPane scrollPane = new JScrollPane(tabel);
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPanel);
		pack();
//		setVisible(true);
	}

	public void selectObject(BusinessObject object){
		int row = tabel.getModel().getRow(object);
		tabel.setRowSelectionInterval(row,row);
		tabel.scrollRectToVisible(tabel.getCellRect(row,0,false));
	}

	public BusinessObject getSelectedObject(){
		return tabel.getModel().getObject(selectedRow,selectedColumn);
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */
	@Override
	public void refresh() {
		tabel.getModel().fireTableDataChanged();
	}
}
