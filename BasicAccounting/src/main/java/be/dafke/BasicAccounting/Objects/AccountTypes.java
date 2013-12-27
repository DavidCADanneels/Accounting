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
    public static final String ACTIVE = "Active";
    public static final String PASSIVE = "Passive";
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
        AccountType active = new AccountType(ACTIVE);
//        active.setName(getBundle("Accounting").getString("ACTIEF"));

        AccountType passive = new AccountType(PASSIVE);
//        passive.setName(getBundle("Accounting").getString("PASSIEF"));
        passive.setInverted(true);

        AccountType cost = new AccountType(COST);
//        cost.setName(getBundle("Accounting").getString("KOST"));

        AccountType revenue = new AccountType(REVENUE);
//        revenue.setName(getBundle("Accounting").getString("OPBRENGST"));
        revenue.setInverted(true);

        AccountType credit = new AccountType(CREDIT);
//        credit.setName(getBundle("Accounting").getString("TEGOED_VAN_KLANT"));

        AccountType debit = new AccountType(DEBIT);
//        debit.setName(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER"));
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