package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;

import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends WriteableBusinessCollection<Balance> {

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";

    public void addDefaultBalances(Accounting accounting){
        ArrayList<Account.AccountType> costs = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> revenues = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> credit = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> debit = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> active = new ArrayList<Account.AccountType>();
        ArrayList<Account.AccountType> passive = new ArrayList<Account.AccountType>();
        costs.add(Account.AccountType.Cost);
        revenues.add(Account.AccountType.Revenue);
        credit.add(Account.AccountType.Credit);
        debit.add(Account.AccountType.Debit);
        active.add(Account.AccountType.Active);
        active.add(Account.AccountType.Credit);
        passive.add(Account.AccountType.Passive);
        passive.add(Account.AccountType.Debit);
        
        Balance resultBalance = new Balance();
        resultBalance.setName(RESULT_BALANCE);
        resultBalance.setLeftName(getBundle("Accounting").getString("KOSTEN"));
        resultBalance.setRightName(getBundle("Accounting").getString("OPBRENGSTEN"));
        resultBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_KOSTEN"));
        resultBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_OPBRENGSTEN"));
        resultBalance.setLeftResultName(getBundle("Accounting").getString("VERLIES"));
        resultBalance.setRightResultName(getBundle("Accounting").getString("WINST"));
        resultBalance.setLeftTypes(costs);
        resultBalance.setRightTypes(revenues);
        resultBalance.setAccounting(accounting);

        Balance relationsBalance = new Balance();
        relationsBalance.setName(RELATIONS_BALANCE);
        relationsBalance.setLeftName(getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"));
        relationsBalance.setRightName(getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS"));
        relationsBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_TEGOEDEN"));
        relationsBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_SCHULDEN"));
        relationsBalance.setLeftResultName(getBundle("Accounting").getString("RESTEREND_TEGOED"));
        relationsBalance.setRightResultName(getBundle("Accounting").getString("RESTERENDE_SCHULD"));
        relationsBalance.setLeftTypes(credit);
        relationsBalance.setRightTypes(debit);
        relationsBalance.setAccounting(accounting);
        
        Balance yearBalance = new Balance();
        yearBalance.setName(YEAR_BALANCE);
        yearBalance.setLeftName(getBundle("Accounting").getString("ACTIVA"));
        yearBalance.setRightName(getBundle("Accounting").getString("PASSIVA"));
        yearBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_ACTIVA_TEGOEDEN"));
        yearBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_PASSIVA_SCHULDEN"));
        yearBalance.setLeftResultName(getBundle("Accounting").getString("WINST"));
        yearBalance.setRightResultName(getBundle("Accounting").getString("VERLIES"));
        yearBalance.setLeftTypes(active);
        yearBalance.setRightTypes(passive);
        yearBalance.setAccounting(accounting);

        try {
            addBusinessObject(resultBalance);
            addBusinessObject(relationsBalance);
            addBusinessObject(yearBalance);
        } catch (EmptyNameException e) {
            System.err.println("The Name of a Balance can not be empty.");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of a Balance already exists.");
        }
    }
}
