package be.dafke.BasicAccounting.Balances;

import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Account;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class BalanceDataModel extends SelectableTableModel<Account> {
	private String[] columnNames;// = {
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };
	private Balance balance;
	private boolean includeEmpty;

	public BalanceDataModel(String leftName, String rightName, boolean includeEmpty) {
		this.includeEmpty = includeEmpty;
		columnNames = new String[]{
				leftName,
				getBundle("Accounting").getString("AMOUNT"),
				getBundle("Accounting").getString("AMOUNT"),
				rightName};
	}

	public BalanceDataModel(Balance balance){
		this(balance, false);
	}
	public BalanceDataModel(Balance balance, boolean includeEmpty){
		this.includeEmpty = includeEmpty;
		setBalance(balance);
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
		columnNames = new String[]{
				balance.getLeftName(),
				getBundle("Accounting").getString("AMOUNT"),
				getBundle("Accounting").getString("AMOUNT"),
				balance.getRightName()};
	}

	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		if(balance==null) return null;
		int size = getRowCount();
		if (row == size - 2 || row == size - 1) {
			// in de onderste 2 rijen komen totalen
			if (row == size - 2 && col == 0) return balance.getLeftTotalName();
			else if (row == size - 2 && col == 3) return balance.getRightTotalName();
			else if (row == size - 1 && (col == 2 || col == 3)) {
				return "";
			} else {
				// Berekening totalen en resultaat
				ArrayList<Account> leftAccounts = balance.getLeftAccounts(includeEmpty);
                ArrayList<Account> rightAccounts = balance.getRightAccounts(includeEmpty);
				BigDecimal totalLeft = new BigDecimal(0);
				BigDecimal totalRight = new BigDecimal(0);
                for(Account left : leftAccounts){
                    totalLeft = totalLeft.add(left.getSaldo());
                }
                for(Account right : rightAccounts){
                    totalRight = totalRight.add(right.getSaldo());
                }
                totalLeft = totalLeft.setScale(2);
                totalRight = totalRight.setScale(2);
				if (size != 0) {
					if (row == size - 2 && col == 1) return totalLeft;
					else if (row == size - 2 && col == 2) return totalRight.negate();
					else {
						String tekst;
						BigDecimal resultaat = totalRight.add(totalLeft);
						if (resultaat.compareTo(BigDecimal.ZERO) > 0) {
							tekst = balance.getLeftResultName();
						} else {
							tekst = balance.getRightResultName();
							resultaat = resultaat.negate();
						}
						if (row == size - 1 && col == 0) return tekst;
						else if (row == size - 1 && col == 1) {
							return resultaat;
						} else return "";
					}
				}
				return "";
			}// einde berekening totalen en resultaten
		}// einde onderste 2 rijen
		if (col == 0 || col == 1) {
			// Left
			if (row < balance.getLeftAccounts(includeEmpty).size()) {
				Account account = balance.getLeftAccounts(includeEmpty).get(row);
				if (col == 0) return account;
				return account.getSaldo();
			}
			return "";
		}
		// Right
		if (row < balance.getRightAccounts(includeEmpty).size()) {
			Account account = balance.getRightAccounts(includeEmpty).get(row);
			if (col == 3) return account;
			return account.getSaldo().negate();
		}
		return "";
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if(balance==null) return 0;
		int size1 = balance.getLeftAccounts(includeEmpty).size();
		int size2 = balance.getRightAccounts(includeEmpty).size();
		int size = size1 > size2 ? size1 : size2;
		if (size != 0) size += 2;
		return size;
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
		int size = getRowCount();
		if (row == size - 2 || row == size - 1) return null;
		Object valueAt;
		if(col == 0 || col == 1) {
			valueAt = getValueAt(row, 0);
		}else {
			valueAt = getValueAt(row, 3);
		}
		if(valueAt!=null && !"".equals(valueAt))
			return (Account) valueAt;
		else return null;
	}
}