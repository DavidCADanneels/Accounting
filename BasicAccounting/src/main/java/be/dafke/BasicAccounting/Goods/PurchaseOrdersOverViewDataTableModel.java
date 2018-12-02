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

public class PurchaseOrdersOverViewDataTableModel extends SelectableTableModel<PurchaseOrder> {
	public static int ORDER_NR_COL = 0;
	public static int DATE_COL = 1;
	public static int CONTACT_COL = 2;
	public static int PRICE_TOTAL_EXCL_COL = 3;
	public static int VAT_AMOUNT_COL = 4;
	public static int PRICE_TOTAL_INCL_COL = 5;
	public static int NR_OF_COL = 6;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	PurchaseOrders purchaseOrders;


	public PurchaseOrdersOverViewDataTableModel(PurchaseOrders purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(ORDER_NR_COL, String.class);
		columnClasses.put(DATE_COL, String.class);
		columnClasses.put(CONTACT_COL, Contact.class);
		columnClasses.put(PRICE_TOTAL_EXCL_COL, BigDecimal.class);
		columnClasses.put(VAT_AMOUNT_COL, BigDecimal.class);
		columnClasses.put(PRICE_TOTAL_INCL_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"));
		columnNames.put(DATE_COL, getBundle("Accounting").getString("ITEMS_TO_ORDER"));
		columnNames.put(CONTACT_COL, getBundle("Contacts").getString("SUPPLIER"));
		columnNames.put(PRICE_TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"));
		columnNames.put(VAT_AMOUNT_COL, getBundle("Accounting").getString("TOTAL_VAT"));
		columnNames.put(PRICE_TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		if (purchaseOrders == null) return null;
		PurchaseOrder purchaseOrder = getObject(row, col);
		if(purchaseOrder == null) return null;

		if (col == CONTACT_COL) {
			return purchaseOrder.getSupplier();
		}
		if (col == PRICE_TOTAL_EXCL_COL) {
			return purchaseOrder.getTotalPurchasePriceExclVat();
		}
		if (col == VAT_AMOUNT_COL) {
			return purchaseOrder.getTotalPurchaseVat();
		}
		if (col == PRICE_TOTAL_INCL_COL) {
			return purchaseOrder.getTotalPurchasePriceInclVat();
		}
		if (col == ORDER_NR_COL) {
			return purchaseOrder.getName();
		}
		if (col == DATE_COL) {
			return purchaseOrder.getDate();
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(purchaseOrders == null) return 0;
		List<PurchaseOrder> businessObjects = purchaseOrders.getBusinessObjects();
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
		// No editable cells !
	}

	@Override
	public PurchaseOrder getObject(int row, int col) {
		List<PurchaseOrder> purchaseOrders = this.purchaseOrders.getBusinessObjects();
		if(purchaseOrders == null || purchaseOrders.size() == 0) return null;
		return purchaseOrders.get(row);
	}
}