package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class TestBalanceDataModel extends SelectableTableModel<Account> {
    private final String[] columnNames = [
            getBundle("BusinessModel").getString("ACCOUNT"),
            getBundle("BusinessModel").getString("TEST_DEBIT"),
            getBundle("BusinessModel").getString("TEST_CREDIT"),
            getBundle("BusinessModel").getString("SALDO_DEBIT"),
            getBundle("BusinessModel").getString("SALDO_CREDIT")
    ]
    private final Class[] columnClasses = [
            Account.class,
            BigDecimal.class,
            BigDecimal.class,
            BigDecimal.class,
            BigDecimal.class
    ]
    private final Accounts accounts

    TestBalanceDataModel(final Accounts accounts) {
        this.accounts = accounts
    }

    // DE GET METHODEN
    // ===============
    Object getValueAt(int row, int col) {
        Account account = getObject(row, col)
        if(account==null) null
        if (col == 0) account
        else if (col == 1) account.getDebetTotal()
        else if (col == 2) account.getCreditTotal()
        else if (col == 3) {
            if (account.getSaldo().compareTo(BigDecimal.ZERO) > 0) account.getSaldo()
            ""
        } else {// col==4)
            if (account.getSaldo().compareTo(BigDecimal.ZERO) < 0) account.getSaldo().negate()
            ""
        }
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        accounts.getBusinessObjects().size()
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
        accounts.getBusinessObjects().get(row)
    }

//	@Override
//	int getRow(Account o) {
//		int row = 0
//		ArrayList<Account> accountList = accounts.getBusinessObjects()
//		for(Account account : accountList){
//			if(account != o){
//				row++
//			}
//			else row
//		}
//		0
//	}
}