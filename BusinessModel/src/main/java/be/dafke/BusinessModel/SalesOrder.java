package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

public class SalesOrder extends Order {

    private Transaction salesTransaction, gainTransaction;
    private String invoiceNumber = "";

    public BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumberOfUnits();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalPurchasePriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumberOfUnits();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalSalesPriceExclVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesPriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesVatAmount()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
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
}
