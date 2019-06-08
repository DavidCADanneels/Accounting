package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AllergenesDataTableModel extends SelectableTableModel<Allergene> {
	private Allergenes allergenes;
	public static int ID_COL = 0;
	public static int NAME_COL = 1;
	public static int DESC_COL = 2;
	public static int NR_OF_COL = 3;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private List<Integer> editableColumns = new ArrayList<>();

	public AllergenesDataTableModel() {
		setColumnNames();
		setColumnClasses();
		setEditableColumns();
	}


	private void setEditableColumns() {
		editableColumns.add(NAME_COL);
		editableColumns.add(DESC_COL);
	}

	private void setColumnClasses() {
		columnClasses.put(ID_COL, String.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(DESC_COL, String.class);
	}

	private void setColumnNames() {
		columnNames.put(ID_COL, getBundle("Accounting").getString("ALLERGENES_ID"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ALLERGENES_NAME"));
		columnNames.put(DESC_COL, getBundle("Accounting").getString("ALLERGENES_DESC"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Allergene allergene = getObject(row, col);
		if(allergene==null) return null;
		if (col == ID_COL) {
			return allergene.getName();
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
		return editableColumns.contains(col);
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Allergene allergene = getObject(row,col);
		if(col == NAME_COL){
			allergene.setShortName((String)value);
		}
		if(col == DESC_COL) {
			allergene.setDescription((String)value);
		}
		fireTableDataChanged();
	}

	public void setAllergenes(Allergenes allergenes) {
		this.allergenes = allergenes;
		fireTableDataChanged();
	}

	@Override
	public Allergene getObject(int row, int col) {
		return allergenes.getBusinessObjects().get(row);
	}
}