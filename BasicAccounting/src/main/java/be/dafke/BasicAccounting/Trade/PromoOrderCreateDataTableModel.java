package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.Article;
import be.dafke.Accounting.BusinessModel.Articles;
import be.dafke.Accounting.BusinessModel.OrderItem;
import be.dafke.Accounting.BusinessModel.PromoOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public class PromoOrderCreateDataTableModel extends PromoOrderViewDataTableModel {
    private final Articles articles;
    private Predicate<Article> filter = Article.inStock();
    private TotalsPanel totalsPanel;

    public PromoOrderCreateDataTableModel(Articles articles, PromoOrder order, TotalsPanel totalsPanel) {
        super();
        this.articles = articles;
        this.totalsPanel = totalsPanel;
        this.order = order;
    }


    public int getRowCount() {
        List<Article> businessObjects = getArticles();
        if(businessObjects == null) return 0;
        return businessObjects.size();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col==NR_OF_ITEMS_COL || col == PURCHASE_PRICE_UNIT_COL;
    }

    private List<Article> getArticles(){
        if(articles==null || filter==null) return null;
        List<Article> businessObjects = articles.getBusinessObjects(filter);
        if(businessObjects == null) return null;
        return businessObjects;
    }


    // DE SET METHODEN
// ===============
    @Override
    public void setValueAt(Object value, int row, int col) {
        OrderItem orderItem = getObject(row,col);
        if(col == NR_OF_ITEMS_COL){
            int nr = (Integer) value;
            orderItem.setNumberOfItems(nr);
        } else if(col == PURCHASE_PRICE_UNIT_COL){
            BigDecimal amount = (BigDecimal) value;
            orderItem.setPurchasePriceForUnit(amount);
        }
        order.setOrderItem(orderItem);
        totalsPanel.fireOrderContentChanged(order);
        fireTableDataChanged();
    }

    @Override
    public OrderItem getObject(int row, int col) {
        List<Article> articleList = getArticles();
        if(articleList == null) return null;
        Article article = articleList.get(row);
        return order.getBusinessObject(article.getName());
    }
}
