package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.ComponentModel.RefreshableTableFrame;
import be.dafke.ComponentModel.RefreshableTableModel;

public class RefreshableBalanceFrame extends RefreshableTableFrame<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefreshableBalanceFrame(String title, RefreshableTableModel<Account> m) {
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