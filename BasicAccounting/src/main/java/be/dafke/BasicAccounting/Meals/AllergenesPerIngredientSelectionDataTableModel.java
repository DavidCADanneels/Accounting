package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Allergene;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.BusinessModel.Ingredient;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AllergenesPerIngredientSelectionDataTableModel extends SelectableTableModel<Allergene> {
	private Allergenes allergenes;
	private Ingredient ingredient;
	public static int CHECKED_COL = 0;
	public static int NAME_COL = 1;
	public static int DESC_COL = 2;
	public static int NR_OF_COL = 3;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();

	public AllergenesPerIngredientSelectionDataTableModel(Ingredient ingredient, Allergenes allergenes){
		this.allergenes = allergenes;
		this.ingredient = ingredient;
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(CHECKED_COL, Boolean.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(DESC_COL, String.class);
	}

	private void setColumnNames() {
		columnNames.put(CHECKED_COL, getBundle("Accounting").getString("ALLERGENES_CHECKED"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ALLERGENES_NAME"));
		columnNames.put(DESC_COL, getBundle("Accounting").getString("ALLERGENES_DESC"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Allergene allergene = getObject(row, col);
		if(allergene==null) return null;
		if (col == CHECKED_COL) {
			return ingredient.getAllergenes().getBusinessObjects().contains(allergene);
		}
		if (col == NAME_COL) {
			return allergene.getShortName();
		}
		if (col == DESC_COL) {
			return allergene.getDescription();
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
		if(allergenes == null){
			return 0;
		}
		return allergenes.getBusinessObjects().size();
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
		return col == CHECKED_COL;
	}


	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Allergene allergene = getObject(row,col);
		if(col == CHECKED_COL){
			Boolean checked = (Boolean) value;
			if(checked){
				ingredient.addAllergene(allergene);
			} else {
				ingredient.removeAllergene(allergene);
			}
		}
	}

	@Override
	public Allergene getObject(int row, int col) {
		return allergenes.getBusinessObjects().get(row);
	}
}