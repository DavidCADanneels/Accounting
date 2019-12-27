package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class BalanceDataModel extends SelectableTableModel<Account> {
    String[] columnNames// = {
    final Class[] columnClasses = [ Account.class, BigDecimal.class, BigDecimal.class, Account.class ]
    Balance balance
    boolean includeEmpty

    BalanceDataModel(String leftName, String rightName, boolean includeEmpty) {
        this.includeEmpty = includeEmpty
        columnNames = [
                leftName,
                getBundle("Accounting").getString("AMOUNT"),
                getBundle("Accounting").getString("AMOUNT"),
                rightName
        ]
    }

    BalanceDataModel(Balance balance){
        this(balance, false)
    }
    BalanceDataModel(Balance balance, boolean includeEmpty){
        this.includeEmpty = includeEmpty
        setBalance(balance)
    }

    void setBalance(Balance balance) {
        this.balance = balance
        columnNames = [
                balance.getLeftName(),
                getBundle("Accounting").getString("AMOUNT"),
                getBundle("Accounting").getString("AMOUNT"),
                balance.getRightName()
        ]
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(balance==null) null
        if (col == 0 || col == 1) {
            // Left
            if (row < balance.getLeftAccounts(includeEmpty).size()) {
                Account account = balance.getLeftAccounts(includeEmpty).get(row)
                if (col == 0) return account
                return account.saldo
            }
            return ""
        }
        // Right
        if (row < balance.getRightAccounts(includeEmpty).size()) {
            Account account = balance.getRightAccounts(includeEmpty).get(row)
            if (col == 3) return account
            return account.saldo.negate()
        }
        return ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        if(balance==null) return 0
        int size1 = balance.getLeftAccounts(includeEmpty).size()
        int size2 = balance.getRightAccounts(includeEmpty).size()
        int size = size1 > size2 ? size1 : size2
        size
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
        Object valueAt
        if(col == 0 || col == 1) {
            valueAt = getValueAt(row, 0)
        }else {
            valueAt = getValueAt(row, 3)
        }
        if(valueAt!=null && !"".equals(valueAt))
            (Account) valueAt
        else null
    }
}