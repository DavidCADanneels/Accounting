package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends BusinessCollection<Balance> {

    public static final String BALANCES = "Balances";
    public static final String BALANCE = "Balance";

    public final static String LEFTNAME = "LeftName";
    public final static String RIGHTNAME = "RightName";
    public final static String LEFTTOTALNAME = "LeftTotalName";
    public final static String RIGHTTOTALNAME = "RightTotalName";
    public final static String LEFTRESULTNAME = "LeftResultName";
    public final static String RIGHTRESULTNAME = "RightResultName";
    public final static String LEFTTYPES = "LeftTypes";
    public final static String RIGHTTYPES = "RightTypes";

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";

    private Accounting accounting;

    public Balances(Accounting accounting) {
        this.accounting = accounting;
        setName(BALANCES);
        addDefaultBalances(accounting.getAccountTypes());
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(LEFTNAME);
        keySet.add(RIGHTNAME);
        keySet.add(LEFTTOTALNAME);
        keySet.add(RIGHTTOTALNAME);
        keySet.add(LEFTRESULTNAME);
        keySet.add(RIGHTRESULTNAME);
        keySet.add(LEFTTYPES);
        keySet.add(RIGHTTYPES);
        return keySet;
    }

    @Override
    public String getChildType() {
        return BALANCE;
    }

    public void addDefaultBalances(AccountTypes accountTypes) {
        ArrayList<AccountType> costs = new ArrayList<AccountType>();
        ArrayList<AccountType> revenues = new ArrayList<AccountType>();
        ArrayList<AccountType> credit = new ArrayList<AccountType>();
        ArrayList<AccountType> debit = new ArrayList<AccountType>();
        ArrayList<AccountType> active = new ArrayList<AccountType>();
        ArrayList<AccountType> passive = new ArrayList<AccountType>();

        // TODO: define AccountType.Cost etc (the default types)
        costs.add(accountTypes.getBusinessObject(AccountTypes.COST));
        revenues.add(accountTypes.getBusinessObject(AccountTypes.REVENUE));
        credit.add(accountTypes.getBusinessObject(AccountTypes.CREDIT));
        debit.add(accountTypes.getBusinessObject(AccountTypes.DEBIT));
        active.add(accountTypes.getBusinessObject(AccountTypes.ASSET));
        active.add(accountTypes.getBusinessObject(AccountTypes.CREDIT));
        passive.add(accountTypes.getBusinessObject(AccountTypes.LIABILITY));
        passive.add(accountTypes.getBusinessObject(AccountTypes.DEBIT));

        Balance resultBalance = new Balance(accounting);
        resultBalance.setName(RESULT_BALANCE);
        resultBalance.setLeftName(getBundle("Accounting").getString("COSTS"));
        resultBalance.setRightName(getBundle("Accounting").getString("REVENUES"));
        resultBalance.setLeftTotalName(getBundle("Balances").getString("COSTS_TOTAL"));
        resultBalance.setRightTotalName(getBundle("Balances").getString("REVENUE_TOTAL"));
        resultBalance.setLeftResultName(getBundle("Balances").getString("LOSS"));
        resultBalance.setRightResultName(getBundle("Balances").getString("GAIN"));
        resultBalance.setLeftTypes(costs);
        resultBalance.setRightTypes(revenues);

        Balance relationsBalance = new Balance(accounting);
        relationsBalance.setName(RELATIONS_BALANCE);
        relationsBalance.setLeftName(getBundle("Accounting").getString("FUNDS_FROM_CUSTOMERS"));
        relationsBalance.setRightName(getBundle("Accounting").getString("DEBTS_TO_SUPPLIERS"));
        relationsBalance.setLeftTotalName(getBundle("Balances").getString("FUNDS_TOTAL"));
        relationsBalance.setRightTotalName(getBundle("Balances").getString("DEBTS_TOTAL"));
        relationsBalance.setLeftResultName(getBundle("Balances").getString("FUND_REMAINING"));
        relationsBalance.setRightResultName(getBundle("Balances").getString("DEBT_REMAINING"));
        relationsBalance.setLeftTypes(credit);
        relationsBalance.setRightTypes(debit);

        Balance yearBalance = new Balance(accounting);
        yearBalance.setName(YEAR_BALANCE);
        yearBalance.setLeftName(getBundle("Accounting").getString("ASSETS"));
        yearBalance.setRightName(getBundle("Accounting").getString("LIABILITIES"));
        yearBalance.setLeftTotalName(getBundle("Balances").getString("ASSETS_FUNDS_TOTAL"));
        yearBalance.setRightTotalName(getBundle("Balances").getString("LIABILITIES_DEBTS_TOTAL"));
        yearBalance.setLeftResultName(getBundle("Balances").getString("GAIN"));
        yearBalance.setRightResultName(getBundle("Balances").getString("LOSS"));
        yearBalance.setLeftTypes(active);
        yearBalance.setRightTypes(passive);

        try {
            addBusinessObject(resultBalance);
            addBusinessObject(relationsBalance);
            addBusinessObject(yearBalance);
        } catch (EmptyNameException e) {
            System.err.println("The Name of a Balance can not be empty.");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of a Balance already exists. ");
        }
    }

    @Override
    public Balance createNewChild(TreeMap<String, String> properties) {
        Balance balance = new Balance(accounting);
        balance.setName(properties.get(NAME));
        balance.setLeftName(properties.get(Balances.LEFTNAME));
        balance.setRightName(properties.get(Balances.RIGHTNAME));
        balance.setLeftTotalName(properties.get(Balances.LEFTTOTALNAME));
        balance.setRightTotalName(properties.get(Balances.RIGHTTOTALNAME));
        balance.setLeftResultName(properties.get(Balances.LEFTRESULTNAME));
        balance.setRightResultName(properties.get(Balances.RIGHTRESULTNAME));

        ArrayList<String> leftTypesString = Utils.parseStringList(properties.get(Balances.LEFTTYPES));
        ArrayList<AccountType> leftTypes = new ArrayList<AccountType>();
        for(String s: leftTypesString){
            leftTypes.add(accounting.getAccountTypes().getBusinessObject(s));
        }
        balance.setLeftTypes(leftTypes);

        ArrayList<String> rightTypesString = Utils.parseStringList(properties.get(Balances.RIGHTTYPES));
        ArrayList<AccountType> rightTypes = new ArrayList<AccountType>();
        for(String s: rightTypesString){
            rightTypes.add(accounting.getAccountTypes().getBusinessObject(s));
        }
        balance.setRightTypes(rightTypes);

        return balance;
    }

    @Override
    public Balance addBusinessObject(Balance value) throws EmptyNameException, DuplicateNameException {
        try {
            return addBusinessObject(value, value.getUniqueProperties());
        } catch (DuplicateNameException ex) {
            String name = value.getName();
            if (YEAR_BALANCE.equals(name) || RESULT_BALANCE.equals(name) || RELATIONS_BALANCE.equals(name)) {
                System.err.println("Default Balance (" + name + ") already exists!");
                return getBusinessObject(name);
            } else {
                throw ex;
            }
        }
    }
    /*@Override
    public void readCollection() {
        readCollection("Balance", false);
    }*/
}
