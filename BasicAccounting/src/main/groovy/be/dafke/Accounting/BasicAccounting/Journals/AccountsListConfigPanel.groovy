package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout

class AccountsListConfigPanel extends JPanel {
    private final ButtonConfigPanel buttonConfigPanel
    private JRadioButton byType, singleAccount
    private AccountsListSingleAccountSelectorPanel accountSelectorPanel
    private JPanel north
    private AccountsList accountsList
    private JTextField taxType

    private AccountsListAccountTypesFilterPanel accountTypesFilterPanel
//    private Journal journal

    AccountsListConfigPanel(Accounts accounts, AccountTypes accountTypes, boolean left) {
        setLayout(new BorderLayout())
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Accounts"))
        taxType = new JTextField(20)
        taxType.setEditable(false)
        ButtonGroup group = new ButtonGroup()
        byType = new JRadioButton("select by type:", true)
        singleAccount = new JRadioButton("single account:", false)
        byType.addActionListener({ e -> refresh() })
        singleAccount.addActionListener({ e -> refresh() })
        group.add(byType)
        group.add(singleAccount)
        north = new JPanel()
        north.setLayout(new GridLayout(2,0))
        north.add(new JLabel("VatType:"))
        north.add(taxType)
        north.add(byType)
        north.add(singleAccount)

        accountsList = new AccountsList()
        accountTypesFilterPanel = new AccountsListAccountTypesFilterPanel(accountTypes, left)
        accountSelectorPanel = new AccountsListSingleAccountSelectorPanel(accounts,accountTypes)
        add(north,BorderLayout.NORTH)
        JPanel center = new JPanel(new BorderLayout())
        center.add(accountSelectorPanel, BorderLayout.NORTH)
        center.add(accountTypesFilterPanel, BorderLayout.CENTER)
        add(center,BorderLayout.CENTER)
//
        buttonConfigPanel = new ButtonConfigPanel()
        add(buttonConfigPanel, BorderLayout.SOUTH)
//        refresh()
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
        buttonConfigPanel.setAccountsList(accountsList)
        singleAccount.setSelected(accountsList.isSingleAccount())
        accountTypesFilterPanel.setAccountsList(accountsList)
        accountSelectorPanel.setAccountsList(accountsList)
    }

    void refresh(){
        boolean singleAccountSelected = singleAccount.isSelected()
        accountsList.setSingleAccount(singleAccountSelected)
        accountSelectorPanel.setEnabled(singleAccountSelected)
        accountSelectorPanel.refresh()

        boolean byTypeSelected = byType.isSelected()
        accountTypesFilterPanel.setEnabled(byTypeSelected)
        accountTypesFilterPanel.refresh()
    }

    void setJournalType(JournalType journalType) {
        accountTypesFilterPanel.setJournalType(journalType)
    }

    void setVatType(VATTransaction.VATType vatType){
        taxType.setText(vatType==null?"":vatType.toString())
    }
}
