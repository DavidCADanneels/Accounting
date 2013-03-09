package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;

public class JournalType extends WriteableBusinessObject {
	/**
	 * 
	 */
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