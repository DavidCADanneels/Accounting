package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableTableModel;

import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

public class AccountManagementTableModel extends RefreshableTableModel<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			getBundle("Accounting").getString("ACCOUNT_NAME"),
			getBundle("Accounting").getString("ACCOUNT_NUMBER"),
            getBundle("Accounting").getString("TYPE"),
			getBundle("Accounting").getString("SALDO"),
            getBundle("BusinessActions").getString("DEFAULT_AMOUNT")};
	private final Class[] columnClasses = { Account.class, Integer.class, String.class, BigDecimal.class,  BigDecimal.class };
	private final Accounts accounts;

	public AccountManagementTableModel(Accounts accounts) {
		this.accounts = accounts;
	}

	public int getColumnCount() {
		return columnClasses.length;
	}

	public int getRowCount() {
		return accounts.getBusinessObjects().size();
	}

	public Object getValueAt(int row, int col) {
		Account account = accounts.getBusinessObjects().get(row);
		if (col == 1) {
			return account.getNumber();
		} else if (col == 2) {
			return account.getType();
		} else if (col == 3) {
			AccountType type = account.getType();
            BigDecimal saldo = account.getSaldo();
            if(type.isInverted()){
                saldo = saldo.negate();
            }
            return saldo;
        } else if (col == 4) {
            BigDecimal defaultAmount = account.getDefaultAmount();
            if(defaultAmount!=null){
                return defaultAmount;
            } else {
                return null;
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

	@Override
	public Account getObject(int row, int col) {
		return null;
	}

	@Override
	public int getRow(Account account) {
		return 0;
	}
}
