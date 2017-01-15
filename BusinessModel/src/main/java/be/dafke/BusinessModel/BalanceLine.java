package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.util.TreeMap;

/**
 * User: david
 * Date: 11-1-14
 * Time: 21:27
 */
public class BalanceLine extends BusinessObject {
    private Account leftAccount, rightAccount;

    public BalanceLine(Account leftAccount, Account rightAccount){
        this.leftAccount = leftAccount;
        this.rightAccount = rightAccount;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<>();
    }

    public Account getLeftAccount() {
        return leftAccount;
    }

    public Account getRightAccount() {
        return rightAccount;
    }
}
