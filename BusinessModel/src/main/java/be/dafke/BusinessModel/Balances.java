package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends BusinessCollection<Balance> implements BusinessCollectionProvider<Account>, BusinessTypeProvider<AccountType>{

    public static final String BALANCES = "Balances";
    public static final String BALANCE = "Balance";

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";

    private BusinessCollection<Account> businessCollection;
    private BusinessTypeCollection<AccountType> businessTypeCollection;

    public Balances(){
        setName(BALANCES);
    }

    @Override
    public String getChildType(){
        return BALANCE;
    }

    public void addDefaultBalances(AccountTypes accountTypes){
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

        Balance resultBalance = createNewChild();
        resultBalance.setName(RESULT_BALANCE);
        resultBalance.setLeftName(getBundle("Accounting").getString("COSTS"));
        resultBalance.setRightName(getBundle("Accounting").getString("REVENUES"));
        resultBalance.setLeftTotalName(getBundle("Balances").getString("COSTS_TOTAL"));
        resultBalance.setRightTotalName(getBundle("Balances").getString("REVENUE_TOTAL"));
        resultBalance.setLeftResultName(getBundle("Balances").getString("LOSS"));
        resultBalance.setRightResultName(getBundle("Balances").getString("GAIN"));
        resultBalance.setLeftTypes(costs);
        resultBalance.setRightTypes(revenues);

        Balance relationsBalance = createNewChild();
        relationsBalance.setName(RELATIONS_BALANCE);
        relationsBalance.setLeftName(getBundle("Accounting").getString("FUNDS_FROM_CUSTOMERS"));
        relationsBalance.setRightName(getBundle("Accounting").getString("DEBTS_TO_SUPPLIERS"));
        relationsBalance.setLeftTotalName(getBundle("Balances").getString("FUNDS_TOTAL"));
        relationsBalance.setRightTotalName(getBundle("Balances").getString("DEBTS_TOTAL"));
        relationsBalance.setLeftResultName(getBundle("Balances").getString("FUND_REMAINING"));
        relationsBalance.setRightResultName(getBundle("Balances").getString("DEBT_REMAINING"));
        relationsBalance.setLeftTypes(credit);
        relationsBalance.setRightTypes(debit);

        Balance yearBalance = createNewChild();
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
    public Balance createNewChild() {
        Balance balance = new Balance();
        balance.setBusinessCollection(businessCollection);
        balance.setBusinessTypeCollection(businessTypeCollection);
        return balance;
    }

    @Override
    public Balance addBusinessObject(Balance value) throws EmptyNameException, DuplicateNameException {
        try {
            return addBusinessObject(value, value.getUniqueProperties());
        } catch (DuplicateNameException ex) {
            String name = value.getName();
            if(YEAR_BALANCE.equals(name) || RESULT_BALANCE.equals(name) || RELATIONS_BALANCE.equals(name)){
                System.err.println("Default Balance ("+name+") already exists!");
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

    public BusinessCollection<Account> getBusinessCollection() {
        return businessCollection;
    }

    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }

    public BusinessTypeCollection<AccountType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }
}
