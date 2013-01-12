package be.dafke.Coda;

import be.dafke.ParentFrame;
import be.dafke.RefreshableTable;

public class GenericMovementTable extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericMovementTable(final ParentFrame parent, CounterParty counterParty, String transactionCode) {
		super("Movements for " + counterParty + "(counterparty)", new GenericMovementDataModel(counterParty,
				transactionCode), parent);
		// tabel.setAutoCreateRowSorter(true);
	}
}
