package be.dafke.Accounting.Balances;

import be.dafke.RefreshableTable;
import be.dafke.Accounting.AccountingGUIFrame;

public class TestBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TestBalance balance = null;

	public static TestBalance getInstance(AccountingGUIFrame parent) {
		if (balance == null) {
			balance = new TestBalance(parent);
		}
		parent.addChildFrame(balance);
		return balance;
	}

	private TestBalance(AccountingGUIFrame parent) {
		super(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("PROEF_EN_SALDI-BALANS"),
				new TestBalanceDataModel(/*parent*/), parent);
		tabel.setAutoCreateRowSorter(true);
	}
}
