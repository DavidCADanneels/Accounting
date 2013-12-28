package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableTable;

public class GenericStatementTable extends RefreshableTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericStatementTable(SearchOptions searchOptions,
                                 Accounting accounting) {
		super("Statements where"+
                (searchOptions.isSearchOnCounterParty()?" [counterParty = "+searchOptions.getCounterParty()+"]":"")+
                (searchOptions.isSearchOnTransactionCode()? " [transactioncode = "+searchOptions.getTransactionCode()+"]":"")+
                (searchOptions.isSearchOnCommunication()? " [communication = "+searchOptions.getCommunication()+"]":""),
                new GenericStatementDataModel(searchOptions,accounting));
		// tabel.setAutoCreateRowSorter(true);
	}
}
