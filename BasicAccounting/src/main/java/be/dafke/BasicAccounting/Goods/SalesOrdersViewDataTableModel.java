package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Order;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrdersViewDataTableModel extends SelectableTableModel<OrderItem> {
	public static int NR_OF_UNITS_COL = 0;
	public static int NR_OF_ITEMS_COL = 1;
	public static int NAME_COL = 2;
	public static int TOTAL_EXCL_COL = 3;
	public static int TOTAL_VAT_COL = 4;
	public static int TOTAL_INCL_COL = 5;
	public static int NR_OF_COL = 6;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private SalesOrder order;

	public SalesOrdersViewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_OF_UNITS_COL, Integer.class);
		columnClasses.put(NR_OF_ITEMS_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(TOTAL_EXCL_COL, BigDecimal.class);
		columnClasses.put(TOTAL_VAT_COL, BigDecimal.class);
		columnClasses.put(TOTAL_INCL_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_OF_UNITS_COL, getBundle("Accounting").getString("UNITS_TO_ORDER"));
		columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(TOTAL_EXCL_COL, getBundle("Accounting").getString("ARTICLE_SALES_VAT_EXCL"));
		columnNames.put(TOTAL_VAT_COL, getBundle("Accounting").getString("ARTICLE_SALES_VAT_TOTAL"));
		columnNames.put(TOTAL_INCL_COL, getBundle("Accounting").getString("ARTICLE_SALES_VAT_INCL"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		if (order == null) return null;

		OrderItem orderItem = getObject(row, col);
		if(orderItem ==null) return null;

		Article article = orderItem.getArticle();
		if (article == null) return null;

		if (col == NAME_COL) {
			return article.getName();
		}
		else {
			if (order == null) return null;
			OrderItem item = order.getBusinessObject(article.getName());
			if(item==null) return null;
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
				return item.getNumberOfUnits();
			}
			if (col == NR_OF_ITEMS_COL) {
				return item.getNumberOfItems();
			}
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(order==null) return 0;
		List<OrderItem> businessObjects = order.getBusinessObjects();
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
		return false;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		// No editable fields
	}

	@Override
	public OrderItem getObject(int row, int col) {
		List<OrderItem> orderItems = order.getBusinessObjects();
		if(orderItems == null || orderItems.size() == 0) return null;
		return orderItems.get(row);
	}

	public SalesOrder getOrder() {
		return order;
	}

	public void setOrder(SalesOrder order) {
		this.order = order;
		fireTableDataChanged();
	}

}