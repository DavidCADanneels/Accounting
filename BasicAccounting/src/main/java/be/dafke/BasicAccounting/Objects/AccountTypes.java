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

    public AccountTypes() {
        AccountType active = new AccountType();
//        active.setName(getBundle("Accounting").getString("ACTIEF"));
        active.setName(ACTIVE);

        AccountType passive = new AccountType();
//        passive.setName(getBundle("Accounting").getString("PASSIEF"));
        passive.setName(PASSIVE);
        passive.setInverted(true);

        AccountType cost = new AccountType();
//        cost.setName(getBundle("Accounting").getString("KOST"));
        cost.setName(COST);

        AccountType revenue = new AccountType();
//        revenue.setName(getBundle("Accounting").getString("OPBRENGST"));
        revenue.setName(REVENUE);
        revenue.setInverted(true);

        AccountType credit = new AccountType();
//        credit.setName(getBundle("Accounting").getString("TEGOED_VAN_KLANT"));
        credit.setName(CREDIT);

        AccountType debit = new AccountType();
//        debit.setName(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER"));
        debit.setName(DEBIT);
        debit.setInverted(true);

        AccountType mortgage = new AccountType();
//        mortgage.setName(getBundle("Accounting").getString("MORTGAGE"));
        mortgage.setName(MORTGAGE);

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