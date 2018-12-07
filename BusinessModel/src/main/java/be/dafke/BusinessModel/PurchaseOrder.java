package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

public class PurchaseOrder extends Order {

    private Transaction purchaseTransaction;

    public OrderItem addBusinessObject(OrderItem orderItem) {
        super.addBusinessObject(orderItem);
//        Article article = orderItem.getArticle();
//        article.addPurchaseOrder(this);
        return orderItem;
    }

    public BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getPurchasePriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public BigDecimal getTotalPurchasePriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            total = total.add(orderItem.getPurchasePriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getPurchasePriceWithVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public BigDecimal getTotalPurchasePriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            total = total.add(orderItem.getPurchasePriceWithVat().setScale(2, RoundingMode.HALF_DOWN));
        }
        return total;
    }

    public BigDecimal getTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getPurchaseVatAmount()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public BigDecimal getTotalPurchaseVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            total = total.add(orderItem.getPurchaseVatAmount()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public BigDecimal calculateTotalPurchaseVat() {
        return getTotalPurchasePriceInclVat().subtract(getTotalPurchasePriceExclVat()).setScale(2, RoundingMode.HALF_DOWN);
    }

    public void addPurchaseOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            article.addPurchaseOrder(this);
        });
    }

    public void setPurchaseTransaction(Transaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction;
    }

    public Transaction getPurchaseTransaction() {
        return purchaseTransaction;
    }
}
