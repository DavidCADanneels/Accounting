package be.dafke.Accounting.BasicAccounting.Journals


import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class OpenCustomerInvoicesPanel extends JPanel {

    final SelectableTable<SalesOrder> tabel
    OpenCustomerInvoicesTableModel openCustomerInvoicesTableModel
    Accounting accounting

    OpenCustomerInvoicesPanel() {
        openCustomerInvoicesTableModel = new OpenCustomerInvoicesTableModel()
        tabel = new SelectableTable<>(openCustomerInvoicesTableModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))

//        DefaultListSelectionModel selection = new DefaultListSelectionModel()
//        selection.addListSelectionListener(this)
//        tabel.setSelectionModel(selection)


        JScrollPane scrollPane = new JScrollPane(tabel)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        openCustomerInvoicesTableModel.setSalesOrders(accounting?accounting.salesOrders:null)
    }
}