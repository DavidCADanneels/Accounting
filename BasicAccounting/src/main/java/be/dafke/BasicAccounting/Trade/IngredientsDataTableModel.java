package be.dafke.BasicAccounting.Trade;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class IngredientsDataTableModel extends SelectableTableModel<Ingredient> {
	private final Ingredients ingredients;
	public static int NAME_COL = 0;
	public static int UNIT_COL = 1;
	public static int ALLERGENES_COL = 2;
	public static int NR_OF_COL = 3;
	private final Component parent;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private List<Integer> editableColumns = new ArrayList<>();


	public IngredientsDataTableModel(Component parent, Ingredients ingredients) {
		this.parent = parent;
		this.ingredients = ingredients;
		setColumnNames();
		setColumnClasses();
		setEditableColumns();
	}


	private void setEditableColumns() {
		editableColumns.add(NAME_COL);
		editableColumns.add(UNIT_COL);
	}

	private void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(UNIT_COL, Unit.class);
		columnClasses.put(ALLERGENES_COL, String.class);
	}

	private void setColumnNames() {
		columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"));
		columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"));
		columnNames.put(ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Ingredient ingredient = getObject(row, col);
		if(ingredient==null) return null;
		if (col == NAME_COL) {
			return ingredient.getName();
		}
		if (col == UNIT_COL) {
			return ingredient.getUnit();
		}
		if (col == ALLERGENES_COL) {
			Set<Allergene> allergenes = ingredient.getAllergenes();
			return allergenes.stream().sorted().map(Allergene::getName).collect(Collectors.joining(","));
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(ingredients == null){
			return 0;
		}
		return ingredients.getBusinessObjects().size();
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
		return editableColumns.contains(col);
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Ingredient ingredient = getObject(row,col);
		if(col == UNIT_COL){
			ingredient.setUnit((Unit) value);
		}
		if(col == NAME_COL) {
//            article.setName((String) value);
			String oldName = ingredient.getName();
			String newName = (String) value;
			if (newName != null && !oldName.trim().equals(newName.trim())) {
				try {
					ingredients.modifyName(oldName, newName);
				} catch (DuplicateNameException e) {
					ActionUtils.showErrorMessage(parent, ActionUtils.INGREDIENT_DUPLICATE_NAME, newName.trim());
				} catch (EmptyNameException e) {
					ActionUtils.showErrorMessage(parent, ActionUtils.INGREDIENT_NAME_EMPTY);
				}
			}
		}
		fireTableDataChanged();
	}

	@Override
	public Ingredient getObject(int row, int col) {
		return ingredients.getBusinessObjects().get(row);
	}
}