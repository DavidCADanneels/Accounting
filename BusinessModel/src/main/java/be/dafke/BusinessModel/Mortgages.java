package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage> {
    private AccountTypes accountTypes;
    private Accounts accounts;

    public static final String MORTGAGES = "Mortgages";
    public static final String MORTGAGE = "Mortgage";

    public Mortgages(){
        setName(MORTGAGES);
    }

    @Override
        public String getChildType(){
            return MORTGAGE;
    }

    @Override
    public Mortgage createNewChild() {
        return new Mortgage(this, accounts);
    }

//    @Override
//    public void readCollection() {
//        readCollection("Mortgage",true);
//    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public AccountTypes getAccountTypes() {
        return accountTypes;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }
}