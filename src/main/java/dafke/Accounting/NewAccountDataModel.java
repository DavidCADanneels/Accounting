package be.dafke.Accounting;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

public class NewAccountDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Account (Name)", "Type", "Saldo" };
	private final Class[] columnClasses = { Account.class, String.class, BigDecimal.class };

//	private final AccountingGUIFrame parent;

//	public NewAccountDataModel(AccountingGUIFrame parent) {
//		this.parent = parent;
//	}

	@Override
	public int getColumnCount() {
		return columnClasses.length;
	}

	@Override
	public int getRowCount() {
		return Accountings.getCurrentAccounting().getAccounts().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Account account = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.getList()).get(row);
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
