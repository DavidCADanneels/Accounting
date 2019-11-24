package be.dafke.Accounting.BusinessModel

import java.util.function.Predicate
// FIXME: should be:
// Transactions extends BusinessCollection<Transaction>
// Journal extends Transaction (+ override add/remove BusinessObject (do not book Accounts)
class Transactions extends Journal {

    private final Accounting accounting
    private Integer id=0

    Transactions(Accounting accounting) {
        super("Master", "MA")
        this.accounting=accounting
    }

    Transaction addBusinessObject(Transaction transaction) {
        for (Booking booking : transaction.getBusinessObjects()) {
            Account account = booking.getAccount()
            Movement movement = booking.getMovement()
            boolean book = !transaction.isBalanceTransaction()
            account.addBusinessObject(movement, book)
        }

        int transactionId = transaction.getTransactionId()
        if(!transaction.isBalanceTransaction()) {
            Mortgage mortgage = transaction.getMortgage()
            if (mortgage != null) {
                mortgage.raiseNrPayed()
            }

            if (accounting.isVatAccounting()){ // && accounting.getVatTransactions() != null) {
//                ArrayList<VATBooking> vatBookings = transaction.getVatBookings()
//                if (vatBookings != null) {
//                    VATTransactions vatTransactions = accounting.getVatTransactions()
//                    // TODO: raise count here, not when creating the VATTransaction (+ set ID)
//                    // TODO: remove below 2 lines
////                    vatBookings.setId(transactionId)
//                    vatTransactions.addBusinessObject(vatBookings)
//                }
                Contact contact = transaction.getContact()
                BigDecimal turnOverAmount = transaction.getTurnOverAmount()
                BigDecimal vatAmount = transaction.getVATAmount()
                if (contact != null){
                    if(turnOverAmount != null)
                        contact.increaseTurnOver(turnOverAmount)
                    if(vatAmount != null)
                        contact.increaseVATTotal(vatAmount)
                }
            }
        }
        //TODO: save per ID, sort per date in UI
//        super.addBusinessObject(transaction)
        transactions.put(transactionId,transaction)
    }

    void removeBusinessObject(Transaction transaction) {
        ArrayList<Booking> bookings = transaction.getBusinessObjects()
        for (Booking booking : bookings) {
            Account account = booking.getAccount()
            boolean book = !transaction.isBalanceTransaction()
            account.removeBusinessObject(booking.getMovement(), book)
        }

        Mortgage mortgage = transaction.getMortgage()
        if (mortgage != null) {
            mortgage.decreaseNrPayed()
        }

        if (accounting.isVatAccounting()){ // && accounting.getVatTransactions() != null) {
//            VATTransaction vatTransaction = transaction.getVatTransaction()
//            if (vatTransaction != null) {
//                VATTransactions vatTransactions = accounting.getVatTransactions()
//                vatTransactions.removeBusinessObject(vatTransaction)
//            }
            Contact contact = transaction.getContact()
            BigDecimal turnOverAmount = transaction.getTurnOverAmount()
            BigDecimal vatAmount = transaction.getVATAmount()
            if (contact != null){
                if(turnOverAmount != null)
                    contact.decreaseTurnOver(turnOverAmount)
                if(vatAmount != null)
                    contact.decreaseVATTotal(vatAmount)
            }
        }
        // do not remove transactions from master, just remove Journal link
        transaction.setJournal(null)
    }

    List<Transaction> getBusinessObjects(Predicate<Transaction> predicate) {
        getBusinessObjects().stream().filter(predicate).collect().toList()
    }

    Transaction getBusinessObject(Integer id){
        List<Transaction> transactions = getBusinessObjects({ transaction -> id.equals(transaction.getTransactionId()) })
        if(transactions==null || transactions.isEmpty()){
            null
        }else transactions.get(0)
    }

    void setId(int newId) {
        System.err.println("setId: "+id+" --> "+newId)
        id = newId
    }
    void setId(Transaction transaction) {
        if(transaction.getTransactionId()==0) {
            transaction.setTransactionId(++id)
        }
    }
}

