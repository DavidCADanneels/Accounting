package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Journals.DateAndDescriptionDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Order
import be.dafke.Accounting.BusinessModel.PromoOrder
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

class PromoOrderDetailPanel extends JPanel {
    final JButton editPromoOrder
    JButton placeOrderButton, deliveredButton
    JButton createPromoOrder
    JCheckBox delivered, placed
    PromoOrder promoOrder
    Accounting accounting

    PromoOrderDetailPanel() {
        createPromoOrder = new JButton(getBundle("Accounting").getString("CREATE_PR"))
        createPromoOrder.addActionListener({ e ->
            PromoOrderCreateGUI promoOrderCreateGUI = PromoOrderCreateGUI.showPromoOrderGUI(accounting)
            promoOrderCreateGUI.setLocation(getLocationOnScreen())
            promoOrderCreateGUI.visible = true
        })


        editPromoOrder = new JButton(getBundle("Accounting").getString("EDIT_ORDER"))
        editPromoOrder.addActionListener({ e ->
            PromoOrderCreateGUI promoOrderCreateGUI = PromoOrderCreateGUI.showPromoOrderGUI(accounting)
            promoOrderCreateGUI.setPromoOrder(promoOrder)
            promoOrderCreateGUI.setLocation(getLocationOnScreen())
            promoOrderCreateGUI.visible = true
        })

        JPanel orderPanel = createOrderPanel()

        disableButtons()

        setLayout(new BorderLayout())
        add(orderPanel, BorderLayout.NORTH)
        add(createPromoOrder, BorderLayout.SOUTH)
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

    JPanel createStatusPanel(){
        placed = new JCheckBox("Ordered")
        delivered = new JCheckBox("Delived")
        placed.enabled = false
        delivered.enabled = false

        JPanel panel = new JPanel()
        panel.add(placed)
        panel.add(delivered)
        panel
    }

    void disableButtons(){
        editPromoOrder.enabled = false
        placeOrderButton.enabled = false
        deliveredButton.enabled = false
    }

    JPanel createButtonPanel(){
        placeOrderButton = new JButton("Place Order")
        placeOrderButton.addActionListener({ e -> placeOrder() })

        deliveredButton = new JButton("Order Delivered")
        deliveredButton.addActionListener({ e -> deliverOrder() })

        JPanel panel = new JPanel()
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))
        JPanel line1 = new JPanel()
        JPanel line2 = new JPanel()

        line2.add(editPromoOrder)
        line2.add(placeOrderButton)
        line2.add(deliveredButton)

        panel.add(line1)
        panel.add(line2)
        panel
    }

    void deliverOrder() {
        Calendar date
        String description = ""
        String deliveryDate = promoOrder.deliveryDate // FIXME: Calendar iso String
        if(deliveryDate==null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
            dateAndDescriptionDialog.description = description
            dateAndDescriptionDialog.date = Calendar.getInstance()
            dateAndDescriptionDialog.visible = true

            date = dateAndDescriptionDialog.date
//            description = dateAndDescriptionDialog.description

            promoOrder.deliveryDate = Utils.toString(date)
            promoOrder.description = description
        }
        StockTransactions stockTransactions = accounting.stockTransactions
        stockTransactions.addOrder(promoOrder)

        StockGUI.fireStockContentChanged()
        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    void placeOrder() {
        createPromoTransaction()
        StockHistoryGUI.fireStockContentChanged()
        updateButtonsAndCheckBoxes()
    }

    void updateButtonsAndCheckBoxes() {
        Transaction paymentTransaction = promoOrder ==null?null: promoOrder.paymentTransaction

        StockTransactions stockTransactions = accounting.stockTransactions
        ArrayList<Order> orders = stockTransactions.getOrders()
        boolean orderDelivered = promoOrder && orders.contains(promoOrder)
        boolean toBeDelivered = promoOrder && !orders.contains(promoOrder)

        placed.setSelected(paymentTransaction)
        delivered.setSelected(orderDelivered)

        deliveredButton.enabled = toBeDelivered
        placeOrderButton.enabled = paymentTransaction==null

        editPromoOrder.enabled = !orderDelivered && paymentTransaction==null
    }

    Transaction createTransaction(){
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.dateAndDescriptionDialog
        dateAndDescriptionDialog.visible = true

        Calendar date = dateAndDescriptionDialog.date
        String description = dateAndDescriptionDialog.description
        new Transaction(date, description)
    }

    void createPromoTransaction() {
        Transaction transaction = createTransaction()

        Account promoCost = StockUtils.getPromoAccount(accounting)
        Account stockAccount = StockUtils.getStockAccount(accounting)
        BigDecimal totalSalesPriceInclVat = promoOrder.totalStockValue

        Booking costBooking = new Booking(promoCost, totalSalesPriceInclVat, true)
        Booking stockBooking = new Booking(stockAccount, totalSalesPriceInclVat, false)
        transaction.addBusinessObject costBooking
        transaction.addBusinessObject stockBooking

        Journal journal = StockUtils.getGainJournal(accounting)
        transaction.journal = journal

        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        journal.addBusinessObject transaction
        promoOrder.paymentTransaction = transaction
        Main.journal = journal
        Main.selectTransaction transaction
        Main.fireJournalDataChanged journal
        for (Account account : transaction.accounts) {
            Main.fireAccountDataChanged account
        }
    }

    void setOrder(PromoOrder promoOrder){
        this.promoOrder = promoOrder
        updateButtonsAndCheckBoxes()
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }

}
