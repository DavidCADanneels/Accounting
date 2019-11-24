package be.dafke.BasicAccounting.Mortgages;

import be.dafke.Accounting.BusinessModel.Mortgage;
import be.dafke.Accounting.BusinessModel.MortgageTransaction;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class MortgageDataModel extends AbstractTableModel {
	private final String[] columnNames = {"Nr", "Mensualiteit", "Intrest", "Kapitaal", "RestKapitaal"};
	private final Class[] columnClasses = {Integer.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
			BigDecimal.class};
	private Mortgage data;

	public MortgageDataModel(Mortgage data) {
		this.data = data;
	}

	public void revalidate(Mortgage data) {
		this.data = data;
	}

	public int getColumnCount() {
		return 5;
	}

	public int getRowCount() {
		return data == null ? 0 : data.getBusinessObjects().size();
	}

	/**
	 * Is called to refresh the displayed data from the Mortgages table --> only read data !!!
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return data.getBusinessObjects().get(row).getNr();
		} else if (col == 1) {
			return data.getBusinessObjects().get(row).getMensuality();
		} else if (col == 2) {
			return data.getBusinessObjects().get(row).getIntrest();
		} else if (col == 3) {
			return data.getBusinessObjects().get(row).getCapital();
		} else if (col == 4) {
			return data.getBusinessObjects().get(row).getRestCapital();
		} else return null;
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
	/**
	 * Is called when user updates the value in the table -> update Mortgages table
	 */
	public void setValueAt(Object value, int row, int col) {
		BigDecimal amount = (BigDecimal) value;
		MortgageTransaction mortgageTransaction = data.getBusinessObjects().get(row);
		if (col == 2) {
			mortgageTransaction.setIntrest(amount, true);
		} else if (col == 3) {
			mortgageTransaction.setCapital(amount, true);
		}
//		if(...){ // TODO add option to disable auto update of below rows.
		data.recalculateTable(row);
//		}
		fireTableDataChanged();
	}
}
