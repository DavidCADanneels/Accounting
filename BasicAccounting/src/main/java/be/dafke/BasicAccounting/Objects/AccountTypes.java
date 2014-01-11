package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountTypes extends BusinessTypeCollection<AccountType> {
    public static final String ASSET = "Asset";
    public static final String LIABILITY = "Liability";
    public static final String COST = "Cost";
    public static final String REVENUE = "Revenue";
    public static final String DEBIT = "Debit";
    public static final String CREDIT = "Credit";
    public static final String MORTGAGE = "Mortgage";

    @Override
    public String getChildType(){
        return "AccountType";
    }

    @Override
    public AccountType createNewChild(String name) {
        return new AccountType(name);
    }

    public AccountTypes() {
        AccountType active = new AccountType(ASSET);
//        active.setName(getBundle("Accounting").getString("ASSET"));

        AccountType passive = new AccountType(LIABILITY);
//        passive.setName(getBundle("Accounting").getString("LIABILITY"));
        passive.setInverted(true);

        AccountType cost = new AccountType(COST);
//        cost.setName(getBundle("Accounting").getString("COST"));

        AccountType revenue = new AccountType(REVENUE);
//        revenue.setName(getBundle("Accounting").getString("REVENUE"));
        revenue.setInverted(true);

        AccountType credit = new AccountType(CREDIT);
//        credit.setName(getBundle("Accounting").getString("FUND_FROM_CUSTOMER"));

        AccountType debit = new AccountType(DEBIT);
//        debit.setName(getBundle("Accounting").getString("DEBT_TO_SUPPLIER"));
        debit.setInverted(true);

        AccountType mortgage = new AccountType(MORTGAGE);
//        mortgage.setName(getBundle("Accounting").getString("MORTGAGE"));

        try{
            addBusinessObject(active);
            addBusinessObject(passive);
            addBusinessObject(cost);
            addBusinessObject(revenue);
            addBusinessObject(credit);
            addBusinessObject(debit);
            addBusinessObject(mortgage);
        } catch (EmptyNameException e) {
            System.err.println("The Name of an AccountType can not be empty.");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of an AccountType already exists.");
        }
    }
}