package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends BusinessCollection<Balance> implements ChildrenNeedSeparateFile {

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";

    private final Accounts accounts;
    private final AccountTypes accountTypes;

    public Balances(Accounts accounts, AccountTypes accountTypes) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
//        addDefaultBalances();
    }

    public void addDefaultBalances() {
        Balance resultBalance = createResultBalance(accounts);
        Balance relationsBalance = createRelationsBalance(accounts);
        Balance yearBalance = createClosingBalance(accounts);

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

    public Balance createResultBalance(Accounts accounts){
        ArrayList<AccountType> costs = new ArrayList<>();
        ArrayList<AccountType> revenues = new ArrayList<>();
        costs.add(accountTypes.getBusinessObject(AccountTypes.COST));
        revenues.add(accountTypes.getBusinessObject(AccountTypes.REVENUE));
        return createResultBalance(accounts, costs, revenues);

    }
    public Balance createClosingBalance(Accounts accounts){
        ArrayList<AccountType> active = new ArrayList<>();
        ArrayList<AccountType> passive = new ArrayList<>();
        active.add(accountTypes.getBusinessObject(AccountTypes.ASSET));
        active.add(accountTypes.getBusinessObject(AccountTypes.CREDIT));
        active.add(accountTypes.getBusinessObject(AccountTypes.TAXCREDIT));
        passive.add(accountTypes.getBusinessObject(AccountTypes.LIABILITY));
        passive.add(accountTypes.getBusinessObject(AccountTypes.DEBIT));
        passive.add(accountTypes.getBusinessObject(AccountTypes.TAXDEBIT));
        return createClosingBalance(accounts, active, passive);
    }
    public Balance createRelationsBalance(Accounts accounts){
        ArrayList<AccountType> credit = new ArrayList<>();
        ArrayList<AccountType> debit = new ArrayList<>();
        credit.add(accountTypes.getBusinessObject(AccountTypes.CREDIT));
        credit.add(accountTypes.getBusinessObject(AccountTypes.TAXCREDIT));
        debit.add(accountTypes.getBusinessObject(AccountTypes.DEBIT));
        debit.add(accountTypes.getBusinessObject(AccountTypes.TAXDEBIT));
        return createRelationsBalance(accounts, credit, debit);
    }

    public Balance createRelationsBalance(Accounts accounts, ArrayList<AccountType> credit, ArrayList<AccountType> debit){
        Balance relationsBalance = new Balance(RELATIONS_BALANCE, accounts);
        relationsBalance.setLeftName(getBundle("BusinessModel").getString("FUNDS_FROM_CUSTOMERS"));
        relationsBalance.setRightName(getBundle("BusinessModel").getString("DEBTS_TO_SUPPLIERS"));
        relationsBalance.setLeftTotalName(getBundle("BusinessModel").getString("FUNDS_TOTAL"));
        relationsBalance.setRightTotalName(getBundle("BusinessModel").getString("DEBTS_TOTAL"));
        relationsBalance.setLeftResultName(getBundle("BusinessModel").getString("FUND_REMAINING"));
        relationsBalance.setRightResultName(getBundle("BusinessModel").getString("DEBT_REMAINING"));
        relationsBalance.setLeftTypes(credit);
        relationsBalance.setRightTypes(debit);
        return relationsBalance;
    }


    public Balance createClosingBalance(Accounts accounts, ArrayList<AccountType> active, ArrayList<AccountType> passive){
        Balance yearBalance = new Balance(YEAR_BALANCE,accounts);
        yearBalance.setLeftName(getBundle("BusinessModel").getString("ASSETS"));
        yearBalance.setRightName(getBundle("BusinessModel").getString("LIABILITIES"));
        yearBalance.setLeftTotalName(getBundle("BusinessModel").getString("ASSETS_FUNDS_TOTAL"));
        yearBalance.setRightTotalName(getBundle("BusinessModel").getString("LIABILITIES_DEBTS_TOTAL"));
        yearBalance.setLeftResultName(getBundle("BusinessModel").getString("GAIN"));
        yearBalance.setRightResultName(getBundle("BusinessModel").getString("LOSS"));
        yearBalance.setLeftTypes(active);
        yearBalance.setRightTypes(passive);
        return yearBalance;
    }

    public Balance createResultBalance(Accounts accounts, ArrayList<AccountType> costs, ArrayList<AccountType> revenues){

        Balance resultBalance = new Balance(RESULT_BALANCE,accounts);
        resultBalance.setLeftName(getBundle("BusinessModel").getString("COSTS"));
        resultBalance.setRightName(getBundle("BusinessModel").getString("REVENUES"));
        resultBalance.setLeftTotalName(getBundle("BusinessModel").getString("COSTS_TOTAL"));
        resultBalance.setRightTotalName(getBundle("BusinessModel").getString("REVENUE_TOTAL"));
        resultBalance.setLeftResultName(getBundle("BusinessModel").getString("LOSS"));
        resultBalance.setRightResultName(getBundle("BusinessModel").getString("GAIN"));
        resultBalance.setLeftTypes(costs);
        resultBalance.setRightTypes(revenues);
        return resultBalance;
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
