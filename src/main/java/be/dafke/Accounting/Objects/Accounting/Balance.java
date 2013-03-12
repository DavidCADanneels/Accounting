package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.WriteableBusinessObject;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance extends WriteableBusinessObject {

    private final static String LEFTNAME = "LeftName";
    private final static String RIGHTNAME = "RightName";
    private final static String LEFTTOTALNAME = "LeftTotalName";
    private final static String RIGHTTOTALNAME = "RightTotalName";
    private final static String LEFTRESULTNAME = "LeftResultName";
    private final static String RIGHTRESULTNAME = "RightResultName";
    private String leftName;
    private String rightName;
    private String leftTotalName;
    private String rightTotalName;
    private String leftResultName;
    private String rightResultName;
    private ArrayList<AccountType> leftTypes;
    private ArrayList<AccountType> rightTypes;
    private Accounting accounting;

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
        return accounting.getAccounts().getAccountsNotEmpty(leftTypes);
    }

    public ArrayList<Account> getRightAccounts() {
        return accounting.getAccounts().getAccountsNotEmpty(rightTypes);
    }

    public Accounting getAccounting() {
        return accounting;
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

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        return keySet;
    }

    @Override
    public TreeMap<String,String> getUniqueProperties() {
        TreeMap<String,String> outputMap = super.getUniqueProperties();
        return outputMap;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> outputMap = super.getInitProperties();
        return outputMap;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
    }

    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String,String> outputMap = super.getProperties();
        outputMap.put(LEFTNAME,leftName);
        outputMap.put(RIGHTNAME,rightName);
        outputMap.put(LEFTTOTALNAME,leftTotalName);
        outputMap.put(RIGHTTOTALNAME,rightTotalName);
        outputMap.put(LEFTRESULTNAME,leftResultName);
        outputMap.put(RIGHTRESULTNAME,rightResultName);
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties) {
        super.setProperties(properties);
        leftName = properties.get(LEFTNAME);
        rightName = properties.get(RIGHTNAME);
        leftTotalName = properties.get(LEFTTOTALNAME);
        rightTotalName = properties.get(RIGHTTOTALNAME);
        leftResultName = properties.get(LEFTRESULTNAME);
        rightResultName = properties.get(RIGHTRESULTNAME);
    }
}
