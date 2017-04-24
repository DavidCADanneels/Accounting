package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class VATTransactionsDataModel extends SelectableTableModel<VATBooking> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = {
			getBundle("Accounting").getString("NR"),
			getBundle("Accounting").getString("DATE"),
			getBundle("VAT").getString("VAT_FIELD"),
			getBundle("VAT").getString("VAT_AMOUNT")
	};
	Class[] columnClasses = {
			String.class,
			String.class,
			VATField.class,
			BigDecimal.class
	};

	private VATTransactions vatTransactions;

	public VATTransactionsDataModel(VATTransactions vatTransactions) {
		this.vatTransactions = vatTransactions;
	}

	public VATBooking getValueAt(int row) {
		ArrayList<VATBooking> boekingen = new ArrayList<>();
		if(vatTransactions==null) return null;
		for(VATTransaction vatTransaction : vatTransactions.getBusinessObjects()){
			boekingen.addAll(vatTransaction.getBusinessObjects());
		}
		return boekingen.get(row);
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		VATBooking vatBooking = getValueAt(row);
		VATTransaction vatTransaction = vatBooking.getVatTransaction();
		boolean first = (vatBooking == vatTransaction.getBusinessObjects().get(0));
		if(vatBooking == null){
			vatBooking = null;
		}
		Transaction transaction = vatTransaction.getTransaction();
		if(transaction==null) return null;
		if (col == 0) {
			if(first)
				return transaction.getAbbreviation() + transaction.getId();
		} else if (col == 1) {
			if(first)
				return Utils.toString(transaction.getDate());
		} else if (col == 2) {
			return vatBooking.getVatField();
		} else if (col == 3) {
			return vatBooking.getVatMovement().getAmount();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if(vatTransactions == null) return 0;
		int size = 0;
		for (VATTransaction vatTransaction:vatTransactions.getBusinessObjects()){
			size += vatTransaction.getBusinessObjects().size();
		}
		return size;
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
		return false;
	}

	// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
//        data[row][col] = value;
	}

	@Override
	public VATBooking getObject(int row, int col) {
		return getValueAt(row);
	}
}