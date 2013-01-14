package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableTable;

public class TestBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestBalance(Accountings accountings) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("PROEF_EN_SALDI-BALANS"),
				new TestBalanceDataModel(accountings));
		tabel.setAutoCreateRowSorter(true);
	}
}
