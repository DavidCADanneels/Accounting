package be.dafke.Accounting.Balances;

import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

public class RelationsBalance extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RelationsBalance(Accountings accountings) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("RELATIES-BALANS"),
				new RelationsBalanceDataModel(accountings), AccountType.Credit, AccountType.Debit, accountings);
		// tabel.setAutoCreateRowSorter(true);
	}
}
