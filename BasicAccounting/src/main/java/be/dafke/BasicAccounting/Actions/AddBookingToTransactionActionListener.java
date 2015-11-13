package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountsGUI;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AddBookingToTransactionActionListener implements ActionListener {
    private Accountings accountings;
    private AccountsGUI accountsGUI;

    public static final String DEBIT = "DEBIT";
    public static final String CREDIT = "CREDIT";

    public AddBookingToTransactionActionListener(Accountings accountings, AccountsGUI accountsGUI) {
        this.accountings = accountings;
        this.accountsGUI = accountsGUI;
    }

    public void actionPerformed(ActionEvent ae) {
        Accounting accounting = accountings.getCurrentObject();
        Journals journals = accounting.getJournals();
        Journal journal = journals.getCurrentObject();
        Account account = accountsGUI.getSelectedAccount();
        Transaction transaction = journal.getCurrentObject();

        String actionCommand = ae.getActionCommand();
        boolean debit = actionCommand.equals(DEBIT);
        BigDecimal amount = askAmount(transaction, account, debit);
        if(amount!=null) {
            Booking booking = new Booking(account);
            booking.addBusinessObject(new Movement(amount, debit));
            transaction.addBusinessObject(booking);
            ComponentMap.refreshAllFrames();
        }
    }

    public static BigDecimal askAmount(Transaction transaction, Account account, boolean debit){
        BigDecimal creditTotal = transaction.getCreditTotaal();
        BigDecimal debitTotal = transaction.getDebetTotaal();
        boolean suggestion = false;
        BigDecimal suggestedAmount = null;
        if(creditTotal.compareTo(debitTotal)>0 && debit){
            suggestion = true;
            suggestedAmount = creditTotal.subtract(debitTotal);
        } else if(debitTotal.compareTo(creditTotal)>0 && !debit){
            suggestion = true;
            suggestedAmount = debitTotal.subtract(creditTotal);
        } else {
            BigDecimal defaultAmount = account.getDefaultAmount();
            if(defaultAmount!=null){
                suggestion = true;
                suggestedAmount = defaultAmount;
            }
        }
        boolean ok = false;
        BigDecimal amount = null;
        while (!ok) {
            String s;
            if(suggestion){
                s = JOptionPane.showInputDialog(getBundle("Accounting").getString(
                        "ENTER_AMOUNT"), suggestedAmount.toString());
            } else {
                s = JOptionPane.showInputDialog(getBundle("Accounting").getString(
                        "ENTER_AMOUNT"));
            }
            if (s == null || s.equals("")) {
                ok = true;
                amount = null;
            } else {
                try {
                    amount = new BigDecimal(s);
                    amount = amount.setScale(2);
                    ok = true;
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null,
                            getBundle("Accounting").getString("INVALID_INPUT"));
                }
            }
        }
        return amount;
    }
}
