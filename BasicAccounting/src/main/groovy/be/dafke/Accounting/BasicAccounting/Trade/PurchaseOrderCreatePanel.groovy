package be.dafke.Accounting.BasicAccounting.Trade

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
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.function.Predicate

class PurchaseOrderCreatePanel extends JPanel {
    private final JButton orderButton
    private final SelectableTable<OrderItem> table
    private final TotalsPanel totalsPanel
    private PurchaseOrder purchaseOrder
    private JComboBox<Contact> comboBox
    private Contacts contacts
    private Articles articles
    private Contact contact
    Predicate<Contact> filter
    private Accounting accounting
    private final PurchaseOrderCreateDataTableModel purchaseOrderCreateDataTableModel

    PurchaseOrderCreatePanel(Accounting accounting) {
        this.accounting = accounting
        this.contacts = accounting.getContacts()
        this.articles = accounting.getArticles()
        purchaseOrder = new PurchaseOrder()
        purchaseOrder.setArticles(articles)

        totalsPanel = new TotalsPanel()
        purchaseOrderCreateDataTableModel = new PurchaseOrderCreateDataTableModel(articles, null, purchaseOrder, totalsPanel)
        table = new SelectableTable<>(purchaseOrderCreateDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        comboBox = new JComboBox<>()
        comboBox.addActionListener({ e ->
            contact = (Contact) comboBox.getSelectedItem()
            purchaseOrderCreateDataTableModel.setContact(contact)
        })
        filter = Contact.&isSupplier
        fireSupplierAddedOrRemoved()

        orderButton = new JButton("Add Purchase Order")
        orderButton.addActionListener({ e -> order() })
        JPanel south = new JPanel(new BorderLayout())
        south.add(orderButton, BorderLayout.SOUTH)
        south.add(totalsPanel, BorderLayout.CENTER)

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)
        add(comboBox, BorderLayout.NORTH)
        add(south, BorderLayout.SOUTH)
    }

    void order(){
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders()
        purchaseOrder.setSupplier(contact)
        try {
            purchaseOrder.removeEmptyOrderItems()
            purchaseOrders.addBusinessObject(purchaseOrder)
            purchaseOrder = new PurchaseOrder()
            purchaseOrder.setArticles(articles)
            purchaseOrderCreateDataTableModel.setOrder(purchaseOrder)
            // TODO: pass view panel and call directly
            PurchaseOrdersOverviewGUI.firePurchaseOrderAddedOrRemovedForAccounting(accounting)
            totalsPanel.fireOrderContentChanged(purchaseOrder)
        } catch (EmptyNameException e1) {
            e1.printStackTrace()
        } catch (DuplicateNameException e1) {
            e1.printStackTrace()
        }
    }

    void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems()
        contacts.getBusinessObjects(filter).forEach({ contact -> comboBox.addItem(contact) })
//        purchaseOrderDataTableModel.fireTableDataChanged()
    }
}
