package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Utils;

import javax.swing.table.AbstractTableModel;

public class CounterPartyDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Aliases", "BankAccounts", "BIC", "Currency", "Account (for Accounting)" };
	private final Class[] columnClasses = { CounterParty.class, String.class, String.class, String.class, String.class, Account.class };

	private final Accounting accounting;

	public CounterPartyDataModel(Accounting accounting) {
		this.accounting = accounting;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		CounterParties counterParties = accounting.getCounterParties();
		CounterParty c = counterParties.getBusinessObjects().get(row);
		if (col == 0) {
			return c;
        } else if (col == 1) {
            return Utils.toString(c.getAliases());
		} else if (col == 2) {
			return c.getBankAccountsString();
		} else if (col == 3) {
			return c.getBICString();
		} else if (col == 4) {
			return c.getCurrencyString();
		} else if (col == 5) {
			return c.getAccount();
		} else return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return accounting.getCounterParties().getBusinessObjects().size();
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