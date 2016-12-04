package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Statements extends BusinessCollection<BusinessObject> {

    public static final String STATEMENTS = "Statements";
    public static final String STATEMENT = "Statement";

    public BusinessCollection<BusinessObject> getCounterParties() {
        return counterParties;
    }

    private BusinessCollection<BusinessObject> counterParties;

    public Statements(Accounting accounting){
        setName(STATEMENTS);
        counterParties = accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
        try {
            accounting.addBusinessObject(this);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public String getChildType(){
        return STATEMENT;
    }

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
    public Statement createNewChild(TreeMap<String, String> properties) {
        Statement statement = new Statement();
        statement.setName(properties.get(NAME));
        statement.setDate(Utils.toCalendar(properties.get(Statement.DATE)));
        statement.setAmount(Utils.parseBigDecimal(properties.get(Statement.AMOUNT)));
        statement.setCommunication(properties.get(Statement.COMMUNICATION));
        statement.setTransactionCode(properties.get(Statement.TRANSACTIONCODE));
        String sign = properties.get(Statement.SIGN);
        statement.setDebit("D".equals(sign));
        String counterPartyString = properties.get(Statement.COUNTERPARTY);
        if(counterPartyString!=null && !counterPartyString.equals("")){
            statement.setCounterParty((CounterParty)getCounterParties().getBusinessObject(counterPartyString));
        }

        return statement;
    }

    // KeySet and Properties
    //
    // Keys found in the CollectionFile e.g. Account.NAME in Accounts.xml file
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(Statement.DATE);
        keySet.add(Statement.SIGN);
        keySet.add(Statement.AMOUNT);
        keySet.add(Statement.COUNTERPARTY);
        keySet.add(Statement.TRANSACTIONCODE);
        keySet.add(Statement.COMMUNICATION);
        return keySet;
    }
    //

}
