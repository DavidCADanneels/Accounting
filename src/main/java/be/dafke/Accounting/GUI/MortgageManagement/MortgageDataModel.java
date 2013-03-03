package be.dafke.Accounting.GUI.MortgageManagement;

import be.dafke.Accounting.Objects.Accounting.Mortgage;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

public class MortgageDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Nr", "Mensualiteit", "Intrest", "Kapitaal", "RestKapitaal" };
	private final Class[] columnClasses = { Integer.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
			BigDecimal.class };
	private ArrayList<Vector<BigDecimal>> data;
	private final Mortgage mortgage;

	public MortgageDataModel(Mortgage mortgage) {
		this.mortgage = mortgage;
		if (mortgage != null) {
			data = mortgage.getTable();
		} else {
			data = new ArrayList<Vector<BigDecimal>>();
		}
	}

	public void revalidate(Mortgage newMortgage) {
		if (newMortgage != null) {
			data = newMortgage.getTable();
		} else {
			data = new ArrayList<Vector<BigDecimal>>();
		}
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return Integer.valueOf(row + 1);
		}
		return data.get(row).get(col - 1);
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 2 || col == 3);
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		if (value instanceof BigDecimal) {
			BigDecimal vorigRestCapital;
			if (row == 0) {
				vorigRestCapital = mortgage.getStartCapital();
			} else {
				vorigRestCapital = (BigDecimal) getValueAt(row - 1, 4);
			}
			BigDecimal amount = (BigDecimal) value;
			Vector<BigDecimal> line = data.get(row);
			BigDecimal mens = line.get(0);
//			BigDecimal total = line.get(3);
			BigDecimal amount2 = mens.subtract(amount);
			if (col == 2) {
				line.set(1, amount); // intrest
				line.set(2, amount2);// capital
				line.set(3, vorigRestCapital.subtract(amount2));
			} else if (col == 3) {
				line.set(2, amount); // capital
				line.set(1, amount2);// intrest
				line.set(3, vorigRestCapital.subtract(amount));
			}
			data.set(row, line);
			fireTableDataChanged();
		}
	}

	public ArrayList<Vector<BigDecimal>> getData() {
		return data;
	}
}
