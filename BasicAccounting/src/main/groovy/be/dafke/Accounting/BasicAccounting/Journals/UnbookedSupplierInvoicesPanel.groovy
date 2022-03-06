package be.dafke.Accounting.BasicAccounting.Journals


import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class UnbookedSupplierInvoicesPanel extends JPanel {

    final JButton book
    final SelectableTable<PurchaseOrder> tabel
    UnbookedSupplierInvoicesTableModel tableModel

    UnbookedSupplierInvoicesPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Open Purchase Orders"))
        tableModel = new UnbookedSupplierInvoicesTableModel()
        tabel = new SelectableTable<>(tableModel)

        JScrollPane scrollPane = new JScrollPane(tabel)

        book = new JButton("Book")

        book.addActionListener({ e ->
            PurchaseOrder purchaseOrder = tabel.getSelectedObject()
            if(purchaseOrder) {
                Transaction transaction = Main.createPurchaseOrder purchaseOrder
//                Transaction transaction = Main.bookPurchaseOrder purchaseOrder
//                Redundant: setTransaction is call in createPurchaseTransaction
//                Journal journal = StockUtils.getPurchaseJournal accounting
//                Main.setJournal(journal)
                Main.displayTransaction(transaction)
//                refresh()
            }
        })

        JPanel buttonPanel = new JPanel()
        buttonPanel.add book

        setLayout new BorderLayout()
        add scrollPane, BorderLayout.CENTER
        add buttonPanel, BorderLayout.SOUTH
    }

    void refresh(){
        tableModel.fireTableDataChanged()
    }
}