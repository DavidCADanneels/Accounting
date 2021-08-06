package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Customer

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class CustomerOrdersGUI extends JFrame {
    final CustomerOrdersPanel supplierOrdersPanel

    static final HashMap<Customer, CustomerOrdersGUI> guis = new HashMap<>()

    CustomerOrdersGUI(Customer customer) {
        super("${getBundle("Accounting").getString("SALE_ORDERS")} for ${customer}")
        supplierOrdersPanel = new CustomerOrdersPanel(customer.salesOrders)
        setContentPane(supplierOrdersPanel)
        pack()
    }

    static CustomerOrdersGUI showOrders(Customer customer){
        CustomerOrdersGUI gui = guis.get(customer)
        if(gui == null){
            gui = new CustomerOrdersGUI(customer)
            guis.put(customer, gui)
            Main.addFrame(gui)
        }
        gui
    }
}
