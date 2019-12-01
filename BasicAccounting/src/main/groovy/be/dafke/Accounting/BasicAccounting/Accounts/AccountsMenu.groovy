package be.dafke.Accounting.BasicAccounting.Accounts

import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Balances.TestBalanceGUI
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModelDao.AccountsIO

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountsMenu extends JMenu {
    private static JMenuItem add, manage, testBalance, generatePdf
    private Accounting accounting

    private Journals journals
    private Accounts accounts
    private AccountTypes accountTypes

    AccountsMenu() {
        super(getBundle("BusinessModel").getString("ACCOUNTS"))
//        setMnemonic(KeyEvent.VK_P)
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"))
        add.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes.getBusinessObjects())
            newAccountDialog.setLocation(locationOnScreen)
            newAccountDialog.setVisible(true)
        })
        add.setEnabled(false)

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"))
        manage.addActionListener({ e ->
            AccountManagementGUI accountManagementGUI = AccountManagementGUI.getInstance(accounts, accountTypes.getBusinessObjects())
            accountManagementGUI.setLocation(getLocationOnScreen())
            accountManagementGUI.setVisible(true)
        })
        manage.setEnabled(false)

        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"))
        testBalance.addActionListener({ e ->
            TestBalanceGUI testBalanceGUI = TestBalanceGUI.getInstance(accounting)
            testBalanceGUI.setLocation(getLocationOnScreen())
            testBalanceGUI.setVisible(true)
        })
        testBalance.setEnabled(false)

        generatePdf = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"))
        generatePdf.addActionListener({ e -> AccountsIO.writeAccountPdfFiles(accounting) })
        generatePdf.setEnabled(false)

        add(add)
        add(manage)
        add(testBalance)
        add(generatePdf)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        journals = accounting==null?null:accounting.getJournals()
        accounts = accounting==null?null:accounting.getAccounts()
        accountTypes = accounting==null?null:accounting.getAccountTypes()
        add.setEnabled(accounts!=null)
        manage.setEnabled(accounts!=null)
        testBalance.setEnabled(accounts!=null)
        generatePdf.setEnabled(accounts!=null)
    }
}
