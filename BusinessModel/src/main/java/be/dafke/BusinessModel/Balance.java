package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance extends BusinessCollection<BalanceLine> {
    private String leftName="";
    private String rightName="";
    private String leftTotalName="";
    private String rightTotalName="";
    private String leftResultName="";
    private String rightResultName="";
    private ArrayList<AccountType> leftTypes = new ArrayList<>();
    private ArrayList<AccountType> rightTypes = new ArrayList<>();
    private Accounts accounts;


    public Balance(String name, Accounts accounts) {
        setName(name);
        this.accounts = accounts;
    }
    @Override
    public boolean isDeletable(){
        return !(getName().equals(Balances.YEAR_BALANCE) || getName().equals(Balances.RESULT_BALANCE) || getName().equals(Balances.RELATIONS_BALANCE));
    }

    public String getLeftName() {
        return leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public ArrayList<AccountType> getLeftTypes() {
        return leftTypes;
    }

    public ArrayList<AccountType> getRightTypes() {
        return rightTypes;
    }

    public ArrayList<Account> getLeftAccounts(boolean includeEmpty) {
        if(includeEmpty) return getAccountsByType(leftTypes);
        else return getAccountsNotEmpty(leftTypes);
    }
    public ArrayList<Account> getLeftAccounts() {
        return getAccountsNotEmpty(leftTypes);
    }

    public ArrayList<Account> getRightAccounts(boolean includeEmpty) {
        if(includeEmpty) return getAccountsByType(rightTypes);
        else return getAccountsNotEmpty(rightTypes);
    }
    public ArrayList<Account> getRightAccounts() {
        return getAccountsNotEmpty(rightTypes);
    }

    public ArrayList<Account> getAccountsNotEmpty(ArrayList<AccountType> types) {
        ArrayList<Account> col = new ArrayList<>();
        for(AccountType type : types) {
            col.addAll(getAccountsNotEmpty(type));
        }
        return col;
    }


    public ArrayList<Account> getAccountsByType(ArrayList<AccountType> types) {
        ArrayList<Account> col = new ArrayList<>();
        for(AccountType type : types) {
            col.addAll(getAccountsByType(type));
        }
        return col;
    }

    private ArrayList<Account> getAccountsByType(AccountType type) {
        return accounts.getAccountsByType(type);
    }

    private ArrayList<Account> getAccountsNotEmpty(AccountType type) {
        ArrayList<Account> col = new ArrayList<>();
        for(Account account : accounts.getBusinessObjects()) {
            if (account.getType() == type && account.getSaldo().compareTo(BigDecimal.ZERO) != 0) col.add(account);
        }
        return col;
    }

    @Override
    public ArrayList<BalanceLine> getBusinessObjects(){
        ArrayList<Account> leftAccounts = getLeftAccounts();
        ArrayList<Account> rightAccounts = getRightAccounts();

        int nrLeft = leftAccounts.size();
        int nrRight = rightAccounts.size();
        int min,max;
        if (nrLeft > nrRight) {
            max = nrLeft;
            min = nrRight;
        } else {
            max = nrRight;
            min = nrLeft;
        }
        ArrayList<BalanceLine> balanceLines = new ArrayList<>();
        for(int i = 0; i < min; i++) {
            Account leftAccount = leftAccounts.get(i);
            Account rightAccount = rightAccounts.get(i);
            balanceLines.add(new BalanceLine(leftAccount,rightAccount));
        }
        for(int i = min; i < max; i++) {
            if(nrLeft > nrRight) {
                Account leftAccount = leftAccounts.get(i);
                balanceLines.add(new BalanceLine(leftAccount,null));
            } else {
                Account rightAccount = rightAccounts.get(i);
                balanceLines.add(new BalanceLine(null,rightAccount));
            }
        }
        return balanceLines;
    }

    public String getLeftTotalName() {
        return leftTotalName;
    }

    public String getRightTotalName() {
        return rightTotalName;
    }

    public String getLeftResultName() {
        return leftResultName;
    }

    public String getRightResultName() {
        return rightResultName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public void setLeftTotalName(String leftTotalName) {
        this.leftTotalName = leftTotalName;
    }

    public void setRightTotalName(String rightTotalName) {
        this.rightTotalName = rightTotalName;
    }

    public void setLeftResultName(String leftResultName) {
        this.leftResultName = leftResultName;
    }

    public void setRightResultName(String rightResultName) {
        this.rightResultName = rightResultName;
    }

    public void setLeftTypes(ArrayList<AccountType> leftTypes) {
        this.leftTypes = leftTypes;
    }

    public void setRightTypes(ArrayList<AccountType> rightTypes) {
        this.rightTypes = rightTypes;
    }

    public void addLeftType(AccountType type) {
        leftTypes.add(type);
    }

    public void addRightType(AccountType type) {
        rightTypes.add(type);
    }

    public void removeLeftType(AccountType type) {
        leftTypes.remove(type);
    }

    public void removeRightType(AccountType type) {
        rightTypes.remove(type);
    }
}
