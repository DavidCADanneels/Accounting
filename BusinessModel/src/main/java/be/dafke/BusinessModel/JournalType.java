package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.Properties;

public class JournalType extends BusinessObject implements MustBeRead {

    public static final String DEBIT_TYPES = "debitTypes";
    public static final String CREDIT_TYPES = "creditTypes";

    /**
	 *
	 */
    public JournalType(String name){
        setName(name);
        debetTypes = new ArrayList<>();
        creditTypes = new ArrayList<>();
    }

    public Properties getOutputProperties(){
        Properties properties = super.getOutputProperties();
        properties.put(DEBIT_TYPES, debetTypes.toString());
//        properties.put(DEBIT_TYPES,debetTypes.stream().map(AccountType::getName).collect(Collectors.joining(",")));
//        properties.put(CREDIT_TYPES,creditTypes.stream().map(AccountType::getName).collect(Collectors.joining(",")));
        return properties;
    }

	private ArrayList<AccountType> debetTypes, creditTypes;

    public ArrayList<AccountType> getDebetTypes() {
        return debetTypes;
    }

    public void setDebetTypes(ArrayList<AccountType> debetTypes) {
        this.debetTypes = debetTypes;
    }

    public ArrayList<AccountType> getCreditTypes() {
        return creditTypes;
    }

    public void setCreditTypes(ArrayList<AccountType> creditTypes) {
        this.creditTypes = creditTypes;
    }
}