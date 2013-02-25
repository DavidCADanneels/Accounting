package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.RefreshableTable;

public class GenericMovementTable extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericMovementTable(SearchOptions searchOptions,
			Accounting accounting) {
		super("Movements where"+
                (searchOptions.isSearchOnCounterParty()?" [counterParty = "+searchOptions.getCounterParty()+"]":"")+
                (searchOptions.isSearchOnTransactionCode()? " [transactioncode = "+searchOptions.getTransactionCode()+"]":"")+
                (searchOptions.isSearchOnCommunication()? " [communication = "+searchOptions.getCommunication()+"]":""),
                new GenericMovementDataModel(searchOptions,accounting));
		// tabel.setAutoCreateRowSorter(true);
	}
}
