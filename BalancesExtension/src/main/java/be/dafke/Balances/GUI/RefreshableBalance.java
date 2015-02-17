package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.table.AbstractTableModel;

public class RefreshableBalance extends RefreshableTable<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefreshableBalance(String title, AbstractTableModel m) {
		super(title, m);
	}

	@Override
	public Account getSelectedObject() {
		return null;
	}
}