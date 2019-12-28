package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Accounts.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.Journals.DateAndDescriptionPanel
import be.dafke.Accounting.BasicAccounting.Journals.JournalSelectorDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

class MealOrderCreatePanel extends JPanel {
    static final int FOOD_SALES_PERCENTAGE = 6
    static final int DELIVERY_PROFIT_PERCENTAGE = 27
    static final int DELIVERY_SERVICE_PERCENTAGE = 21
    JTextField price
    JButton book
    DeliveryTotalsPanel totalsPanel
    DateAndDescriptionPanel dateAndDescriptionPanel
    Transaction transaction
    Accounting accounting

    JTable table
    MealOrderCreateDataTableModel tableModel
    MealOrder mealOrder

    MealOrderCreatePanel(Accounting accounting) {
        this.accounting = accounting
        totalsPanel = new DeliveryTotalsPanel()
        transaction = new Transaction(Calendar.getInstance(),"")

        tableModel = new MealOrderCreateDataTableModel(accounting)//, totalsPanel)
        tableModel.setMealOrder(mealOrder)
        table = new JTable(tableModel)

        JScrollPane orderPanel = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(totalsPanel, BorderLayout.SOUTH)
        add(createTopPanel(), BorderLayout.NORTH)
        add(orderPanel, BorderLayout.CENTER)
        clear()
    }

    void clear() {
        totalsPanel.clear()
        price.setText("")
        Calendar date = transaction.date
        transaction = new Transaction(date, "")
        dateAndDescriptionPanel.setTransaction(transaction)
        dateAndDescriptionPanel.fireTransactionDataChanged()

        book.enabled = false

        mealOrder = new MealOrder()
        mealOrder.meals = accounting.meals
        tableModel.setMealOrder(mealOrder)
    }

    JPanel createTopPanel(){
        dateAndDescriptionPanel = new DateAndDescriptionPanel()
        dateAndDescriptionPanel.addDescriptionFocusListener(new FocusAdapter() {
            @Override
            void focusLost(FocusEvent e) {
                enableButtonIfPossible()
            }
        })
        dateAndDescriptionPanel.setTransaction(transaction)
        dateAndDescriptionPanel.fireTransactionDataChanged()

        book = new JButton("Book")
        book.addActionListener({ e ->
            Calendar date = dateAndDescriptionPanel.date
            String description = dateAndDescriptionPanel.description
            mealOrder.setDate(date)
            mealOrder.setDescription(description)
            addHistory()
            book()
            clear()
        })
        price = new JTextField(10)
        price.enabled = false
//        price.addFocusListener(new FocusAdapter() {
//            @Override
//            void focusLost(FocusEvent e) {
//                calculateTotals()
//            }
//        })
        JPanel panel = new JPanel()
        panel.add(dateAndDescriptionPanel)
        JPanel right = new JPanel(new GridLayout(0,2))
        right.add(new JLabel("Sales Price:"))
        right.add(price)
        right.add(new JLabel(""))
        right.add(book)
        panel.add(right)
        panel
    }

    void addHistory() {
        mealOrder.removeEmptyOrderItems()
        MealOrders mealOrders = accounting.mealOrders
        try {
            mealOrders.addBusinessObject(mealOrder)
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace()
        }
        MealOrdersOverviewGUI.fireOrderAddedForAccounting(accounting, mealOrder)
        Main.fireMealCountUpdated(accounting)
    }

