package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class OpenSupplierInvoicesPanel extends JPanel {

    final JButton pay, receive
    final SelectableTable<PurchaseOrder> tabel
    OpenSupplierInvoicesTableModel openSupplierInvoicesTableModel
    Accounting accounting

    OpenSupplierInvoicesPanel() {
        openSupplierInvoicesTableModel = new OpenSupplierInvoicesTableModel()
        tabel = new SelectableTable<>(openSupplierInvoicesTableModel)
//        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))

//        DefaultListSelectionModel selection = new DefaultListSelectionModel()
//        selection.addListSelectionListener(this)
//        tabel.setSelectionModel(selection)

        JScrollPane scrollPane = new JScrollPane(tabel)

        pay = new JButton("Pay")
        receive = new JButton("Receive (CN)")

        pay.addActionListener({ e ->
            AccountActions.book selectedAccount, true, null, accounting, this, orderAmount
        })

        receive.addActionListener({ e ->
            AccountActions.book selectedAccount, false, null, accounting, this, orderAmount
        })

        JPanel buttonPanel = new JPanel()
        buttonPanel.add pay
        buttonPanel.add receive

        setLayout new BorderLayout()
        add scrollPane, BorderLayout.CENTER
        add buttonPanel, BorderLayout.SOUTH
    }

    Account getSelectedAccount(){
        PurchaseOrder purchaseOrder = tabel.getSelectedObject()
        purchaseOrder?purchaseOrder.supplier.supplierAccount:null
    }

    BigDecimal getOrderAmount(){
        PurchaseOrder purchaseOrder = tabel.getSelectedObject()
        if (purchaseOrder == null) return null
        purchaseOrder.getTotalPurchasePriceInclVat()
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        openSupplierInvoicesTableModel.setPurchaseOrders accounting?accounting.purchaseOrders:null
    }
}