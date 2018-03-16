package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesOrder extends Order {

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
}
