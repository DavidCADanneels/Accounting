package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class SupplierOrdersPanel extends JPanel {

    final SupplierOrdersDataModel model
    final SelectableTable<PurchaseOrder> table

    SupplierOrdersPanel(ArrayList<PurchaseOrder> list) {
        setLayout(new BorderLayout())

        model = new SupplierOrdersDataModel(list)
        table = new SelectableTable<>(model)
        
        JScrollPane scrollPane = new JScrollPane(table)
        
        add scrollPane, BorderLayout.CENTER
    }
}
