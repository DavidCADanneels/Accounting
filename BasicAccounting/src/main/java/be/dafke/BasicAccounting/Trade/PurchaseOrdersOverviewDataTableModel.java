package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class PurchaseOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<PurchaseOrder> {

	PurchaseOrders purchaseOrders;


	public PurchaseOrdersOverviewDataTableModel() {
		super();
		columnNames.put(CONTACT_COL, getBundle("Contacts").getString("SUPPLIER"));
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
			return purchaseOrder.getDeliveryDate();
		}
		return null;
	}

	@Override
	public int getRowCount() {
		if(purchaseOrders == null) return 0;
		List<PurchaseOrder> businessObjects = purchaseOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public PurchaseOrder getObject(int row, int col) {
		List<PurchaseOrder> purchaseOrders = this.purchaseOrders.getBusinessObjects();
		if(purchaseOrders == null || purchaseOrders.size() == 0) return null;
		return purchaseOrders.get(row);
	}

	public int getRow(PurchaseOrder order) {
		if(purchaseOrders == null) return -1;
		ArrayList<PurchaseOrder> businessObjects = purchaseOrders.getBusinessObjects();
		for(int row=0;row<businessObjects.size();row++){
			if(getObject(row,0)==order) return row;
		}
		return -1;
	}

	public void setAccounting(Accounting accounting) {
		purchaseOrders = accounting.getPurchaseOrders();
		fireTableDataChanged();
	}
}