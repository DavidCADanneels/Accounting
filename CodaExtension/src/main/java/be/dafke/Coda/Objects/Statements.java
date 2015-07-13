package be.dafke.Coda.Objects;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.Coda.GUI.SearchOptions;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;

public class Statements extends BusinessCollection<BusinessObject> implements BusinessCollectionProvider<BusinessObject>{

    public static final String STATEMENTS = "Statements";
    public static final String STATEMENT = "Statement";

    public Statements(Accounting accounting){
        setName(STATEMENTS);
        setBusinessCollection(accounting.getBusinessObject(CounterParties.COUNTERPARTIES));
        try {
            accounting.addBusinessObject(this);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(STATEMENTS);

    }

    @Override
    public String getChildType(){
        return STATEMENT;
    }

    private BusinessCollection<BusinessObject> businessCollection;

    public ArrayList<Statement> getStatements(SearchOptions searchOptions) {
		ArrayList<Statement> result = new ArrayList<Statement>();
        CounterParty counterParty = searchOptions.getCounterParty();
        String transactionCode = searchOptions.getTransactionCode();
        String communication = searchOptions.getCommunication();
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty();
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode();
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication();
		for(BusinessObject businessObject : getBusinessObjects()) {
            Statement statement = (Statement)businessObject;
			if ((!searchOnTransactionCode || transactionCode.equals(statement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(statement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == statement.getCounterParty())) {
				result.add(statement);
			}
		}
		return result;
	}

    @Override
    public Statement createNewChild() {
        return new Statement();
    }

//    @Override
//    public void readCollection() {
//        readCollection("Statement", false);
//    }

    @Override
    public BusinessCollection<BusinessObject> getBusinessCollection() {
        return businessCollection;
    }

    @Override
    public void setBusinessCollection(BusinessCollection<BusinessObject> businessCollection) {
        this.businessCollection = businessCollection;
    }
}
