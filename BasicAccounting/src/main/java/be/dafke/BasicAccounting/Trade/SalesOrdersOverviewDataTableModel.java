package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.Accounting.BusinessModel.SalesOrder;
import be.dafke.Accounting.BusinessModel.SalesOrders;

import java.util.List;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class SalesOrdersOverviewDataTableModel extends OrdersOverviewDataTableModel<SalesOrder> {

	SalesOrders salesOrders;
	Predicate<SalesOrder> filter;

	public SalesOrdersOverviewDataTableModel(){
		super();
		columnNames.put(CONTACT_COL, getBundle("Contacts").getString("CUSTOMER"));
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
		List<SalesOrder> businessObjects = filter==null?salesOrders.getBusinessObjects():salesOrders.getBusinessObjects(filter);
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public SalesOrder getObject(int row, int col) {
		List<SalesOrder> businessObjects = filter==null?salesOrders.getBusinessObjects():salesOrders.getBusinessObjects(filter);
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects.get(row);
	}

	public void setAccounting(Accounting accounting) {
		salesOrders = accounting.getSalesOrders();
		fireTableDataChanged();
	}

	public void setFilter(Predicate<SalesOrder> filter) {
		this.filter = filter;
	}
}