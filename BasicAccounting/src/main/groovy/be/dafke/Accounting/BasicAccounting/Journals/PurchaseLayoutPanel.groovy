package be.dafke.Accounting.BasicAccounting.Journals

import javax.swing.JPanel
import java.awt.BorderLayout

class PurchaseLayoutPanel extends JPanel {

    UnbookedSupplierInvoicesPanel unbookedSupplierInvoicesPanel

    PurchaseLayoutPanel() {
        unbookedSupplierInvoicesPanel = new UnbookedSupplierInvoicesPanel()
        setLayout(new BorderLayout())
        add(unbookedSupplierInvoicesPanel, BorderLayout.CENTER)
    }

}
