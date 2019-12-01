package be.dafke.Accounting.BusinessModel

import java.util.function.Predicate

class AccountsList {
    boolean singleAccount = false
    Account account = null
    Predicate<Account> filter = null
    HashMap<AccountType, Boolean> availableAccountTypes = new HashMap()
    HashMap<AccountType, Boolean> checkedAccountTypes = new HashMap()
    VATTransaction.VATType vatType
    public static String DEBIT = "Debit"
    public static String CREDIT = "Credit"

    boolean leftAction = true
    boolean rightAction = false
    String leftButton = DEBIT
    String rightButton = CREDIT

//    void addAllTypes(AccountTypes accountTypes){
//        addAllTypes(accountTypes, false)
//    }
    void addAllTypes(AccountTypes accountTypes, boolean enabled){
        accountTypes.businessObjects.forEach({ accountType ->
            availableAccountTypes.put(accountType, enabled)
        })
    }

    void setTypeAvailable(AccountType accountType, boolean available){
        availableAccountTypes.put(accountType, available)
    }

    boolean isTypeAvailable(AccountType accountType){
        availableAccountTypes.get(accountType)
    }

    void setSingleAccount(boolean singleAccount) {
        this.singleAccount = singleAccount
    }

    void setAccount(Account account) {
        this.account = account
    }

    void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType
    }

    VATTransaction.VATType getVatType() {
        vatType
    }

    boolean isSingleAccount() {
        singleAccount
    }

    Account getAccount() {
        account
    }

    ArrayList<AccountType> getAccountTypes() {
        ArrayList<AccountType> accountTypes = new ArrayList()
        for(Map.Entry<AccountType, Boolean> entry : availableAccountTypes.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey())
            }
        }
        accountTypes
    }

    ArrayList<AccountType> getCheckedTypes(boolean left) {
        ArrayList<AccountType> accountTypes = new ArrayList()
        for(Map.Entry<AccountType, Boolean> entry : checkedAccountTypes.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey())
            }
        }
        accountTypes
    }

//    boolean isChecked(AccountType accountType){
//        checkedAccountTypes.get(accountType)
//    }

    boolean isLeftAction() {
        leftAction
    }

    void setLeftAction(boolean leftAction) {
        this.leftAction = leftAction
    }

    boolean isRightAction() {
        rightAction
    }

    void setRightAction(boolean rightAction) {
        this.rightAction = rightAction
    }

    String getLeftButton() {
        leftButton
    }

    void setLeftButton(String leftButton) {
        this.leftButton = leftButton
    }

    String getRightButton() {
        rightButton
    }

    void setRightButton(String rightButton) {
        this.rightButton = rightButton
    }
}
