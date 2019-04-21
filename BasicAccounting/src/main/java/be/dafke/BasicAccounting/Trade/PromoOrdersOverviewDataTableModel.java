package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.PromoOrder;
import be.dafke.BusinessModel.PromoOrders;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class PromoOrdersOverviewDataTableModel extends SelectableTableModel<PromoOrder> {
	public static int ORDER_NR_COL = 0;
	public static int DATE_COL = 1;
	public static int TOTAL_VALUE_COL = 2;
	public static int NR_OF_COL = 3;
	protected HashMap<Integer,String> columnNames = new HashMap<>();
	protected HashMap<Integer,Class> columnClasses = new HashMap<>();

	private PromoOrders promoOrders;

	public PromoOrdersOverviewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	protected void setColumnClasses() {
		columnClasses.put(ORDER_NR_COL, String.class);
		columnClasses.put(DATE_COL, String.class);
		columnClasses.put(TOTAL_VALUE_COL, BigDecimal.class);
	}

	protected void setColumnNames() {
		columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"));
		columnNames.put(DATE_COL, getBundle("Accounting").getString("DELIVERY_DATE"));
		columnNames.put(TOTAL_VALUE_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"));
	}

	public int getColumnCount() {
		return NR_OF_COL;
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

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		if (promoOrders == null) return null;
		PromoOrder promoOrder = getObject(row, col);
		if(promoOrder == null) return null;

		if (col == TOTAL_VALUE_COL) {
			return promoOrder.getTotalStockValue();
		}
		if (col == ORDER_NR_COL) {
			return promoOrder.getName();
		}
		if (col == DATE_COL) {
			return promoOrder.getDeliveryDate();
		}
		return null;
	}

	@Override
	public int getRowCount() {
		if(promoOrders == null) return 0;
		List<PromoOrder> businessObjects = promoOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public PromoOrder getObject(int row, int col) {
		List<PromoOrder> businessObjects = this.promoOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects.get(row);
	}

	public void setAccounting(Accounting accounting) {
		promoOrders = accounting.getPromoOrders();
		fireTableDataChanged();
	}
}