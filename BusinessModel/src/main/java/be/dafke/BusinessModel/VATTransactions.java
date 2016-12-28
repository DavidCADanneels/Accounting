package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.*;

import static be.dafke.BusinessModel.VATField.AMOUNT;
import static be.dafke.BusinessModel.VATField.NR;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATField> implements MustBeRead{
    public static final String VATFIELD = "VATField";
    public static final String VAT_FIELDS = "VATFields";
    public static final String DEBIT_ACCOUNT = "DebitAccount";
    public static final String CREDIT_ACCOUNT = "CreditAccount";
    public static final String DEBIT_CN_ACCOUNT = "DebitCNAccount";
    public static final String CREDIT_CN_ACCOUNT = "CreditCNAccount";
    private Account creditAccount, debitAccount, creditCNAccount, debitCNAccount;
    private Accounts accounts;
    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private VATTransaction vatTransaction = new VATTransaction();

    public Integer[] getVatPercentages() {
        return vatPercentages;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public Account getDebitAccount() {
        return debitAccount;
    }

    public Account getCreditCNAccount() {
        return creditCNAccount;
    }

    public void setCreditCNAccount(Account creditCNAccount) {
        this.creditCNAccount = creditCNAccount;
    }

    public Account getDebitCNAccount() {
        return debitCNAccount;
    }

    public void setDebitCNAccount(Account debitCNAccount) {
        this.debitCNAccount = debitCNAccount;
    }

    public VATTransactions(Accounts accounts) {
        setName(VAT_FIELDS);
        this.accounts = accounts;
    }

    public BigDecimal getField(String nr){
        if("XX".equals(nr)){
            return getXX();
        } else if("YY".equals(nr)){
            return getYY();
        } else if("71".equals(nr)){
            BigDecimal XX = getXX();
            BigDecimal YY = getYY();
            if(XX.compareTo(YY)>0){
                return XX.subtract(YY);
            } else return BigDecimal.ZERO;
        } else if("72".equals(nr)){
            BigDecimal YY = getYY();
            BigDecimal XX = getXX();
            if(YY.compareTo(XX)>0){
                return YY.subtract(XX);
            } else return BigDecimal.ZERO;
        } else{
            int integer = Integer.parseInt(nr);
            BigDecimal bigDecimal = vatTransaction.get(integer);
            return bigDecimal==null?BigDecimal.ZERO:bigDecimal;
        }
    }

    public VATTransaction getVatTransaction() {
        return vatTransaction;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NR);
        keySet.add(AMOUNT);
//        keySet.add(DEBIT_ACCOUNT);
//        keySet.add(CREDIT_ACCOUNT);
        return keySet;
    }

    public void setVatTransaction(VATTransaction vatTransaction) {
        this.vatTransaction = vatTransaction;
    }

    @Override
    public String getChildType() {
        return VATFIELD;
    }

    @Override
    public VATField createNewChild(TreeMap<String, String> properties) {
        String nrString = properties.get(NR);
        String amountString = properties.get(AMOUNT);
        if(nrString!=null && amountString!=null) {
            int nr = Utils.parseInt(nrString);
            BigDecimal amount = Utils.parseBigDecimal(amountString);
            return new VATField(nr,amount, this);
        }
        return null;
    }

    @Override
    public Set<String> getExtraFields() {
        Set<String> set = new TreeSet<>();
        set.add(DEBIT_ACCOUNT);
        set.add(CREDIT_ACCOUNT);
        set.add(DEBIT_CN_ACCOUNT);
        set.add(CREDIT_CN_ACCOUNT);
        set.add("VAT1");
        set.add("VAT2");
        set.add("VAT3");
        set.add("VAT49");
        set.add("VAT54");
        set.add("VAT59");
        set.add("VAT63");
        set.add("VAT64");
        set.add("VAT81");
        set.add("VAT82");
        set.add("VAT83");
        set.add("VAT85");
        return set;
    }

    @Override
    public void setExtraProperties(TreeMap<String,String> extraProperties) {
        String debitAccountString = extraProperties.get(DEBIT_ACCOUNT);
        if(debitAccountString!=null) {
            debitAccount = accounts.getBusinessObject(debitAccountString);
        }
        String creditAccountString = extraProperties.get(CREDIT_ACCOUNT);
        if(creditAccountString!=null) {
            creditAccount = accounts.getBusinessObject(creditAccountString);
        }
        String debitCNAccountString = extraProperties.get(DEBIT_CN_ACCOUNT);
        if(debitCNAccountString!=null) {
            debitCNAccount = accounts.getBusinessObject(debitCNAccountString);
        }
        String creditCNAccountString = extraProperties.get(CREDIT_CN_ACCOUNT);
        if(creditCNAccountString!=null) {
            creditCNAccount = accounts.getBusinessObject(creditCNAccountString);
        }
        for(Map.Entry<String,String> entry: extraProperties.entrySet()){
            String key = entry.getKey();
            if(key.startsWith("VAT")){
                int nr = Utils.parseInt(key.replace("VAT",""));
                String amountString = entry.getValue();
                if(amountString!=null) {
                    BigDecimal amount = Utils.parseBigDecimal(amountString);
                    vatTransaction.put(nr, amount);
                }
            }
        }
    }

    public Properties getOutputProperties(){
        Properties outputProperties = super.getOutputProperties();
        for(Map.Entry<Integer, BigDecimal> entry : vatTransaction.entrySet()){
            outputProperties.put("VAT"+entry.getKey(),entry.getValue());
        }
        if(debitAccount!=null) {
            outputProperties.put(DEBIT_ACCOUNT, debitAccount);
        }
        if(creditAccount!=null) {
            outputProperties.put(CREDIT_ACCOUNT, creditAccount);
        }
        if(debitCNAccount!=null) {
            outputProperties.put(DEBIT_CN_ACCOUNT, debitCNAccount);
        }
        if(creditCNAccount!=null) {
            outputProperties.put(CREDIT_CN_ACCOUNT, creditCNAccount);
        }
        return outputProperties;
    }

    @Override
    public VATField addBusinessObject(VATField vatField){
        increase(vatField.getNr(),vatField.getAmount());
        return vatField;
    }

    public BigDecimal getXX() {
        return getField("54").add(getField("63"));
    }

    public BigDecimal getYY() {
        return getField("59").add(getField("64"));
    }

    public void book(VATTransaction vatTransaction) {
        for (Map.Entry<Integer, BigDecimal> entry : vatTransaction.entrySet()) {
            Integer key = entry.getKey();
            BigDecimal addedValue = entry.getValue();
            increase(key, addedValue);
        }
    }

    public void increase(int nr, BigDecimal amount){
        BigDecimal currentValue = vatTransaction.get(nr);
        if(currentValue==null){
            currentValue = BigDecimal.ZERO;
        }
        currentValue = currentValue.add(amount).setScale(2);
        vatTransaction.put(nr,currentValue);
    }

    public void decrease(int nr, BigDecimal amount){
        BigDecimal currentValue = vatTransaction.get(nr);
        if(currentValue==null){
            currentValue = BigDecimal.ZERO;
        }
        currentValue = currentValue.subtract(amount).setScale(2);
        vatTransaction.put(nr,currentValue);
    }
}
