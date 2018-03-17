package be.dafke.BusinessModel;

import java.util.Collection;
import java.util.HashMap;

public class Stock extends OrderItems {
    private boolean removeIfEmpty = true;
    protected final HashMap<Transaction,Order> transactions = new HashMap<>();

    public void purchaseUnits(PurchaseOrder purchaseOrder){
        purchaseOrder.getBusinessObjects().forEach(this::addBusinessObject);
        addTransaction(purchaseOrder.getPurchaseTransaction(), purchaseOrder);
    }

    public void sellItems(SalesOrder salesOrder){
        for (OrderItem orderItem : salesOrder.getBusinessObjects()) {
            remove(orderItem, true, removeIfEmpty);
        }
        addTransaction(salesOrder.getSalesTransaction(),salesOrder);
    }

    public void payOrder(Order order){
        addTransaction(order.getPaymentTransaction(), order);
    }

    public Collection<Order> getTransactions() {
        return transactions.values();
    }

    public void addTransaction(Transaction transaction, Order orderItems){
        transactions.put(transaction, orderItems);
    }
}
