package be.dafke.BasicAccounting.GUI.Balances;

import be.dafke.BusinessModel.Account;
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

}