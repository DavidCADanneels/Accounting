package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrderCreateDataTableModel extends SelectableTableModel<OrderItem> {
	private final Articles articles;
	public static int NR_OF_UNITS_COL = 0;
	public static int NR_OF_ITEMS_COL = 1;
	public static int ITEMS_PER_UNIT_COL = 2;
	public static int NAME_COL = 3;
	public static int SUPPLIER_COL = 4;
	public static int PRICE_ITEM_COL = 5;
	public static int PRICE_UNIT_COL = 6;
	public static int VAT_RATE_COL = 7;
	public static int TOTAL_EXCL_COL = 8;
	public static int TOTAL_VAT_COL = 9;
	public static int TOTAL_INCL_COL = 10;
	public static int NR_OF_COL = 11;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private Contact contact;
	private SalesOrder order;
	private SaleTotalsPanel saleTotalsPanel;

	public SalesOrderCreateDataTableModel(Articles articles, Contact contact, SalesOrder order, SaleTotalsPanel saleTotalsPanel) {
		this.saleTotalsPanel = saleTotalsPanel;
		this.articles = articles;
		this.contact = contact;
		this.order = order;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_OF_UNITS_COL, Integer.class);
		columnClasses.put(NR_OF_ITEMS_COL, Integer.class);
		columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(PRICE_ITEM_COL, BigDecimal.class);
		columnClasses.put(PRICE_UNIT_COL, BigDecimal.class);
		columnClasses.put(TOTAL_EXCL_COL, BigDecimal.class);
		columnClasses.put(TOTAL_VAT_COL, BigDecimal.class);
		columnClasses.put(TOTAL_INCL_COL, BigDecimal.class);
		columnClasses.put(SUPPLIER_COL, Contact.class);
		columnClasses.put(VAT_RATE_COL, Integer.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_OF_UNITS_COL, getBundle("Accounting").getString("UNITS_TO_ORDER"));
		columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"));
		columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ARTICLE_ITEMS_PER_UNIT"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(PRICE_ITEM_COL, getBundle("Accounting").getString("ARTICLE_SALES_PRICE_ITEM"));
		columnNames.put(PRICE_UNIT_COL, getBundle("Accounting").getString("ARTICLE_SALES_PRICE_UNIT"));
		columnNames.put(TOTAL_EXCL_COL, getBundle("Accounting").getString("ARTICLE_SALES_VAT_EXCL"));
		columnNames.put(TOTAL_VAT_COL, getBundle("Accounting").getString("ARTICLE_SALES_VAT_TOTAL"));
		columnNames.put(TOTAL_INCL_COL, getBundle("Accounting").getString("ARTICLE_SALES_VAT_INCL"));
		columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"));
		columnNames.put(VAT_RATE_COL, getBundle("Accounting").getString("ARTICLE_VAT"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		OrderItem orderItem = getObject(row, col);
		if (orderItem==null) return null;
		Article article = orderItem.getArticle();
		if (article == null) return null;

		if (col == NAME_COL) {
			return article.getName();
		}
		if (col == ITEMS_PER_UNIT_COL) {
			return article.getItemsPerUnit();
		}
		if (col == PRICE_ITEM_COL) {
			return article.getSalesPriceSingleWithVat();
		}
		if (col == PRICE_UNIT_COL) {
			return article.getSalesPricePromoWithVat();
		}
		if (col == SUPPLIER_COL) {
			return article.getSupplier();
		}
		if (col == VAT_RATE_COL) {
			return article.getSalesVatRate();
		}
		else {
			if (order == null) return null;
			OrderItem item = order.getBusinessObject(article.getName());
			if (col == TOTAL_EXCL_COL) {
				return article.getSalesPriceWithoutVat(item.getNumberOfItems());
			}
			if (col == TOTAL_INCL_COL) {
				return article.getSalesPriceWithVat(item.getNumberOfItems());
			}
			if (col == TOTAL_VAT_COL) {
				return article.getSalesVatAmount(item.getNumberOfItems());
			}
			if (col == NR_OF_UNITS_COL) {
				return item == null ? 0 : item.getNumberOfUnits();
			}
			if (col == NR_OF_ITEMS_COL) {
				return item == null ? 0 : item.getNumberOfItems();
			}
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(articles==null || contact==null) return 0;
		List<Article> businessObjects = articles.getBusinessObjects(article -> article.getSalesPriceWithVat()!=null);
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col==NR_OF_UNITS_COL || col==NR_OF_ITEMS_COL;
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
			saleTotalsPanel.fireOrderContentChanged(order);
		} else if(col == NR_OF_ITEMS_COL){
			int nr = (Integer) value;
			orderItem.setNumberOfItems(nr);
			orderItem.calculateNumberOfUnits();
			order.setOrderItem(orderItem);
			saleTotalsPanel.fireOrderContentChanged(order);
		}
	}

	@Override
	public OrderItem getObject(int row, int col) {
		List<Article> articleList = articles.getBusinessObjects();
		if(articleList == null || articleList.size() == 0) return null;
		Article article = articleList.get(row);
		return order.getBusinessObject(article.getName());
	}

	public void setContact(Contact contact) {
		this.contact = contact;
		order.setCustomer(contact);
		fireTableDataChanged();
	}

	public void setOrder(SalesOrder order) {
		this.order = order;
		fireTableDataChanged();
	}
}