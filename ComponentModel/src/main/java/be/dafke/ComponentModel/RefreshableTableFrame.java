package be.dafke.ComponentModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

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

	/**
	 * Constructor
	 * @param title titel van het RefreshableFrame
	 * @param model het TableModel van de tabel
	 * @see RefreshableFrame#RefreshableFrame(java.lang.String) RefreshableFrame(String)
	 * @see javax.swing.table.AbstractTableModel
	 */
	public RefreshableTableFrame(String title, RefreshableTableModel<BusinessObject> model) {
	    this(title,model,new Dimension(500, 200));
    }
	public RefreshableTableFrame(String title, RefreshableTableModel<BusinessObject> model, Dimension dimension) {
	    this(title, model, dimension, null);
    }
	public RefreshableTableFrame(String title, RefreshableTableModel<BusinessObject> model, Dimension dimension, RowSorter<TableModel> rowSorter) {
		super(title);
		tabel = new RefreshableTable<>(model);
		tabel.setPreferredScrollableViewportSize(dimension);
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(rowSorter);

		JScrollPane scrollPane = new JScrollPane(tabel);
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPanel);
		pack();
//		setVisible(true);
	}

	public void selectObject(BusinessObject object){
		tabel.selectObject(object);
	}

	public BusinessObject getSelectedObject(){
		return tabel.getSelectedObject();
	}

	public ArrayList<BusinessObject> getSelectedObjects(){
		return tabel.getSelectedObjects();
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */
	public void refresh() {
		tabel.refresh();
	}
}
