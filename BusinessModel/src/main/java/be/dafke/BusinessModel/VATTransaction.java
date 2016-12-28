package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransaction extends HashMap<Integer,BigDecimal>{

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

    public enum VATType{
        SALE, PURCHASE, NONE;
    }

    public static VATTransaction purchaseCN(BigDecimal amount, BigDecimal btwAmount, PurchaseType purchaseType) {
        // We assume amount is negative !!!
        VATTransaction purchaseCN = new VATTransaction();
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

    public static VATTransaction saleCN(BigDecimal amount, BigDecimal btwAmount) {
        // We assume amount is negative !!!
        VATTransaction saleCN = new VATTransaction();
        saleCN.put(49,amount.negate());
        saleCN.put(64,btwAmount.negate());
        return saleCN;
    }

    public static VATTransaction purchase(BigDecimal amount, BigDecimal btwAmount, PurchaseType purchaseType) {
        VATTransaction purchase = new VATTransaction();
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

    public static VATTransaction sale(BigDecimal amount, BigDecimal btwAmount, Integer pct) {
        VATTransaction sale = new VATTransaction();
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
