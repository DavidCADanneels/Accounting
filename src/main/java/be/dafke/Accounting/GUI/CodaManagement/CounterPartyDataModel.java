package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;

import javax.swing.table.AbstractTableModel;

public class CounterPartyDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Account", "BIC", "Currency", "Account (for Accounting)" };
	private final Class[] columnClasses = { CounterParty.class, String.class, String.class, String.class, Account.class };

	private final Accountings accountings;

	public CounterPartyDataModel(Accountings accountings) {
		this.accountings = accountings;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		CounterParties counterParties = accountings.getCurrentAccounting().getCounterParties();
		CounterParty c = counterParties.getCounterParties().get(row);
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
		if (accountings == null || accountings.getCurrentAccounting() == null) {
			return 0;
		}
		CounterParties counterParties = accountings.getCurrentAccounting().getCounterParties();
		return counterParties.getCounterParties().size();
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