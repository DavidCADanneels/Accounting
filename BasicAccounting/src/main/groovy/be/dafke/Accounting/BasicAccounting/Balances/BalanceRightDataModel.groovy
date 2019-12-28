package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class BalanceRightDataModel extends SelectableTableModel<Account> {
    String[] columnNames// = {
    final Class[] columnClasses = [ BigDecimal.class, Account.class ]
    Balance balance
    boolean includeEmpty
    final int ACCOUNT_COL = 1
    final int SALDO_COL = 0
    final int NR_OF_COL = 2

    BalanceRightDataModel(String rightName, boolean includeEmpty) {
        this.includeEmpty = includeEmpty
        columnNames = [ getBundle("Accounting").getString("AMOUNT"), rightName ]
    }

    BalanceRightDataModel(Balance balance, boolean includeEmpty){
        this(balance.rightName, includeEmpty)
        setBalance(balance)
    }

    void setBalance(Balance balance) {
        this.balance = balance
        columnNames = [ getBundle("Accounting").getString("AMOUNT"), balance.rightName ]
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(balance==null) null
        if (row < balance.getLeftAccounts(includeEmpty).size()) {
            Account account = balance.getLeftAccounts(includeEmpty).get(row)
            if (col == ACCOUNT_COL) return account
            if (col == SALDO_COL) return account.saldo
        }
        return null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(balance==null) return 0
        balance.getRightAccounts(includeEmpty).size()
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
//        Object valueAt =
        getValueAt(row, 1)
//        if(valueAt && !"".equals(valueAt))
//            (Account) valueAt
//        else null
    }
}