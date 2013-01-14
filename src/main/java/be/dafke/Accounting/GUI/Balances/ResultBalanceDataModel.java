package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class ResultBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("Accounting").getString("KOSTEN"),
			java.util.ResourceBundle.getBundle("Accounting").getString("BEDRAG"),
			java.util.ResourceBundle.getBundle("Accounting").getString("BEDRAG"),
			java.util.ResourceBundle.getBundle("Accounting").getString("OPBRENGSTEN") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };
	private final Accountings accountings;

	public ResultBalanceDataModel(Accountings accountings) {
		this.accountings = accountings;
	}

// DE GET METHODEN
// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Accounting accounting = accountings.getCurrentAccounting();
		int size = getRowCount();
		if (row == size - 2 || row == size - 1) {
			// in de onderste 2 rijen komen totalen
			if (row == size - 2 && col == 0) return java.util.ResourceBundle.getBundle("Accounting").getString(
					"TOTAAL_KOSTEN");
			else if (row == size - 2 && col == 3) return java.util.ResourceBundle.getBundle(
					"Accounting").getString("TOTAAL_OPBRENGSTEN");
			else if (row == size - 1 && (col == 2 || col == 3)) {
				return "";
			} else {
				// Berekening totalen en resultaat
				ArrayList<Account> kosten = accounting.getAccounts().getAccounts(AccountType.Cost);
				ArrayList<Account> opbrengsten = accounting.getAccounts().getAccounts(AccountType.Revenue);
				BigDecimal totaalKosten = new BigDecimal(0);
				BigDecimal totaalOpbrengsten = new BigDecimal(0);
				Iterator<Account> it1 = kosten.iterator();
				while (it1.hasNext())
					totaalKosten = totaalKosten.add(it1.next().saldo());
				Iterator<Account> it2 = opbrengsten.iterator();
				while (it2.hasNext())
					totaalOpbrengsten = totaalOpbrengsten.add(it2.next().saldo());
				totaalKosten.setScale(2);
				totaalOpbrengsten.setScale(2);
				if (size != 0) {
					if (row == size - 2 && col == 1) return totaalKosten;
					else if (row == size - 2 && col == 2) return BigDecimal.ZERO.subtract(totaalOpbrengsten);
					else {
						String tekst;
						BigDecimal total = totaalKosten.add(totaalOpbrengsten);
						if (total.compareTo(BigDecimal.ZERO) > 0) {
							tekst = java.util.ResourceBundle.getBundle("Accounting").getString(
									"VERLIES");
						} else {
							tekst = java.util.ResourceBundle.getBundle("Accounting").getString("WINST");
							total = BigDecimal.ZERO.subtract(total);
						}
						if (row == size - 1 && col == 0) return tekst;
						else if (row == size - 1 && col == 1) {
							return total;
						} else return "";
					}
				}
				return "";
			}// einde berekening totalen en resultaten
		}// einde onderste 2 rijen
		if (col == 0 || col == 1) {
			// Kosten
			if (row < accounting.getAccounts().getAccounts(AccountType.Cost).size()) {
				Account account = accounting.getAccounts().getAccounts(AccountType.Cost).get(row);
				if (col == 0) return account;
				return account.saldo();
			}
			return "";
		}
		// Opbrengsten
		if (row < accounting.getAccounts().getAccounts(AccountType.Revenue).size()) {
			Account account = accounting.getAccounts().getAccounts(AccountType.Revenue).get(row);
			if (col == 3) return account;
			return BigDecimal.ZERO.subtract(account.saldo());
		}
		return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if (accountings == null || accountings.getCurrentAccounting() == null) {
			return 0;
		}
		Accounting accounting = accountings.getCurrentAccounting();
		int size1 = accounting.getAccounts().getAccounts(AccountType.Cost).size();
		int size2 = accounting.getAccounts().getAccounts(AccountType.Revenue).size();
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