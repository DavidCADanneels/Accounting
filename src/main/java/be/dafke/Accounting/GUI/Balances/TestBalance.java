package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableTable;

import static java.util.ResourceBundle.getBundle;

public class TestBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestBalance(Accounting accounting) {
		super(getBundle("Accounting").getString("PROEF_EN_SALDI-BALANS") + " (" + accounting.toString() + ")",
				new TestBalanceDataModel(accounting));
		tabel.setAutoCreateRowSorter(true);
	}
}
