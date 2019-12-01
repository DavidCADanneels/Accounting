package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.CounterParties
import be.dafke.Accounting.BusinessModel.CounterParty
import be.dafke.Utils.Utils

import javax.swing.table.AbstractTableModel

class CounterPartyDataModel extends AbstractTableModel {
    final String[] columnNames = [ "Name", "Aliases", "BankAccounts", "BIC", "Currency", "Account (for Accounting)" ]
    final Class[] columnClasses = [ CounterParty.class, String.class, String.class, String.class, String.class, Account.class ]

    final CounterParties counterParties

    CounterPartyDataModel(CounterParties counterParties) {
        this.counterParties = counterParties
    }

    // DE GET METHODEN
    // ===============
    Object getValueAt(int row, int col) {
        CounterParty c = (CounterParty)counterParties.businessObjects.get(row)
        if (col == 0) return c
        if (col == 1) return Utils.toString(c.getAliases())
        if (col == 2) return c.getBankAccountsString()
        if (col == 3) return c.getBICString()
        if (col == 4) return c.getCurrencyString()
        if (col == 5) return c.account
        return  ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        counterParties.businessObjects.size()
    }

    @Override
    String getColumnName(int col) {
        columnNames[col]
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses[col]
    }

    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
    }
}