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
    private Contact noInvoice
    private SalesOrder salesOrder
    private JCheckBox invoice
    private JCheckBox creditNote
    private JComboBox<Contact> comboBox
    private Contacts contacts
    private Articles articles
    private Contact contact
    private Predicate<Contact> filter
    private final SalesOrderCreateDataTableModel salesOrderCreateDataTableModel

    SalesOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts()
        this.articles = accounting.getArticles()
        noInvoice=accounting.getContactNoInvoice()
        salesOrder = new SalesOrder()
        salesOrder.setArticles(articles)

        TotalsPanel saleTotalsPanel = new TotalsPanel()
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(articles, salesOrder, saleTotalsPanel)
        SelectableTable<OrderItem> table = new SelectableTable<>(salesOrderCreateDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        creditNote = new JCheckBox("CreditNote")
        creditNote.setSelected(false)
        creditNote.addActionListener({ e ->
            salesOrder.setCreditNote(creditNote.isSelected())
        })

        invoice = new JCheckBox("Invoice")
        invoice.addActionListener({ e ->
            comboBox.setEnabled(invoice.isSelected())
            salesOrder.setInvoice(invoice.isSelected())
            ComboBoxModel<Contact> model = comboBox.getModel()
            if (invoice.isSelected()) {
                model.setSelectedItem(contact)
            } else {
                if (noInvoice == null) {
                    ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, Contact.ContactType.CUSTOMERS)
                    contactSelectorDialog.setLocation(getLocationOnScreen())
                    contactSelectorDialog.setVisible(true)
                    noInvoice = contactSelectorDialog.getSelection()
                    accounting.setContactNoInvoice(noInvoice)
                }
                model.setSelectedItem(noInvoice)
            }
        })
        //
        comboBox = new JComboBox<>()
        comboBox.addActionListener({ e ->
            if (invoice.isSelected()) {
                contact = (Contact) comboBox.getSelectedItem()
            }
        })
        JPanel north = new JPanel()
        north.add(invoice)
        north.add(comboBox)
        north.add(creditNote)

        filter = Contact.&isCustomer
        fireCustomerAddedOrRemoved()
        comboBox.setSelectedItem(noInvoice)
        comboBox.setEnabled(false)

        JButton orderButton = new JButton("Add Sales Order")
        orderButton.addActionListener({ e ->
            SalesOrders salesOrders = accounting.getSalesOrders()
            if (invoice.isSelected()) {
                salesOrder.setCustomer(contact)
            } else {
                salesOrder.setCustomer(noInvoice)
            }
            try {
                salesOrder.removeEmptyOrderItems()
                SalesOrder existing = salesOrders.getBusinessObject(salesOrder.getName())
                if (existing == null) {
                    salesOrders.addBusinessObject(salesOrder)
                }
                salesOrder = new SalesOrder()
                salesOrder.setArticles(articles)
                salesOrderCreateDataTableModel.setOrder(salesOrder)
                saleTotalsPanel.fireOrderContentChanged(salesOrder)
                SalesOrdersOverviewGUI.fireSalesOrderAddedOrRemovedForAccounting(accounting)
            } catch (EmptyNameException e1) {
                e1.printStackTrace()
            } catch (DuplicateNameException e1) {
                e1.printStackTrace()
            }
        })
        JPanel south = new JPanel(new BorderLayout())
        south.add(orderButton, BorderLayout.SOUTH)
        south.add(saleTotalsPanel, BorderLayout.CENTER)

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())

        add(scrollPane, BorderLayout.CENTER)
        add(north, BorderLayout.NORTH)
        add(south, BorderLayout.SOUTH)
    }

    void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder
        comboBox.setEnabled(salesOrder!=null)
        invoice.setEnabled(salesOrder!=null)
        creditNote.setEnabled(salesOrder != null)
        if (salesOrder!=null){
            invoice.setSelected(salesOrder.isInvoice())
            if(salesOrder.isInvoice()) {
                contact = salesOrder.getCustomer()
                comboBox.setSelectedItem(contact)
            } else {
                noInvoice = salesOrder.getCustomer()
                comboBox.setSelectedItem(noInvoice)
            }
            creditNote.setSelected(salesOrder.isCreditNote())
        } else {
            contact = null
            noInvoice = null
        }
        salesOrderCreateDataTableModel.setOrder(salesOrder)
        salesOrderCreateDataTableModel.fireTableDataChanged()
    }

    void fireCustomerAddedOrRemoved() {
        comboBox.removeAllItems()
        contacts.getBusinessObjects(filter).forEach({ contact -> comboBox.addItem(contact) })
//        salesOrderDataTableModel.fireTableDataChanged()
    }
}
