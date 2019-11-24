package be.dafke.BasicAccounting.Accounts.AccountManagement;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Account;
import be.dafke.Accounting.BusinessModel.AccountType;
import be.dafke.Accounting.BusinessModel.Accounts;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class AccountManagementTableModel extends SelectableTableModel<Account> {
	public static final int NAME_COL = 0;
	public static final int NUMBER_COL = 1;
	public static final int TYPE_COL = 2;
	public static final int SALDO_COL = 3;
	public static final int DEFAULT_AMOUNT_COL = 4;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private ArrayList<Integer> nonEditableColumns = new ArrayList<>();

	private final Accounts accounts;
	private final Component parent;

	public AccountManagementTableModel(Component parent, Accounts accounts) {
		this.parent = parent;
		this.accounts = accounts;
		nonEditableColumns.add(SALDO_COL);
//		nonEditableColumns.add(TYPE_COL);
		setColumnNames();
		setColumnClasses();
	}

	private void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(NUMBER_COL, BigInteger.class);
		columnClasses.put(TYPE_COL, AccountType.class);
		columnClasses.put(SALDO_COL, BigDecimal.class);
		columnClasses.put(DEFAULT_AMOUNT_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(NAME_COL, getBundle("Accounting").getString("ACCOUNT_NAME"));
		columnNames.put(NUMBER_COL, getBundle("Accounting").getString("ACCOUNT_NUMBER"));
		columnNames.put(TYPE_COL, getBundle("Accounting").getString("TYPE"));
		columnNames.put(SALDO_COL, getBundle("Accounting").getString("SALDO"));
		columnNames.put(DEFAULT_AMOUNT_COL, getBundle("BusinessActions").getString("DEFAULT_AMOUNT"));
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return accounts.getBusinessObjects().size();
	}

	public Object getValueAt(int row, int col) {
		Account account = accounts.getBusinessObjects().get(row);
		if (col == NAME_COL) {
			return account.getName();
		} else if (col == NUMBER_COL) {
			return account.getNumber();
		} else if (col == TYPE_COL) {
			return account.getType();
		} else if (col == SALDO_COL) {
			AccountType type = account.getType();
            BigDecimal saldo = account.getSaldo();
            if(type.isInverted()){
                saldo = saldo.negate();
            }
            return saldo;
        } else if (col == DEFAULT_AMOUNT_COL) {
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
		return columnNames.get(col);
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return !nonEditableColumns.contains(col);
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Account account = getObject(row, col);
		if(isCellEditable(row, col) && account!=null){
			if(col== NAME_COL){
				String oldName = account.getName();
				String newName = (String)value;
				if (newName != null && !oldName.trim().equals(newName.trim())) {
					try {
						accounts.modifyName(oldName, newName);
						Main.fireAccountDataChanged(account);
					} catch (DuplicateNameException e) {
						ActionUtils.showErrorMessage(parent, ActionUtils.ACCOUNT_DUPLICATE_NAME,newName.trim());
					} catch (EmptyNameException e) {
						ActionUtils.showErrorMessage(parent, ActionUtils.ACCOUNT_NAME_EMPTY);
					}
				}
			}else if(col== TYPE_COL){
				AccountType accountType = (AccountType)value;
				account.setType(accountType);
			}else if(col== NUMBER_COL){
				account.setNumber((BigInteger)value);
			} else if(col== DEFAULT_AMOUNT_COL) {
				if (value == null || BigDecimal.ZERO.compareTo((BigDecimal) value) == NAME_COL) {
					account.setDefaultAmount(null);
				} else {
					account.setDefaultAmount(((BigDecimal) value).setScale(TYPE_COL));
				}
			}
		}
	}

	@Override
	public Account getObject(int row, int col) {
		return accounts.getBusinessObjects().get(row);
	}

}
