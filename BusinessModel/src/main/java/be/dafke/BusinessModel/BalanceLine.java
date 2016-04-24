package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;
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
        return new TreeMap<String, String>();
    }

    @Override
    public Properties getOutputProperties(){
        Properties properties = new Properties();
        if(leftAccount!=null){
            properties.put(Balance.NAME1,leftAccount.getName());
            properties.put(Balance.AMOUNT1,leftAccount.getSaldo().toString());
        }
        if(rightAccount!=null){
            properties.put(Balance.NAME2,rightAccount.getName());
            properties.put(Balance.AMOUNT2, BigDecimal.ZERO.subtract(rightAccount.getSaldo()).toString());
        }
        return properties;
    }

}
