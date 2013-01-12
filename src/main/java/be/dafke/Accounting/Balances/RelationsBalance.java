package be.dafke.Accounting.Balances;

import be.dafke.Accounting.AccountingGUIFrame;
import be.dafke.Accounting.Objects.Account.AccountType;

public class RelationsBalance extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static RelationsBalance balance = null;

	public static RelationsBalance getInstance(AccountingGUIFrame parent) {
		if (balance == null) {
			balance = new RelationsBalance(parent);
		}
		parent.addChildFrame(balance);
		return balance;
	}

	private RelationsBalance(AccountingGUIFrame parent) {
		super(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("RELATIES-BALANS"),
				new RelationsBalanceDataModel(/*parent*/), parent, AccountType.Credit, AccountType.Debit);
		// tabel.setAutoCreateRowSorter(true);
	}
}
