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
    private HashMap<Integer, BigDecimal> vatAccounts = new HashMap<>();

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

    public HashMap<Integer, BigDecimal> getVatAccounts() {
        return vatAccounts;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NR);
        keySet.add(AMOUNT);
//        keySet.add(DEBIT_ACCOUNT);
//        keySet.add(CREDIT_ACCOUNT);
        return keySet;
    }

    public void setVatAccounts(HashMap<Integer, BigDecimal> vatAccounts) {
        this.vatAccounts = vatAccounts;
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
                    vatAccounts.put(nr, amount);
                }
            }
        }
    }

    public Properties getOutputProperties(){
        Properties outputProperties = super.getOutputProperties();
        for(Map.Entry<Integer, BigDecimal> entry : vatAccounts.entrySet()){
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

    public enum VATType{
        SALE, PURCHASE, NONE;
    }

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

    public void book(HashMap<Integer, BigDecimal> vatTransaction) {
        for (Map.Entry<Integer, BigDecimal> entry : vatTransaction.entrySet()) {
            Integer key = entry.getKey();
            BigDecimal addedValue = entry.getValue();
            increase(key, addedValue);
        }
    }

    public void increase(int nr, BigDecimal amount){
        BigDecimal currentValue = vatAccounts.get(nr);
        if(currentValue==null){
            currentValue = BigDecimal.ZERO;
        }
        currentValue = currentValue.add(amount).setScale(2);
        vatAccounts.put(nr,currentValue);
    }

    public void decrease(int nr, BigDecimal amount){
        BigDecimal currentValue = vatAccounts.get(nr);
        if(currentValue==null){
            currentValue = BigDecimal.ZERO;
        }
        currentValue = currentValue.subtract(amount).setScale(2);
        vatAccounts.put(nr,currentValue);
    }

    public static HashMap<Integer, BigDecimal> purchaseCN(BigDecimal amount, BigDecimal btwAmount, PurchaseType purchaseType) {
        // We assume amount is negative !!!
        HashMap<Integer, BigDecimal> purchaseCN = new HashMap<>();
        purchaseCN.put(85,amount.negate());
        purchaseCN.put(63,btwAmount.negate());
        if(purchaseType==PurchaseType.GOODS){
            purchaseCN.put(81,amount);
        } else if(purchaseType==PurchaseType.SERVICES){
            purchaseCN.put(82,amount);
        } else if(purchaseType==PurchaseType.INVESTMENTS){
            purchaseCN.put(83,amount);
        }
        return purchaseCN;
    }

    public static HashMap<Integer, BigDecimal> saleCN(BigDecimal amount, BigDecimal btwAmount) {
        // We assume amount is negative !!!
        HashMap<Integer, BigDecimal> saleCN = new HashMap<>();
        saleCN.put(49,amount.negate());
        saleCN.put(64,btwAmount.negate());
        return saleCN;
    }

    public static HashMap<Integer, BigDecimal> purchase(BigDecimal amount, BigDecimal btwAmount, PurchaseType purchaseType) {
        HashMap<Integer, BigDecimal> purchase = new HashMap<>();
        if(purchaseType==PurchaseType.GOODS){
            purchase.put(81,amount);
        } else if(purchaseType==PurchaseType.SERVICES){
            purchase.put(82,amount);
        } else if(purchaseType==PurchaseType.INVESTMENTS){
            purchase.put(83,amount);
        }
        purchase.put(59,btwAmount);
        return purchase;
    }

    public static HashMap<Integer, BigDecimal> sale(BigDecimal amount, BigDecimal btwAmount, Integer pct) {
        HashMap<Integer, BigDecimal> sale = new HashMap<>();
        if(pct==6){
            sale.put(1,amount);
        } else if(pct==12){
            sale.put(2,amount);
        } else if(pct==21){
            sale.put(3,amount);
        }
        sale.put(54,btwAmount);
        return sale;
    }
}
