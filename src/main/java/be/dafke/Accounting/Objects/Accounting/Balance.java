package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 0:54
 */
public class Balance extends BusinessObject{

    private final String leftName;
    private final String rightName;
    private final String leftTotalName;
    private final String rightTotalName;
    private final String leftResultName;
    private final String rightResultName;
    private final ArrayList<Account.AccountType> leftTypes;
    private final ArrayList<Account.AccountType> rightTypes;
    private final Accounting accounting;

    public Balance(String name,
                   String leftName, String rightName,
                   String leftTotalName, String rightTotalName,
                   String leftResultName, String rightResultName,
                   ArrayList<Account.AccountType> leftTypes, ArrayList<Account.AccountType> rightTypes,
                   Accounting accounting){
        super(name, "Balance");
        this.leftName = leftName;
        this.rightName = rightName;
        this.leftTotalName = leftTotalName;
        this.rightTotalName = rightTotalName;
        this.leftResultName = leftResultName;
        this.rightResultName = rightResultName;
        this.leftTypes = leftTypes;
        this.rightTypes = rightTypes;
        this.accounting = accounting;
    }

    public String getLeftName() {
        return leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public ArrayList<Account.AccountType> getLeftTypes() {
        return leftTypes;
    }

    public ArrayList<Account.AccountType> getRightTypes() {
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
}
