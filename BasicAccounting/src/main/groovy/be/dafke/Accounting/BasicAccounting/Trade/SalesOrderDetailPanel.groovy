package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Contacts.ContactDetailsPanel
import be.dafke.Accounting.BasicAccounting.Contacts.ContactSelectorDialog
import be.dafke.Accounting.BasicAccounting.Contacts.ContactsPanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.DateAndDescriptionDialog
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.PDFGeneration.InvoicePDF
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.SalesOrderIO
import be.dafke.Utils.Utils

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.util.List

import static be.dafke.Utils.Utils.parseInt
import static java.util.ResourceBundle.getBundle

class SalesOrderDetailPanel extends JPanel {
    JButton placeOrderButton, deliveredButton, createInvoiceButton
    JButton salesTransactionButton, gainTransactionButton, paymentTransactionButton
    JButton createSalesOrder
    JButton editSalesOrder
    JTextField invoiceNr
    JCheckBox payed, delivered, placed, creditNote, promoOrder
    SalesOrder salesOrder
    Accounting accounting
    ContactDetailsPanel contactDetailsPanel

    SalesOrderDetailPanel() {
        createSalesOrder = new JButton(getBundle("Accounting").getString("CREATE_SO"))
        createSalesOrder.addActionListener({ e ->
            SalesOrderCreateGUI salesOrderCreateGUI = SalesOrderCreateGUI.showSalesOrderGUI(accounting)
            salesOrderCreateGUI.setLocation(getLocationOnScreen())
            salesOrderCreateGUI.visible = true
        })

        editSalesOrder = new JButton(getBundle("Accounting").getString("EDIT_ORDER"))
        editSalesOrder.addActionListener({ e ->
            SalesOrderCreateGUI salesOrderCreateGUI = SalesOrderCreateGUI.showSalesOrderEditGUI(accounting)
            salesOrderCreateGUI.setSalesOrder(salesOrder)
            salesOrderCreateGUI.setLocation(getLocationOnScreen())
            salesOrderCreateGUI.visible = true
        })

        JPanel orderPanel = createOrderPanel()
        JPanel customerPanel = createCustomerPanel()

        disableButtons()

        setLayout(new BorderLayout())
        add(orderPanel, BorderLayout.NORTH)
        add(customerPanel,BorderLayout.CENTER)
        add(createSalesOrder, BorderLayout.SOUTH)
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
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Customer"))

        panel.setLayout(new BorderLayout())
        contactDetailsPanel = new ContactDetailsPanel()
        contactDetailsPanel.enabled = false

        panel.add(contactDetailsPanel, BorderLayout.NORTH)

        panel
    }

    JPanel createStatusPanel(){
        placed = new JCheckBox("Ordered")
        payed = new JCheckBox("Payed")
        delivered = new JCheckBox("Delived")
        creditNote = new JCheckBox("CN")
        promoOrder = new JCheckBox("Promo")
        placed.enabled = false
        payed.enabled = false
        delivered.enabled = false
        creditNote.enabled = false
        promoOrder.enabled = false

        JPanel panel = new JPanel()
        panel.add(placed)
        panel.add(delivered)
        panel.add(payed)
        panel.add(creditNote)
        panel.add(promoOrder)
        panel
    }

    void disableButtons(){
        editSalesOrder.enabled = false
        placeOrderButton.enabled = false
        deliveredButton.enabled = false
        createInvoiceButton.enabled = false
        salesTransactionButton.enabled = false
        gainTransactionButton.enabled = false
        paymentTransactionButton.enabled = false
    }

