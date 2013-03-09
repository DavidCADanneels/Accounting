package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance extends WriteableBusinessObject {

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
}
