package be.dafke.Accounting.BasicAccounting.Trade


import be.dafke.Accounting.BasicAccounting.Contacts.ContactDetailsPanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.DateAndDescriptionDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.PDFGeneration.PDFViewerFrame
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Utils.Utils

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

import static java.util.ResourceBundle.getBundle

class PurchaseOrdersDetailPanel extends JPanel {
    JButton placeOrderButton, deliveredButton, payedButton, viewInvoiceButton, selectInvoiceButton
    final JButton createPurchaseOrder
    JCheckBox payed, delivered, placed
    PurchaseOrder purchaseOrder
    ContactDetailsPanel contactDetailsPanel
    JTextField invoiceNr
    JFileChooser fileChooser = new JFileChooser()

    PurchaseOrdersDetailPanel() {

        createPurchaseOrder = new JButton(getBundle("Accounting").getString("CREATE_PO"))
        createPurchaseOrder.addActionListener({ e ->
            PurchaseOrderCreateGUI purchaseOrderCreateGUI = PurchaseOrderCreateGUI.showPurchaseOrderGUI(Session.activeAccounting)
            purchaseOrderCreateGUI.setLocation(getLocationOnScreen())
            purchaseOrderCreateGUI.visible = true
        })

        JPanel orderPanel = createOrderPanel()
        JPanel customerPanel = createCustomerPanel()

        disableButtons()

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

        viewInvoiceButton = new JButton("View Invoice")
        viewInvoiceButton.addActionListener( { e -> viewInvoice()})
        viewInvoiceButton.enabled = false

        selectInvoiceButton = new JButton("Select Invoice")
        selectInvoiceButton.addActionListener({ e -> selectInvoice() })

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        JPanel invoiceLine = new JPanel()
        JPanel orderLine = new JPanel()

        invoiceNr = new JTextField(10)
        invoiceNr.enabled = false
        invoiceLine.add(new JLabel("Invoice:"))
        invoiceLine.add(invoiceNr)
        invoiceLine.add(viewInvoiceButton)
        invoiceLine.add(selectInvoiceButton)

        orderLine.add(placeOrderButton)
        orderLine.add(deliveredButton)
        orderLine.add(payedButton)

        panel.add(invoiceLine)
        panel.add(orderLine)

        panel
    }

    void disableButtons(){
        placeOrderButton.enabled = false
        deliveredButton.enabled = false
        payedButton.enabled = false
        viewInvoiceButton.enabled = false
        selectInvoiceButton.enabled = false
        createPurchaseOrder.enabled = false
    }

    void setOrder(PurchaseOrder purchaseOrder){
        this.purchaseOrder = purchaseOrder
        updateButtonsAndCheckBoxes()
    }

    void payOrder(){
        updateButtonsAndCheckBoxes()
    }

    void placeOrder(){
        Transaction transaction = Main.createPurchaseOrder(purchaseOrder)

        // Either book directly or display only,
        // if display only the bookPurchase order method should be call when clicking the Book Transaction button
        Accounting accounting = Session.activeAccounting
        Journal journal = StockUtils.getPurchaseJournal accounting
        Main.bookPurchaseOrder(purchaseOrder, transaction, journal)
//        Main.switchView(JournalType.PURCHASE_TYPE)
        Main.setJournal(journal)
        Main.selectTransaction(transaction)
//        Main.displayTransaction(transaction)

//        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    void viewInvoice(){
        if(purchaseOrder.invoicePath) {
            PDFViewerFrame viewerFrame = PDFViewerFrame.showInvoice(purchaseOrder.invoicePath, purchaseOrder.name)
            viewerFrame.visible = true
        }
    }

    void selectInvoice(){
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile()
            fileChooser.setCurrentDirectory(file.parentFile)
            purchaseOrder.invoicePath = file.getPath()
        }
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

        StockTransactions stockTransactions = Session.activeAccounting.stockTransactions
        stockTransactions.addOrder purchaseOrder

//        StockGUI.fireStockContentChanged()
//        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    void updateButtonsAndCheckBoxes() {
        StockTransactions stockTransactions = Session.activeAccounting.stockTransactions
        ArrayList<Order> orders = stockTransactions.getOrders()
        Transaction purchaseTransaction = purchaseOrder?.purchaseTransaction
        payed.setSelected(purchaseOrder && purchaseOrder.paymentTransaction)
        placed.setSelected(purchaseOrder && purchaseOrder.purchaseTransaction)
        delivered.setSelected(purchaseOrder && orders.contains(purchaseOrder))
        deliveredButton.enabled = purchaseOrder && !orders.contains(purchaseOrder)
        placeOrderButton.enabled = purchaseTransaction==null
        payedButton.enabled = purchaseOrder && purchaseOrder.purchaseTransaction==null
        viewInvoiceButton.enabled = purchaseOrder?.invoicePath != null
        selectInvoiceButton.enabled = purchaseOrder != null
        if(purchaseOrder?.invoicePath != null) {
            invoiceNr.setText purchaseOrder.invoicePath
        } else {
            invoiceNr.setText ''
        }
        if(purchaseOrder&&purchaseOrder.supplier){
            contactDetailsPanel.setContact(purchaseOrder.supplier)
        } else {
            contactDetailsPanel.clearFields()
        }
    }

