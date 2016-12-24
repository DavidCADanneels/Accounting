package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountTypes extends BusinessCollection<AccountType>{
    public static final String ASSET = "Asset";
    public static final String LIABILITY = "Liability";
    public static final String COST = "Cost";
    public static final String REVENUE = "Revenue";
    public static final String DEBIT = "Debit";
    public static final String CREDIT = "Credit";
    public static final String ACCOUNT_TYPE = "AccountType";

    @Override
    public String getChildType(){
        return ACCOUNT_TYPE;
    }

    @Override
    public AccountType createNewChild(TreeMap<String, String> properties) {
        return new AccountType();
    }

    public void addDefaultTypes() {
        AccountType active = new AccountType();
        active.setName(ASSET);
//        active.setName(getBundle("Balances").getString("ASSET"));

        AccountType passive = new AccountType();
        passive.setName(LIABILITY);
//        passive.setName(getBundle("Balances").getString("LIABILITY"));
        passive.setInverted(true);

        AccountType cost = new AccountType();
        cost.setName(COST);
//        cost.setName(getBundle("Balances").getString("COST"));

        AccountType revenue = new AccountType();
        revenue.setName(REVENUE);
//        revenue.setName(getBundle("Balances").getString("REVENUE"));
        revenue.setInverted(true);

        AccountType credit = new AccountType();
        credit.setName(CREDIT);
//        credit.setName(getBundle("Balances").getString("FUND_FROM_CUSTOMER"));

        AccountType debit = new AccountType();
        debit.setName(DEBIT);
//        debit.setName(getBundle("Balances").getString("DEBT_TO_SUPPLIER"));
        debit.setInverted(true);

        try{
            addBusinessObject(active);
            addBusinessObject(passive);
            addBusinessObject(cost);
            addBusinessObject(revenue);
            addBusinessObject(credit);
            addBusinessObject(debit);
        } catch (EmptyNameException e) {
            System.err.println("The Name of an AccountType can not be empty.");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of an AccountType already exists.");
        }
    }
}