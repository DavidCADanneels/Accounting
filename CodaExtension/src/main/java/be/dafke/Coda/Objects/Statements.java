package be.dafke.Coda.Objects;

import be.dafke.Coda.GUI.SearchOptions;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessObject;

import java.util.ArrayList;

public class Statements extends BusinessCollection<Statement> implements BusinessCollectionProvider<CounterParty>{

    @Override
    public String getChildType(){
        return "Statement";
    }

    private BusinessCollection<CounterParty> businessCollection;

    public ArrayList<Statement> getStatements(SearchOptions searchOptions) {
		ArrayList<Statement> result = new ArrayList<Statement>();
        BusinessObject counterParty = searchOptions.getCounterParty();
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

//    @Override
//    public void readCollection() {
//        readCollection("Statement", false);
//    }

    @Override
    public BusinessCollection<CounterParty> getBusinessCollection() {
        return businessCollection;
    }

    @Override
    public void setBusinessCollection(BusinessCollection<CounterParty> businessCollection) {
        this.businessCollection = businessCollection;
    }
}
