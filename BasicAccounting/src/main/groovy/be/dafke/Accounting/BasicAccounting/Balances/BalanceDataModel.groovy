package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle

class BalanceDataModel extends SelectableTableModel<Account> {
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    Balance balance
    boolean left
    boolean includeEmpty, showNumbers
    int accountCol
    int saldoCol
    int nrCol
    int nrOfCols

    BalanceDataModel(Balance balance, boolean left, boolean includeEmpty, boolean showNumbers = false){
        this.includeEmpty = includeEmpty
        this.showNumbers = showNumbers
        this.left = left
        setBalance(balance)
    }

    void setBalance(Balance balance) {
        this.balance = balance
        setColumnNumbers()
        setColumnNames()
        setColumnClasses()
        fireTableStructureChanged()
        fireTableDataChanged()
    }

    void setColumnNumbers(){
        nrOfCols = showNumbers?3:2
        nrCol = showNumbers?(left?0:nrOfCols-1):-1
//        nrCol=showNumbers?(left?0:2):-1
        accountCol = left?(showNumbers?1:0):1
        saldoCol = left?nrOfCols-1:0
    }

    void setColumnNames() {
        columnNames.clear()
        if (nrCol > -1) columnNames.put(nrCol, getBundle("Accounting").getString("NR"))
        columnNames.put(accountCol, balance.leftName)
        columnNames.put(saldoCol, getBundle("Accounting").getString("AMOUNT"))
    }

    void setColumnClasses(){
        columnClasses.clear()
        if (nrCol > -1) columnClasses.put(nrCol, BigInteger.class)
        columnClasses.put(accountCol, Account.class)
        columnClasses.put(saldoCol, BigDecimal.class)
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(balance==null) return null
        def accounts = left?balance.getLeftAccounts(includeEmpty):balance.getRightAccounts(includeEmpty)
        if (row < accounts.size()) {
            Account account = accounts.get(row)
            if (col == accountCol) return account
            if (col == nrCol) return account.number
            if (col == saldoCol) return account.type.inverted?account.saldo.negate():account.saldo
        }
        return null
    }

    int getColumnCount() {
        nrOfCols
    }

    int getRowCount() {
        if(balance==null) return 0
        def accounts = left?balance.getLeftAccounts(includeEmpty):balance.getRightAccounts(includeEmpty)
        accounts.size()
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
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
        getValueAt(row, accountCol)
//        if(valueAt && !"".equals(valueAt))
//            (Account) valueAt
//        else null
    }
}