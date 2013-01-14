package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

public class RelationsBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"),
			java.util.ResourceBundle.getBundle("Accounting").getString("BEDRAG"),
			java.util.ResourceBundle.getBundle("Accounting").getString("BEDRAG"),
			java.util.ResourceBundle.getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };
	private final Accountings accountings;

	public RelationsBalanceDataModel(Accountings accountings) {
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
					"TOTAAL_TEGOEDEN");
			else if (row == size - 2 && col == 3) return java.util.ResourceBundle.getBundle(
					"Accounting").getString("TOTAAL_SCHULDEN");
			else if (row == size - 1 && (col == 2 || col == 3)) {
				return "";
			} else {
				// Berekening totalen en resultaat
				Collection<Account> klanten = accounting.getAccounts().getAccounts(AccountType.Credit);
				Collection<Account> leveranciers = accounting.getAccounts().getAccounts(AccountType.Debit);
				BigDecimal totaalKlanten = new BigDecimal(0);
				BigDecimal totaalLeveranciers = new BigDecimal(0);
				Iterator<Account> it1 = klanten.iterator();
				while (it1.hasNext())
					totaalKlanten = totaalKlanten.add(it1.next().saldo());
				Iterator<Account> it2 = leveranciers.iterator();
				while (it2.hasNext())
					totaalLeveranciers = totaalLeveranciers.add(it2.next().saldo());
				totaalKlanten.setScale(2);
				totaalLeveranciers.setScale(2);
				if (size != 0) {
					if (row == size - 2 && col == 1) return totaalKlanten;
					else if (row == size - 2 && col == 2) return BigDecimal.ZERO.subtract(totaalLeveranciers);
					else {
						String tekst;
						BigDecimal resultaat = totaalLeveranciers.add(totaalKlanten);
						if (resultaat.compareTo(BigDecimal.ZERO) > 0) {
							tekst = java.util.ResourceBundle.getBundle("Accounting").getString(
									"RESTEREND_TEGOED");
						} else {
							tekst = java.util.ResourceBundle.getBundle("Accounting").getString(
									"RESTERENDE_SCHULD");
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
			// Tegoeden
			if (row < accounting.getAccounts().getAccounts(AccountType.Credit).size()) {
				Account account = accounting.getAccounts().getAccounts(AccountType.Credit).get(row);
				if (col == 0) return account;
				return account.saldo();
			}
			return "";
		}
		// Schulden
		if (row < accounting.getAccounts().getAccounts(AccountType.Debit).size()) {
			Account account = accounting.getAccounts().getAccounts(AccountType.Debit).get(row);
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
		int size1 = accounting.getAccounts().getAccounts(AccountType.Credit).size();
		int size2 = accounting.getAccounts().getAccounts(AccountType.Debit).size();
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