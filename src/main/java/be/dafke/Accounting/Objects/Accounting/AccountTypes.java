package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 6:25
 */
public class AccountTypes extends WriteableBusinessCollection<AccountType> {

    public AccountTypes() {
        AccountType active = new AccountType();
//        active.setName(getBundle("Accounting").getString("ACTIEF"));
        active.setName("Active");

        AccountType passive = new AccountType();
//        passive.setName(getBundle("Accounting").getString("PASSIEF"));
        passive.setName("Passive");
        passive.setInverted(true);

        AccountType cost = new AccountType();
//        cost.setName(getBundle("Accounting").getString("KOST"));
        cost.setName("Cost");

        AccountType revenue = new AccountType();
//        revenue.setName(getBundle("Accounting").getString("OPBRENGST"));
        revenue.setName("Revenue");
        revenue.setInverted(true);

        AccountType credit = new AccountType();
//        credit.setName(getBundle("Accounting").getString("TEGOED_VAN_KLANT"));
        credit.setName("Credit");

        AccountType debit = new AccountType();
//        debit.setName(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER"));
        debit.setName("Debit");
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