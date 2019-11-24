package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class PromoOrderViewDataTableModel extends SelectableTableModel<OrderItem> {
	public static int NR_OF_ITEMS_COL = 0;
	public static int NAME_COL = 1;
	public static int PURCHASE_PRICE_ITEM_COL = 2;
	public static int PURCHASE_PRICE_UNIT_COL = 3;
	public static int TOTAL_PURCHASE_PRICE_COL = 4;
	public static int NR_OF_COL = 5;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	protected PromoOrder order;

	public PromoOrderViewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NR_OF_ITEMS_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(PURCHASE_PRICE_ITEM_COL, BigDecimal.class);
		columnClasses.put(PURCHASE_PRICE_UNIT_COL, BigDecimal.class);
		columnClasses.put(TOTAL_PURCHASE_PRICE_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_OF_ITEMS_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ARTICLE_NAME"));
		columnNames.put(PURCHASE_PRICE_ITEM_COL, getBundle("Accounting").getString("PRICE_ITEM"));
		columnNames.put(PURCHASE_PRICE_UNIT_COL, getBundle("Accounting").getString("PRICE_UNIT"));
		columnNames.put(TOTAL_PURCHASE_PRICE_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"));
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
		if (col == PURCHASE_PRICE_ITEM_COL) {
			return orderItem.getPurchasePriceForItem();
		}
		if (col == PURCHASE_PRICE_UNIT_COL) {
			return orderItem.getPurchasePriceForUnit();
		}
		if (col == TOTAL_PURCHASE_PRICE_COL) {
			return orderItem.getStockValue();
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
		if(order==null) return null;
		List<OrderItem> orderItems = order.getBusinessObjects();
		if(orderItems == null || orderItems.size() == 0) return null;
		return orderItems.get(row);
	}

	public PromoOrder getOrder() {
		return order;
	}

	public void setOrder(PromoOrder order) {
		this.order = order;
		fireTableDataChanged();
	}

}