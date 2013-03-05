package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends BusinessCollection<Balance>{

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";

    private HashMap<String, Balance> balances = new HashMap<String,Balance>();

    public Balances(){
        super("Balances");
    }

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
        balances.put(RESULT_BALANCE, new Balance(RESULT_BALANCE,
                getBundle("Accounting").getString("KOSTEN"), getBundle("Accounting").getString("OPBRENGSTEN"),
                getBundle("Accounting").getString("TOTAAL_KOSTEN"), getBundle("Accounting").getString("TOTAAL_OPBRENGSTEN"),
                getBundle("Accounting").getString("VERLIES"), getBundle("Accounting").getString("WINST"),
                costs, revenues,
                accounting));
        balances.put(RELATIONS_BALANCE, new Balance(RELATIONS_BALANCE,
                getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"), getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS"),
                getBundle("Accounting").getString("TOTAAL_TEGOEDEN"), getBundle("Accounting").getString("TOTAAL_SCHULDEN"),
                getBundle("Accounting").getString("RESTEREND_TEGOED"), getBundle("Accounting").getString("RESTERENDE_SCHULD"),
                credit, debit,
                accounting));
        balances.put(YEAR_BALANCE, new Balance(YEAR_BALANCE,
                getBundle("Accounting").getString("ACTIVA"), getBundle("Accounting").getString("PASSIVA"),
                getBundle("Accounting").getString("TOTAAL_ACTIVA_TEGOEDEN"), getBundle("Accounting").getString("TOTAAL_PASSIVA_SCHULDEN"),
                getBundle("Accounting").getString("WINST"), getBundle("Accounting").getString("VERLIES"),
                active, passive,
                accounting));
    }

    @Override
    public Balance getBusinessObject(String name) {
        return balances.get(name);
    }

    @Override
    public ArrayList<Balance> getBusinessObjects() {
        ArrayList<Balance> list = new ArrayList<Balance>();
        for(Balance balance:balances.values()){
            list.add(balance);
        }
        return list;
    }
}
