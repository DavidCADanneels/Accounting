package be.dafke.Balances.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.ComponentModel.RefreshableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class TestBalanceDataModel extends RefreshableTableModel<Account> {
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
	private final Accounts accounts;
	private final AccountTypes accountTypes;

	public TestBalanceDataModel(final Accounts accounts, final AccountTypes accountTypes) {
		this.accounts = accounts;
		this.accountTypes = accountTypes;
	}

	// DE GET METHODEN
	// ===============
	public Object getValueAt(int row, int col) {
		Account account = getObject(row, col);
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

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return accounts.getBusinessObjects().size();
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

	@Override
	public Account getObject(int row, int col) {
		return accounts.getAccounts(accountTypes.getBusinessObjects()).get(row);
	}

	@Override
	public int getRow(Account o) {
		int row = 0;
		ArrayList<Account> accountList = accounts.getAccounts(accountTypes.getBusinessObjects());
		for(Account account : accountList){
			if(account != o){
				row++;
			}
			else return row;
		}
		return 0;
	}
}