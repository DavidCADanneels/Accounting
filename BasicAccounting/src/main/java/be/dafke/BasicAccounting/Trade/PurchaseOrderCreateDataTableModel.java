package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author David Danneels
 */

public class PurchaseOrderCreateDataTableModel extends PurchaseOrderViewDataTableModel {
	private final Articles articles;
	private Contact contact;
	private Predicate<Article> filter;
	private TotalsPanel purchaseTotalsPanel;

	public PurchaseOrderCreateDataTableModel(Articles articles, Contact contact, PurchaseOrder order, TotalsPanel purchaseTotalsPanel) {
		super();
		this.purchaseTotalsPanel = purchaseTotalsPanel;
		this.order = order;
		this.articles = articles;
		this.contact = contact;
	}
	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == NR_OF_UNITS_COL || col == NR_OF_ITEMS_COL || col == ITEMS_PER_UNIT_COL || col == PRICE_UNIT_COL);
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		OrderItem orderItem = getObject(row,col);
		if(col == NR_OF_UNITS_COL){
			int nr = (Integer) value;
			orderItem.setNumberOfUnits(nr);
			orderItem.calculateNumberOfItems();
		} else if(col == NR_OF_ITEMS_COL){
			int nr = (Integer) value;
			orderItem.setNumberOfItems(nr);
			orderItem.calculateNumberOfUnits();
		} else if(col == ITEMS_PER_UNIT_COL){
			int nr = (Integer) value;
			orderItem.setItemsPerUnit(nr);
			orderItem.calculateNumberOfItems();
		} else if(col == PRICE_UNIT_COL){
			BigDecimal purchasePriceForUnit = (BigDecimal) value;
			orderItem.setPurchasePriceForUnit(purchasePriceForUnit);
		}
		order.setOrderItem(orderItem);
		purchaseTotalsPanel.fireOrderContentChanged(order);
		fireTableDataChanged();
	}

	public int getRowCount() {
		if(articles==null || contact==null || filter==null) return 0;
		List<Article> businessObjects = articles.getBusinessObjects(filter);
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public OrderItem getObject(int row, int col) {
		if(contact==null || filter==null) return null;
		List<Article> articleList = articles.getBusinessObjects(filter);
		if(articleList == null || articleList.size() == 0) return null;
		Article article = articleList.get(row);
		return order.getBusinessObject(article.getName());
	}

	public void setContact(Contact contact) {
		this.contact = contact;
		filter = Article.ofSupplier(contact);
		order.setSupplier(contact);
		fireTableDataChanged();
	}
}