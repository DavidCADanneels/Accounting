package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author David Danneels
 */

public class BuyIngredientsDataTableModel extends IngredientsOrderViewDataTableModel {

	private Ingredients ingredients;

	public BuyIngredientsDataTableModel(IngredientOrder order, Ingredients ingredients) {
		super();
		this.order = order;
		this.ingredients = ingredients;
//		fireTableDataChanged();
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
		return col == QUANTITY_COL;
	}

	// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		IngredientOrderItem ingredientOrderItem = getObject(row, col);
		if(col == QUANTITY_COL) {
			BigDecimal quantity = (BigDecimal) value;
			ingredientOrderItem.setQuantity(quantity);
		}
	}

	@Override
	public int getRowCount() {
		if(ingredients == null) return 0;
		List<Ingredient> businessObjects = ingredients.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	private List<Ingredient> getIngredients(){
		if(ingredients==null) return null;
		List<Ingredient> businessObjects = ingredients.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects;
	}

	@Override
	public IngredientOrderItem getObject(int row, int col) {
		List<Ingredient> articleList = getIngredients();
		if(articleList == null) return null;
		Ingredient ingredient = articleList.get(row);
		return order.getBusinessObject(ingredient.getName());
	}

	public void setAccounting(Accounting accounting) {
		ingredients = accounting.getIngredients();
		fireTableDataChanged();
	}
}