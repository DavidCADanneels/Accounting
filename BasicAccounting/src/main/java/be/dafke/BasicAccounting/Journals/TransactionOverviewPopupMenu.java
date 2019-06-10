package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static java.util.ResourceBundle.getBundle;

public class TransactionOverviewPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, balance;

    private SelectableTable<Transaction> gui;
    private Journals journals;

    public TransactionOverviewPopupMenu(Journals journals, SelectableTable<Transaction> gui) {
        this(gui);
        this.journals=journals;
    }

    public TransactionOverviewPopupMenu(SelectableTable<Transaction> gui) {
        this.gui = gui;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        balance = new JMenuItem(getBundle("Accounting").getString("BALANCE_CALCULATION"));

        delete.addActionListener(e -> deleteTransaction());
        move.addActionListener(e -> moveTransaction());
        edit.addActionListener(e -> editTransaction());
        balance.addActionListener(e -> showBalance());

        add(delete);
        add(move);
        add(edit);
        addSeparator();
        add(balance);
    }

    private void showBalance() {
        setVisible(false);
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

        Accounting accounting = Accountings.getActiveAccounting();

        int year = gui.getSelectedObject().getDate().get(Calendar.YEAR);
        Accounts subAccounts = accounting.getAccounts().getSubAccounts(Movement.ofYear(year));

        Journals journals = accounting.getJournals();

        Balances balances = accounting.getBalances();
        Balance closingBalance = balances.createClosingBalance(subAccounts);
        Balance relationsBalance = balances.createRelationsBalance(subAccounts);
        Balance resultBalance = balances.createResultBalance(subAccounts);

        BalanceGUI.getBalance(accounting, closingBalance).setVisible(true);
        BalanceGUI.getBalance(accounting, resultBalance).setVisible(true);
        BalanceGUI.getBalance(accounting, relationsBalance).setVisible(true);

        // choice 2: year=year of selected transaction
//        Accounting accounting = Accountings.getActiveAccounting();
//        Balances balances = accounting.getBalances();
//        BalanceGUI.getBalance()

    }

    public void setJournals(Journals journals){
        this.journals=journals;
    }

    private void moveTransaction() {
        setVisible(false);
        ArrayList<Transaction> transactions = gui.getSelectedObjects();
        Set<Transaction> set = new HashSet<>(transactions);
        Main.moveTransactions(set, journals);
    }

    private void deleteTransaction() {
        setVisible(false);
        ArrayList<Transaction> transactions = gui.getSelectedObjects();
        Set<Transaction> set = new HashSet<>(transactions);
        Main.deleteTransactions(set);
    }

    private void editTransaction() {
        setVisible(false);
        Transaction transaction = gui.getSelectedObject();
        Main.editTransaction(transaction);
    }
}
