package be.dafke.Accounting.Balances;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class YearBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("Accounting").getString("ACTIVA"),
			java.util.ResourceBundle.getBundle("Accounting").getString("BEDRAG"),
			java.util.ResourceBundle.getBundle("Accounting").getString("BEDRAG"),
			java.util.ResourceBundle.getBundle("Accounting").getString("PASSIVA") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };

//	private final AccountingGUIFrame parent;

//	public YearBalanceDataModel(AccountingGUIFrame parent) {
//		this.parent = parent;
//	}

// DE GET METHODEN
// ===============
	@Override
	public Object getValueAt(int row, int col) {
		int size = getRowCount();
		if (row == size - 1) {
			// in de onderste rij komen totalen
			if (col == 0) return java.util.ResourceBundle.getBundle("Accounting").getString(
					"TOTAAL_ACTIVA_TEGOEDEN");
			else if (col == 3) return java.util.ResourceBundle.getBundle("Accounting").getString(
					"TOTAAL_PASSIVA_SCHULDEN");
			else {
				// Berekening totalen en resultaat
				ArrayList<Account> activa = Accountings.getCurrentAccounting().getAccounts().getAccounts(
						AccountType.Active);
				ArrayList<Account> passiva = Accountings.getCurrentAccounting().getAccounts().getAccounts(
						AccountType.Passive);
				ArrayList<Account> klanten = Accountings.getCurrentAccounting().getAccounts().getAccounts(
						AccountType.Credit);
				ArrayList<Account> leveranciers = Accountings.getCurrentAccounting().getAccounts().getAccounts(
						AccountType.Debit);
				BigDecimal totaalActiva = new BigDecimal(0);
				BigDecimal totaalPassiva = new BigDecimal(0);
				BigDecimal totaalKlanten = new BigDecimal(0);
				BigDecimal totaalLeveranciers = new BigDecimal(0);
				Iterator<Account> it1 = activa.iterator();
				while (it1.hasNext())
					totaalActiva = totaalActiva.add(it1.next().saldo());
				Iterator<Account> it2 = passiva.iterator();
				while (it2.hasNext())
					totaalPassiva = totaalPassiva.add(it2.next().saldo());
				Iterator<Account> it3 = klanten.iterator();
				while (it3.hasNext())
					totaalKlanten = totaalKlanten.add(it3.next().saldo());
				Iterator<Account> it4 = leveranciers.iterator();
				while (it4.hasNext())
					totaalLeveranciers = totaalLeveranciers.add(it4.next().saldo());
				totaalActiva.setScale(2);
				totaalPassiva.setScale(2);
				totaalKlanten.setScale(2);
				totaalLeveranciers.setScale(2);
				BigDecimal totaalLinks = totaalActiva.add(totaalKlanten);
				BigDecimal totaalRechts = totaalPassiva.add(totaalLeveranciers);
				if (col == 1) return totaalLinks;
				return BigDecimal.ZERO.subtract(totaalRechts);
			}
		}// einde totalen
		ArrayList<Account> activa = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.Active);
		ArrayList<Account> passiva = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.Passive);
		ArrayList<Account> klanten = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.Credit);
		ArrayList<Account> leveranciers = Accountings.getCurrentAccounting().getAccounts().getAccounts(
				AccountType.Debit);
		int max;
		if (activa.size() < passiva.size()) max = passiva.size();
		else max = activa.size();
		if (row < max) {
			if (col == 0) {
				if (row < activa.size()) return activa.get(row);
				return "";
			} else if (col == 1) {
				if (row < activa.size()) return activa.get(row).saldo();
				return "";
			} else if (col == 2) {
				if (row < passiva.size()) return BigDecimal.ZERO.subtract(passiva.get(row).saldo());
				return "";
			} else { // col==4
				if (row < passiva.size()) return passiva.get(row);
				return "";
			}
		}
		// row>=max
		if (col == 0) {
			if (row < max + klanten.size()) return klanten.get(row - max);
			return "";
		} else if (col == 1) {
			if (row < max + klanten.size()) return klanten.get(row - max).saldo();
			return "";
		} else if (col == 2) {
			if (row < max + leveranciers.size()) return BigDecimal.ZERO.subtract(leveranciers.get(row - max).saldo());
			return "";
		}
		// col==3
		if (row < max + leveranciers.size()) return leveranciers.get(row - max);
		return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		ArrayList<Account> activa = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.Active);
		ArrayList<Account> passiva = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.Passive);
		ArrayList<Account> klanten = Accountings.getCurrentAccounting().getAccounts().getAccounts(AccountType.Credit);
		ArrayList<Account> leveranciers = Accountings.getCurrentAccounting().getAccounts().getAccounts(
				AccountType.Debit);
		int size1 = activa.size() > passiva.size() ? activa.size() : passiva.size();
		int size2 = klanten.size() > leveranciers.size() ? klanten.size() : leveranciers.size();
		int size = size1 + size2;
		if (size != 0) size++;
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