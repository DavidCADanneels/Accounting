package be.dafke.Accounting.BusinessModel

import java.util.function.Predicate
// FIXME: should be:
// Transactions extends BusinessCollection<Transaction>
// Journal extends Transaction (+ override add/remove BusinessObject (do not book Accounts)
class Transactions extends Journal {

    final Accounting accounting
    int id=0

    Transactions(Accounting accounting) {
        super("Master", "MA")
        this.accounting=accounting
    }

    Transaction addBusinessObject(Transaction transaction) {
        for (Booking booking : transaction.businessObjects) {
            Account account = booking.account
            Movement movement = booking.getMovement()
            boolean book = !transaction.balanceTransaction
            account.addBusinessObject(movement, book)
        }

        int transactionId = transaction.transactionId
        if(!transaction.balanceTransaction) {
            Mortgage mortgage = transaction.mortgage
            if (mortgage != null) {
                mortgage.raiseNrPayed()
            }

            if (accounting.vatAccounting){ // && accounting.vatTransactions != null) {
//                ArrayList<VATBooking> vatBookings = transaction.vatBookings
//                if (vatBookings != null) {
//                    VATTransactions vatTransactions = accounting.vatTransactions
//                    // TODO: raise count here, not when creating the VATTransaction (+ set ID)
//                    // TODO: remove below 2 lines
////                    vatBookings.setId(transactionId)
//                    vatTransactions.addBusinessObject(vatBookings)
//                }
                BigDecimal turnOverAmount = transaction.turnOverAmount
                BigDecimal vatAmount = transaction.VATAmount
                Customer customer = transaction.contact?.customer
                if (customer != null){
                    if(turnOverAmount != null)
                        customer.increaseTurnOver turnOverAmount
                    if(vatAmount != null)
                        customer.increaseVATTotal vatAmount
                    // TODO: add SalesOrder to Customer
                    Order order = transaction.order
                    if(order instanceof SalesOrder){
                        SalesOrder salesOrder = (SalesOrder)order
                        customer.salesOrders.add(salesOrder)
                    } else{
                        // error?
                    }
                }
                Supplier supplier = transaction.contact?.supplier
                if(supplier){
                    // TODO: add PurchaseOrder to Supplier
                    Order order = transaction.order
                    if(order instanceof PurchaseOrder){
                        PurchaseOrder purchaseOrder = (PurchaseOrder)order
                        supplier.purchaseOrders.add(purchaseOrder)
                    } else {
                        // error?
                    }
                }
            }
        }
        //TODO: save per ID, sort per date in UI
//        super.addBusinessObject(transaction)
        transactions.put transactionId,transaction
    }

    void removeBusinessObject(Transaction transaction) {
        ArrayList<Booking> bookings = transaction.businessObjects
        for (Booking booking : bookings) {
            Account account = booking.account
            boolean book = !transaction.balanceTransaction
            account.removeBusinessObject booking.getMovement(), book
        }

        Mortgage mortgage = transaction.mortgage
        if (mortgage != null) {
            mortgage.decreaseNrPayed()
        }

        if (accounting.vatAccounting){ // && accounting.vatTransactions != null) {
//            VATTransaction vatTransaction = transaction.getVatTransaction()
//            if (vatTransaction != null) {
//                VATTransactions vatTransactions = accounting.vatTransactions
//                vatTransactions.removeBusinessObject(vatTransaction)
//            }
            BigDecimal turnOverAmount = transaction.turnOverAmount
            BigDecimal vatAmount = transaction.VATAmount
            Customer customer = transaction.contact?.customer
            if (customer != null){
                if(turnOverAmount != null)
                    customer.decreaseTurnOver turnOverAmount
                if(vatAmount != null)
                    customer.decreaseVATTotal vatAmount
            }
        }
        // do not remove transactions from master, just remove Journal link
        transaction.journal = null
    }

    List<Transaction> getBusinessObjects(Predicate<Transaction> predicate) {
        getBusinessObjects().stream().filter(predicate).collect().toList()
    }

    Transaction getBusinessObject(Integer id){
        List<Transaction> transactions = getBusinessObjects({ transaction -> id.equals(transaction.transactionId) })
        if(transactions==null || transactions.isEmpty()){
            null
        }else transactions.get(0)
    }

    void setId(int newId) {
        System.err.println("setId: "+id+" --> "+newId)
        id = newId
    }
    void setId(Transaction transaction) {
        if(transaction.transactionId==0) {
            transaction.transactionId = ++id
        }
    }
}

