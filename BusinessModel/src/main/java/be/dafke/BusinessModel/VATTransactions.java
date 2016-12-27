package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VATTransactions {
    private Account creditAccount, debitAccount;

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

    public enum VATType{
        SALE, PURCHASE, NONE;
    }

//    public String toString(){
//        return type.toString();
//    }

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

//    private VATType type;

    private Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private HashMap<Integer, BigDecimal> vatAccounts = new HashMap<>();

    public VATTransactions() {
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    //    public void setVATAccount(int nr, Account account){
//        vatAccounts.put(nr, account);
//    }
//
//    public Account getVATAccount(int nr){
//        return vatAccounts.get(nr);
//    }

//    public VATType getType() {
//        return type;
//    }
//
//    public void setType(VATType type) {
//        this.type = type;
//    }

    public Integer[] getVatPercentages() {
        return vatPercentages;
    }

//    public static Account getVatAccount(boolean debt, int pct) {
//        return debt?getVatDebtAccount(pct):getVatCreditAccount(pct);
//    }

    public Account getVatCreditAccount(int pct) {
        return creditAccount;
//        if(pct==6){
//            return vatAccounts.get(1);
//        } else if(pct==12){
//            return vatAccounts.get(2);
//        } else if(pct==21){
//            return vatAccounts.get(3);
//        } else return null;
    }

    public Account getVatDebitAccount(PurchaseType purchaseType) {
        return debitAccount;
//        if(purchaseType==PurchaseType.GOODS){
//            return vatAccounts.get(81);
//        } else if(purchaseType==PurchaseType.SERVICES){
//            return vatAccounts.get(82);
//        } else if(purchaseType==PurchaseType.INVESTMENTS){
//            return vatAccounts.get(83);
//        } else return null;
    }
}
