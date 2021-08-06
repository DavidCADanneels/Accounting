package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class OpenSupplierInvoicesPanel extends JPanel {

    final JButton pay, receive
    final SelectableTable<PurchaseOrder> tabel
    OpenSupplierInvoicesTableModel openSupplierInvoicesTableModel

    OpenSupplierInvoicesPanel() {
        openSupplierInvoicesTableModel = new OpenSupplierInvoicesTableModel()
        tabel = new SelectableTable<>(openSupplierInvoicesTableModel)

        JScrollPane scrollPane = new JScrollPane(tabel)

        pay = new JButton("Pay")
        receive = new JButton("Receive (CN)")

        pay.addActionListener({ e ->
            PurchaseOrder purchaseOrder = tabel.getSelectedObject()
            if(purchaseOrder) {
                AccountActions.book selectedAccount, true, null, this, purchaseOrder
//                refresh()
            }
        })

        receive.addActionListener({ e ->
            PurchaseOrder purchaseOrder = tabel.getSelectedObject()
            if(purchaseOrder) {
                AccountActions.book selectedAccount, false, null, this, purchaseOrder
//                refresh()
            }
        })

        JPanel buttonPanel = new JPanel()
        buttonPanel.add pay
        buttonPanel.add receive

        setLayout new BorderLayout()
        add scrollPane, BorderLayout.CENTER
        add buttonPanel, BorderLayout.SOUTH
    }

    void refresh(){
        openSupplierInvoicesTableModel.fireTableDataChanged()
    }

    Account getSelectedAccount(){
        PurchaseOrder purchaseOrder = tabel.getSelectedObject()
        purchaseOrder?.supplier?.supplier?.supplierAccount?:null
    }
}