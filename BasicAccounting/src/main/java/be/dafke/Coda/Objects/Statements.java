package be.dafke.Coda.Objects;

import be.dafke.Coda.GUI.SearchOptions;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.WriteableBusinessCollection;

import java.io.File;
import java.util.ArrayList;

public class Statements extends WriteableBusinessCollection<Statement> implements BusinessCollectionProvider<CounterParty>{

    private WriteableBusinessCollection<CounterParty> businessCollection;

    public Statements(File xmlFolder) {
        super(xmlFolder);
    }

    public ArrayList<Statement> getStatements(SearchOptions searchOptions) {
		ArrayList<Statement> result = new ArrayList<Statement>();
        CounterParty counterParty = searchOptions.getCounterParty();
        String transactionCode = searchOptions.getTransactionCode();
        String communication = searchOptions.getCommunication();
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty();
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode();
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication();
		for(Statement statement : getBusinessObjects()) {
			if ((!searchOnTransactionCode || transactionCode.equals(statement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(statement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == statement.getCounterParty())) {
				result.add(statement);
			}
		}
		return result;
	}

    @Override
    public Statement createNewChild(String name) {
        return new Statement();
    }

    @Override
    public void readCollection() {
        readCollection("Statement", false);
    }

    @Override
    public WriteableBusinessCollection<CounterParty> getBusinessCollection() {
        return businessCollection;
    }

    @Override
    public void setBusinessCollection(WriteableBusinessCollection<CounterParty> businessCollection) {
        this.businessCollection = businessCollection;
    }
}
