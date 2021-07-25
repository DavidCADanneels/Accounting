package be.dafke.Accounting.BasicAccounting.Accounts

import be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI
import be.dafke.Accounting.BasicAccounting.Accounts.New.NewAccountDialog
import be.dafke.Accounting.BasicAccounting.Balances.TestBalanceGUI
import be.dafke.Accounting.BusinessModelDao.AccountsIO
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountsMenu extends JMenu {
    static JMenuItem add, manage, testBalance, generatePdf

    AccountsMenu() {
        super(getBundle("BusinessModel").getString("ACCOUNTS"))
//        setMnemonic(KeyEvent.VK_P)
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"))
        add.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            NewAccountDialog newAccountDialog = new NewAccountDialog()
            newAccountDialog.setLocation(locationOnScreen)
            newAccountDialog.visible = true
        })
        add.enabled = false

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"))
        manage.addActionListener({ e ->
            AccountManagementGUI accountManagementGUI = AccountManagementGUI.getInstance(Session.activeAccounting.accounts, Session.activeAccounting.accountTypes.businessObjects)
            accountManagementGUI.setLocation(getLocationOnScreen())
            accountManagementGUI.visible = true
        })
        manage.enabled = false

        testBalance = new JMenuItem(getBundle("BusinessModel").getString("TESTBALANCE"))
        testBalance.addActionListener({ e ->
            TestBalanceGUI testBalanceGUI = TestBalanceGUI.getInstance(Session.activeAccounting)
            testBalanceGUI.setLocation(getLocationOnScreen())
            testBalanceGUI.visible = true
        })
        testBalance.enabled = false

        generatePdf = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"))
        generatePdf.addActionListener({ e -> AccountsIO.writeAccountPdfFiles(Session.activeAccounting) })
        generatePdf.enabled = false

        add(add)
        add(manage)
        add(testBalance)
        add(generatePdf)
    }

    void refresh() {
        boolean enabledMenu = Session.activeAccounting.accounts!=null
        add.enabled = enabledMenu
        manage.enabled = enabledMenu
        testBalance.enabled = enabledMenu
        generatePdf.enabled = enabledMenu
    }
}
