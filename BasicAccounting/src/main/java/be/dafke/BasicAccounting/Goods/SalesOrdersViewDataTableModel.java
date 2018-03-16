package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Order;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrdersViewDataTableModel extends SelectableTableModel<OrderItem> {
	public static int NR_COL = 0;
	public static int NAME_COL = 1;
	public static int HS_COL = 2;
	public static int PRICE_COL = 3;
	public static int VAT_COL = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private Order order;

	public SalesOrdersViewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(HS_COL, String.class);
		columnClasses.put(PRICE_COL, BigDecimal.class);
		columnClasses.put(VAT_COL, Integer.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_COL, getBundle("Accounting").getString("NR_TO_ORDER"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"));
		columnNames.put(PRICE_COL, getBundle("Accounting").getString("ARTICLE_PURCHASE_PRICE"));
		columnNames.put(VAT_COL, getBundle("Accounting").getString("ARTICLE_VAT"));
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
		if (col == VAT_COL) {
			return article.getPurchaseVatRate();
		}
		if (col == HS_COL) {
			return article.getHSCode();
		}
		if (col == PRICE_COL) {
			return article.getPurchasePrice();
		}
		if (col == NR_COL) {
			return orderItem.getNumberOfItems();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.size();
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
		OrderItem orderItem = getObject(row,col);
		if(col == NR_COL){
			int nr = (Integer) value;
			orderItem.setNumberOfItems(nr);
			orderItem.calculateNumberOfUnits();
//			order.setItem(orderItem);
		}
	}

	@Override
	public OrderItem getObject(int row, int col) {
		List<OrderItem> orderItems = order.getBusinessObjects();
		if(orderItems == null || orderItems.size() == 0) return null;
		return orderItems.get(row);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
		fireTableDataChanged();
	}

}