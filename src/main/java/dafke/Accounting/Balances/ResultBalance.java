package be.dafke.Accounting.Balances;

import be.dafke.Accounting.AccountingGUIFrame;
import be.dafke.Accounting.Objects.Account.AccountType;

public class ResultBalance extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ResultBalance balance = null;

	public static ResultBalance getInstance(AccountingGUIFrame parent) {
		if (balance == null) {
			balance = new ResultBalance(parent);
		}
		parent.addChildFrame(balance);
		return balance;
	}

	private ResultBalance(AccountingGUIFrame parent) {
		super(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("RESULTATENBALANS"),
				new ResultBalanceDataModel(/*parent*/), parent, AccountType.Cost, AccountType.Revenue);
		// tabel.setAutoCreateRowSorter(true);
	}
}
