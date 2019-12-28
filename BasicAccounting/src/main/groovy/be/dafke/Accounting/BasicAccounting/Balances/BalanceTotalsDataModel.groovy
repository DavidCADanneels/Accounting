package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Balance

import javax.swing.table.AbstractTableModel

import static java.util.ResourceBundle.getBundle

class BalanceTotalsDataModel extends AbstractTableModel {
    String[] columnNames// = {
    final Class[] columnClasses = [ Account.class, BigDecimal.class, BigDecimal.class, Account.class ]
    Balance balance
    boolean includeEmpty

    BalanceTotalsDataModel(String leftName, String rightName, boolean includeEmpty) {
        this.includeEmpty = includeEmpty
        columnNames = [
                leftName,
                getBundle("Accounting").getString("AMOUNT"),
                getBundle("Accounting").getString("AMOUNT"),
                rightName
        ]
    }

    BalanceTotalsDataModel(Balance balance, boolean includeEmpty){
        this(balance.leftName, balance.rightName, includeEmpty)
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
        if (row == 0 && col == 0) return balance.getLeftTotalName()
        if (row == 0 && col == 3) return balance.getRightTotalName()
        if (row == 1 && (col == 2 || col == 3)) {
            return ""
        } else {
            // Berekening totalen en resultaat
            ArrayList<Account> leftAccounts = balance.getLeftAccounts(includeEmpty)
            ArrayList<Account> rightAccounts = balance.getRightAccounts(includeEmpty)
            BigDecimal totalLeft = new BigDecimal(0)
            BigDecimal totalRight = new BigDecimal(0)
            for(Account left : leftAccounts){
                totalLeft = totalLeft.add(left.saldo)
            }
            for(Account right : rightAccounts){
                totalRight = totalRight.add(right.saldo)
            }
            totalLeft = totalLeft.setScale(2)
            totalRight = totalRight.setScale(2)
            if (row == 0 && col == 1) return totalLeft
            if (row == 0 && col == 2) return totalRight.negate()
            String tekst
            BigDecimal resultaat = totalRight.add(totalLeft)
            if (resultaat.compareTo(BigDecimal.ZERO) > 0) {
                tekst = balance.getLeftResultName()
            } else {
                tekst = balance.getRightResultName()
                resultaat = resultaat.negate()
            }
            if (row == 1 && col == 0) return tekst
            else if (row == 1 && col == 1) return resultaat
            else return ""
        }// einde berekening totalen en resultaten
        return ""
    }

    int getColumnCount() {
        columnNames.length
    }

    int getRowCount() {
        2
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