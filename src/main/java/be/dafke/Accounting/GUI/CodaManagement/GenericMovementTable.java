package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.RefreshableTable;

public class GenericMovementTable extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericMovementTable(SearchOptions searchOptions,
			Accountings accountings) {
		super("Movements for counterparty", new GenericMovementDataModel(searchOptions,
				accountings));
		// tabel.setAutoCreateRowSorter(true);
	}
}
