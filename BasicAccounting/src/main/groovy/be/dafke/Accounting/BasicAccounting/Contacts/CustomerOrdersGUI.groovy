package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Customer

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class CustomerOrdersGUI extends JFrame {
    final CustomerOrdersPanel supplierOrdersPanel

    static final HashMap<Contact, CustomerOrdersGUI> guis = new HashMap<>()

    CustomerOrdersGUI(Contact contact) {
        super("${getBundle("Accounting").getString("SALE_ORDERS")} for ${contact.name}")
        Customer customer = contact.customer
        supplierOrdersPanel = new CustomerOrdersPanel(customer.salesOrders)
        setContentPane(supplierOrdersPanel)
        pack()
    }

    static CustomerOrdersGUI showOrders(Contact contact){
        CustomerOrdersGUI gui = guis.get(contact)
        if(gui == null){
            gui = new CustomerOrdersGUI(contact)
            guis.put(contact, gui)
            Main.addFrame(gui)
        }
        gui
    }
}
