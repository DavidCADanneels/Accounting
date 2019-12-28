package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.BusinessObject

class Statements extends BusinessCollection<BusinessObject> {

    static final String STATEMENTS = "Statements"
    static final String STATEMENT = "Statement"

    BusinessCollection<BusinessObject> getCounterParties() {
        counterParties
    }

    BusinessCollection<BusinessObject> counterParties

    Statements(Accounting accounting){
        setName(STATEMENTS)
        counterParties = accounting.counterParties
    }

    ArrayList<Statement> getStatements(SearchOptions searchOptions) {
        ArrayList<Statement> result = new ArrayList()
        CounterParty counterParty = searchOptions.getCounterParty()
        String transactionCode = searchOptions.getTransactionCode()
        String communication = searchOptions.getCommunication()
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty()
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode()
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication()
        for(BusinessObject businessObject : getBusinessObjects()) {
            Statement statement = (Statement)businessObject
            if ((!searchOnTransactionCode || transactionCode.equals(statement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(statement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == statement.getCounterParty())) {
                result.add(statement)
            }
        }
        result
    }

    Statement createNewChild(TreeMap<String, String> properties) {
        Statement statement = new Statement()
        statement.setName(properties.get(NAME))
        statement.setDate(Utils.toCalendar(properties.get(Statement.DATE)))
        statement.setAmount(Utils.parseBigDecimal(properties.get(Statement.AMOUNT)))
        statement.setCommunication(properties.get(Statement.COMMUNICATION))
        statement.setTransactionCode(properties.get(Statement.TRANSACTIONCODE))
        String sign = properties.get(Statement.SIGN)
        statement.setDebit("D".equals(sign))
        String counterPartyString = properties.get(Statement.COUNTERPARTY)
        if(counterPartyString && !counterPartyString.equals("")){
            statement.setCounterParty((CounterParty)getCounterParties().getBusinessObject(counterPartyString))
        }

        statement
    }

    // KeySet and Properties
    //
    // Keys found in the CollectionFile e.g. Account.NAME in Accounts.xml file
    Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>()
        keySet.add(NAME)
        keySet.add(Statement.DATE)
        keySet.add(Statement.SIGN)
        keySet.add(Statement.AMOUNT)
        keySet.add(Statement.COUNTERPARTY)
        keySet.add(Statement.TRANSACTIONCODE)
        keySet.add(Statement.COMMUNICATION)
        keySet
    }
    //

}
