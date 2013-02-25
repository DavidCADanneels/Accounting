package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.RefreshableTable;

import static java.util.ResourceBundle.getBundle;

public class YearBalance extends RefreshableTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YearBalance(Accounting accounting) {
		super(getBundle("Accounting").getString("EINDBALANS")+ " (" + accounting.toString() + ")",
				new YearBalanceDataModel(accounting));
		tabel.setAutoCreateRowSorter(true);
	}
}
