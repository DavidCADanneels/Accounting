package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableTableModel;

import java.math.BigDecimal;

/**
 * @author David Danneels
 */

public class AccountDataModel extends RefreshableTableModel<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = {"Name","Saldo" };
	Class[] columnClasses = { Account.class, BigDecimal.class };

    private Accounts accounts;

// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Account account = accounts.getBusinessObjects().get(row);
		if (col == 0) {
			return account.getName();
		}
		if (col == 1) {
			return account.getSaldo();
		}
		return null;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
        if(accounts == null){
            return 0;
        }
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

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

	@Override
	public Account getObject(int row, int col) {
		return accounts.getBusinessObjects().get(row);
	}

	@Override
	public int getRow(Account account) {
		return 0;
	}
}