package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * User: david
 * Date: 11-1-14
 * Time: 21:27
 */
public class BalanceLine extends BusinessObject {
    private final static String NAME1 = "name1";
    private final static String NAME2 = "name2";
    private final static String AMOUNT1 = "amount1";
    private final static String AMOUNT2 = "amount2";
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
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME1);
        keySet.add(NAME2);
        keySet.add(AMOUNT1);
        keySet.add(AMOUNT2);
        return keySet;
    }

    @Override
    public Properties getInitProperties(){
        Properties properties = new Properties();
        if(leftAccount!=null){
            properties.put(NAME1,leftAccount.getName());
            properties.put(AMOUNT1,leftAccount.getSaldo().toString());
        }
        if(rightAccount!=null){
            properties.put(NAME2,rightAccount.getName());
            properties.put(AMOUNT2, BigDecimal.ZERO.subtract(rightAccount.getSaldo()).toString());
        }
        return properties;
    }

}
