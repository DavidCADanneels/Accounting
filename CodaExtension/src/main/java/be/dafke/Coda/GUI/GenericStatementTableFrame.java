package be.dafke.Coda.GUI;

import be.dafke.Coda.Objects.Statement;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ComponentModel.RefreshableTableFrame;

public class GenericStatementTableFrame extends RefreshableTableFrame<Statement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericStatementTableFrame(SearchOptions searchOptions,
                                      Statements statements) {
		super("Statements where"+
                (searchOptions.isSearchOnCounterParty()?" [counterParty = "+searchOptions.getCounterParty()+"]":"")+
                (searchOptions.isSearchOnTransactionCode()? " [transactioncode = "+searchOptions.getTransactionCode()+"]":"")+
                (searchOptions.isSearchOnCommunication()? " [communication = "+searchOptions.getCommunication()+"]":""),
                new GenericStatementDataModel(searchOptions,statements));
		// tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
	}

    @Override
    public void selectObject(Statement statement) {

    }

    @Override
    public Statement getSelectedObject() {
        return null;
    }
}
