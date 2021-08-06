package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Contacts.ContactsDataModel
import be.dafke.Accounting.BusinessModel.Contact

import javax.swing.*

class ContactsSelectorPanel extends JPanel {

    JToggleButton customers, suppliers, all
    JCheckBox withOrdersOnly

    ButtonGroup buttonGroup
    ContactsDataModel dataModel

    ContactsSelectorPanel(ContactsDataModel contactsDataModel) {
        dataModel = contactsDataModel
        customers = new JToggleButton('Customers only')
        suppliers = new JToggleButton('Suppliers only')
        all = new JToggleButton('All', true)

        withOrdersOnly = new JCheckBox("With Orders only")

        buttonGroup = new ButtonGroup()

        buttonGroup.add all
        buttonGroup.add customers
        buttonGroup.add suppliers

        add all
        add customers
        add suppliers
        add withOrdersOnly

        customers.addActionListener( {
//            Main.switchView(Main.CONTACTS_CENTER_VIEW)
            dataModel.setContactType(Contact.ContactType.CUSTOMERS)
        })
        suppliers.addActionListener( {
//            Main.switchView(Main.CONTACTS_CENTER_VIEW)
            dataModel.setContactType(Contact.ContactType.SUPPLIERS)
        })
        all.addActionListener( {
//            Main.switchView(Main.CONTACTS_CENTER_VIEW)
            dataModel.setContactType(Contact.ContactType.ALL)
        })
        withOrdersOnly.addActionListener( {
            dataModel.setWithOrdersOnly(withOrdersOnly.isSelected())
        })
    }
}
