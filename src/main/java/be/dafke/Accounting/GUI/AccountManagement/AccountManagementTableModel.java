package be.dafke.Accounting.GUI.AccountManagement;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class AccountManagementTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Account (Name)", "Type", "Saldo" };
	private final Class[] columnClasses = { Account.class, String.class, BigDecimal.class };
	private final Accounting accounting;

	public AccountManagementTableModel(Accounting accounting) {
		this.accounting = accounting;
	}

	public int getColumnCount() {
		return columnClasses.length;
	}

	public int getRowCount() {
		return accounting.getAccounts().getBusinessObjects().size();
	}

	public Object getValueAt(int row, int col) {
		Account account = accounting.getAccounts().getAccounts(AccountType.getList()).get(row);
		if (col == 1) {
			return account.getAccountType();
		} else if (col == 2) {
			AccountType type = account.getAccountType();
			switch (type) {
				case Active:
				case Cost:
				case Credit:
					return account.getSaldo();
				case Passive:
				case Revenue:
				case Debit:
					return BigDecimal.ZERO.subtract(account.getSaldo());
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
