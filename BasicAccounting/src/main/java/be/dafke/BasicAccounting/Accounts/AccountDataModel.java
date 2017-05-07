package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author David Danneels
 */

public class AccountDataModel extends SelectableTableModel<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = {"Name","Saldo" };
	Class[] columnClasses = { Account.class, BigDecimal.class };

    private Accounts accounts;
    private List<AccountType> accountTypes;
    private Predicate<Account> filter;

// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Account account = getFilteredAccounts().get(row);
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
		return getFilteredAccounts().size();
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

	private List<Account> getFilteredAccounts(){
		if(filter==null){
			return accounts.getAccountsByType(accountTypes).stream().collect(Collectors.toCollection(ArrayList::new));
		}
		return accounts.getAccountsByType(accountTypes).stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
	}

	public void setFilter(Predicate<Account> filter) {
		this.filter = filter;
		fireTableDataChanged();
	}

	public void setAccountTypes(List<AccountType> accountTypes) {
		this.accountTypes = accountTypes;
		fireTableDataChanged();
	}

	@Override
	public Account getObject(int row, int col) {
		return getFilteredAccounts().get(row);
	}

}