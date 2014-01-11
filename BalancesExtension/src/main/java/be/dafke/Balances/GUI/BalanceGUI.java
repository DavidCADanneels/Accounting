package be.dafke.Balances.GUI;

import be.dafke.Balances.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Accounting;

public class BalanceGUI extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BalanceGUI(Accounting accounting, Balance balance) {
		super(balance.getName() + " (" + accounting.toString() + ")",
				new BalanceDataModel(balance));
		// tabel.setAutoCreateRowSorter(true);
	}
}
