package be.dafke;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

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
	private final AbstractTableModel model;
	protected JTable tabel;

	/**
	 * Constructor
	 * @param title titel van het RefreshableFrame
	 * @param m het TableModel van de tabel
	 * @see be.dafke.RefreshableFrame#RefreshableFrame(java.lang.String) RefreshableFrame(String)
	 * @see javax.swing.table.AbstractTableModel
	 */
	public RefreshableTable(String title, AbstractTableModel m, ParentFrame parent) {
		super(title, parent);
		model = m;
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(panel);
		pack();
		setVisible(true);
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
