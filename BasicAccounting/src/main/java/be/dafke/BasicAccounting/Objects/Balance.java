package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollectionDependent;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeCollectionDependent;
import be.dafke.ObjectModel.WriteableBusinessCollection;
import be.dafke.ObjectModel.WriteableBusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance extends WriteableBusinessObject implements BusinessCollectionDependent<Account>, BusinessTypeCollectionDependent<AccountType>{

    private final static String LEFTNAME = "LeftName";
    private final static String RIGHTNAME = "RightName";
    private final static String LEFTTOTALNAME = "LeftTotalName";
    private final static String RIGHTTOTALNAME = "RightTotalName";
    private final static String LEFTRESULTNAME = "LeftResultName";
    private final static String RIGHTRESULTNAME = "RightResultName";
    private final static String LEFTTYPES = "LeftTypes";
    private final static String RIGHTTYPES = "RightTypes";
    private String leftName;
    private String rightName;
    private String leftTotalName;
    private String rightTotalName;
    private String leftResultName;
    private String rightResultName;
    private ArrayList<AccountType> leftTypes;
    private ArrayList<AccountType> rightTypes;
    private WriteableBusinessCollection<Account> businessCollection;
    private BusinessTypeCollection<AccountType> businessTypeCollection;

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
        for(Account account : businessCollection.getBusinessObjects()) {
            if (account.getType() == type && account.getSaldo().compareTo(BigDecimal.ZERO) != 0) col.add(account);
        }
        return col;
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
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = super.getInitProperties();
        properties.put(NAME, getName());
        properties.put(LEFTNAME, leftName);
        properties.put(RIGHTNAME, rightName);
        properties.put(LEFTTOTALNAME, leftTotalName);
        properties.put(RIGHTTOTALNAME, rightTotalName);
        properties.put(LEFTRESULTNAME, leftResultName);
        properties.put(RIGHTRESULTNAME, rightResultName);
        ArrayList<String> leftTypesString = new ArrayList<String>();
        for(AccountType type:leftTypes){
            leftTypesString.add(type.getName());
        }
        properties.put(LEFTTYPES, Utils.toString(leftTypesString));
        ArrayList<String> righttTypesString = new ArrayList<String>();
        for(AccountType type:rightTypes){
            righttTypesString.add(type.getName());
        }
        properties.put(RIGHTTYPES, Utils.toString(righttTypesString));
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        setName(properties.get(NAME));
        leftName = properties.get(LEFTNAME);
        rightName = properties.get(RIGHTNAME);
        leftTotalName = properties.get(LEFTTOTALNAME);
        rightTotalName = properties.get(RIGHTTOTALNAME);
        leftResultName = properties.get(LEFTRESULTNAME);
        rightResultName = properties.get(RIGHTRESULTNAME);
        leftTypes = new ArrayList<AccountType>();
        ArrayList<String> leftTypesString = Utils.parseStringList(properties.get(LEFTTYPES));
        for(String s: leftTypesString){
            leftTypes.add(businessTypeCollection.getBusinessObject(s));
        }
        rightTypes = new ArrayList<AccountType>();
        ArrayList<String> rightTypesString = Utils.parseStringList(properties.get(RIGHTTYPES));
        for(String s: rightTypesString){
            rightTypes.add(businessTypeCollection.getBusinessObject(s));
        }
    }

    @Override
    public void setBusinessCollection(WriteableBusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }
}
