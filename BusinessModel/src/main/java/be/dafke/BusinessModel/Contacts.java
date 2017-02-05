package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class Contacts extends BusinessCollection<Contact> {
    private Accounting accounting;

    public Contacts(Accounting accounting) {
        this.accounting = accounting;
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
