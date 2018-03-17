package be.dafke.BusinessModel;

import java.util.Collection;
import java.util.HashMap;

public class Stock extends OrderItems {
    private boolean removeIfEmpty = true;
    protected final HashMap<Transaction,Order> transactions = new HashMap<>();

    public void purchaseUnits(Order load){
        load.getBusinessObjects().forEach(this::addBusinessObject);
        addTransaction(load.getTransaction(), load);
    }

    public void sellItems(Order load){
        for (OrderItem orderItem : load.getBusinessObjects()) {
            remove(orderItem, true, removeIfEmpty);
        }
        addTransaction(load.getTransaction(),load);
    }

    public Collection<Order> getTransactions() {
        return transactions.values();
    }

    public void addTransaction(Transaction transaction, Order orderItems){
        transactions.put(transaction, orderItems);
    }
}
