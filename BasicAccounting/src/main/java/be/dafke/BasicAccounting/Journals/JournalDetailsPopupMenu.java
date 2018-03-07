package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem move, delete, edit, details, balance;
    private JournalEditPanel journalEditPanel;

    private SelectableTable<Booking> gui;
    private Journals journals;

    public JournalDetailsPopupMenu(Journals journals, SelectableTable<Booking> gui, JournalEditPanel journalEditPanel) {
        this(gui, journalEditPanel);
        this.journals=journals;
    }

    public JournalDetailsPopupMenu(SelectableTable<Booking> gui, JournalEditPanel journalEditPanel) {
        this.gui = gui;
        this.journalEditPanel = journalEditPanel;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        balance = new JMenuItem(getBundle("Accounting").getString("BALANCE_CALCULATION"));

        delete.addActionListener(e -> deleteTransaction());
        move.addActionListener(e -> moveTransaction());
        edit.addActionListener(e -> editTransaction());
        details.addActionListener(e -> showDetails());
        balance.addActionListener(e -> showBalance());

        add(delete);
        add(move);
        add(edit);
        add(details);
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

//        Journal master = accounting.getTransactions();
//        List<Transaction> transactions = master.getBusinessObjects(new TransactionOfYear(year));
//        List<Movement> movements = master.getBusinessObjects(new TransactionOfYear(year));

        int year = gui.getSelectedObject().getTransaction().getDate().get(Calendar.YEAR);
        Accounts subAccounts = accounting.getAccounts().getSubAccounts(new MovementOfYear(year));

        Journals journals = accounting.getJournals();

        Balances balances = accounting.getBalances();
        Balance closingBalance = balances.createClosingBalance(subAccounts);
        Balance relationsBalance = balances.createRelationsBalance(subAccounts);
        Balance resultBalance = balances.createResultBalance(subAccounts);

        BalanceGUI.getBalance(journals, closingBalance, journalEditPanel);
        BalanceGUI.getBalance(journals, resultBalance, journalEditPanel);
        BalanceGUI.getBalance(journals, relationsBalance, journalEditPanel);

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
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        Set<Transaction> transactions = journalEditPanel.getTransactions(bookings);
        journalEditPanel.moveTransaction(transactions, journals);
    }

    private void deleteTransaction() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        Set<Transaction> transactions = journalEditPanel.getTransactions(bookings);
        journalEditPanel.deleteTransaction(transactions);
    }

    private void editTransaction() {
        setVisible(false);
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        journalEditPanel.editTransaction(transaction);
    }

    private void showDetails() {
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Account account = booking.getAccount();
            AccountDetailsGUI newGui = AccountDetailsGUI.getAccountDetails(account, journals, journalEditPanel);
            newGui.selectObject(booking);
        }
    }

}
