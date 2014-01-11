package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

public class TestBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			getBundle("Balances").getString("ACCOUNT"),
			getBundle("Balances").getString("TEST_DEBIT"),
			getBundle("Balances").getString("TEST_CREDIT"),
			getBundle("Balances").getString("SALDO_DEBIT"),
			getBundle("Balances").getString("SALDO_CREDIT") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, BigDecimal.class,
			BigDecimal.class };
	private final Accounting accounting;

	public TestBalanceDataModel(Accounting accounting) {
		this.accounting = accounting;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Account account = accounting.getAccounts().getAccounts(accounting.getAccountTypes().getBusinessObjects()).get(row);
		if (col == 0) return account;
		else if (col == 1) return account.getDebetTotal();
		else if (col == 2) return account.getCreditTotal();
		else if (col == 3) {
			if (account.getSaldo().compareTo(BigDecimal.ZERO) > 0) return account.getSaldo();
			return "";
		} else {// col==4)
			if (account.getSaldo().compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO.subtract(account.getSaldo());
			return "";
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return accounting.getAccounts().getBusinessObjects().size();
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