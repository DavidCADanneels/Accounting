package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class Contacts extends BusinessCollection<Contact> {
    private Accounting accounting;

    public Contacts(Accounting accounting) {
        this.accounting = accounting;
    }

    public Contacts(Contacts contacts, Accounts accounts) {
        for (Contact contact: contacts.getBusinessObjects()){
            try {
                addBusinessObject(new Contact(contact, accounts));
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
