package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class TestBalanceDataModel extends SelectableTableModel<Account> {
    final String[] columnNames = [
            getBundle("BusinessModel").getString("ACCOUNT"),
            getBundle("BusinessModel").getString("TEST_DEBIT"),
            getBundle("BusinessModel").getString("TEST_CREDIT"),
            getBundle("BusinessModel").getString("SALDO_DEBIT"),
            getBundle("BusinessModel").getString("SALDO_CREDIT")
    ]
    final Class[] columnClasses = [
            Account.class,
            BigDecimal.class,
            BigDecimal.class,
            BigDecimal.class,
            BigDecimal.class
    ]
    final Accounts accounts

    TestBalanceDataModel(final Accounts accounts) {
        this.accounts = accounts
    }

    // DE GET METHODEN
    // ===============
    Object getValueAt(int row, int col) {
        Account account = getObject(row, col)
        if(account==null) return null
        if (col == 0) return account
        if (col == 1) return account.debitTotal
        if (col == 2) return account.creditTotal
        if (col == 3) {
            if (account.saldo.compareTo(BigDecimal.ZERO) > 0) return account.saldo
            else return ""
        } else {// col==4)
            if (account.saldo.compareTo(BigDecimal.ZERO) < 0) return account.saldo.negate()
            else return ""
        }
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        accounts.businessObjects.size()
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

    @Override
    Account getObject(int row, int col) {
        accounts.businessObjects.get(row)
    }

//	@Override
//	int getRow(Account o) {
//		int row = 0
//		ArrayList<Account> accountList = accounts.businessObjects
//		for(Account account : accountList){
//			if(account != o){
//				row++
//			}
//			else row
//		}
//		0
//	}
}