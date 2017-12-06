package be.dafke.BasicAccounting.Accounts.AccountsTable;

import be.dafke.BasicAccounting.Accounts.AccountDataModel;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementTableModel.NUMBER_COL;
import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountDataTableModel extends SelectableTableModel<Account> implements AccountDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ACCOUNT_COL = 1;
	private int SALDO_COL = 2;
	private int NUMBER_COL = 0;
	private int NR_OF_COL = 3;
	private boolean showNumbers = true;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();

    private Account account = null;
    private Accounts accounts;
    private List<AccountType> accountTypes;
    private Predicate<Account> filter;
	private boolean singleAccount = false;

	public AccountDataTableModel() {
		initialize();
	}

	private void initialize(){
		setColumnNumbers();
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnNumbers() {
		if(showNumbers){
			NUMBER_COL = 0;
			ACCOUNT_COL = 1;
			SALDO_COL = 2;
			//
			NR_OF_COL = 3;
		} else {
			ACCOUNT_COL = 0;
			SALDO_COL = 1;
			//
			NR_OF_COL = 2;
		}
	}

	public boolean isShowNumbers() {
		return showNumbers;
	}

	public void setShowNumbers(boolean showNumbers) {
		this.showNumbers = showNumbers;
		initialize();
	}

	private void setColumnClasses() {
		columnClasses.clear();
		if(showNumbers) {
			columnClasses.put(ACCOUNT_COL, Account.class);
			columnClasses.put(SALDO_COL, BigDecimal.class);
			columnClasses.put(NUMBER_COL, BigInteger.class);
		} else {
			columnClasses.put(ACCOUNT_COL, Account.class);
			columnClasses.put(SALDO_COL, BigDecimal.class);
		}
	}

	private void setColumnNames() {
		columnNames.clear();
		if(showNumbers) {
			columnNames.put(ACCOUNT_COL, getBundle("Accounting").getString("ACCOUNT_NAME"));
			columnNames.put(SALDO_COL, getBundle("Accounting").getString("SALDO"));
			columnNames.put(NUMBER_COL, getBundle("Accounting").getString("ACCOUNT_NUMBER"));
		} else {
			columnNames.put(ACCOUNT_COL, getBundle("Accounting").getString("ACCOUNT_NAME"));
			columnNames.put(SALDO_COL, getBundle("Accounting").getString("SALDO"));
		}
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Account account = getFilteredAccounts().get(row);
		if (col == ACCOUNT_COL) {
			return account;
		}
		if (col == NUMBER_COL) {
			if(account==null) return null;
			return account.getNumber();
		}
		if (col == SALDO_COL) {
			if(account==null) return null;
			if(account.getType().isInverted())
				return account.getSaldo().negate();
			else return account.getSaldo();
			// TODO: use this isInverted() switch to call negate() in other places
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
        if(accounts == null){
            return 0;
        }
		return getFilteredAccounts().size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col==NUMBER_COL;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		if(col == NUMBER_COL){
			Account account = getObject(row,col);
            account.setNumber((BigInteger)value);
		}
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