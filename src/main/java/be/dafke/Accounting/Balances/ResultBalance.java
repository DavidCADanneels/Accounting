package be.dafke.Accounting.Balances;

import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

public class ResultBalance extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultBalance(Accountings accountings) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("RESULTATENBALANS"),
				new ResultBalanceDataModel(accountings), AccountType.Cost, AccountType.Revenue, accountings);
		// tabel.setAutoCreateRowSorter(true);
	}
}
