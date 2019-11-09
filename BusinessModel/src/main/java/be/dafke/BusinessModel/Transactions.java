package be.dafke.BusinessModel;

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
    private Integer id=0;

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

        int transactionId = transaction.getTransactionId();
        if(!transaction.isBalanceTransaction()) {
            Mortgage mortgage = transaction.getMortgage();
            if (mortgage != null) {
                mortgage.raiseNrPayed();
            }

            if (accounting.isVatAccounting()){ // && accounting.getVatTransactions() != null) {
//                ArrayList<VATBooking> vatBookings = transaction.getVatBookings();
//                if (vatBookings != null) {
//                    VATTransactions vatTransactions = accounting.getVatTransactions();
//                    // TODO: raise count here, not when creating the VATTransaction (+ set ID)
//                    // TODO: remove below 2 lines
////                    vatBookings.setId(transactionId);
//                    vatTransactions.addBusinessObject(vatBookings);
//                }
                Contact contact = transaction.getContact();
                BigDecimal turnOverAmount = transaction.getTurnOverAmount();
                BigDecimal vatAmount = transaction.getVATAmount();
                if (contact != null){
                    if(turnOverAmount != null)
                        contact.increaseTurnOver(turnOverAmount);
                    if(vatAmount != null)
                        contact.increaseVATTotal(vatAmount);
                }
            }
        }
        //TODO: save per ID, sort per date in UI
//        super.addBusinessObject(transaction);
        return transactions.put(transactionId,transaction);
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

        if (accounting.isVatAccounting()){ // && accounting.getVatTransactions() != null) {
//            VATTransaction vatTransaction = transaction.getVatTransaction();
//            if (vatTransaction != null) {
//                VATTransactions vatTransactions = accounting.getVatTransactions();
//                vatTransactions.removeBusinessObject(vatTransaction);
//            }
            Contact contact = transaction.getContact();
            BigDecimal turnOverAmount = transaction.getTurnOverAmount();
            BigDecimal vatAmount = transaction.getVATAmount();
            if (contact != null){
                if(turnOverAmount != null)
                    contact.decreaseTurnOver(turnOverAmount);
                if(vatAmount != null)
                    contact.decreaseVATTotal(vatAmount);
            }
        }
        // do not remove transactions from master, just remove Journal link
        transaction.setJournal(null);
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

    public void setId(int newId) {
        System.err.println("setId: "+id+" --> "+newId);
        id = newId;
    }
    public void setId(Transaction transaction) {
        if(transaction.getTransactionId()==0) {
            transaction.setTransactionId(++id);
        }
    }
}