    void book() {
        //  MealOrder
        //
        MealOrders mealOrders = accounting.mealOrders
        Journal salesJournal = mealOrders.getMealOrderSalesJournal()
        Journal serviceJournal = mealOrders.getMealOrderServiceJournal()
        Account mealOrderBalanceAccount = mealOrders.getMealOrderBalanceAccount()
        Account mealOrderServiceAccount = mealOrders.getMealOrderServiceAccount()
        Account mealOrderRevenueAccount = mealOrders.getMealOrderRevenueAccount()
        if(salesJournal==null){
            JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
            journalSelectorDialog.visible = true
            salesJournal = journalSelectorDialog.getSelection()
            mealOrders.setMealOrderSalesJournal(salesJournal)
        }
        if(serviceJournal==null){
            JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
            journalSelectorDialog.visible = true
            serviceJournal = journalSelectorDialog.getSelection()
            mealOrders.setMealOrderServiceJournal(serviceJournal)
        }
        if(mealOrderBalanceAccount==null){
            Accounts accounts = accounting.accounts
            ArrayList<AccountType> accountTypes = accounting.accountTypes.businessObjects
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Meal Order Balance Account")
            accountSelectorDialog.visible = true
            mealOrderBalanceAccount = accountSelectorDialog.getSelection()
            mealOrders.setMealOrderBalanceAccount(mealOrderBalanceAccount)
        }
        if(mealOrderServiceAccount==null){
            Accounts accounts = accounting.accounts
            ArrayList<AccountType> accountTypes = accounting.accountTypes.businessObjects
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Meal Order Service Account")
            accountSelectorDialog.visible = true
            mealOrderServiceAccount = accountSelectorDialog.getSelection()
            mealOrders.setMealOrderServiceAccount(mealOrderServiceAccount)
        }
        if(mealOrderRevenueAccount==null){
            Accounts accounts = accounting.accounts
            ArrayList<AccountType> accountTypes = accounting.accountTypes.businessObjects
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Meal Order Revenue Account")
            accountSelectorDialog.visible = true
            mealOrderRevenueAccount = accountSelectorDialog.getSelection()
            mealOrders.setMealOrderRevenueAccount(mealOrderRevenueAccount)
        }

        // VAT
        //
        Booking customerBooking = new Booking(mealOrderBalanceAccount, totalsPanel.getSalesAmountInclVat(), true)
        Booking salesVatBooking = AccountActions.createSalesVatBooking accounting, totalsPanel.getSalesAmountVat()
        Booking salesRevenueBooking = new Booking(mealOrderRevenueAccount, totalsPanel.getSalesAmountExclVat(), false)
        AccountActions.addSalesVatTransaction salesRevenueBooking, SalesType.VAT_1

        transaction.journal = salesJournal
        Calendar date = transaction.date
        String description = dateAndDescriptionPanel.description

        transaction.addBusinessObject customerBooking
        transaction.addBusinessObject salesRevenueBooking
        transaction.addBusinessObject salesVatBooking

        Transactions transactions = accounting.transactions
        transactions.setId(transaction)
        transactions.addBusinessObject transaction
        salesJournal.addBusinessObject transaction


        Booking serviceBooking = new Booking(mealOrderServiceAccount, totalsPanel.getServiceAmountExclVat(), true)
        AccountActions.addPurchaseVatTransaction serviceBooking, PurchaseType.VAT_82
        Booking serviceVatBooking = AccountActions.createPurchaseVatBooking accounting, totalsPanel.getServiceAmountVat()
        Booking debtsBooking = new Booking(mealOrderBalanceAccount, totalsPanel.getServiceAmountInclVat(), false)

        Transaction serviceTransaction = new Transaction(date, description)
        serviceTransaction.journal = serviceJournal

        serviceTransaction.addBusinessObject serviceBooking
        serviceTransaction.addBusinessObject serviceVatBooking
        serviceTransaction.addBusinessObject debtsBooking

        transactions.setId(serviceTransaction)
        transactions.addBusinessObject serviceTransaction
        serviceJournal.addBusinessObject serviceTransaction
    }

    void calculateTotals() {
        BigDecimal totalPrice = mealOrder.getTotalPrice()
        totalsPanel.setSalesAmountInclVat(totalPrice)
        totalsPanel.calculateTotals()
        BigDecimal salesAmountInclVat = totalsPanel.getSalesAmountInclVat()
        salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN)
        price.text = salesAmountInclVat.toString()
        enableButtonIfPossible()
    }

    void enableButtonIfPossible(){
        String text = price.getText()
        BigDecimal salesAmountInclVat = Utils.parseBigDecimal(text)
        String description = dateAndDescriptionPanel.description
        book.enabled = salesAmountInclVat  && salesAmountInclVat.compareTo(BigDecimal.ZERO)>0 && !description.empty
    }
}
