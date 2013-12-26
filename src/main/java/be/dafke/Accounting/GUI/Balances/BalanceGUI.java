package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Balance;

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
