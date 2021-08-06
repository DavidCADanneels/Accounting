package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Supplier

import javax.swing.JFrame

import static java.util.ResourceBundle.getBundle

class SupplierOrdersGUI extends JFrame {
    final SupplierOrdersPanel supplierOrdersPanel

    static final HashMap<Contact, SupplierOrdersGUI> guis = new HashMap<>()

    SupplierOrdersGUI(Contact contact) {
        super("${getBundle("Accounting").getString("PURCHASE_ORDERS")} for ${contact.name}")
        Supplier supplier = contact.supplier
        supplierOrdersPanel = new SupplierOrdersPanel(supplier.purchaseOrders)
        setContentPane(supplierOrdersPanel)
        pack()
    }

    static SupplierOrdersGUI showOrders(Contact contact){
        SupplierOrdersGUI gui = guis.get(contact)
        if(gui == null){
            gui = new SupplierOrdersGUI(contact)
            guis.put(contact, gui)
            Main.addFrame(gui)
        }
        gui
    }
}
