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

    BalanceRightDataModel(String rightName, boolean includeEmpty) {
        this.includeEmpty = includeEmpty
        columnNames = [ getBundle("Accounting").getString("AMOUNT"), rightName ]
    }

    BalanceRightDataModel(Balance balance){
        this(balance, false)
    }

    BalanceRightDataModel(Balance balance, boolean includeEmpty){
        this.includeEmpty = includeEmpty
        setBalance(balance)
    }

    void setBalance(Balance balance) {
        this.balance = balance
        columnNames = [ getBundle("Accounting").getString("AMOUNT"), balance.getRightName() ]
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(balance==null) null
        if (row < balance.getRightAccounts(includeEmpty).size()) {
            Account account = balance.getRightAccounts(includeEmpty).get(row)
            if (col == 1) return account
            return account.saldo.negate()
        }
        return ""
    }

    int getColumnCount() {
        columnNames.length
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