package be.dafke.Coda.GUI;

import be.dafke.Coda.Objects.Statement;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ComponentModel.RefreshableTable;

public class GenericStatementTable extends RefreshableTable<Statement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericStatementTable(SearchOptions searchOptions,
                                 Statements statements) {
		super("Statements where"+
                (searchOptions.isSearchOnCounterParty()?" [counterParty = "+searchOptions.getCounterParty()+"]":"")+
                (searchOptions.isSearchOnTransactionCode()? " [transactioncode = "+searchOptions.getTransactionCode()+"]":"")+
                (searchOptions.isSearchOnCommunication()? " [communication = "+searchOptions.getCommunication()+"]":""),
                new GenericStatementDataModel(searchOptions,statements));
		// tabel.setAutoCreateRowSorter(true);
	}

    @Override
    public Statement getSelectedObject() {
        return null;
    }
}
