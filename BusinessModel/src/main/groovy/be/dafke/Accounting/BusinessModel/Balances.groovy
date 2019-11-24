package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import static java.util.ResourceBundle.getBundle

class Balances extends BusinessCollection<Balance> {

    static String RESULT_BALANCE = "ResultBalance"
    static String RELATIONS_BALANCE = "RelationsBalance"
    static String YEAR_BALANCE = "YearBalance"

    private final Accounts accounts
    private final AccountTypes accountTypes

    Balances(Accounts accounts, AccountTypes accountTypes) {
        this.accounts = accounts
        this.accountTypes = accountTypes
//        addDefaultBalances()
    }

    void addDefaultBalances() {
        Balance resultBalance = createResultBalance(accounts)
        Balance relationsBalance = createRelationsBalance(accounts)
        Balance yearBalance = createClosingBalance(accounts)

        try {
            addBusinessObject(resultBalance)
            addBusinessObject(relationsBalance)
            addBusinessObject(yearBalance)
        } catch (EmptyNameException e) {
            System.err.println("The Name of a Balance can not be empty.")
        } catch (DuplicateNameException e) {
            System.err.println("The Name of a Balance already exists. ")
        }
    }

    Balance createResultBalance(Accounts accounts){
        ArrayList<AccountType> costs = new ArrayList()
        ArrayList<AccountType> revenues = new ArrayList()
        costs.add(accountTypes.getBusinessObject(AccountTypes.COST))
        revenues.add(accountTypes.getBusinessObject(AccountTypes.REVENUE))
        createResultBalance(accounts, costs, revenues)

    }
    Balance createClosingBalance(Accounts accounts){
        ArrayList<AccountType> active = new ArrayList()
        ArrayList<AccountType> passive = new ArrayList()
        active.add(accountTypes.getBusinessObject(AccountTypes.ASSET))
        active.add(accountTypes.getBusinessObject(AccountTypes.CREDIT))
        active.add(accountTypes.getBusinessObject(AccountTypes.TAXCREDIT))
        passive.add(accountTypes.getBusinessObject(AccountTypes.LIABILITY))
        passive.add(accountTypes.getBusinessObject(AccountTypes.DEBIT))
        passive.add(accountTypes.getBusinessObject(AccountTypes.TAXDEBIT))
        createClosingBalance(accounts, active, passive)
    }
    Balance createRelationsBalance(Accounts accounts){
        ArrayList<AccountType> credit = new ArrayList()
        ArrayList<AccountType> debit = new ArrayList()
        credit.add(accountTypes.getBusinessObject(AccountTypes.CREDIT))
        credit.add(accountTypes.getBusinessObject(AccountTypes.TAXCREDIT))
        debit.add(accountTypes.getBusinessObject(AccountTypes.DEBIT))
        debit.add(accountTypes.getBusinessObject(AccountTypes.TAXDEBIT))
        createRelationsBalance(accounts, credit, debit)
    }

    Balance createRelationsBalance(Accounts accounts, ArrayList<AccountType> credit, ArrayList<AccountType> debit){
        Balance relationsBalance = new Balance(RELATIONS_BALANCE, accounts)
        relationsBalance.setLeftName(getBundle("BusinessModel").getString("FUNDS_FROM_CUSTOMERS"))
        relationsBalance.setRightName(getBundle("BusinessModel").getString("DEBTS_TO_SUPPLIERS"))
        relationsBalance.setLeftTotalName(getBundle("BusinessModel").getString("FUNDS_TOTAL"))
        relationsBalance.setRightTotalName(getBundle("BusinessModel").getString("DEBTS_TOTAL"))
        relationsBalance.setLeftResultName(getBundle("BusinessModel").getString("FUND_REMAINING"))
        relationsBalance.setRightResultName(getBundle("BusinessModel").getString("DEBT_REMAINING"))
        relationsBalance.setLeftTypes(credit)
        relationsBalance.setRightTypes(debit)
        relationsBalance
    }


    Balance createClosingBalance(Accounts accounts, ArrayList<AccountType> active, ArrayList<AccountType> passive){
        Balance yearBalance = new Balance(YEAR_BALANCE,accounts)
        yearBalance.setLeftName(getBundle("BusinessModel").getString("ASSETS"))
        yearBalance.setRightName(getBundle("BusinessModel").getString("LIABILITIES"))
        yearBalance.setLeftTotalName(getBundle("BusinessModel").getString("ASSETS_FUNDS_TOTAL"))
        yearBalance.setRightTotalName(getBundle("BusinessModel").getString("LIABILITIES_DEBTS_TOTAL"))
        yearBalance.setLeftResultName(getBundle("BusinessModel").getString("GAIN"))
        yearBalance.setRightResultName(getBundle("BusinessModel").getString("LOSS"))
        yearBalance.setLeftTypes(active)
        yearBalance.setRightTypes(passive)
        yearBalance
    }

    Balance createResultBalance(Accounts accounts, ArrayList<AccountType> costs, ArrayList<AccountType> revenues){

        Balance resultBalance = new Balance(RESULT_BALANCE,accounts)
        resultBalance.setLeftName(getBundle("BusinessModel").getString("COSTS"))
        resultBalance.setRightName(getBundle("BusinessModel").getString("REVENUES"))
        resultBalance.setLeftTotalName(getBundle("BusinessModel").getString("COSTS_TOTAL"))
        resultBalance.setRightTotalName(getBundle("BusinessModel").getString("REVENUE_TOTAL"))
        resultBalance.setLeftResultName(getBundle("BusinessModel").getString("LOSS"))
        resultBalance.setRightResultName(getBundle("BusinessModel").getString("GAIN"))
        resultBalance.setLeftTypes(costs)
        resultBalance.setRightTypes(revenues)
        resultBalance
    }

    @Override
    Balance addBusinessObject(Balance value) throws EmptyNameException, DuplicateNameException {
        try {
            addBusinessObject(value, value.getUniqueProperties())
        } catch (DuplicateNameException ex) {
            String name = value.getName()
            if (YEAR_BALANCE.equals(name) || RESULT_BALANCE.equals(name) || RELATIONS_BALANCE.equals(name)) {
                System.err.println("Default Balance (" + name + ") already exists!")
                getBusinessObject(name)
            } else {
                throw ex
            }
        }
    }
    /*@Override
    void readCollection() {
        readCollection("Balance", false)
    }*/
}
