package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesOrder extends Order {

    private Transaction salesTransaction, gainTransaction;

    public BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int number = orderItem.getNumberOfUnits();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalPurchaseExcl;
    }

    public BigDecimal getTotalSalesPriceExclVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int numberOfItems = orderItem.getNumberOfItems();
            totalSalesExcl = totalSalesExcl.add(article.getSalesPriceWithoutVat(numberOfItems)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int numberOfItems = orderItem.getNumberOfItems();
            totalSalesExcl = totalSalesExcl.add(article.getSalesVatAmount(numberOfItems)).setScale(2, RoundingMode.HALF_DOWN);
        }
        return totalSalesExcl;
    }

    public BigDecimal getTotalSalesPriceInclVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            Article article = orderItem.getArticle();
            int numberOfItems = orderItem.getNumberOfItems();
            totalSalesExcl = totalSalesExcl.add(article.getSalesPriceWithVat(numberOfItems)).setScale(2, RoundingMode.HALF_DOWN);
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
}
