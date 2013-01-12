package be.dafke.Accounting.Balances;

import be.dafke.Accounting.AccountingGUIFrame;
import be.dafke.RefreshableTable;

public class YearBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static YearBalance balance = null;

	public static YearBalance getInstance(AccountingGUIFrame parent) {
		if (balance == null) {
			balance = new YearBalance(parent);
		}
		parent.addChildFrame(balance);
		return balance;
	}

	private YearBalance(AccountingGUIFrame parent) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("EINDBALANS"),
				new YearBalanceDataModel(/*parent*/), parent);
		tabel.setAutoCreateRowSorter(true);
	}
}