    JPanel createButtonPanel(){
        createInvoiceButton = new JButton("Create Invoice")
        createInvoiceButton.enabled = false
        createInvoiceButton.addActionListener({ e -> createInvoice() })

        placeOrderButton = new JButton("Place Order")
        placeOrderButton.addActionListener({ e -> placeOrder() })

        deliveredButton = new JButton("Order Delivered")
        deliveredButton.addActionListener({ e -> deliverOrder() })

        salesTransactionButton = new JButton("SalesTransaction")
        salesTransactionButton.addActionListener({ e -> selectSalesTransaction() })

        gainTransactionButton = new JButton("GainTransaction")
        gainTransactionButton.addActionListener({ e -> selectGainTransaction() })

        paymentTransactionButton = new JButton("PaymentTransaction")
        paymentTransactionButton.addActionListener({ e -> selectPaymentTransaction() })

        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        JPanel line1 = new JPanel()
        JPanel line2 = new JPanel()
        JPanel line3 = new JPanel()

        invoiceNr = new JTextField(10)
        invoiceNr.enabled = false
        line1.add(new JLabel("Invoice:"))
        line1.add(invoiceNr)
        line1.add(createInvoiceButton)

        line2.add(editSalesOrder)
        line2.add(placeOrderButton)
        line2.add(deliveredButton)

        line3.add(salesTransactionButton)
        line3.add(gainTransactionButton)
        line3.add(paymentTransactionButton)

        panel.add(line1)
        panel.add(line2)
        panel.add(line3)
        panel
    }

    void selectPaymentTransaction() {
        Contact customer = salesOrder.customer
        if("Daginkomsten".equals(customer.name)) {
            salesOrder.paymentTransaction = askId("CASH")
        } else {
            salesOrder.paymentTransaction = askId("BE")
        }
        updateButtonsAndCheckBoxes()
    }

    void selectSalesTransaction() {
        Contact customer = salesOrder.customer
        if("Daginkomsten".equals(customer.name)) {
            salesOrder.salesTransaction = askId("DAG")
        } else {
            salesOrder.salesTransaction = askId("VB")
        }
        updateButtonsAndCheckBoxes()
    }

    void selectGainTransaction() {
        salesOrder.gainTransaction = askId("GAIN")
        updateButtonsAndCheckBoxes()
    }

    Transaction askId(String abbr) {
        Journals journals = accounting.journals
        Journal journal = journals.getJournal(abbr)

        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
        journalSelectorDialog.setSelectedJournal(journal)
        journalSelectorDialog.visible = true
        journal = journalSelectorDialog.getSelection()

        String idString = JOptionPane.showInputDialog(this, "Enter id for " + journal.name + ":")
        int id = parseInt(idString)
        ArrayList<Transaction> businessObjects = journal.businessObjects
        Transaction transaction = businessObjects.get(id - 1)
        transaction
    }

