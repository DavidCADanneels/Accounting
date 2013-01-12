package be.dafke.Accounting.Balances;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

public class TestBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("REKENING"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("PROEF(DEBET)"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("PROEF(CREDIT)"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("SALDO(DEBET)"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("SALDO(CREDIT)") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
			BigDecimal.class };

//	private final AccountingGUIFrame parent;

//	public TestBalanceDataModel(AccountingGUIFrame parent) {
//		this.parent = parent;
//	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Account account = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.getList()).get(row);
		if (col == 0) return account;
		else if (col == 1) return account.getDebetTotal();
		else if (col == 2) return account.getCreditTotal();
		else if (col == 3) {
			if (account.saldo().compareTo(BigDecimal.ZERO) > 0) return account.saldo();
			return "";
		} else {// col==4)
			if (account.saldo().compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO.subtract(account.saldo());
			return "";
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return Accountings.getCurrentAccounting().getAccounts().size();
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