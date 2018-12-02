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

public class PurchaseOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<PurchaseOrder> {

	PurchaseOrders purchaseOrders;


	public PurchaseOrdersOverviewDataTableModel(PurchaseOrders purchaseOrders) {
		super();
		columnNames.put(CONTACT_COL, getBundle("Contacts").getString("SUPPLIER"));
		this.purchaseOrders = purchaseOrders;
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
}