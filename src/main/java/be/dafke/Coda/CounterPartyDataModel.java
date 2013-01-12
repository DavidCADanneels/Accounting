package be.dafke.Coda;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import be.dafke.Accounting.Objects.Account;

public class CounterPartyDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Account", "BIC", "Currency", "Account (for Accounting)" };
	private final Class[] columnClasses = { CounterParty.class, String.class, String.class, String.class, Account.class };

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		ArrayList<CounterParty> counterparties = CounterParties.getCounterParties();
		CounterParty c = counterparties.get(row);
		if (col == 0) {
			return c;
		} else if (col == 1) {
			if (c.getBankAccounts().isEmpty()) {
				return "";
			}
			return ((BankAccount) c.getBankAccounts().values().toArray()[0]).getAccountNumber();
		} else if (col == 2) {
			if (c.getBankAccounts().isEmpty()) {
				return "";
			}
			return ((BankAccount) c.getBankAccounts().values().toArray()[0]).getBic();
		} else if (col == 3) {
			if (c.getBankAccounts().isEmpty()) {
				return "";
			}
			return ((BankAccount) c.getBankAccounts().values().toArray()[0]).getCurrency();
		} else if (col == 4) {
			return c.getAccount();
		} else return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return CounterParties.getCounterParties().size();
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
	}
}