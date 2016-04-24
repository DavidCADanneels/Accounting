package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance extends BusinessCollection<BalanceLine> {
    private String leftName;
    private String rightName;
    private String leftTotalName;
    private String rightTotalName;
    private String leftResultName;
    private String rightResultName;
    private ArrayList<AccountType> leftTypes;
    private ArrayList<AccountType> rightTypes;
    private Accounting accounting;

    public final static String NAME1 = "name1";
    public final static String NAME2 = "name2";
    public final static String AMOUNT1 = "amount1";
    public final static String AMOUNT2 = "amount2";

    public Balance(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public String getChildType(){
        return "BalanceLine";
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME1);
        keySet.add(NAME2);
        keySet.add(AMOUNT1);
        keySet.add(AMOUNT2);
        return keySet;
    }


    @Override
    public BalanceLine createNewChild(TreeMap<String, String> properties){
        return null;
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

    public ArrayList<Account> getLeftAccounts() {
        return getAccountsNotEmpty(leftTypes);
    }

    public ArrayList<Account> getRightAccounts() {
        return getAccountsNotEmpty(rightTypes);
    }

    public ArrayList<Account> getAccountsNotEmpty(ArrayList<AccountType> types) {
        ArrayList<Account> col = new ArrayList<Account>();
        for(AccountType type : types) {
            col.addAll(getAccountsNotEmpty(type));
        }
        return col;
    }

    private ArrayList<Account> getAccountsNotEmpty(AccountType type) {
        ArrayList<Account> col = new ArrayList<Account>();
        for(Account account : accounting.getAccounts().getBusinessObjects()) {
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
        ArrayList<BalanceLine> balanceLines = new ArrayList<BalanceLine>();
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

    @Override
    public Properties getInitProperties() {
        Properties properties = super.getInitProperties();
        properties.put(NAME, getName());
        properties.put(Balances.LEFTNAME, leftName);
        properties.put(Balances.RIGHTNAME, rightName);
        properties.put(Balances.LEFTTOTALNAME, leftTotalName);
        properties.put(Balances.RIGHTTOTALNAME, rightTotalName);
        properties.put(Balances.LEFTRESULTNAME, leftResultName);
        properties.put(Balances.RIGHTRESULTNAME, rightResultName);
        ArrayList<String> leftTypesString = new ArrayList<String>();
        for(AccountType type:leftTypes){
            leftTypesString.add(type.getName());
        }
        properties.put(Balances.LEFTTYPES, Utils.toString(leftTypesString));
        ArrayList<String> righttTypesString = new ArrayList<String>();
        for(AccountType type:rightTypes){
            righttTypesString.add(type.getName());
        }
        properties.put(Balances.RIGHTTYPES, Utils.toString(righttTypesString));
        return properties;
    }
}