//    void createPurchaseTransaction() {
//        Accounting accounting = Session.activeAccounting
//
//        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
//        dateAndDescriptionDialog.enableDescription(true)
//        dateAndDescriptionDialog.description = purchaseOrder.name
//        dateAndDescriptionDialog.visible = true
//
//        Calendar date = dateAndDescriptionDialog.date
//        String description = dateAndDescriptionDialog.description
//
//        Transaction transaction = new Transaction(date, description)
//
//        Contact supplier = purchaseOrder.supplier
//        if(supplier == null){
//            // TODO
//        }
//        Account supplierAccount = StockUtils.getSupplierAccount supplier, accounting
//
//        boolean creditNote = purchaseOrder.creditNote
//
//
//        List<OrderItem> goodItems = purchaseOrder.getBusinessObjects(OrderItem.isGood())
//        List<OrderItem> serviceItems = purchaseOrder.getBusinessObjects(OrderItem.isService())
//
//        BigDecimal goodsAmount = BigDecimal.ZERO
//
//        if(!goodItems.empty) {
//            goodItems.forEach { goodItem ->
//                goodsAmount = goodsAmount.add(goodItem.purchasePriceWithoutVat)
//            }
//            goodsAmount = goodsAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN)
//        }
//
//        if(goodsAmount.compareTo(BigDecimal.ZERO)>0) {
//            PurchaseType purchaseType = PurchaseType.VAT_81
//            Account stockAccount = StockUtils.getStockAccount accounting
//            Booking purchaseBooking = new Booking(stockAccount, goodsAmount, !creditNote)
//            BigDecimal vatAmount = purchaseOrder.getTotalPurchaseVat(OrderItem.isGood()).setScale(2, RoundingMode.HALF_UP)
//            Booking bookingVat
//            if (!creditNote) {
//                // Cost excl VAT
//                //
//                AccountActions.addPurchaseVatTransaction purchaseBooking, purchaseType
//                // VAT
//                //
//                bookingVat = AccountActions.createPurchaseVatBooking accounting, vatAmount
//            } else {
//                // CN excl VAT
//                //
//                AccountActions.addPurchaseCnVatTransaction purchaseBooking, purchaseType
//                // VAT
//                //
//                bookingVat = AccountActions.createPurchaseCnVatBooking accounting, vatAmount
//            }
//            transaction.addBusinessObject purchaseBooking
//            transaction.addBusinessObject bookingVat
//        }
//
//        if(!serviceItems.empty) {
//            PurchaseType purchaseType = PurchaseType.VAT_82
//            serviceItems.forEach { serviceOrderItem ->
//                BigDecimal purchasePriceWithoutVat = serviceOrderItem.getPurchasePriceWithoutVat().setScale(2, RoundingMode.HALF_UP)
//                Service service = (Service) serviceOrderItem.article
//
//                Account costAccount = getCostAccount(service) // FIXME: will be 'null' initially
//                Booking costBooking = new Booking(costAccount, purchasePriceWithoutVat, !creditNote)
//                BigDecimal vatAmount = serviceOrderItem.getPurchaseVatAmount().setScale(2, RoundingMode.HALF_UP)
//                Booking bookingVat
//                if (!creditNote) {
//                    // Cost excl VAT
//                    //
//                    AccountActions.addPurchaseVatTransaction costBooking, purchaseType
//                    // VAT
//                    //
//                    bookingVat = AccountActions.createPurchaseVatBooking accounting, vatAmount
//
//                } else {
//                    // CN excl VAT
//                    //
//                    AccountActions.addPurchaseCnVatTransaction costBooking, purchaseType
//                    // VAT
//                    //
//                    bookingVat = AccountActions.createPurchaseCnVatBooking accounting, vatAmount
//                }
//                transaction.addBusinessObject costBooking
//                transaction.addBusinessObject bookingVat
//            }
//        }
//
//        // Supplier booking is common
//        //
//        BigDecimal supplierAmount = purchaseOrder.totalPurchasePriceInclVat
//        Booking supplierBooking = new Booking(supplierAccount, supplierAmount, creditNote)
//        // (no VAT Booking for Supplier)
//        transaction.addBusinessObject(supplierBooking)
//
//        Journal journal = StockUtils.getPurchaseJournal accounting
//        transaction.journal = journal
//        // TODO: ask for Date and Description
//
//        purchaseOrder.purchaseTransaction = transaction
//
//        Transactions transactions = accounting.transactions
//        transactions.setId(transaction)
//        transactions.addBusinessObject transaction
//        journal.addBusinessObject transaction
////        Main.journal = journal
//    }
//
//    static Account getCostAccount(Service service){
//        Account result = service.costAccount
//        if(result==null) {
//            AccountType accountType = Session.activeAccounting.accountTypes.getBusinessObject(AccountTypes.COST)
//            ArrayList<AccountType> list = new ArrayList<>()
//            list.add accountType
//            AccountSelectorDialog dialog = new AccountSelectorDialog(Session.activeAccounting.accounts, list, "Select Cost account for Service: ${service.name}")
//            dialog.visible = true
//            result = dialog.getSelection()
//            service.costAccount = result
//        }
//        result
//    }
}
