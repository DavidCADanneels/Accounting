package be.dafke.BusinessModel;

import java.util.HashMap;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class VAT {
    public enum VATType{
        SALE, PURCHASE;
    }

//    public String toString(){
//        return type.toString();
//    }

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

//    private VATType type;

    private static Integer[] vatPercentages = new Integer[]{0, 6, 12, 21};
    private static HashMap<Integer, Account> vatAccounts = new HashMap<>();

    public VAT() {
    }

    public void setVATAccount(int nr, Account account){
        vatAccounts.put(nr, account);
    }

    public Account getVATAccount(int nr){
        return vatAccounts.get(nr);
    }

//    public VATType getType() {
//        return type;
//    }
//
//    public void setType(VATType type) {
//        this.type = type;
//    }

    public static Integer[] getVatPercentages() {
        return vatPercentages;
    }

//    public static Account getVatAccount(boolean debt, int pct) {
//        return debt?getVatDebtAccount(pct):getVatCreditAccount(pct);
//    }

    public static Account getVatDebtAccount(int pct) {
        if(pct==6){
            return vatAccounts.get(1);
        } else if(pct==6){
            return vatAccounts.get(2);
        } else if(pct==6){
            return vatAccounts.get(3);
        } else return null;
    }

    public static Account getVatCreditAccount(PurchaseType purchaseType) {
        if(purchaseType==PurchaseType.GOODS){
            return vatAccounts.get(81);
        } else if(purchaseType==PurchaseType.SERVICES){
            return vatAccounts.get(82);
        } else if(purchaseType==PurchaseType.INVESTMENTS){
            return vatAccounts.get(83);
        } else return null;
    }
}
