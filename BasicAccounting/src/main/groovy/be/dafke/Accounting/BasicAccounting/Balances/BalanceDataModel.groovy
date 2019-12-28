package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class BalanceDataModel extends SelectableTableModel<Account> {
    String[] columnNames// = {
    final Class[] columnClasses = [ BigDecimal.class, Account.class ]
    Balance balance
    boolean left
    boolean includeEmpty
    int ACCOUNT_COL
    int SALDO_COL
    final int NR_OF_COL = 2

    BalanceDataModel(Balance balance, boolean left, boolean includeEmpty){
        this.includeEmpty = includeEmpty
        this.left = left
        setBalance(balance)
        ACCOUNT_COL = left?0:1
        SALDO_COL = left?1:0
    }

    void setBalance(Balance balance) {
        this.balance = balance

        columnNames = left?
                [ balance.leftName, getBundle("Accounting").getString("AMOUNT") ]
                :
                [ getBundle("Accounting").getString("AMOUNT"), balance.rightName ]
        fireTableDataChanged()
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(balance==null) null
        def accounts = left?balance.getLeftAccounts(includeEmpty):balance.getRightAccounts(includeEmpty)
        if (row < accounts.size()) {
            Account account = accounts.get(row)
            if (col == ACCOUNT_COL) return account
            if (col == SALDO_COL) return account.type.inverted?account.saldo.negate():account.saldo
        }
        return null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(balance==null) return 0
        def accounts = left?balance.getLeftAccounts(includeEmpty):balance.getRightAccounts(includeEmpty)
        accounts.size()
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