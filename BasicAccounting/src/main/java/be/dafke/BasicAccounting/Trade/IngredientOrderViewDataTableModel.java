package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class IngredientOrderViewDataTableModel extends SelectableTableModel<IngredientOrderItem> {
	public static int QUANTITY_COL = 0;
	public static int UNIT_COL = 1;
	public static int INGREDIENT_NAME_COL = 2;
	public static int ARTICLE_COL = 3;
	public static int NR_OF_COL = 4;
	protected HashMap<Integer,String> columnNames = new HashMap<>();
	protected HashMap<Integer,Class> columnClasses = new HashMap<>();
	protected IngredientOrder order;

	public IngredientOrderViewDataTableModel() {
		setColumnNames();
		setColumnClasses();
	}

	protected void setColumnClasses() {
		columnClasses.put(QUANTITY_COL, BigDecimal.class);
		columnClasses.put(UNIT_COL, String.class);
		columnClasses.put(INGREDIENT_NAME_COL, BigDecimal.class);
		columnClasses.put(ARTICLE_COL, Article.class);
	}

	protected void setColumnNames() {
		columnNames.put(QUANTITY_COL, getBundle("Accounting").getString("QUANTITY"));
		columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"));
		columnNames.put(INGREDIENT_NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"));
		columnNames.put(ARTICLE_COL, getBundle("Accounting").getString("ARTICLE"));
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		IngredientOrderItem orderItem = getObject(row, col);
		if (orderItem == null) return null;
		Ingredient ingredient = orderItem.getIngredient();
		if (col == ARTICLE_COL) {
			Article article = orderItem.getArticle();
			return article==null?null:article.getName();
		}
		if (col == INGREDIENT_NAME_COL) {
			return ingredient == null?null:ingredient.getName();
		}
		if (col == QUANTITY_COL) {
			return orderItem.getQuantity();
		}
		if (col == UNIT_COL) {
			return ingredient == null?null:ingredient.getUnit();
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(order==null) return 0;
		List<IngredientOrderItem> businessObjects = order.getBusinessObjects();
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
	public IngredientOrderItem getObject(int row, int col) {
		List<IngredientOrderItem> orderItems = order.getBusinessObjects();
		if(orderItems == null || orderItems.size() == 0) return null;
		return orderItems.get(row);
	}

	public IngredientOrder getOrder() {
		return order;
	}

	public void setOrder(IngredientOrder order) {
		this.order = order;
		fireTableDataChanged();
	}

}