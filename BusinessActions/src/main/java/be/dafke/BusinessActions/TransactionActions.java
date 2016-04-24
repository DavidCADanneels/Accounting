package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.Mortgage;
import be.dafke.BusinessModel.MortgageTransaction;
import be.dafke.BusinessModel.Movement;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class TransactionActions {

    public static void createMortgageTransaction(Mortgage mortgage, Transaction transaction){
        if (mortgage.isPayedOff()) {
            System.out.println("Payed Off already");
            return;
        }
        Account capitalAccount = mortgage.getCapitalAccount();
        Account intrestAccount = mortgage.getIntrestAccount();
        if(capitalAccount==null || intrestAccount==null){
            return;
        }
        Booking capitalBooking = new Booking(capitalAccount, mortgage.getNextCapitalAmount(),true);
        Booking intrestBooking = new Booking(intrestAccount, mortgage.getNextIntrestAmount(),true);


        transaction.addBusinessObject(capitalBooking);
        transaction.addBusinessObject(intrestBooking);

        MortgageTransaction mortgageTransaction = mortgage.createNewChild();
        mortgageTransaction.addBusinessObject(capitalBooking);
        mortgageTransaction.addBusinessObject(intrestBooking);
        mortgage.addBusinessObject(mortgageTransaction);

        ComponentMap.refreshAllFrames();
    }

    public static void addBookingToTransaction(Account account, Transaction transaction, boolean debit) {
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

    public static void deleteTransaction(Transaction transaction) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
        Object[] messageArguments = {journal.getName()};
        MessageFormat formatter = new MessageFormat(text);
        String output = formatter.format(messageArguments);
        JOptionPane.showMessageDialog(null, output);
        ComponentMap.refreshAllFrames();
    }

    public static void editTransaction(Transaction transaction, Journals journals) {
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        journal.setCurrentObject(transaction);
        journals.setCurrentObject(journal);
        String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
        Object[] messageArguments = {journal.getName()};
        MessageFormat formatter = new MessageFormat(text);
        String output = formatter.format(messageArguments);
        JOptionPane.showMessageDialog(null, output);
        ComponentMap.refreshAllFrames();
    }

    public static void moveTransaction(Transaction transaction, Journals journals) {
        Journal journal = transaction.getJournal();
        ArrayList<Journal> dagboeken = journals.getAllJournalsExcept(journal);
        Object[] lijst = dagboeken.toArray();
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("Accounting").getString("CHOOSE_JOURNAL"),
                getBundle("Accounting").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
        if(keuze!=JOptionPane.CANCEL_OPTION && keuze!=JOptionPane.CLOSED_OPTION){
            Journal newJournal = (Journal) lijst[keuze];
            journal.removeBusinessObject(transaction);
            newJournal.addBusinessObject(transaction);
            String text = getBundle("Accounting").getString("TRANSACTION_MOVED");
            Object[] messageArguments = {journal.getName(), newJournal.getName()};
            MessageFormat formatter = new MessageFormat(text);
            String output = formatter.format(messageArguments);
            JOptionPane.showMessageDialog(null,output);
        }
        ComponentMap.refreshAllFrames();
    }
}
