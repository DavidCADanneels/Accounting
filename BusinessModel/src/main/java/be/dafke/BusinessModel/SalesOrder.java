package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

public class SalesOrder extends Order {

    private Transaction salesTransaction, gainTransaction;
    private String invoiceNumber = "";
    private boolean invoice = false;
    private boolean promoOrder = false;

    public BigDecimal getTotalSalesPriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getSalesPriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public BigDecimal getTotalSalesPriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesVat() {
        BigDecimal totalSalesVat = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesVat = totalSalesVat.add(orderItem.getSalesVatAmount()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesVat;
    }

    public BigDecimal getTotalSalesVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesVatAmount()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesPriceInclVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesPriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithVat().setScale(2, RoundingMode.HALF_DOWN));
        }
        return totalSalesExcl;
    }

    public Transaction getSalesTransaction() {
        return salesTransaction;
    }

    public void setSalesTransaction(Transaction salesTransaction) {
        this.salesTransaction = salesTransaction;

        if(!isCreditNote()) {
            getBusinessObjects().forEach(orderItem -> {
                Article article = orderItem.getArticle();
                int numberOfItems = orderItem.getNumberOfItems();
                // TODO: do not 'setSoOrdered' for PromoOrder
                article.setSoOrdered(numberOfItems);
            });
        } else {
            getBusinessObjects().forEach(orderItem -> {
                Article article = orderItem.getArticle();
                int numberOfItems = orderItem.getNumberOfItems();
                article.setSoCnOrdered(numberOfItems);
            });
        }
    }

    public Transaction getGainTransaction() {
        return gainTransaction;
    }

    public void setGainTransaction(Transaction gainTransaction) {
        this.gainTransaction = gainTransaction;
    }

    public BigDecimal calculateTotalSalesVat() {
        return getTotalSalesPriceInclVat().subtract(getTotalSalesPriceExclVat()).setScale(2, RoundingMode.HALF_DOWN);
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public void setInvoice(boolean invoice) {
        this.invoice = invoice;
    }

    public void addSalesOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            article.addSalesOrder(this);
        });
    }

    public boolean isPromoOrder() {
        return promoOrder;
    }
}
