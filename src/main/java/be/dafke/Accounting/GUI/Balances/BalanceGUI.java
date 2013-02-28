package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Balance;

public class BalanceGUI extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceGUI(Balance balance) {
		super(balance.getName() + " (" + balance.getAccounting().toString() + ")",
				new BalanceDataModel(balance));
		// tabel.setAutoCreateRowSorter(true);
	}
}
