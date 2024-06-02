package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.PurchaseOrders
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.function.Predicate

class PurchaseOrderCreatePanel extends JPanel {
    final JButton orderButton
    final SelectableTable<OrderItem> table
    final TotalsPanel totalsPanel
    PurchaseOrder purchaseOrder
    JComboBox<Contact> comboBox
    JCheckBox bookDirect
    Contacts contacts
    Articles articles
    Contact contact
    Predicate<Contact> filter
    Accounting accounting
    final PurchaseOrderCreateDataTableModel purchaseOrderCreateDataTableModel
    PurchaseOrdersOverviewPanel parentOverviewPanel

    PurchaseOrderCreatePanel(Accounting accounting, PurchaseOrdersOverviewPanel parentOverviewPanel) {
        this.accounting = accounting
        this.parentOverviewPanel = parentOverviewPanel
        this.contacts = accounting.contacts
        this.articles = accounting.articles
        purchaseOrder = new PurchaseOrder()
        purchaseOrder.articles = articles

        totalsPanel = new TotalsPanel()
        purchaseOrderCreateDataTableModel = new PurchaseOrderCreateDataTableModel(articles, null, purchaseOrder, totalsPanel)
        table = new SelectableTable<>(purchaseOrderCreateDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        comboBox = new JComboBox<>()
        comboBox.addActionListener({ e ->
            contact = (Contact) comboBox.selectedItem
            purchaseOrderCreateDataTableModel.setContact(contact)
        })
        filter = {it.supplier != null}
        contacts.getBusinessObjects(filter).forEach({ contact -> comboBox.addItem(contact) })

        bookDirect = new JCheckBox("Book Immediately")
        orderButton = new JButton("Add Purchase Order")
        orderButton.addActionListener({ e -> order() })
        JPanel buttonPanel = new JPanel()
        buttonPanel.add bookDirect
        buttonPanel.add orderButton

        JPanel south = new JPanel(new BorderLayout())
        south.add(buttonPanel, BorderLayout.SOUTH)
        south.add(totalsPanel, BorderLayout.CENTER)

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)
        add(comboBox, BorderLayout.NORTH)
        add(south, BorderLayout.SOUTH)
    }

    void order(){
        PurchaseOrders purchaseOrders = accounting.purchaseOrders
        purchaseOrder.supplier = contact
        try {
            purchaseOrder.removeEmptyOrderItems()
            purchaseOrder.amountLeftToPay = purchaseOrder.getTotalPurchasePriceInclVat()
            purchaseOrders.addBusinessObject purchaseOrder
            parentOverviewPanel.refresh()
            purchaseOrder = new PurchaseOrder()
            purchaseOrder.articles = articles
            purchaseOrderCreateDataTableModel.order = purchaseOrder
            // TODO: pass view panel and call directly
//            PurchaseOrdersOverviewGUI.firePurchaseOrderAddedOrRemovedForAccounting accounting
            totalsPanel.fireOrderContentChanged purchaseOrder
            if(bookDirect.isSelected()){
                // TODO: this is partial copy-paste from PurchaseOrdersDetailPanel.placeOrder()
                // FIXME: refactoring needed
//                Transaction transaction =
                Main.createAndBookPurchaseOrder(purchaseOrder)



//                Main.switchView(JournalType.PURCHASE_TYPE)
//                Main.setJournal(journal)
//                Main.selectTransaction(transaction)
//                Main.displayTransaction(transaction)
//                StockHistoryGUI.fireStockContentChanged()
            }
        } catch (EmptyNameException e1) {
            e1.printStackTrace()
        } catch (DuplicateNameException e1) {
            e1.printStackTrace()
        }

    }
}
