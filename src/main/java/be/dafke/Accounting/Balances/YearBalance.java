package be.dafke.Accounting.Balances;

import be.dafke.Accounting.Objects.Accountings;
import be.dafke.RefreshableTable;

public class YearBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YearBalance(Accountings accountings) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("EINDBALANS"),
				new YearBalanceDataModel(accountings));
		tabel.setAutoCreateRowSorter(true);
	}
}
