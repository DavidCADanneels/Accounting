package be.dafke;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Uitbreiding op RefreshableFrame, refresh() herlaad de gegevens van de tabel
 * @author David Danneels
 * @since 01/10/2010
 * @see #refresh()
 */
public class RefreshableTable extends RefreshableFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final AbstractTableModel model;
	protected JTable tabel;
	protected JPanel contentPanel;

	/**
	 * Constructor
	 * @param title titel van het RefreshableFrame
	 * @param m het TableModel van de tabel
	 * @see be.dafke.RefreshableFrame#RefreshableFrame(java.lang.String) RefreshableFrame(String)
	 * @see javax.swing.table.AbstractTableModel
	 */
	public RefreshableTable(String title, AbstractTableModel m) {
		super(title);
		model = m;
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		tabel.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(tabel);
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPanel);
		pack();
//		setVisible(true);
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */
	@Override
	public void refresh() {
		model.fireTableDataChanged();
	}
}
