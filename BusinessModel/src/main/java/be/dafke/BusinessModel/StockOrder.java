package be.dafke.BusinessModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;

public class StockOrder extends Order {

    private Transaction balanceTransaction;

    public BigDecimal getTotalStockValue() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getStockValue()).setScale(2, RoundingMode.HALF_DOWN);
        }
        return total;
    }

    public void addStockOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            article.addStockOrder(this);
        });
    }

    public void setBalanceTransaction(Transaction balanceTransaction) {
        this.balanceTransaction = balanceTransaction;
    }

    public Transaction getBalanceTransaction() {
        return balanceTransaction;
    }
}
