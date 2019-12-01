package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Balances.BalanceGUI
import be.dafke.Accounting.BasicAccounting.VAT.VATFieldsGUI
import be.dafke.Accounting.BusinessModel.*
import be.dafke.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class TransactionOverviewPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, balance, vatCalculation

    private SelectableTable<Transaction> gui
    private Journals journals
    private Accounting accounting

    TransactionOverviewPopupMenu(Journals journals, SelectableTable<Transaction> gui) {
        this(gui)
        this.journals=journals
    }

    TransactionOverviewPopupMenu(SelectableTable<Transaction> gui) {
        this.gui = gui
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"))
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"))
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"))
        balance = new JMenuItem(getBundle("Accounting").getString("BALANCE_CALCULATION"))
        vatCalculation = new JMenuItem(getBundle("Accounting").getString("VAT_CALCULATION"))

        delete.addActionListener({ e -> deleteTransaction() })
        move.addActionListener({ e -> moveTransaction() })
        edit.addActionListener({ e -> editTransaction() })
        balance.addActionListener({ e -> showBalance() })
        vatCalculation.addActionListener({ e -> book() })

        add(delete)
        add(move)
        add(edit)
        addSeparator()
        add(balance)
        add(vatCalculation)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        setJournals(accounting==null?null:accounting.getJournals())
    }

    private void book() {
        Point locationOnScreen = getLocationOnScreen()
        setVisible(false)
        ArrayList<Transaction> transactions = gui.getSelectedObjects()
        VATFieldsGUI vatFieldsGUI = VATFieldsGUI.getInstance(transactions, accounting)
        vatFieldsGUI.setLocation(locationOnScreen)
        vatFieldsGUI.setVisible(true)
    }

    private void showBalance() {
        setVisible(false)
        // choices:
        // 1: all transactions from this journal (e.g. FAM16)
        // 2: all transaction from a certain year (2016)
        // 3: all transaction of a certain period (e.g. 01/07/2016 - 30/06/2017)

        // TODO first:
        // save transactions per year as well in Accounting
        // Accounting.getTransactions(int year)
        // later:
        // Accounting.getTransactions(Bookyear year)
        // with Bookyear: a period in time (e.g. 01/07 - 30/06)

        Accounting accounting = Session.getActiveAccounting()

        int year = gui.getSelectedObject().getDate().get(Calendar.YEAR)
        Accounts subAccounts = accounting.getAccounts().getSubAccounts(Movement.ofYear(year))

        Journals journals = accounting.getJournals()

        Balances balances = accounting.getBalances()
        Balance closingBalance = balances.createClosingBalance(subAccounts)
        Balance relationsBalance = balances.createRelationsBalance(subAccounts)
        Balance resultBalance = balances.createResultBalance(subAccounts)

        BalanceGUI.getBalance(accounting, closingBalance).setVisible(true)
        BalanceGUI.getBalance(accounting, resultBalance).setVisible(true)
        BalanceGUI.getBalance(accounting, relationsBalance).setVisible(true)

        // choice 2: year=year of selected transaction
//        Accounting accounting = Session.getActiveAccounting()
//        Balances balances = accounting.getBalances()
//        BalanceGUI.getBalance()

    }

    void setJournals(Journals journals){
        this.journals=journals
    }

    private void moveTransaction() {
        setVisible(false)
        ArrayList<Transaction> transactions = gui.getSelectedObjects()
        Set<Transaction> set = new HashSet<>(transactions)
        Main.moveTransactions(set, journals)
    }

    private void deleteTransaction() {
        setVisible(false)
        ArrayList<Transaction> transactions = gui.getSelectedObjects()
        Set<Transaction> set = new HashSet<>(transactions)
        Main.deleteTransactions(set)
    }

    private void editTransaction() {
        setVisible(false)
        Transaction transaction = gui.getSelectedObject()
        Main.editTransaction(transaction)
    }
}
