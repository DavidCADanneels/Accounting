package be.dafke.BasicAccounting.GUI.Coda;

import be.dafke.BasicAccounting.Objects.Statement;
import be.dafke.BasicAccounting.Objects.Statements;
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
