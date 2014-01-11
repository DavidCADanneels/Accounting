package be.dafke.Coda.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.CounterParty;
import be.dafke.Utils.Utils;

import javax.swing.table.AbstractTableModel;

public class CounterPartyDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Aliases", "BankAccounts", "BIC", "Currency", "Account (for Accounting)" };
	private final Class[] columnClasses = { CounterParty.class, String.class, String.class, String.class, String.class, Account.class };

	private final CounterParties counterParties;

	public CounterPartyDataModel(CounterParties counterParties) {
		this.counterParties = counterParties;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
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
		return counterParties.getBusinessObjects().size();
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