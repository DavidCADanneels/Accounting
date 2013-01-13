package be.dafke.Coda;

import be.dafke.Accounting.Objects.Accountings;
import be.dafke.RefreshableTable;

public class GenericMovementTable extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericMovementTable(CounterParty counterParty, String transactionCode, boolean allowNull,
			Accountings accountings) {
		super("Movements for counterparty", new GenericMovementDataModel(counterParty, transactionCode, allowNull,
				accountings));
		// tabel.setAutoCreateRowSorter(true);
	}
}
