package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PurchaseOrder extends Order {

    private Transaction purchaseTransaction;

    public BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumberOfUnits();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchaseVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumberOfUnits();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchaseVat(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumberOfUnits();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePriceWithVat(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public void setPurchaseTransaction(Transaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction;
    }

    public Transaction getPurchaseTransaction() {
        return purchaseTransaction;
    }
}