    void deliverOrder() {
        Calendar date
        Contact customer = salesOrder.customer
        String description = customer.name
        String deliveryDate = salesOrder.deliveryDate // FIXME: Calendar iso String
        if(deliveryDate==null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
            dateAndDescriptionDialog.description = description
            dateAndDescriptionDialog.date = Calendar.getInstance()
            dateAndDescriptionDialog.visible = true

            date = dateAndDescriptionDialog.date
//            description = dateAndDescriptionDialog.description

            salesOrder.deliveryDate = Utils.toString(date)
            salesOrder.description = description
        }
        StockTransactions stockTransactions = accounting.stockTransactions
        stockTransactions.addOrder(salesOrder)

        StockGUI.fireStockContentChanged()
        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    void placeOrder() {
        if(salesOrder.promoOrder){
            createPromoTransaction()
        } else {
            Transaction salesTransaction = createTransaction(null, salesOrder.name)
            createSalesTransaction(salesTransaction)
            createGainTransaction(salesTransaction.date)
        }
        StockHistoryGUI.fireStockContentChanged()
        updateButtonsAndCheckBoxes()
    }

    void createInvoice(){
        if (salesOrder.customer == null) {
            // should not occur
        }

        if (salesOrder.supplier == null){
            Contact companyContact = accounting.getCompanyContact()
            if (companyContact == null) {
                ContactsPanel.setCompanyContact(accounting)
            }
            salesOrder.supplier = companyContact
        }
        String invoiceNumber = salesOrder.invoiceNumber
        if (invoiceNumber==null || "".equals(invoiceNumber)){
            invoiceNumber = JOptionPane.showInputDialog(this, "Enter Invoice Number")
            salesOrder.invoiceNumber = invoiceNumber
        }

        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
        dateAndDescriptionDialog.description = ""
        dateAndDescriptionDialog.enableDescription(true)
        dateAndDescriptionDialog.visible = true

        Calendar date = dateAndDescriptionDialog.date
        String description = dateAndDescriptionDialog.description

        salesOrder.deliveryDate = Utils.toString(date)
        salesOrder.description = description

        String xmlPath = SalesOrderIO.writeInvoiceXmlInputFile accounting, salesOrder
        String pdfPath = SalesOrderIO.calculatePdfPath accounting, salesOrder
        InvoicePDF.createInvoice xmlPath, pdfPath
    }

    void updateButtonsAndCheckBoxes() {
        Transaction salesTransaction = salesOrder?salesOrder.salesTransaction:null
        Transaction paymentTransaction = salesOrder?salesOrder.paymentTransaction:null

        StockTransactions stockTransactions = accounting.stockTransactions
        ArrayList<Order> orders = stockTransactions.getOrders()
        boolean orderDelivered = salesOrder && orders.contains(salesOrder)
        boolean toBeDelivered = salesOrder && !orders.contains(salesOrder)

        boolean isCreditNote = salesOrder && salesOrder.creditNote
        boolean isPromoOrder = salesOrder && salesOrder.promoOrder
        boolean editable = salesOrder && salesOrder.editable

        placed.setSelected(salesTransaction)
        delivered.setSelected(orderDelivered)
        payed.setSelected(paymentTransaction )
        creditNote.setSelected(isCreditNote)
        promoOrder.setSelected(isPromoOrder)

//        editSalesOrder.enabled = true
        editSalesOrder.enabled = editable
        deliveredButton.enabled = toBeDelivered
        placeOrderButton.enabled = salesTransaction==null

        updateInvoiceButtonAndField()

        updateContactDetails(salesOrder)

        salesTransactionButton.enabled = salesOrder && salesOrder.salesTransaction==null
        gainTransactionButton.enabled = salesOrder  && salesOrder.gainTransaction==null
        paymentTransactionButton.enabled = salesOrder  && paymentTransaction ==null
    }

    void updateInvoiceButtonAndField() {
        if(salesOrder&&salesOrder.invoice){
            createInvoiceButton.enabled = true
            invoiceNr.setText(salesOrder.invoiceNumber)
        } else {
            createInvoiceButton.enabled = false
            invoiceNr.setText("")
        }
    }

    void updateContactDetails(SalesOrder salesOrder){
        if(salesOrder&&salesOrder.customer){
            contactDetailsPanel.setContact(salesOrder.customer)
        } else {
            contactDetailsPanel.clearFields()
        }
    }

    Contact getCustomer(){
        Contact customer = salesOrder.customer
        if(customer == null){
            Contacts contacts = accounting.contacts
            ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, Contact.ContactType.CUSTOMERS)
            contactSelectorDialog.visible = true
            customer = contactSelectorDialog.getSelection()
        }
        customer
    }

    Transaction createTransaction(Calendar date, String description){
        if (date == null || description == null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
            dateAndDescriptionDialog.enableDescription(true)
            if(description){
                dateAndDescriptionDialog.description = description
            } else {
                dateAndDescriptionDialog.description = ""
            }
            dateAndDescriptionDialog.visible = true

            date = dateAndDescriptionDialog.date
            description = dateAndDescriptionDialog.description
        }
        new Transaction(date, description)
    }

