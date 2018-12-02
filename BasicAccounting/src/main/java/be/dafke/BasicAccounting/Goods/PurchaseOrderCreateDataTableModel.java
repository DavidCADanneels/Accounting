package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class PurchaseOrderCreateDataTableModel extends PurchaseOrdersViewDataTableModel {
	private final Articles articles;
	private Contact contact;
	private Predicate<Article> filter;
	private PurchaseTotalsPanel purchaseTotalsPanel;

	public PurchaseOrderCreateDataTableModel(Articles articles, Contact contact, PurchaseOrder order, PurchaseTotalsPanel purchaseTotalsPanel) {
		super();
		this.purchaseTotalsPanel = purchaseTotalsPanel;
		this.order = order;
		this.articles = articles;
		this.contact = contact;
	}
	@Override
	public boolean isCellEditable(int row, int col) {
		return (col==NR_OF_UNITS_COL || col == PRICE_UNIT_COL);
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
			order.setOrderItem(orderItem);
			purchaseTotalsPanel.fireOrderContentChanged(order);
		} else if(col == PRICE_UNIT_COL){
			BigDecimal purchasePriceForUnit = (BigDecimal) value;
			orderItem.setPurchasePriceForUnit(purchasePriceForUnit);
		}
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