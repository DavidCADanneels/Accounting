package be.dafke.Accounting.BusinessModel

import java.util.function.Predicate

class AccountsList {
    private boolean singleAccount = false
    private Account account = null
    private Predicate<Account> filter = null
    private HashMap<AccountType, Boolean> availableAccountTypes = new HashMap()
    private HashMap<AccountType, Boolean> checkedAccountTypes = new HashMap()
    private VATTransaction.VATType vatType
    public static String DEBIT = "Debit"
    public static String CREDIT = "Credit"

    private boolean leftAction = true
    private boolean rightAction = false
    private String leftButton = DEBIT
    private String rightButton = CREDIT

//    void addAllTypes(AccountTypes accountTypes){
//        addAllTypes(accountTypes, false)
//    }
    void addAllTypes(AccountTypes accountTypes, boolean enabled){
        accountTypes.getBusinessObjects().forEach({ accountType ->
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
