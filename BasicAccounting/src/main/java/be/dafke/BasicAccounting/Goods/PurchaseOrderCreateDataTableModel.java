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

public class PurchaseOrderCreateDataTableModel extends SelectableTableModel<OrderItem> {
	private final Articles articles;
	public static int NR_OF_UNITS_COL = 0;
	public static int NR_OF_ITEMS_COL = 1;
	public static int NAME_COL = 2;
	public static int HS_COL = 3;
	public static int PRICE_UNIT_COL = 4;
	public static int VAT_RATE_COL = 5;
	public static int PRICE_TOTAL_EXCL_COL = 6;
	public static int VAT_AMOUNT_COL = 7;
	public static int PRICE_TOTAL_INCL_COL = 8;
	public static int NR_OF_COL = 9;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private Contact contact;
	private PurchaseOrder order;
	private Predicate<Article> filter;
	private PurchaseTotalsPanel purchaseTotalsPanel;

	public PurchaseOrderCreateDataTableModel(Articles articles, Contact contact, PurchaseOrder order, PurchaseTotalsPanel purchaseTotalsPanel) {
		this.purchaseTotalsPanel = purchaseTotalsPanel;
		this.order = order;
		this.articles = articles;
		this.contact = contact;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_OF_UNITS_COL, Integer.class);
		columnClasses.put(NR_OF_ITEMS_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(HS_COL, String.class);
		columnClasses.put(PRICE_UNIT_COL, BigDecimal.class);
		columnClasses.put(VAT_RATE_COL, Integer.class);
		columnClasses.put(PRICE_TOTAL_EXCL_COL, BigDecimal.class);
		columnClasses.put(VAT_AMOUNT_COL, BigDecimal.class);
		columnClasses.put(PRICE_TOTAL_INCL_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_OF_UNITS_COL, getBundle("Accounting").getString("UNITS_TO_ORDER"));
		columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"));
		columnNames.put(PRICE_UNIT_COL, getBundle("Accounting").getString("PRICE_UNIT"));
		columnNames.put(VAT_RATE_COL, getBundle("Accounting").getString("VAT_RATE"));
		columnNames.put(PRICE_TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"));
		columnNames.put(VAT_AMOUNT_COL, getBundle("Accounting").getString("TOTAL_VAT"));
		columnNames.put(PRICE_TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		OrderItem orderItem = getObject(row, col);
		if(orderItem==null) return null;
		Article article = orderItem.getArticle();
		if (article == null) return null;

		if (col == NAME_COL) {
			return article.getName();
		}
		if (col == VAT_RATE_COL) {
			return orderItem.getPurchaseVatRate();
		}
		if (col == HS_COL) {
			return article.getHSCode();
		}
		if (col == PRICE_UNIT_COL) {
			return orderItem.getPurchasePriceForUnit();
		}
		if (col == PRICE_TOTAL_EXCL_COL) {
			return orderItem.getPurchasePriceWithoutVat();
		}
		if (col == VAT_AMOUNT_COL) {
			return orderItem.getPurchaseVatAmount();
		}
		if (col == PRICE_TOTAL_INCL_COL) {
			return orderItem.getPurchasePriceWithVat();
		}
		if (col == NR_OF_UNITS_COL) {
			return orderItem.getNumberOfUnits();
		}
		if (col == NR_OF_ITEMS_COL) {
			return orderItem.getNumberOfItems();
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(articles==null || contact==null || filter==null) return 0;
		List<Article> businessObjects = articles.getBusinessObjects(filter);
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
		return col==NR_OF_UNITS_COL;
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

	public void setOrder(PurchaseOrder order) {
		this.order = order;
		fireTableDataChanged();
	}
}