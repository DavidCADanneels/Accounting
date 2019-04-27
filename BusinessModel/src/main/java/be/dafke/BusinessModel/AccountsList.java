package be.dafke.BusinessModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class AccountsList {
    private boolean singleAccount = false;
    private Account account = null;
    private Predicate<Account> filter = null;
    private HashMap<AccountType, Boolean> availableAccountTypes = new HashMap<>();
    private HashMap<AccountType, Boolean> checkedAccountTypes = new HashMap<>();
    private VATTransaction.VATType vatType;
    public static String DEBIT = "Debit";
    public static String CREDIT = "Credit";

    private boolean leftAction = true;
    private boolean rightAction = false;
    private String leftButton = DEBIT;
    private String rightButton = CREDIT;

//    public void addAllTypes(AccountTypes accountTypes){
//        addAllTypes(accountTypes, false);
//    }
    public void addAllTypes(AccountTypes accountTypes, boolean enabled){
        accountTypes.getBusinessObjects().forEach(accountType -> {
            availableAccountTypes.put(accountType,enabled);
        });
    }

    public void setTypeAvailable(AccountType accountType, boolean available){
        availableAccountTypes.put(accountType, available);
    }

    public boolean isTypeAvailable(AccountType accountType){
        return availableAccountTypes.get(accountType);
    }

    public void setSingleAccount(boolean singleAccount) {
        this.singleAccount = singleAccount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    public VATTransaction.VATType getVatType() {
        return vatType;
    }

    public boolean isSingleAccount() {
        return singleAccount;
    }

    public Account getAccount() {
        return account;
    }

    public ArrayList<AccountType> getAccountTypes() {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : availableAccountTypes.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }

    public ArrayList<AccountType> getCheckedTypes(boolean left) {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypes.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }

//    public boolean isChecked(AccountType accountType){
//        return checkedAccountTypes.get(accountType);
//    }

    public boolean isLeftAction() {
        return leftAction;
    }

    public void setLeftAction(boolean leftAction) {
        this.leftAction = leftAction;
    }

    public boolean isRightAction() {
        return rightAction;
    }

    public void setRightAction(boolean rightAction) {
        this.rightAction = rightAction;
    }

    public String getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(String leftButton) {
        this.leftButton = leftButton;
    }

    public String getRightButton() {
        return rightButton;
    }

    public void setRightButton(String rightButton) {
        this.rightButton = rightButton;
    }
}
