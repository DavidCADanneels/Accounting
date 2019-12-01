package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Contacts.ContactDetailsPanel
import be.dafke.Accounting.BasicAccounting.Journals.DateAndDescriptionDialog
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
    private JButton placeOrderButton, deliveredButton, payedButton
    private final JButton createPurchaseOrder
    private JCheckBox payed, delivered, placed
    private PurchaseOrder purchaseOrder
    private Accounting accounting
    private ContactDetailsPanel contactDetailsPanel


    PurchaseOrdersDetailPanel() {

        createPurchaseOrder = new JButton(getBundle("Accounting").getString("CREATE_PO"))
        createPurchaseOrder.addActionListener({ e ->
            PurchaseOrderCreateGUI purchaseOrderCreateGUI = PurchaseOrderCreateGUI.showPurchaseOrderGUI(accounting)
            purchaseOrderCreateGUI.setLocation(getLocationOnScreen())
            purchaseOrderCreateGUI.setVisible(true)
        })

        JPanel orderPanel = createOrderPanel()
        JPanel customerPanel = createCustomerPanel()

        setLayout(new BorderLayout())
        add(orderPanel, BorderLayout.NORTH)
        add(customerPanel,BorderLayout.CENTER)
        add(createPurchaseOrder, BorderLayout.SOUTH)
    }

    private JPanel createOrderPanel(){
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Order"))

        JPanel statusPanel = createStatusPanel()
        JPanel buttonPanel = createButtonPanel()

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        panel.add(statusPanel)
        panel.add(buttonPanel)
        panel
    }

    private JPanel createCustomerPanel(){
        JPanel panel = new JPanel()
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Supplier"))

        panel.setLayout(new BorderLayout())
        contactDetailsPanel = new ContactDetailsPanel()
        contactDetailsPanel.setEnabled(false)

        panel.add(contactDetailsPanel, BorderLayout.NORTH)

        panel
    }

    private JPanel createStatusPanel(){
        JPanel statusPanel = new JPanel()

        placed = new JCheckBox("Ordered")
        payed = new JCheckBox("Payed")
        delivered = new JCheckBox("Delived")
        placed.setEnabled(false)
        payed.setEnabled(false)
        delivered.setEnabled(false)

        statusPanel.add(placed)
        statusPanel.add(delivered)
        statusPanel.add(payed)

        statusPanel
    }

    private JPanel createButtonPanel(){
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

    private void payOrder(){
        updateButtonsAndCheckBoxes()
    }

    private void placeOrder(){
        createPurchaseTransaction()

        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    private void deliverOrder(){
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
        Contact supplier = purchaseOrder.getSupplier()
        dateAndDescriptionDialog.setDescription(supplier.getName())
        dateAndDescriptionDialog.enableDescription(false)
        dateAndDescriptionDialog.setVisible(true)

        Calendar date = dateAndDescriptionDialog.getDate()
        String description = dateAndDescriptionDialog.getDescription()

        purchaseOrder.setDeliveryDate(Utils.toString(date))
        purchaseOrder.setDescription(description)

        StockTransactions stockTransactions = accounting.getStockTransactions()
        stockTransactions.addOrder(purchaseOrder)

        StockGUI.fireStockContentChanged()
        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    private void updateButtonsAndCheckBoxes() {
        StockTransactions stockTransactions = accounting.getStockTransactions()
        ArrayList<Order> orders = stockTransactions.getOrders()
        Transaction purchaseTransaction = purchaseOrder==null?null:purchaseOrder.getPurchaseTransaction()
        payed.setSelected(purchaseOrder !=null&& purchaseOrder.getPurchaseTransaction()!=null)
        placed.setSelected(purchaseTransaction!=null)
        delivered.setSelected(purchaseOrder !=null&& orders.contains(purchaseOrder))
        deliveredButton.setEnabled(purchaseOrder !=null&&!orders.contains(purchaseOrder))
        placeOrderButton.setEnabled(purchaseTransaction==null)
        payedButton.setEnabled(purchaseOrder !=null&&purchaseOrder.getPurchaseTransaction()==null)
        if(purchaseOrder!=null&&purchaseOrder.getSupplier()!=null){
            contactDetailsPanel.setContact(purchaseOrder.getSupplier())
        } else {
            contactDetailsPanel.clearFields()
        }
    }

    private void createPurchaseTransaction() {
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
        dateAndDescriptionDialog.enableDescription(true)
        dateAndDescriptionDialog.setDescription(purchaseOrder.getName())
        dateAndDescriptionDialog.setVisible(true)

        Calendar date = dateAndDescriptionDialog.getDate()
        String description = dateAndDescriptionDialog.getDescription()

        Transaction transaction = new Transaction(date, description)
        Account stockAccount = StockUtils.getStockAccount(accounting)

        Contact supplier = purchaseOrder.getSupplier()
        if(supplier == null){
            // TODO
        }
        Account supplierAccount = StockUtils.getSupplierAccount(supplier, accounting)

        boolean creditNote = purchaseOrder.isCreditNote()

        BigDecimal stockAmount = purchaseOrder.getTotalPurchasePriceExclVat()
        Booking stockBooking = new Booking(stockAccount, stockAmount, !creditNote)

        PurchaseType purchaseType = PurchaseType.VAT_81
        if(!creditNote){
            AccountActions.addPurchaseVatTransaction(stockBooking, purchaseType)
        } else {
            AccountActions.addPurchaseCnVatTransaction(stockBooking, purchaseType)
        }
        transaction.addBusinessObject(stockBooking)

        BigDecimal supplierAmount = purchaseOrder.getTotalPurchasePriceInclVat()
        Booking supplierBooking = new Booking(supplierAccount, supplierAmount, creditNote)
        // (no VAT Booking for Supplier)
        transaction.addBusinessObject(supplierBooking)

        BigDecimal vatAmount = purchaseOrder.getTotalPurchaseVat()
        if(vatAmount.compareTo(BigDecimal.ZERO) != 0) {
            if(!creditNote) {
                Booking bookingVat = AccountActions.createPurchaseVatBooking(accounting, vatAmount)
                transaction.addBusinessObject(bookingVat)
            } else {
                Booking bookingVat = AccountActions.createPurchaseCnVatBooking(accounting, vatAmount)
                transaction.addBusinessObject(bookingVat)
            }
        }

        Journal journal = StockUtils.getPurchaseJournal(accounting)
        transaction.setJournal(journal)
        // TODO: ask for Date and Description

        purchaseOrder.setPurchaseTransaction(transaction)

        Transactions transactions = accounting.getTransactions()
        transactions.setId(transaction)
        transactions.addBusinessObject(transaction)
        journal.addBusinessObject(transaction)
        Main.setJournal(journal)
        Main.selectTransaction(transaction)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        contactDetailsPanel.setAccounting(accounting)
    }

}
