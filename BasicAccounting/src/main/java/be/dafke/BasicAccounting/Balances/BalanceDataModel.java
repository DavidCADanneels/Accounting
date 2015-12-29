package be.dafke.BasicAccounting.Balances;

import be.dafke.BusinessModel.Balance;
import be.dafke.BusinessModel.Account;
import be.dafke.ComponentModel.RefreshableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class BalanceDataModel extends RefreshableTableModel<Account> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames;// = {
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };
	private final Balance balance;

	public BalanceDataModel(Balance balance) {
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
		int size = getRowCount();
		if (row == size - 2 || row == size - 1) {
			// in de onderste 2 rijen komen totalen
			if (row == size - 2 && col == 0) return balance.getLeftTotalName();
			else if (row == size - 2 && col == 3) return balance.getRightTotalName();
			else if (row == size - 1 && (col == 2 || col == 3)) {
				return "";
			} else {
				// Berekening totalen en resultaat
				ArrayList<Account> leftAccounts = balance.getLeftAccounts();
                ArrayList<Account> rightAccounts = balance.getRightAccounts();
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
					else if (row == size - 2 && col == 2) return BigDecimal.ZERO.subtract(totalRight);
					else {
						String tekst;
						BigDecimal resultaat = totalRight.add(totalLeft);
						if (resultaat.compareTo(BigDecimal.ZERO) > 0) {
							tekst = balance.getLeftResultName();
						} else {
							tekst = balance.getRightResultName();
							resultaat = BigDecimal.ZERO.subtract(resultaat);
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
			if (row < balance.getLeftAccounts().size()) {
				Account account = balance.getLeftAccounts().get(row);
				if (col == 0) return account;
				return account.getSaldo();
			}
			return "";
		}
		// Right
		if (row < balance.getRightAccounts().size()) {
			Account account = balance.getRightAccounts().get(row);
			if (col == 3) return account;
			return BigDecimal.ZERO.subtract(account.getSaldo());
		}
		return "";
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		int size1 = balance.getLeftAccounts().size();
		int size2 = balance.getRightAccounts().size();
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
		if(col == 0 || col == 1) {
			return (Account)getValueAt(row, 0);
		}else {
			return (Account)getValueAt(row, 3);
		}
	}

	@Override
	public int getRow(Account account) {
		return 0;
	}
}