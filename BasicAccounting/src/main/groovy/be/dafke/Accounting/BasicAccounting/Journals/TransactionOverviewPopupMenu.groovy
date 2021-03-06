package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Balances.BalanceGUI
import be.dafke.Accounting.BasicAccounting.VAT.VATFieldsGUI
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class TransactionOverviewPopupMenu extends JPopupMenu {
    final JMenuItem move, delete, edit, balance, vatCalculation

    SelectableTable<Transaction> gui
    Journals journals
    Accounting accounting

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
        setJournals(accounting?accounting.journals:null)
    }

    void book() {
        Point locationOnScreen = getLocationOnScreen()
        setVisible(false)
        ArrayList<Transaction> transactions = gui.selectedObjects
        VATFieldsGUI vatFieldsGUI = VATFieldsGUI.getInstance(transactions, accounting)
        vatFieldsGUI.setLocation(locationOnScreen)
        vatFieldsGUI.visible = true
    }

    void showBalance() {
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

        Accounting accounting = Session.activeAccounting

        int year = gui.selectedObject.date.get(Calendar.YEAR)
        Accounts subAccounts = accounting.accounts.getSubAccounts(Movement.ofYear(year))

        Journals journals = accounting.journals

        Balances balances = accounting.balances
        Balance closingBalance = balances.createClosingBalance(subAccounts)
        Balance relationsBalance = balances.createRelationsBalance(subAccounts)
        Balance resultBalance = balances.createResultBalance(subAccounts)

        BalanceGUI.getBalance(accounting, closingBalance).visible = true
        BalanceGUI.getBalance(accounting, resultBalance).visible = true
        BalanceGUI.getBalance(accounting, relationsBalance).visible = true

        // choice 2: year=year of selected transaction
//        Accounting accounting = Session.activeAccounting
//        Balances balances = accounting.balances
//        BalanceGUI.getBalance()

    }

    void setJournals(Journals journals){
        this.journals=journals
    }

    void moveTransaction() {
        setVisible(false)
        ArrayList<Transaction> transactions = gui.selectedObjects
        Set<Transaction> set = new HashSet<>(transactions)
        Main.moveTransactions(set, journals)
    }

    void deleteTransaction() {
        setVisible(false)
        ArrayList<Transaction> transactions = gui.selectedObjects
        Set<Transaction> set = new HashSet<>(transactions)
        Main.deleteTransactions(set)
    }

    void editTransaction() {
        setVisible(false)
        Transaction transaction = gui.selectedObject
        Main.editTransaction(transaction)
    }
}
