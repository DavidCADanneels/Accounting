package be.dafke.Accounting.Balances;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class TestBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("Accounting").getString("REKENING"),
			java.util.ResourceBundle.getBundle("Accounting").getString("PROEF(DEBET)"),
			java.util.ResourceBundle.getBundle("Accounting").getString("PROEF(CREDIT)"),
			java.util.ResourceBundle.getBundle("Accounting").getString("SALDO(DEBET)"),
			java.util.ResourceBundle.getBundle("Accounting").getString("SALDO(CREDIT)") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
			BigDecimal.class };
	private final Accountings accountings;

	public TestBalanceDataModel(Accountings accountings) {
		this.accountings = accountings;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Accounting accounting = accountings.getCurrentAccounting();
		Account account = accounting.getAccounts().getAccounts(AccountType.getList()).get(row);
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
		if (accountings == null || accountings.getCurrentAccounting() == null) {
			return 0;
		}
		Accounting accounting = accountings.getCurrentAccounting();
		return accounting.getAccounts().size();
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