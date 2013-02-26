package be.dafke.Accounting.GUI.Balances;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class YearBalanceDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = {
			getBundle("Accounting").getString("ACTIVA"),
			getBundle("Accounting").getString("BEDRAG"),
			getBundle("Accounting").getString("BEDRAG"),
			getBundle("Accounting").getString("PASSIVA") };
	private final Class[] columnClasses = { Account.class, BigDecimal.class, BigDecimal.class, Account.class };
	private final Accounting accounting;

	public YearBalanceDataModel(Accounting accounting) {
		this.accounting = accounting;
	}

// DE GET METHODEN
// ===============

	public Object getValueAt(int row, int col) {
		int size = getRowCount();
		if (row == size - 1) {
			// in de onderste rij komen totalen
			if (col == 0) return getBundle("Accounting").getString(
					"TOTAAL_ACTIVA_TEGOEDEN");
			else if (col == 3) return getBundle("Accounting").getString(
					"TOTAAL_PASSIVA_SCHULDEN");
			else {
				// Berekening totalen en resultaat
				ArrayList<Account> activa = accounting.getAccounts().getAccounts(AccountType.Active);
				ArrayList<Account> passiva = accounting.getAccounts().getAccounts(AccountType.Passive);
				ArrayList<Account> klanten = accounting.getAccounts().getAccounts(AccountType.Credit);
				ArrayList<Account> leveranciers = accounting.getAccounts().getAccounts(AccountType.Debit);
				BigDecimal totaalActiva = new BigDecimal(0);
				BigDecimal totaalPassiva = new BigDecimal(0);
				BigDecimal totaalKlanten = new BigDecimal(0);
				BigDecimal totaalLeveranciers = new BigDecimal(0);
                for(Account actief : activa){
                    totaalActiva = totaalActiva.add(actief.saldo());
                }
                for(Account passief : passiva){
                    totaalPassiva = totaalPassiva.add(passief.saldo());
                }
                for(Account klant : klanten){
                    totaalKlanten = totaalKlanten.add(klant.saldo());
                }
                for(Account leverancier : leveranciers){
                    totaalLeveranciers = totaalLeveranciers.add(leverancier.saldo());
                }
                totaalActiva = totaalActiva.setScale(2);
                totaalPassiva = totaalPassiva.setScale(2);
                totaalKlanten = totaalKlanten.setScale(2);
                totaalLeveranciers = totaalLeveranciers.setScale(2);
				BigDecimal totaalLinks = totaalActiva.add(totaalKlanten);
				BigDecimal totaalRechts = totaalPassiva.add(totaalLeveranciers);
				if (col == 1) return totaalLinks;
				return BigDecimal.ZERO.subtract(totaalRechts);
			}
		}// einde totalen
		ArrayList<Account> activa = accounting.getAccounts().getAccounts(AccountType.Active);
		ArrayList<Account> passiva = accounting.getAccounts().getAccounts(AccountType.Passive);
		ArrayList<Account> klanten = accounting.getAccounts().getAccounts(AccountType.Credit);
		ArrayList<Account> leveranciers = accounting.getAccounts().getAccounts(AccountType.Debit);
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

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		ArrayList<Account> activa = accounting.getAccounts().getAccounts(AccountType.Active);
		ArrayList<Account> passiva = accounting.getAccounts().getAccounts(AccountType.Passive);
		ArrayList<Account> klanten = accounting.getAccounts().getAccounts(AccountType.Credit);
		ArrayList<Account> leveranciers = accounting.getAccounts().getAccounts(AccountType.Debit);
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