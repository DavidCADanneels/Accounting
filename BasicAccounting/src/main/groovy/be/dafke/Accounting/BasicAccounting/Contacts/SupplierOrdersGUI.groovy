package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Supplier

import javax.swing.JFrame

import static java.util.ResourceBundle.getBundle

class SupplierOrdersGUI extends JFrame {
    final SupplierOrdersPanel supplierOrdersPanel

    static final HashMap<Supplier, SupplierOrdersGUI> guis = new HashMap<>()

    SupplierOrdersGUI(Supplier supplier) {
        super("${getBundle("Accounting").getString("PURCHASE_ORDERS")} for ${supplier}")
        supplierOrdersPanel = new SupplierOrdersPanel(supplier.purchaseOrders)
        setContentPane(supplierOrdersPanel)
        pack()
    }

    static SupplierOrdersGUI showOrders(Supplier supplier){
        SupplierOrdersGUI gui = guis.get(supplier)
        if(gui == null){
            gui = new SupplierOrdersGUI(supplier)
            guis.put(supplier, gui)
            Main.addFrame(gui)
        }
        gui
    }
}
