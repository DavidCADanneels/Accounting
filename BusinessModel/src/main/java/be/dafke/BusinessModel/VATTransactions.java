package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions {
    private Account creditAccount, debitAccount;
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

    public HashMap<Integer, BigDecimal> getVatAccounts() {
        return vatAccounts;
    }

    public void setVatAccounts(HashMap<Integer, BigDecimal> vatAccounts) {
        this.vatAccounts = vatAccounts;
    }

    public enum VATType{
        SALE, PURCHASE, NONE;
    }

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

    public void book(HashMap<Integer, BigDecimal> vatTransaction) {
        for(Map.Entry<Integer, BigDecimal> entry:vatTransaction.entrySet()){
            Integer key = entry.getKey();
            BigDecimal addedValue = entry.getValue();
            BigDecimal currentValue = vatAccounts.get(key);
            if(currentValue==null){
                currentValue = BigDecimal.ZERO;
            }
            currentValue = currentValue.add(addedValue).setScale(2);
            vatAccounts.put(key,currentValue);
        }
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
