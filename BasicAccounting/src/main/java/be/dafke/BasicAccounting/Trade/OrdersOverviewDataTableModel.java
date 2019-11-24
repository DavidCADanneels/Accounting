package be.dafke.BasicAccounting.Trade;

import be.dafke.Accounting.BusinessModel.Contact;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public abstract class OrdersOverviewDataTableModel<T> extends SelectableTableModel<T> {
	public static int ORDER_NR_COL = 0;
	public static int DATE_COL = 1;
	public static int CONTACT_COL = 2;
	public static int PRICE_TOTAL_EXCL_COL = 3;
	public static int VAT_AMOUNT_COL = 4;
	public static int PRICE_TOTAL_INCL_COL = 5;
	public static int NR_OF_COL = 6;
	protected HashMap<Integer,String> columnNames = new HashMap<>();
	protected HashMap<Integer,Class> columnClasses = new HashMap<>();

	public OrdersOverviewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	protected void setColumnClasses() {
		columnClasses.put(ORDER_NR_COL, String.class);
		columnClasses.put(DATE_COL, String.class);
		columnClasses.put(CONTACT_COL, Contact.class);
		columnClasses.put(PRICE_TOTAL_EXCL_COL, BigDecimal.class);
		columnClasses.put(VAT_AMOUNT_COL, BigDecimal.class);
		columnClasses.put(PRICE_TOTAL_INCL_COL, BigDecimal.class);
	}

	protected void setColumnNames() {
		columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"));
		columnNames.put(DATE_COL, getBundle("Accounting").getString("DELIVERY_DATE"));
		columnNames.put(CONTACT_COL, getBundle("Contacts").getString("CONTACT"));
		columnNames.put(PRICE_TOTAL_EXCL_COL, getBundle("Accounting").getString("TOTAL_VAT_EXCL"));
		columnNames.put(VAT_AMOUNT_COL, getBundle("Accounting").getString("TOTAL_VAT"));
		columnNames.put(PRICE_TOTAL_INCL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"));
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		return 0;
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
	public abstract T getObject(int row, int col);
}