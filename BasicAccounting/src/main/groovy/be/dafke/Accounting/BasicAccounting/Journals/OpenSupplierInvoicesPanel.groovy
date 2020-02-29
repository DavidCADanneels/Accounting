package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class OpenSupplierInvoicesPanel extends JPanel {

    final SelectableTable<PurchaseOrder> tabel
    OpenSupplierInvoicesTableModel openSupplierInvoicesTableModel
    Accounting accounting

    OpenSupplierInvoicesPanel() {
        openSupplierInvoicesTableModel = new OpenSupplierInvoicesTableModel()
        tabel = new SelectableTable<>(openSupplierInvoicesTableModel)


//        DefaultListSelectionModel selection = new DefaultListSelectionModel()
//        selection.addListSelectionListener(this)
//        tabel.setSelectionModel(selection)


        JScrollPane scrollPane = new JScrollPane(tabel)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        openSupplierInvoicesTableModel.setPurchaseOrders(accounting?accounting.purchaseOrders:null)
    }
}