package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;

import static java.util.ResourceBundle.getBundle;

public class RelationsBalance extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RelationsBalance(Accounting accounting) {
		super(getBundle("Accounting").getString("RELATIES-BALANS") + " (" + accounting.toString() + ")",
				new RelationsBalanceDataModel(accounting), AccountType.Credit, AccountType.Debit, accounting);
		// tabel.setAutoCreateRowSorter(true);
	}
}
