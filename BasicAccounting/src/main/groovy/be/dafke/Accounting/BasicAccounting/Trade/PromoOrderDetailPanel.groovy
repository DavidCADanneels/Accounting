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
    private final JButton editPromoOrder
    private JButton placeOrderButton, deliveredButton
    private JButton createPromoOrder
    private JCheckBox delivered, placed
    private PromoOrder promoOrder
    private Accounting accounting

    PromoOrderDetailPanel() {
        createPromoOrder = new JButton(getBundle("Accounting").getString("CREATE_PR"))
        createPromoOrder.addActionListener({ e ->
            PromoOrderCreateGUI promoOrderCreateGUI = PromoOrderCreateGUI.showPromoOrderGUI(accounting)
            promoOrderCreateGUI.setLocation(getLocationOnScreen())
            promoOrderCreateGUI.setVisible(true)
        })


        editPromoOrder = new JButton(getBundle("Accounting").getString("EDIT_ORDER"))
        editPromoOrder.addActionListener({ e ->
            PromoOrderCreateGUI promoOrderCreateGUI = PromoOrderCreateGUI.showPromoOrderGUI(accounting)
            promoOrderCreateGUI.setPromoOrder(promoOrder)
            promoOrderCreateGUI.setLocation(getLocationOnScreen())
            promoOrderCreateGUI.setVisible(true)
        })

        JPanel orderPanel = createOrderPanel()

        disableButtons()

        setLayout(new BorderLayout())
        add(orderPanel, BorderLayout.NORTH)
        add(createPromoOrder, BorderLayout.SOUTH)
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

    private JPanel createStatusPanel(){
        placed = new JCheckBox("Ordered")
        delivered = new JCheckBox("Delived")
        placed.setEnabled(false)
        delivered.setEnabled(false)

        JPanel panel = new JPanel()
        panel.add(placed)
        panel.add(delivered)
        panel
    }

    void disableButtons(){
        editPromoOrder.setEnabled(false)
        placeOrderButton.setEnabled(false)
        deliveredButton.setEnabled(false)
    }

    private JPanel createButtonPanel(){
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

    private void deliverOrder() {
        Calendar date
        String description = ""
        String deliveryDate = promoOrder.getDeliveryDate() // FIXME: Calendar iso String
        if(deliveryDate==null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
            dateAndDescriptionDialog.setDescription(description)
            dateAndDescriptionDialog.setDate(Calendar.getInstance())
            dateAndDescriptionDialog.setVisible(true)

            date = dateAndDescriptionDialog.getDate()
//            description = dateAndDescriptionDialog.getDescription()

            promoOrder.setDeliveryDate(Utils.toString(date))
            promoOrder.setDescription(description)
        }
        StockTransactions stockTransactions = accounting.getStockTransactions()
        stockTransactions.addOrder(promoOrder)

        StockGUI.fireStockContentChanged()
        StockHistoryGUI.fireStockContentChanged()

        updateButtonsAndCheckBoxes()
    }

    private void placeOrder() {
        createPromoTransaction()
        StockHistoryGUI.fireStockContentChanged()
        updateButtonsAndCheckBoxes()
    }

    void updateButtonsAndCheckBoxes() {
        Transaction paymentTransaction = promoOrder ==null?null: promoOrder.getPaymentTransaction()

        StockTransactions stockTransactions = accounting.getStockTransactions()
        ArrayList<Order> orders = stockTransactions.getOrders()
        boolean orderDelivered = promoOrder !=null && orders.contains(promoOrder)
        boolean toBeDelivered = promoOrder !=null && !orders.contains(promoOrder)

        placed.setSelected(paymentTransaction!=null)
        delivered.setSelected(orderDelivered)

        deliveredButton.setEnabled(toBeDelivered)
        placeOrderButton.setEnabled(paymentTransaction==null)

        editPromoOrder.setEnabled(!orderDelivered && paymentTransaction==null)
    }

    Transaction createTransaction(){
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog()
        dateAndDescriptionDialog.setVisible(true)

        Calendar date = dateAndDescriptionDialog.getDate()
        String description = dateAndDescriptionDialog.getDescription()
        new Transaction(date, description)
    }

    private void createPromoTransaction() {
        Transaction transaction = createTransaction()

        Account promoCost = StockUtils.getPromoAccount(accounting)
        Account stockAccount = StockUtils.getStockAccount(accounting)
        BigDecimal totalSalesPriceInclVat = promoOrder.getTotalStockValue()

        Booking costBooking = new Booking(promoCost, totalSalesPriceInclVat, true)
        Booking stockBooking = new Booking(stockAccount, totalSalesPriceInclVat, false)
        transaction.addBusinessObject(costBooking)
        transaction.addBusinessObject(stockBooking)

        Journal journal = StockUtils.getGainJournal(accounting)
        transaction.setJournal(journal)

        Transactions transactions = accounting.getTransactions()
        transactions.setId(transaction)
        transactions.addBusinessObject(transaction)
        journal.addBusinessObject(transaction)
        promoOrder.setPaymentTransaction(transaction)
        Main.setJournal(journal)
        Main.selectTransaction(transaction)
        Main.fireJournalDataChanged(journal)
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account)
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
