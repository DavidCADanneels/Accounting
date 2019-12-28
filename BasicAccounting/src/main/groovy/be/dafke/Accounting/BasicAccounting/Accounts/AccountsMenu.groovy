package be.dafke.Accounting.BasicAccounting.Accounts

import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Accounts.New.NewAccountDialog
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
    static JMenuItem add, manage, testBalance, generatePdf
    Accounting accounting

    Journals journals
    Accounts accounts
    AccountTypes accountTypes

    AccountsMenu() {
        super(getBundle("BusinessModel").getString("ACCOUNTS"))
//        setMnemonic(KeyEvent.VK_P)
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"))
        add.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes.businessObjects)
            newAccountDialog.setLocation(locationOnScreen)
            newAccountDialog.visible = true
        })
        add.enabled = false

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"))
        manage.addActionListener({ e ->
            AccountManagementGUI accountManagementGUI = AccountManagementGUI.getInstance(accounts, accountTypes.businessObjects)
            accountManagementGUI.setLocation(getLocationOnScreen())
            accountManagementGUI.visible = true
        })
        manage.enabled = false

        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"))
        testBalance.addActionListener({ e ->
            TestBalanceGUI testBalanceGUI = TestBalanceGUI.getInstance(accounting)
            testBalanceGUI.setLocation(getLocationOnScreen())
            testBalanceGUI.visible = true
        })
        testBalance.enabled = false

        generatePdf = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"))
        generatePdf.addActionListener({ e -> AccountsIO.writeAccountPdfFiles(accounting) })
        generatePdf.enabled = false

        add(add)
        add(manage)
        add(testBalance)
        add(generatePdf)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        journals = accounting?accounting.journals:null
        accounts = accounting?accounting.accounts:null
        accountTypes = accounting?accounting.accountTypes:null
        add.enabled = accounts!=null
        manage.enabled = accounts!=null
        testBalance.enabled = accounts!=null
        generatePdf.enabled = accounts!=null
    }
}
