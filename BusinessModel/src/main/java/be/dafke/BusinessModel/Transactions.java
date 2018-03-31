package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


// FIXME: should be:
// Transactions extends BusinessCollection<Transaction>
// Journal extends Transaction (+ override add/remove BusinessObject (do not book Accounts)
public class Transactions extends Journal {

    private final Accounting accounting;

    public Transactions(Accounting accounting) {
        super("Master", "MA");
        this.accounting=accounting;
    }

    public Transaction addBusinessObject(Transaction transaction) {
        for (Booking booking : transaction.getBusinessObjects()) {
            Account account = booking.getAccount();
            Movement movement = booking.getMovement();
            boolean book = !transaction.isBalanceTransaction();
            account.addBusinessObject(movement, book);
        }

        if(!transaction.isBalanceTransaction()) {
            Mortgage mortgage = transaction.getMortgage();
            if (mortgage != null) {
                mortgage.raiseNrPayed();
            }

            if (accounting.isVatAccounting() && accounting.getVatTransactions() != null) {
                VATTransaction vatTransaction = transaction.getVatTransaction();
                if (vatTransaction != null) {
                    VATTransactions vatTransactions = accounting.getVatTransactions();
                    // TODO: raise count here, not when creating the VATTransaction (+ set ID)
                    // TODO: remove below 2 lines
                    int count = VATTransaction.raiseCount();
                    vatTransaction.setId(count);
                    vatTransactions.addBusinessObject(vatTransaction);
                }
                Contact contact = transaction.getContact();
                BigDecimal turnOverAmount = transaction.getTurnOverAmount();
                BigDecimal vatAmount = transaction.getVATAmount();
                if (contact != null && turnOverAmount != null && vatAmount != null) {
                    contact.increaseTurnOver(turnOverAmount);
                    contact.increaseVATTotal(vatAmount);
                }
            }
        }
        return transactions.addValue(transaction.getDate(),transaction);
    }

    public void removeBusinessObject(Transaction transaction) {
        ArrayList<Booking> bookings = transaction.getBusinessObjects();
        for (Booking booking : bookings) {
            Account account = booking.getAccount();
            boolean book = !transaction.isBalanceTransaction();
            account.removeBusinessObject(booking.getMovement(), book);
        }

        Mortgage mortgage = transaction.getMortgage();
        if (mortgage != null) {
            mortgage.decreaseNrPayed();
        }

        if (accounting.isVatAccounting() && accounting.getVatTransactions() != null) {
            VATTransaction vatTransaction = transaction.getVatTransaction();
            if (vatTransaction != null) {
                VATTransactions vatTransactions = accounting.getVatTransactions();
                vatTransactions.removeBusinessObject(vatTransaction);
            }
            Contact contact = transaction.getContact();
            BigDecimal turnOverAmount = transaction.getTurnOverAmount();
            BigDecimal vatAmount = transaction.getVATAmount();
            if (contact != null && turnOverAmount != null && vatAmount != null) {
                contact.decreaseTurnOver(turnOverAmount);
                contact.decreaseVATTotal(vatAmount);
            }
        }
        // do not remove transactions from master
    }

    public List<Transaction> getBusinessObjects(Predicate<Transaction> predicate) {
        return getBusinessObjects().stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public Transaction getBusinessObject(Integer id){
        List<Transaction> transactions = getBusinessObjects(transaction -> id.equals(transaction.getTransactionId()));
        if(transactions==null || transactions.isEmpty()){
            return null;
        }else return transactions.get(0);
    }
}

