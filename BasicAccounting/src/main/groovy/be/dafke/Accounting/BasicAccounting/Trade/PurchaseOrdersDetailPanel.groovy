package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Accounts.Selector.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.Contacts.ContactDetailsPanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.DateAndDescriptionDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Utils.Utils

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.math.RoundingMode
import java.util.List

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
        Transaction purchaseTransaction = purchaseOrder?.purchaseTransaction
        payed.setSelected(purchaseOrder && purchaseOrder.paymentTransaction)
        placed.setSelected(purchaseOrder && purchaseOrder.purchaseTransaction)
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

        Contact supplier = purchaseOrder.supplier
        if(supplier == null){
            // TODO
        }
        Account supplierAccount = StockUtils.getSupplierAccount supplier, accounting

        boolean creditNote = purchaseOrder.creditNote


        List<OrderItem> goodItems = purchaseOrder.getBusinessObjects(OrderItem.isGood())
        List<OrderItem> serviceItems = purchaseOrder.getBusinessObjects(OrderItem.isService())

        BigDecimal goodsAmount = BigDecimal.ZERO

        if(!goodItems.empty) {
            goodItems.forEach { goodItem ->
                goodsAmount = goodsAmount.add(goodItem.purchasePriceWithoutVat)
            }
            goodsAmount = goodsAmount.setScale(2)
        }

        if(goodsAmount.compareTo(BigDecimal.ZERO)>0) {
            PurchaseType purchaseType = PurchaseType.VAT_81
            Account stockAccount = StockUtils.getStockAccount accounting
            Booking purchaseBooking = new Booking(stockAccount, goodsAmount, !creditNote)
            BigDecimal vatAmount = purchaseOrder.getTotalPurchaseVat(OrderItem.isGood()).setScale(2, RoundingMode.HALF_UP)
            Booking bookingVat
            if (!creditNote) {
                // Cost excl VAT
                //
                AccountActions.addPurchaseVatTransaction purchaseBooking, purchaseType
                // VAT
                //
                bookingVat = AccountActions.createPurchaseVatBooking accounting, vatAmount
            } else {
                // CN excl VAT
                //
                AccountActions.addPurchaseCnVatTransaction purchaseBooking, purchaseType
                // VAT
                //
                bookingVat = AccountActions.createPurchaseCnVatBooking accounting, vatAmount
            }
            transaction.addBusinessObject purchaseBooking
            transaction.addBusinessObject bookingVat
        }

        if(!serviceItems.empty) {
            PurchaseType purchaseType = PurchaseType.VAT_82
            serviceItems.forEach { serviceOrderItem ->
                BigDecimal purchasePriceWithoutVat = serviceOrderItem.getPurchasePriceWithoutVat().setScale(2, RoundingMode.HALF_UP)
                Service service = (Service) serviceOrderItem.article

                Account costAccount = getCostAccount(service) // FIXME: will be 'null' initially
                Booking costBooking = new Booking(costAccount, purchasePriceWithoutVat, !creditNote)
                BigDecimal vatAmount = serviceOrderItem.getPurchaseVatAmount().setScale(2, RoundingMode.HALF_UP)
                Booking bookingVat
                if (!creditNote) {
                    // Cost excl VAT
                    //
                    AccountActions.addPurchaseVatTransaction costBooking, purchaseType
                    // VAT
                    //
                    bookingVat = AccountActions.createPurchaseVatBooking accounting, vatAmount

                } else {
                    // CN excl VAT
                    //
                    AccountActions.addPurchaseCnVatTransaction costBooking, purchaseType
                    // VAT
                    //
                    bookingVat = AccountActions.createPurchaseCnVatBooking accounting, vatAmount
                }
                transaction.addBusinessObject costBooking
                transaction.addBusinessObject bookingVat
            }
        }

        // Supplier booking is common
        //
        BigDecimal supplierAmount = purchaseOrder.totalPurchasePriceInclVat
        Booking supplierBooking = new Booking(supplierAccount, supplierAmount, creditNote)
        // (no VAT Booking for Supplier)
        transaction.addBusinessObject(supplierBooking)

        Journal journal = StockUtils.getPurchaseJournal accounting
        transaction.journal = journal
        // TODO: ask for Date and Description

        purchaseOrder.purchaseTransaction = transaction

        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        journal.addBusinessObject transaction
        Main.journal = journal
    }

    Account getCostAccount(Service service){
        Account result = service.costAccount
        if(result==null) {
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.COST)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add accountType
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Cost account for Service: ${service.name}")
            dialog.visible = true
            result = dialog.getSelection()
            service.costAccount = result
        }
        result
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        contactDetailsPanel.accounting = accounting
    }

}
