package be.dafke.Accounting.BasicAccounting.Journals.Edit

import be.dafke.Accounting.BasicAccounting.Journals.JournalActions
import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.table.TableColumn
import java.awt.*
import java.awt.event.*

import static java.util.ResourceBundle.getBundle

class JournalEditPanel extends JPanel implements ActionListener {
    JTextField debet, credit, ident
    JButton singleBook, save, clear
    JCheckBox balanceTransaction

    final SelectableTable<Booking> table
    TableColumn debitAccount, creditAccount
    JComboBox<Account> comboBox
    final JournalGUIPopupMenu popup
    final JournalDataModel journalDataModel
    BigDecimal debettotaal, credittotaal

    Journal journal
    Transaction transaction
    DateAndDescriptionPanel dateAndDescriptionPanel

    JournalEditPanel() {
        setLayout(new BorderLayout())
        debettotaal = new BigDecimal(0)
        credittotaal = new BigDecimal(0)

        journalDataModel = new JournalDataModel()

        table = new SelectableTable<>(journalDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(800, 200))

        popup = new JournalGUIPopupMenu(table)
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))

        JScrollPane scrollPane = new JScrollPane(table)
        add(scrollPane, BorderLayout.CENTER)

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent me) {
                popup.visible = false
            }
        })
        JPanel onder = createInputPanel()
        add(onder, BorderLayout.SOUTH)
    }

    JPanel createInputPanel(){
        ident = new JTextField(4)
        ident.setEditable(false)
        balanceTransaction = new JCheckBox("balanceTransaction",false)
        balanceTransaction.addActionListener({ e -> transaction.balanceTransaction = balanceTransaction.selected })
        singleBook = new JButton(getBundle("Accounting").getString("OK"))
        singleBook.addActionListener(this)
        singleBook.setMnemonic(KeyEvent.VK_B)
        save = new JButton(getBundle("Accounting").getString("SAVE"))
        save.addActionListener(this)
        clear = new JButton(getBundle("Accounting").getString("CLEAR_PANEL"))
        clear.addActionListener(this)

        JPanel paneel2 = new JPanel()
        paneel2.add(new JLabel(getBundle("Accounting").getString("TRANSACTION")))
        paneel2.add(ident)
        paneel2.add(balanceTransaction)

        JPanel paneel3 = new JPanel()
        paneel3.add(singleBook)
        paneel3.add(clear)
        paneel3.add(save)
        debet = new JTextField(8)
        credit = new JTextField(8)
        debet.setEditable(false)
        credit.setEditable(false)

        JPanel paneel4 = new JPanel()
        paneel4.add(new JLabel(getBundle("Accounting").getString("TOTAL_DEBIT")))
        paneel4.add(debet)
        paneel4.add(new JLabel(getBundle("Accounting").getString("TOTAL_CREDIT")))
        paneel4.add(credit)

        JPanel mainPanel = new JPanel()
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS))
//        mainPanel.setLayout(new GridLayout(0, 1))
        dateAndDescriptionPanel = new DateAndDescriptionPanel()
        mainPanel.add(paneel4)
        mainPanel.add(paneel2)
        mainPanel.add(dateAndDescriptionPanel)
        mainPanel.add(paneel3)
        mainPanel
    }

    void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            clear()
        } else if (e.getSource() == save) {
            saveTransaction()
        } else if (e.getSource() == singleBook){
            saveTransaction()
            if(journal && transaction && transaction.bookable){
                JournalActions.addTransactionToJournal(transaction, journal)
                Main.selectTransaction(transaction)
                // FIXME: add 'purchaseOrder' and 'salesOrder' iso using 'order' + casting ???
                // The JournalType is more relevant
                if(transaction.order){
                    if(journal.type.name == JournalType.PAYMENT_TYPE) {
                        transaction.order.paymentTransaction = transaction
                    } else if(journal.type.name == JournalType.PURCHASE_TYPE){
                        PurchaseOrder purchaseOrder = (PurchaseOrder)transaction.order
                        purchaseOrder.purchaseTransaction = transaction
                    } else if(journal.type.name == JournalType.SALE_TYPE){
                        SalesOrder salesOrder = (SalesOrder)transaction.order
                        salesOrder.salesTransaction = transaction
                    }
                }
                Mortgage mortgage = transaction.mortgage
                if(mortgage){
                    Main.fireMortgageEditedPayButton mortgage
                    Main.fireMortgageEdited mortgage
                }
                clear()
            }
            Main.fireTransactionBooked()
        }
    }

    void clear() {
        transaction = new Transaction(dateAndDescriptionPanel.date, "")
        transaction.journal = journal
        balanceTransaction.selected = false
        journal.currentTransaction = transaction
        setTransaction(transaction)
        ident.setText(journal==null?"":journal.abbreviation + " " + journal.id)
    }

    Transaction saveTransaction(){
        if(transaction){
            Calendar date = dateAndDescriptionPanel.date
            if(date == null){
                ActionUtils.showErrorMessage(this, ActionUtils.FILL_IN_DATE)
                null
            } else {
                // TODO Encode text for XML / HTML (not here, but in toXML() / here escaping ?)
                transaction.description = dateAndDescriptionPanel.description
                transaction.date = dateAndDescriptionPanel.date
            }
        }
        transaction
    }

    static JComboBox<Account> createComboBox() {
        JComboBox<Account> comboBox = new JComboBox<>()
        comboBox.removeAllItems()
        if(Session.activeAccounting && Session.activeAccounting.accounts) {
            Session.activeAccounting.accounts.businessObjects.forEach({ account -> comboBox.addItem(account) })
        }
        comboBox
    }

    void refresh(){
        Accounting accounting = Session.activeAccounting
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession?accountingSession.activeJournal:null)

        comboBox=createComboBox()
        debitAccount = table.getColumnModel().getColumn(JournalDataModel.DEBIT_ACCOUNT)
        debitAccount.setCellEditor(new DefaultCellEditor(comboBox))
        creditAccount = table.getColumnModel().getColumn(JournalDataModel.CREDIT_ACCOUNT)
        creditAccount.setCellEditor(new DefaultCellEditor(comboBox))
    }

    void setJournal(Journal journal) {
        this.journal=journal
        dateAndDescriptionPanel.journal = journal
        ident.setText(journal==null?"":journal.abbreviation + " " + journal.id)
        setTransaction(journal?.currentTransaction)
    }

    void setTransaction(Transaction transaction){
        this.transaction = transaction
        dateAndDescriptionPanel.setTransaction(transaction)
        journalDataModel.setTransaction(transaction)
        if(transaction?.balanceTransaction)
            balanceTransaction.setSelected(transaction?.balanceTransaction)
        fireTransactionDataChanged()
    }

    void addBooking(Booking booking){
        transaction.addBusinessObject(booking)
        fireTransactionDataChanged()
    }

    Transaction getTransaction() {
        transaction
    }

    void addMortgageTransaction(Mortgage mortgage){
        if (mortgage.isPayedOff()) {
            System.out.println("Payed Off already")
            return
        }
        if (transaction.mortgage){
            System.out.println("Transaction already contains a mortgage")
            return
        }
        Account capitalAccount = mortgage.getCapitalAccount()
        Account intrestAccount = mortgage.getIntrestAccount()
        if(capitalAccount==null || intrestAccount==null){
            return
        }
        Booking capitalBooking = new Booking(capitalAccount, mortgage.getNextCapitalAmount(),true)
        Booking intrestBooking = new Booking(intrestAccount, mortgage.getNextIntrestAmount(),true)

        transaction.addBusinessObject(capitalBooking)
        transaction.addBusinessObject(intrestBooking)

        transaction.mortgage = mortgage
        // TODO: pass function "increaseNrPayed/decreaseNrPayed" to call after journal.addBusinessObject(transaction)

        fireTransactionDataChanged()
    }

    Journal getJournal() {
        return journal
    }

    void fireTransactionDataChanged() {
        journalDataModel.fireTableDataChanged()

        debettotaal = (transaction)?transaction.debitTotal:BigDecimal.ZERO//.setScale(2)
        credittotaal = (transaction)?transaction.creditTotal:BigDecimal.ZERO//.setScale(2)
        debet.setText(debettotaal.toString())
        credit.setText(credittotaal.toString())
        balanceTransaction.selected = transaction&&transaction.balanceTransaction
        dateAndDescriptionPanel.fireTransactionDataChanged()

        boolean okEnabled = journal && transaction && transaction.bookable
        boolean clearEnabled = journal && transaction && !transaction.businessObjects.isEmpty()
        clear.enabled = clearEnabled
        save.enabled = clearEnabled
        singleBook.enabled = okEnabled
    }
}
