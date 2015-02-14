package be.dafke.BasicAccounting.Actions;

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

    public static final String DEBIT = "DEBIT";
    public static final String CREDIT = "CREDIT";

    public AddBookingToTransactionActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        boolean debit = actionCommand.equals(DEBIT);
        Accounting accounting = accountings.getCurrentObject();
        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();
        Journal journal = journals.getCurrentObject();
        Account account = accounts.getCurrentObject();
        Transaction transaction = journal.getCurrentObject();
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
            } else {
                try {
                    BigDecimal amount = new BigDecimal(s);
                    amount = amount.setScale(2);
                    Booking booking = new Booking(account);
                    booking.addBusinessObject(new Movement(amount,debit));
                    transaction.addBusinessObject(booking);
                    ok = true;
                    ComponentMap.refreshAllFrames();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null,
                            getBundle("Accounting").getString("INVALID_INPUT"));
                }
            }
        }
    }
}
