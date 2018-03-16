package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class SalesOrders extends BusinessCollection<SalesOrder>{

    private static int id = 0;
    private Account VATAccount;
    private Account stockAccount;
    private Journal journal;
    private Account gainAccount;

    public SalesOrders() {
        super();
    }

    public static void setId(int id) {
        SalesOrders.id = id;
    }

    public SalesOrder addBusinessObject(SalesOrder order) throws EmptyNameException, DuplicateNameException {
        if (order.getName()==null) {
            order.setName("SO" + ++id);
        }
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }

    public Account getVATAccount() {
        return VATAccount;
    }

    public Account getStockAccount() {
        return stockAccount;
    }

    public void setVATAccount(Account VATAccount) {
        this.VATAccount = VATAccount;
    }

    public void setStockAccount(Account stockAccount) {
        this.stockAccount = stockAccount;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public Account getGainAccount() {
        return gainAccount;
    }

    public void setGainAccount(Account gainAccount) {
        this.gainAccount = gainAccount;
    }
}
