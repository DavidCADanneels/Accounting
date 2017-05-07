package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.ComponentModel.FilterableModel;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author David Danneels
 */

public class AccountDataModel extends SelectableTableModel<Account> implements FilterableModel<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = {"Name","Saldo" };
	Class[] columnClasses = { Account.class, BigDecimal.class };

    private List<Account> accounts;

// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Account account = accounts.get(row);
		if (col == 0) {
			return account;
		}
		if (col == 1) {
			if(account==null) return null;
			if(account.getType().isInverted())
				return account.getSaldo().negate();
			else return account.getSaldo();
			// TODO: use this isInverted() switch to call negate() in other places
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
		return accounts.size();
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

    public void setCollection(List<Account> accounts) {
        this.accounts = accounts;
    }

	@Override
	public Account getObject(int row, int col) {
		return accounts.get(row);
	}

}