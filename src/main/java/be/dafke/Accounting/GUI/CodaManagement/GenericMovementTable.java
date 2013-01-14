package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.CounterParty;
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
