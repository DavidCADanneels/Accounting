package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.*;

import static be.dafke.BusinessModel.VATField.AMOUNT;
import static be.dafke.BusinessModel.VATField.NR;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions extends BusinessCollection<VATField> implements ChildrenNeedSeparateFile, MustBeRead{
    public static final String VATFIELD = "VATField";
    public static final String VAT_FIELDS = "VATFields";
    public static final String DEBIT_ACCOUNT = "DebitAccount";
    public static final String CREDIT_ACCOUNT = "CreditAccount";
    private Account creditAccount, debitAccount;
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
        keySet.add(DEBIT_ACCOUNT);
        keySet.add(CREDIT_ACCOUNT);
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
        String debitAccountString = properties.get(DEBIT_ACCOUNT);
        if(debitAccountString!=null) {
            Account debitAccount = accounts.getBusinessObject(debitAccountString);
            setDebitAccount(debitAccount);
        }
        String creditAccountString = properties.get(CREDIT_ACCOUNT);
        if(creditAccountString!=null) {
            Account creditAccount = accounts.getBusinessObject(creditAccountString);
            setCreditAccount(creditAccount);
        }
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
