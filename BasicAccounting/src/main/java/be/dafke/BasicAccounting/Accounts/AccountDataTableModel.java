package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author David Danneels
 */

public class AccountDataTableModel extends SelectableTableModel<Account> implements AccountDataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = {"Name","Saldo" };
	Class[] columnClasses = { Account.class, BigDecimal.class };

    private Account account = null;
    private Accounts accounts;
    private List<AccountType> accountTypes;
    private Predicate<Account> filter;
	private boolean singleAccount = false;

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
		ArrayList<Account> accountsList;
		if(accountTypes!=null) {
			accountsList = this.accounts.getAccountsByType(accountTypes);
		} else {
			accountsList = this.accounts.getBusinessObjects();
		}
		if(filter==null){
			return accountsList.stream().collect(Collectors.toCollection(ArrayList::new));
		}
		return accountsList.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
	}

	public void setFilter(Predicate<Account> filter) {
		if(!singleAccount || account == null) {
			this.filter = filter;
			fireTableDataChanged();
		}
	}

	public void setAccountList(AccountsList accountList) {
		singleAccount = accountList.isSingleAccount();
		if(singleAccount){
			accountTypes = null;
			account = accountList.getAccount();
			filter = account==null?null:Account.name(accountList.getAccount().getName());
		} else {
			accountTypes = accountList.getAccountTypes();
			filter = null;
		}
		fireTableDataChanged();
	}

	@Deprecated
	public void setAccountTypes(List<AccountType> accountTypes) {
		this.accountTypes = accountTypes;
		fireTableDataChanged();
	}

	@Override
	public Account getObject(int row, int col) {
		return getFilteredAccounts().get(row);
	}

}