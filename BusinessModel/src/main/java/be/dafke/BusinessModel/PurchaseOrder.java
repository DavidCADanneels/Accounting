package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

public class PurchaseOrder extends Order {

    private Transaction purchaseTransaction;

    public BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchasePriceWithoutVat = orderItem.getPurchasePriceWithoutVat();
            if(purchasePriceWithoutVat!=null) {
                total = total.add(purchasePriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN);
            }
        }
        return total;
    }

    public BigDecimal getTotalPurchasePriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchasePriceWithoutVat = orderItem.getPurchasePriceWithoutVat();
            if(purchasePriceWithoutVat!=null) {
                total = total.add(purchasePriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN);
            }
        }
        return total;
    }

    public BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchasePriceWithVat = orderItem.getPurchasePriceWithVat();
            if(purchasePriceWithVat!=null) {
                total = total.add(purchasePriceWithVat).setScale(2, RoundingMode.HALF_DOWN);
            }
        }
        return total;
    }

    public BigDecimal getTotalPurchasePriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchasePriceWithVat = orderItem.getPurchasePriceWithVat();
            if(purchasePriceWithVat!=null) {
                total = total.add(purchasePriceWithVat.setScale(2, RoundingMode.HALF_DOWN));
            }
        }
        return total;
    }

    public BigDecimal getTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchaseVatAmount = orderItem.getPurchaseVatAmount();
            if (purchaseVatAmount != null) {
                total = total.add(purchaseVatAmount).setScale(2, RoundingMode.HALF_DOWN);
            }
        }
        return total;
    }

    public BigDecimal getTotalPurchaseVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchaseVatAmount = orderItem.getPurchaseVatAmount();
            if(purchaseVatAmount!=null) {
                total = total.add(purchaseVatAmount).setScale(2, RoundingMode.HALF_DOWN);
            }
        }
        return total;
    }

    public BigDecimal calculateTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        BigDecimal totalPurchasePriceExclVat = getTotalPurchasePriceExclVat();
        BigDecimal totalPurchasePriceInclVat = getTotalPurchasePriceInclVat();
        if(totalPurchasePriceExclVat!=null && totalPurchasePriceInclVat!=null) {
            total = totalPurchasePriceInclVat.subtract(totalPurchasePriceExclVat).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public void addPurchaseOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            article.addPurchaseOrder(this);
        });
    }

    public void setPurchaseTransaction(Transaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction;

        if(!isCreditNote()) {
            getBusinessObjects().forEach(orderItem -> {
                Article article = orderItem.getArticle();
                int numberOfItems = orderItem.getNumberOfItems();
                article.setPoOrdered(numberOfItems);
            });
        } else {
            getBusinessObjects().forEach(orderItem -> {
                Article article = orderItem.getArticle();
                int numberOfItems = orderItem.getNumberOfItems();
                article.setPoCnOrdered(numberOfItems);
            });
        }

    }

    public Transaction getPurchaseTransaction() {
        return purchaseTransaction;
    }
}
