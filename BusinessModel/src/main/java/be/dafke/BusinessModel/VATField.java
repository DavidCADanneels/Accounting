package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.TreeMap;

import static be.dafke.BusinessModel.VATTransactions.CREDIT_ACCOUNT;
import static be.dafke.BusinessModel.VATTransactions.DEBIT_ACCOUNT;

/**
 * Created by ddanneels on 27/12/2016.
 */
public class VATField extends BusinessObject {


    public static final String NR = "nr";
    public static final String AMOUNT = "amount";
    private int nr;
    private BigDecimal amount;
    private VATTransactions parent;

    public VATField(int nr, BigDecimal amount, VATTransactions parent) {
        this.nr = nr;
        this.amount = amount;
        this.parent = parent;
    }

    public int getNr() {
        return nr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Properties getOutputProperties(){
        Properties properties = new Properties();
        properties.put(NR,nr);
        properties.put(AMOUNT, amount);
        properties.put(DEBIT_ACCOUNT,parent.getDebitAccount());
        properties.put(CREDIT_ACCOUNT,parent.getCreditAccount());
        return properties;
    }

    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<>();
        properties.put(NR,nr+"");
        return properties;
    }
}
