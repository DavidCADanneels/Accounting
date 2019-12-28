package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Contacts.ContactSelectorDialog
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.Accounting.BusinessModel.SalesOrders
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.ComboBoxModel
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.function.Predicate

class SalesOrderCreatePanel extends JPanel {
    Contact noInvoice
    SalesOrder salesOrder
    JCheckBox invoice
    JCheckBox creditNote
    JComboBox<Contact> comboBox
    Contacts contacts
    Articles articles
    Accounting accounting
    Contact contact
    Predicate<Contact> filter
    TotalsPanel saleTotalsPanel
    final SalesOrderCreateDataTableModel salesOrderCreateDataTableModel

    SalesOrderCreatePanel(Accounting accounting) {
        this.accounting = accounting
        this.contacts = accounting.contacts
        this.articles = accounting.articles
        noInvoice=accounting.contactNoInvoice
        salesOrder = new SalesOrder()
        salesOrder.articles = articles

        saleTotalsPanel = new TotalsPanel()
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(accounting, salesOrder, saleTotalsPanel)
        SelectableTable<OrderItem> table = new SelectableTable<>(salesOrderCreateDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        creditNote = new JCheckBox("CreditNote")
        creditNote.selected = false
        creditNote.addActionListener({ e ->
            salesOrder.creditNote = creditNote.selected
        })

        invoice = new JCheckBox("Invoice")
        invoice.addActionListener({ e ->
            comboBox.enabled = invoice.selected
            salesOrder.invoice = invoice.selected
            ComboBoxModel<Contact> model = comboBox.getModel()
            if (invoice.selected) {
                model.selectedItem = contact
            } else {
                if (noInvoice == null) {
                    ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, Contact.ContactType.CUSTOMERS)
                    contactSelectorDialog.setLocation getLocationOnScreen()
                    contactSelectorDialog.visible = true
                    noInvoice = contactSelectorDialog.selection
                    accounting.contactNoInvoice = noInvoice
                }
                model.selectedItem = noInvoice
            }
        })
        //
        comboBox = new JComboBox<>()
        comboBox.addActionListener({ e ->
            if (invoice.selected) {
                contact = (Contact) comboBox.selectedItem
            }
        })
        JPanel north = new JPanel()
        north.add(invoice)
        north.add(comboBox)
        north.add(creditNote)

        filter = {it.customer}
        fireCustomerAddedOrRemoved()
        comboBox.setSelectedItem(noInvoice)
        comboBox.enabled = false

        JButton orderButton = new JButton("Add Sales Order")
        orderButton.addActionListener({ e -> orderAction() })
        JPanel south = new JPanel(new BorderLayout())
        south.add(orderButton, BorderLayout.SOUTH)
        south.add(saleTotalsPanel, BorderLayout.CENTER)

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())

        add(scrollPane, BorderLayout.CENTER)
        add(north, BorderLayout.NORTH)
        add(south, BorderLayout.SOUTH)
    }

    void orderAction(){
        SalesOrders salesOrders = accounting.salesOrders
        if (invoice.selected) {
            salesOrder.customer = contact
        } else {
            salesOrder.customer = noInvoice
        }
        try {
            salesOrder.removeEmptyOrderItems()
            if (!salesOrder.name) {
                salesOrders.addBusinessObject salesOrder
            }
            salesOrder = new SalesOrder()
            salesOrder.articles = articles
            salesOrderCreateDataTableModel.order = salesOrder
            saleTotalsPanel.fireOrderContentChanged salesOrder
            SalesOrdersOverviewGUI.fireSalesOrderAddedOrRemovedForAccounting accounting
        } catch (EmptyNameException e1) {
            e1.printStackTrace()
        } catch (DuplicateNameException e1) {
            e1.printStackTrace()
        }
    }

    void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder
        comboBox.enabled = salesOrder!=null
        invoice.enabled = salesOrder!=null
        creditNote.enabled = salesOrder != null
        if (salesOrder){
            invoice.selected = salesOrder.invoice
            if(salesOrder.invoice) {
                contact = salesOrder.customer
                comboBox.selectedItem = contact
            } else {
                noInvoice = salesOrder.customer
                comboBox.selectedItem = noInvoice
            }
            creditNote.selected = salesOrder.creditNote
        } else {
            contact = null
            noInvoice = null
        }
        salesOrderCreateDataTableModel.order = salesOrder
        salesOrderCreateDataTableModel.fireTableDataChanged()
    }

    void fireCustomerAddedOrRemoved() {
        comboBox.removeAllItems()
        contacts.getBusinessObjects(filter).forEach({ contact -> comboBox.addItem(contact) })
//        salesOrderDataTableModel.fireTableDataChanged()
    }
}
