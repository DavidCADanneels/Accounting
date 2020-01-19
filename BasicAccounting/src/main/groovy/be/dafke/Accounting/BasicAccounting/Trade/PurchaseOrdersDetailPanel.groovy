package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Contacts.ContactDetailsPanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.DateAndDescriptionDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Order
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.Accounting.BusinessModel.PurchaseType
import be.dafke.Accounting.BusinessModel.StockTransactions
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.Accounting.BusinessModel.Transactions
import be.dafke.Utils.Utils

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.BorderLayout
import java.awt.Color

import static java.util.ResourceBundle.getBundle

class PurchaseOrdersDetailPanel extends JPanel {
    JButton placeOrderButton, deliveredButton, payedButton
    final JButton createPurchaseOrder
    JCheckBox payed, delivered, placed
    PurchaseOrder purchaseOrder
    Accounting accounting
    ContactDetailsPanel contactDetailsPanel


    PurchaseOrdersDetailPanel() {

        createPurchaseOrder = new JButton(getBundle("Accounting").getString("CREATE_PO"))
        createPurchaseOrder.addActionListener({ e ->
            PurchaseOrderCreateGUI purchaseOrderCreateGUI = PurchaseOrderCreateGUI.showPurchaseOrderGUI(accounting)
            purchaseOrderCreateGUI.setLocation(getLocationOnScreen())
            purchaseOrderCreateGUI.visible = true
        })

        JPanel orderPanel = createOrderPanel()
        JPanel customerPanel = createCustomerPanel()

        setLayout(new BorderLayout())
        add(orderPanel, BorderLayout.NORTH)
        add(customerPanel,BorderLayout.CENTER)
        add(createPurchaseOrder, BorderLayout.SOUTH)
    }

    JPanel createOrderPanel(){
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Order"))

        JPanel statusPanel = createStatusPanel()
        JPanel buttonPanel = createButtonPanel()

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        panel.add(statusPanel)
        panel.add(buttonPanel)
        panel
    }

    JPanel createCustomerPanel(){
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Supplier"))

        panel.setLayout(new BorderLayout())
        contactDetailsPanel = new ContactDetailsPanel()
        contactDetailsPanel.enabled = false

        panel.add(contactDetailsPanel, BorderLayout.NORTH)

        panel
    }

    JPanel createStatusPanel(){
        JPanel statusPanel = new JPanel()

        placed = new JCheckBox("Ordered")
        payed = new JCheckBox("Payed")
        delivered = new JCheckBox("Delived")
        placed.enabled = false
        payed.enabled = false
        delivered.enabled = false

        statusPanel.add(placed)
        statusPanel.add(delivered)
        statusPanel.add(payed)

        statusPanel
    }

    JPanel createButtonPanel(){
        JPanel panel = new JPanel()

        placeOrderButton = new JButton("Place Order")
        placeOrderButton.addActionListener({ e -> placeOrder() })

        deliveredButton = new JButton("Order Delivered")
        deliveredButton.addActionListener({ e -> deliverOrder() })

        payedButton = new JButton("Pay Order")
        payedButton.addActionListener({ e -> payOrder() })

        panel.add(placeOrderButton)
        panel.add(deliveredButton)
        panel.add(payedButton)

        panel
    }

    void setOrder(PurchaseOrder purchaseOrder){
        this.purchaseOrder = purchaseOrder
        updateButtonsAndCheckBoxes()
    }

    void payOrder(){
        updateButtonsAndCheckBoxes()
    }

    void placeOrder(){
        createPurchaseTransaction()

        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    void deliverOrder(){
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
        Contact supplier = purchaseOrder.supplier
        dateAndDescriptionDialog.description = supplier.name
        dateAndDescriptionDialog.enableDescription false
        dateAndDescriptionDialog.visible = true

        Calendar date = dateAndDescriptionDialog.date
        String description = dateAndDescriptionDialog.description

        purchaseOrder.deliveryDate = Utils.toString date
        purchaseOrder.deliveryDescription = description

        StockTransactions stockTransactions = accounting.stockTransactions
        stockTransactions.addOrder purchaseOrder

        StockGUI.fireStockContentChanged()
        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    void updateButtonsAndCheckBoxes() {
        StockTransactions stockTransactions = accounting.stockTransactions
        ArrayList<Order> orders = stockTransactions.getOrders()
        Transaction purchaseTransaction = purchaseOrder==null?null:purchaseOrder.purchaseTransaction
        payed.setSelected(purchaseOrder && purchaseOrder.purchaseTransaction)
        placed.setSelected(purchaseTransaction)
        delivered.setSelected(purchaseOrder && orders.contains(purchaseOrder))
        deliveredButton.enabled = purchaseOrder && !orders.contains(purchaseOrder)
        placeOrderButton.enabled = purchaseTransaction==null
        payedButton.enabled = purchaseOrder && purchaseOrder.purchaseTransaction==null
        if(purchaseOrder&&purchaseOrder.supplier){
            contactDetailsPanel.setContact(purchaseOrder.supplier)
        } else {
            contactDetailsPanel.clearFields()
        }
    }

    void createPurchaseTransaction() {
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
        dateAndDescriptionDialog.enableDescription(true)
        dateAndDescriptionDialog.description = purchaseOrder.name
        dateAndDescriptionDialog.visible = true

        Calendar date = dateAndDescriptionDialog.date
        String description = dateAndDescriptionDialog.description

        Transaction transaction = new Transaction(date, description)
        Account stockAccount = StockUtils.getStockAccount accounting

        Contact supplier = purchaseOrder.supplier
        if(supplier == null){
            // TODO
        }
        Account supplierAccount = StockUtils.getSupplierAccount supplier, accounting

        boolean creditNote = purchaseOrder.creditNote

        BigDecimal stockAmount = purchaseOrder.totalPurchasePriceExclVat
        Booking stockBooking = new Booking(stockAccount, stockAmount, !creditNote)

        PurchaseType purchaseType = PurchaseType.VAT_81
        if(!creditNote){
            AccountActions.addPurchaseVatTransaction stockBooking, purchaseType
        } else {
            AccountActions.addPurchaseCnVatTransaction stockBooking, purchaseType
        }
        transaction.addBusinessObject stockBooking

        BigDecimal supplierAmount = purchaseOrder.totalPurchasePriceInclVat
        Booking supplierBooking = new Booking(supplierAccount, supplierAmount, creditNote)
        // (no VAT Booking for Supplier)
        transaction.addBusinessObject(supplierBooking)

        BigDecimal vatAmount = purchaseOrder.totalPurchaseVat
        if(vatAmount.compareTo(BigDecimal.ZERO) != 0) {
            if(!creditNote) {
                Booking bookingVat = AccountActions.createPurchaseVatBooking accounting, vatAmount
                transaction.addBusinessObject bookingVat
            } else {
                Booking bookingVat = AccountActions.createPurchaseCnVatBooking accounting, vatAmount
                transaction.addBusinessObject bookingVat
            }
        }

        Journal journal = StockUtils.getPurchaseJournal accounting
        transaction.journal = journal
        // TODO: ask for Date and Description

        purchaseOrder.purchaseTransaction = transaction

        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        journal.addBusinessObject transaction
        Main.journal = journal
        Main.selectTransaction transaction
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        contactDetailsPanel.accounting = accounting
    }

}
