package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author David Danneels
 */

public class SalesOrderCreateDataTableModel extends SalesOrderViewDataTableModel {
	private final Articles articles;
//	private Predicate<Article> filter = article -> article.getSalesPriceItemWithVat()!=null;
	private Predicate<Article> filter = Article.inStock();
	private TotalsPanel totalsPanel;

	public SalesOrderCreateDataTableModel(Articles articles, SalesOrder order, TotalsPanel totalsPanel) {
		super();
		this.totalsPanel = totalsPanel;
		// articles should be stock instead (stock/articles contains nrInStock, nrSold, ...
		this.articles = articles;
		this.order = order;
	}

	public int getRowCount() {
		List<Article> businessObjects = getArticles();
		if(businessObjects == null) return 0;
		return businessObjects.size();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col==NR_OF_ITEMS_COL || col == PRICE_ITEM_COL || col == VAT_RATE_COL;
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
		} else if (col == PRICE_ITEM_COL){
			BigDecimal amount = (BigDecimal) value;
			orderItem.setSalesPriceForItem(amount);
		} else if (col == VAT_RATE_COL){
            int nr = (Integer) value;
            orderItem.setSalesVatRate(nr);
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