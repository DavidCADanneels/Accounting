package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ComponentModel.RefreshableTableModel;

public class RefreshableBalance extends RefreshableTable<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefreshableBalance(String title, RefreshableTableModel<Account> m) {
		super(title, m);
	}

	@Override
	public void selectObject(Account account) {

	}

	@Override
	public Account getSelectedObject() {
		return null;
	}
}