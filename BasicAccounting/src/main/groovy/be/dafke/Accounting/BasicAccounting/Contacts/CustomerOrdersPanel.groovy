package be.dafke.Accounting.BasicAccounting.Contacts

import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.BorderLayout

class CustomerOrdersPanel extends JPanel {
    
    final CustomerOrdersDataModel model
    final SelectableTable<SalesOrder> table
    
    CustomerOrdersPanel(ArrayList<SalesOrder> list) {
        setLayout(new BorderLayout())

        model = new CustomerOrdersDataModel(list)
        table = new SelectableTable<>(model)
        
        JScrollPane scrollPane = new JScrollPane(table)
        
        add scrollPane, BorderLayout.CENTER
    }
}
