package be.dafke.Accounting.BusinessModel

import java.math.RoundingMode
import java.util.function.Predicate

class SalesOrder extends Order {

    private Transaction salesTransaction, gainTransaction
    private String invoiceNumber = ""
    private boolean invoice = false
    private boolean promoOrder = false

    BigDecimal getTotalSalesPriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getSalesPriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN)
        }
        total
    }

    BigDecimal getTotalSalesPriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithoutVat()).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesExcl
    }

    BigDecimal getTotalSalesVat() {
        BigDecimal totalSalesVat = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesVat = totalSalesVat.add(orderItem.getSalesVatAmount()).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesVat
    }

    BigDecimal getTotalSalesVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesVatAmount()).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesExcl
    }

    BigDecimal getTotalSalesPriceInclVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithVat()).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesExcl
    }

    BigDecimal getTotalSalesPriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.getSalesPriceWithVat().setScale(2, RoundingMode.HALF_DOWN))
        }
        totalSalesExcl
    }

    Transaction getSalesTransaction() {
        salesTransaction
    }

    void setSalesTransaction(Transaction salesTransaction) {
        this.salesTransaction = salesTransaction

        if(!isCreditNote()) {
            getBusinessObjects().forEach({ orderItem ->
                Article article = orderItem.getArticle()
                int numberOfItems = orderItem.getNumberOfItems()
                // TODO: do not 'setSoOrdered' for PromoOrder
                article.setSoOrdered(numberOfItems)
            })
        } else {
            getBusinessObjects().forEach({ orderItem ->
                Article article = orderItem.getArticle()
                int numberOfItems = orderItem.getNumberOfItems()
                article.setSoCnOrdered(numberOfItems)
            })
        }
    }

    @Override
    boolean isEditable(){
        // TODO: change into: 'salesTransaction==null||!salesTransaction.isVatBooked()'
        salesTransaction==null
    }

    Transaction getGainTransaction() {
        gainTransaction
    }

    void setGainTransaction(Transaction gainTransaction) {
        this.gainTransaction = gainTransaction
    }

    BigDecimal calculateTotalSalesVat() {
        getTotalSalesPriceInclVat().subtract(getTotalSalesPriceExclVat()).setScale(2, RoundingMode.HALF_DOWN)
    }

    String getInvoiceNumber() {
        invoiceNumber
    }

    void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber
    }

    boolean isInvoice() {
        invoice
    }

    void setInvoice(boolean invoice) {
        this.invoice = invoice
    }

    void addSalesOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.getArticle()
            article.addSalesOrder(this)
        })
    }

    boolean isPromoOrder() {
        promoOrder
    }

}
