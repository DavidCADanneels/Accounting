package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class UnpayedSupplierInvoicesPanel extends JPanel {

    final JButton pay, receive
    final SelectableTable<PurchaseOrder> tabel
    UnpayedSupplierInvoicesTableModel openSupplierInvoicesTableModel

    UnpayedSupplierInvoicesPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Open Purchase Orders"))
        openSupplierInvoicesTableModel = new UnpayedSupplierInvoicesTableModel()
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