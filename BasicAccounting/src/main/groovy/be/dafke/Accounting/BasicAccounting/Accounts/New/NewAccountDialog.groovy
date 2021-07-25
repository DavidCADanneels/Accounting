package be.dafke.Accounting.BasicAccounting.Accounts.New

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.ComponentModel.RefreshableDialog

import static java.util.ResourceBundle.getBundle

class NewAccountDialog extends RefreshableDialog {
    NewAccountPanel newAccountPanel

    NewAccountDialog() {
        super(getBundle("Accounting").getString("NEW_ACCOUNT_GUI_TITLE"))
        newAccountPanel = new NewAccountPanel()
        setContentPane(newAccountPanel)
        pack()
    }

    void setAccount(Account account) {
        newAccountPanel.setAccount(account)
    }
}