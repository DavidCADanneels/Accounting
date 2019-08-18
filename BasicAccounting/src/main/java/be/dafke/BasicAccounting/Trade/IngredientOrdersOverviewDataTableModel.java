package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.IngredientOrder;
import be.dafke.BusinessModel.IngredientOrders;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class IngredientOrdersOverviewDataTableModel extends SelectableTableModel<IngredientOrder> {
	public static int ORDER_NR_COL = 0;
	public static int DATE_COL = 1;
	public static int NR_OF_COL = 2;
	protected HashMap<Integer,String> columnNames = new HashMap<>();
	protected HashMap<Integer,Class> columnClasses = new HashMap<>();

	private IngredientOrders ingredientOrders;

	public IngredientOrdersOverviewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	protected void setColumnClasses() {
		columnClasses.put(ORDER_NR_COL, String.class);
		columnClasses.put(DATE_COL, String.class);
	}

	protected void setColumnNames() {
		columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"));
		columnNames.put(DATE_COL, getBundle("Accounting").getString("TRANSFER_DATE"));
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
		if (ingredientOrders == null) return null;
		IngredientOrder ingredientOrder = getObject(row, col);
		if(ingredientOrder == null) return null;

		if (col == ORDER_NR_COL) {
			return ingredientOrder.getName();
		}
		if (col == DATE_COL) {
			return "";
		}
		return null;
	}

	@Override
	public int getRowCount() {
		if(ingredientOrders == null) return 0;
		List<IngredientOrder> businessObjects = ingredientOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public IngredientOrder getObject(int row, int col) {
		List<IngredientOrder> businessObjects = ingredientOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects.get(row);
	}

	public void setAccounting(Accounting accounting) {
		ingredientOrders = accounting.getIngredientOrders();
		fireTableDataChanged();
	}
}