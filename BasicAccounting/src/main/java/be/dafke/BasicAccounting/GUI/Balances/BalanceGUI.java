package be.dafke.BasicAccounting.GUI.Balances;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Balance;

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
