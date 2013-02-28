package be.dafke.Accounting.GUI.Balances;

import be.dafke.RefreshableTable;

import javax.swing.table.AbstractTableModel;

public class RefreshableBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefreshableBalance(String title, AbstractTableModel m) {
		super(title, m);
	}
}