    void createPromoTransaction() {
        Transaction transaction = createTransaction(null, salesOrder.name)

        Account promoCost = StockUtils.getPromoAccount accounting
        Account stockAccount = StockUtils.getStockAccount accounting
        BigDecimal totalSalesPriceInclVat = salesOrder.totalSalesPriceInclVat

        Booking costBooking = new Booking(promoCost, totalSalesPriceInclVat, true)
        Booking stockBooking = new Booking(stockAccount, totalSalesPriceInclVat, false)
        transaction.addBusinessObject(costBooking)
        transaction.addBusinessObject(stockBooking)

        Journal journal = StockUtils.getGainJournal accounting
        transaction.journal = journal

        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        journal.addBusinessObject transaction
        salesOrder.salesTransaction = transaction
        salesOrder.gainTransaction = transaction
        Main.journal = journal
        Main.selectTransaction(transaction)
        Main.fireJournalDataChanged(journal)
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged(account)
        }
    }

    void createSalesTransaction(Transaction transaction){

        Contact customer = getCustomer()
        transaction.contact = customer

        Account customerAccount = StockUtils.getCustomerAccount(customer, accounting)
        Account salesAccount = StockUtils.getSalesAccount(accounting)

        boolean creditNote = salesOrder.creditNote

        BigDecimal salesPriceInclVat = salesOrder.totalSalesPriceInclVat

        Booking customerBooking = new Booking(customerAccount, salesPriceInclVat, !creditNote)
        transaction.addBusinessObject(customerBooking)

        // Calculate Net Amounts per VAT Rate -> Fields 0, 1, 2, 3
        List<SalesType> salesTypes = new ArrayList<>()
        salesTypes.add(SalesType.VAT_1)
        salesTypes.add(SalesType.VAT_3)

        for(SalesType salesType:salesTypes){
            BigDecimal netAmount = salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(salesType.getPct()))
            if(netAmount.compareTo(BigDecimal.ZERO) != 0){
                if(!creditNote) {
                    Booking salesBooking = new Booking(salesAccount, netAmount, false)
                    AccountActions.addSalesVatTransaction(salesBooking, salesType)
                    transaction.addBusinessObject(salesBooking)
                    transaction.increaseTurnOverAmount(netAmount)
                } else {
                    Booking salesCnBooking = new Booking(salesAccount, netAmount, true)
                    AccountActions.addSalesCnVatTransaction(salesCnBooking)
                    transaction.addBusinessObject(salesCnBooking)
                    transaction.increaseTurnOverAmount(netAmount.negate())
                }
            }
        }

        // Calculate Total VAT Amount -> Field 54
        BigDecimal vatAmount = salesOrder.calculateTotalSalesVat() // ensure no cent different
        if(!creditNote) {
            Booking vatBooking = AccountActions.createSalesVatBooking(accounting, vatAmount)
            transaction.VATAmount = vatAmount
            transaction.addBusinessObject(vatBooking)
        } else {
            Booking vatBooking = AccountActions.createSalesCnVatBooking(accounting, vatAmount)
            transaction.VATAmount = vatAmount.negate()
            transaction.addBusinessObject(vatBooking)
        }
        // ---

        Journal salesJournal
        if(salesOrder.invoice) {
            salesJournal = StockUtils.getSalesJournal(accounting)
        } else {
            salesJournal = StockUtils.getSalesNoInvoiceJournal(accounting)
        }
        transaction.journal = salesJournal
        // TODO: ask for Date and Description

        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        salesJournal.addBusinessObject transaction
        salesOrder.salesTransaction = transaction
        Main.setJournal salesJournal
        Main.selectTransaction transaction
        Main.fireJournalDataChanged salesJournal
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged account
        }
    }

    void createGainTransaction(Calendar date){
        Account salesGainAccount = StockUtils.getSalesGainAccount accounting
        Account stockAccount = StockUtils.getStockAccount accounting
        Account gainAccount = StockUtils.getGainAccount accounting

        BigDecimal stockAmount = salesOrder.totalStockValue
        BigDecimal totalSalesPriceExclVat = salesOrder.totalSalesPriceExclVat
        BigDecimal gainAmount = totalSalesPriceExclVat.subtract stockAmount

        boolean creditNote = salesOrder.creditNote

        Booking stockBooking = new Booking(stockAccount, stockAmount, creditNote)
        Booking gainBooking = new Booking(gainAccount, gainAmount, creditNote)
        Booking salesDivBooking = new Booking(salesGainAccount, totalSalesPriceExclVat, !creditNote)

        Transaction gainTransaction = new Transaction(date, salesOrder.name)

        gainTransaction.addBusinessObject(gainBooking)
        gainTransaction.addBusinessObject(stockBooking)
        gainTransaction.addBusinessObject(salesDivBooking)

        // ---

        Journal gainJournal = StockUtils.getGainJournal(accounting)
        gainTransaction.journal = gainJournal


        Transactions transactions = accounting.transactions
        transactions.setId(gainTransaction)
        transactions.addBusinessObject gainTransaction
        gainJournal.addBusinessObject gainTransaction
        salesOrder.gainTransaction = gainTransaction
        Main.fireJournalDataChanged(gainJournal)
        for (Account account : gainTransaction.accounts) {
            Main.fireAccountDataChanged(account)
        }
    }

    void setOrder(SalesOrder salesOrder){
        this.salesOrder = salesOrder
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        contactDetailsPanel.accounting = accounting
    }
}
