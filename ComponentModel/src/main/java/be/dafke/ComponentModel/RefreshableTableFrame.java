package be.dafke.ComponentModel;

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

	public RefreshableTableFrame(String title) {
		super(title);
	}

	public void selectObject(BusinessObject object){
		if(tabel!=null) tabel.selectObject(object);
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */
	public void refresh() {
		tabel.refresh();
	}
}
