package be.dafke.Balances.GUI;

import be.dafke.Balances.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Account;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class BalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames;// = {
//			getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"),
//			getBundle("Accounting").getString("BEDRAG"),
//			getBundle("Accounting").getString("BEDRAG"),
//			getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };
	private final Balance balance;

	public BalanceDataModel(Balance balance) {
		this.balance = balance;
        columnNames = new String[]{
                balance.getLeftName(),
                getBundle("Accounting").getString("BEDRAG"),
                getBundle("Accounting").getString("BEDRAG"),
                balance.getRightName()};
	}

// DE GET METHODEN
// ===============
	@Override
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

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
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
}