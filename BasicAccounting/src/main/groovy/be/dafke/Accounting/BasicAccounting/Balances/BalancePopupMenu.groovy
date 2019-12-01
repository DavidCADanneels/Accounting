package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI
import be.dafke.Accounting.BasicAccounting.Accounts.NewAccountDialog
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class BalancePopupMenu extends JPopupMenu {
    final JMenuItem details, edit
    Accounting accounting
    SelectableTable<Account> gui

    BalancePopupMenu(Accounting accounting, SelectableTable<Account> gui) {
        this.accounting = accounting
        this.gui = gui

        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"))
        details.addActionListener({ e -> showDetails() })
        add(details)

        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_ACCOUNT"))
        edit.addActionListener({ e -> editAccount() })
        add(edit)
    }

    void showDetails() {
        ArrayList<Account> accounts = gui.selectedObjects
        for(Account account: accounts) {
            Point locationOnScreen = getLocationOnScreen()
            AccountDetailsGUI.getAccountDetails(locationOnScreen, account, accounting.journals)
        }
        setVisible(false)
    }

    void editAccount(){
        AccountTypes accountTypes = accounting.accountTypes
        ArrayList<Account> accounts = gui.selectedObjects
        for(Account account: accounts) {
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.accounts, accountTypes.businessObjects)
            newAccountDialog.setAccount(account)
            newAccountDialog.visible = true
        }
        setVisible(false)
    }
}
