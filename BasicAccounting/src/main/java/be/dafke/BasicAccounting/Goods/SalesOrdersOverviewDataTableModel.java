package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.SalesOrder;
import be.dafke.BusinessModel.SalesOrders;

import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<SalesOrder> {

	SalesOrders salesOrders;


	public SalesOrdersOverviewDataTableModel(SalesOrders salesOrders) {
		super();
		columnNames.put(CONTACT_COL, getBundle("Contacts").getString("CUSTOMER"));
		this.salesOrders = salesOrders;
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		if (salesOrders == null) return null;
		SalesOrder salesOrder = getObject(row, col);
		if(salesOrder == null) return null;

		if (col == CONTACT_COL) {
			return salesOrder.getCustomer();
		}
		if (col == PRICE_TOTAL_EXCL_COL) {
			return salesOrder.getTotalSalesPriceExclVat();
		}
		if (col == VAT_AMOUNT_COL) {
			return salesOrder.getTotalSalesVat();
		}
		if (col == PRICE_TOTAL_INCL_COL) {
			return salesOrder.getTotalSalesPriceInclVat();
		}
		if (col == ORDER_NR_COL) {
			return salesOrder.getName();
		}
		if (col == DATE_COL) {
			return salesOrder.getDeliveryDate();
		}
		return null;
	}

	@Override
	public int getRowCount() {
		if(salesOrders == null) return 0;
		List<SalesOrder> businessObjects = salesOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public SalesOrder getObject(int row, int col) {
		List<SalesOrder> businessObjects = this.salesOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects.get(row);
	}
}