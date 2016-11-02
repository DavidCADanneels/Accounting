package be.dafke.BusinessActions;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static be.dafke.BusinessActions.ActionUtils.TRANSACTION_REMOVED;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class TransactionActions {

    public static void createMortgageTransaction(Accounts accounts, Mortgage mortgage, Transaction transaction){
        if (mortgage.isPayedOff()) {
            System.out.println("Payed Off already");
            return;
        }
        if (transaction.getMortgage()!=null){
            System.out.println("Transaction already contains a mortgages");
            return;
        }
        transaction.setMortgage(mortgage);
        Account capitalAccount = mortgage.getCapitalAccount();
        Account intrestAccount = mortgage.getIntrestAccount();
        if(capitalAccount==null || intrestAccount==null){
            return;
        }
        Booking capitalBooking = new Booking(capitalAccount, mortgage.getNextCapitalAmount(),true);
        Booking intrestBooking = new Booking(intrestAccount, mortgage.getNextIntrestAmount(),true);

        transaction.addBusinessObject(capitalBooking);
        transaction.addBusinessObject(intrestBooking);

        ComponentMap.refreshAllFrames();
    }

    public static void addBookingToTransaction(Accounts accounts, Account account, Transaction transaction, boolean debit) {
        BigDecimal amount = askAmount(transaction, account, debit);
        if (amount != null) {
            Booking booking = new Booking(account, amount, debit);
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
                // TODO: add title ...
                s = JOptionPane.showInputDialog(getBundle("BusinessActions").getString(
                        "ENTER_AMOUNT")+ account.getName(), suggestedAmount.toString());
            } else {
                s = JOptionPane.showInputDialog(getBundle("BusinessActions").getString(
                        "ENTER_AMOUNT")+ account.getName());
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
                    ActionUtils.showErrorMessage(ActionUtils.INVALID_INPUT);
                }
            }
        }
        return amount;
    }

    public static void deleteTransaction(Transaction transaction) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_REMOVED, journal.getName());
        ComponentMap.refreshAllFrames();
    }

    public static void editTransaction(Transaction transaction, Journals journals) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        journal.setCurrentObject(transaction);
        journals.setCurrentObject(journal);
        ActionUtils.showErrorMessage(TRANSACTION_REMOVED,journal.getName());
        ComponentMap.refreshAllFrames();
    }

    public static void moveTransaction(Transaction transaction, Journals journals) {
        Journal journal = transaction.getJournal();
        ArrayList<Journal> dagboeken = journals.getAllJournalsExcept(journal);
        Object[] lijst = dagboeken.toArray();
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("BusinessActions").getString("CHOOSE_JOURNAL"),
                getBundle("BusinessActions").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
        if(keuze!=JOptionPane.CANCEL_OPTION && keuze!=JOptionPane.CLOSED_OPTION){
            Journal newJournal = (Journal) lijst[keuze];
            journal.removeBusinessObject(transaction);
            newJournal.addBusinessObject(transaction);
            ActionUtils.showErrorMessage(ActionUtils.TRANSACTION_MOVED, journal.getName(), newJournal.getName());
        }
        ComponentMap.refreshAllFrames();
    }
}
