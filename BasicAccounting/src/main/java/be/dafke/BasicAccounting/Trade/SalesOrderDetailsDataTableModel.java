package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PurchaseOrder;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrderDetailsDataTableModel extends SelectableTableModel<OrderItem> {
	public static int NR_OF_ITEMS_COL = 0;
	public static int ITEMS_PER_UNIT_COL = 1;
	public static int NAME_COL = 2;
	public static int PO_COL = 3;
	public static int PRICE_ITEM_COL = 4;
	public static int VAT_RATE_COL = 5;
	public static int TOTAL_EXCL_COL = 6;
	public static int TOTAL_VAT_COL = 7;
	public static int TOTAL_INCL_COL = 8;
	public static int NR_OF_COL = 9;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	protected SalesOrder order;

	public SalesOrderDetailsDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_OF_ITEMS_COL, Integer.class);
		columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(PRICE_ITEM_COL, BigDecimal.class);
		columnClasses.put(TOTAL_EXCL_COL, BigDecimal.class);
		columnClasses.put(TOTAL_VAT_COL, BigDecimal.class);
		columnClasses.put(TOTAL_INCL_COL, BigDecimal.class);
		columnClasses.put(PO_COL, PurchaseOrder.class);
		columnClasses.put(VAT_RATE_COL, Integer.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"));
		columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(PRICE_ITEM_COL, getBundle("Accounting").getString("PRICE_ITEM"));
		columnNames.put(TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"));
		columnNames.put(TOTAL_VAT_COL, getBundle("Accounting").getString("TOTAL_VAT"));
		columnNames.put(TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"));
		columnNames.put(PO_COL, getBundle("Accounting").getString("PO"));
		columnNames.put(VAT_RATE_COL, getBundle("Accounting").getString("VAT_RATE"));
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		OrderItem orderItem = getObject(row, col);
		if (orderItem==null)
			return null;
		if (col == NAME_COL) {
			return orderItem.getName();
		}
		if (col == ITEMS_PER_UNIT_COL) {
			return orderItem.getItemsPerUnit();
		}
		if (col == PRICE_ITEM_COL) {
			return orderItem.getSalesPriceForItem();
		}
		if (col == PO_COL) {
			return orderItem.getPurchaseOrder();
		}
		if (col == VAT_RATE_COL) {
			return orderItem.getSalesVatRate();
		}
		if (col == TOTAL_EXCL_COL) {
			return orderItem.getSalesPriceWithoutVat();
		}
		if (col == TOTAL_INCL_COL) {
			return orderItem.getSalesPriceWithVat();
		}
		if (col == TOTAL_VAT_COL) {
			return orderItem.getSalesVatAmount();
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