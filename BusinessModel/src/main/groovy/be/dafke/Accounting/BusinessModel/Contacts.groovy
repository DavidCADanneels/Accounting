package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

class Contacts extends BusinessCollection<Contact> {
    Accounting accounting

    Contacts(Accounting accounting) {
        this.accounting = accounting
    }

    Contacts(Contacts contacts, Accounts accounts) {
        for (Contact contact: contacts.businessObjects){
            try {
                addBusinessObject(new Contact(contact, accounts))
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    Accounting getAccounting() {
        accounting
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }
}
