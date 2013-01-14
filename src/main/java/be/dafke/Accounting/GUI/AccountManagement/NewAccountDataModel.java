package be.dafke.Accounting.GUI.AccountManagement;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class NewAccountDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Account (Name)", "Type", "Saldo" };
	private final Class[] columnClasses = { Account.class, String.class, BigDecimal.class };
	private final Accountings accountings;

	public NewAccountDataModel(Accountings accountings) {
		this.accountings = accountings;
	}

	public int getColumnCount() {
		return columnClasses.length;
	}

	public int getRowCount() {
		Accounting accounting = accountings.getCurrentAccounting();
		return accounting.getAccounts().size();
	}

	public Object getValueAt(int row, int col) {
		Accounting accounting = accountings.getCurrentAccounting();
		Account account = accounting.getAccounts().getAccounts(AccountType.getList()).get(row);
		if (col == 1) {
			return account.getType();
		} else if (col == 2) {
			AccountType type = account.getType();
			switch (type) {
				case Active:
				case Cost:
				case Credit:
					return account.saldo();
				case Passive:
				case Revenue:
				case Debit:
					return BigDecimal.ZERO.subtract(account.saldo());
				default:
					return "";
			}
		} else return account;
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
