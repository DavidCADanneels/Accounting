package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class BalanceLeftDataModel extends SelectableTableModel<Account> {
    String[] columnNames// = {
    final Class[] columnClasses = [ Account.class, BigDecimal.class]
    Balance balance
    boolean includeEmpty

    BalanceLeftDataModel(String leftName, boolean includeEmpty) {
        this.includeEmpty = includeEmpty
        columnNames = [ leftName, getBundle("Accounting").getString("AMOUNT") ]
    }

    BalanceLeftDataModel(Balance balance){
        this(balance, false)
    }

    BalanceLeftDataModel(Balance balance, boolean includeEmpty){
        this.includeEmpty = includeEmpty
        setBalance(balance)
    }

    void setBalance(Balance balance) {
        this.balance = balance
        columnNames = [ balance.getLeftName(), getBundle("Accounting").getString("AMOUNT") ]
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(balance==null) null
        if (row < balance.getLeftAccounts(includeEmpty).size()) {
            Account account = balance.getLeftAccounts(includeEmpty).get(row)
            if (col == 0) return account
            return account.saldo
        }
        return ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        if(balance==null) return 0
        balance.getLeftAccounts(includeEmpty).size()
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
        getValueAt(row, 0)
//        if(valueAt && !"".equals(valueAt))
//            (Account) valueAt
//        else null
    }
}