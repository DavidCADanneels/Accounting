package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Accountings;
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
