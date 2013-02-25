package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;

import static java.util.ResourceBundle.getBundle;

public class ResultBalance extends RefreshableBalance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultBalance(Accounting accounting) {
		super(getBundle("Accounting").getString("RESULTATENBALANS") + " (" + accounting.toString() + ")",
				new ResultBalanceDataModel(accounting), AccountType.Cost, AccountType.Revenue, accounting);
		// tabel.setAutoCreateRowSorter(true);
	}
}
