package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

public class TestBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			getBundle("Accounting").getString("REKENING"),
			getBundle("Accounting").getString("PROEF(DEBET)"),
			getBundle("Accounting").getString("PROEF(CREDIT)"),
			getBundle("Accounting").getString("SALDO(DEBET)"),
			getBundle("Accounting").getString("SALDO(CREDIT)") };
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