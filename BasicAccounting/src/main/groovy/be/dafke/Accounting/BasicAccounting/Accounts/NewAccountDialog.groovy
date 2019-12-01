package be.dafke.Accounting.BasicAccounting.Accounts

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.ComponentModel.RefreshableDialog

import static java.util.ResourceBundle.getBundle

class NewAccountDialog extends RefreshableDialog {
    NewAccountPanel newAccountPanel

    NewAccountDialog(Accounts accounts, ArrayList<AccountType> accountTypes) {
        super(getBundle("Accounting").getString("NEW_ACCOUNT_GUI_TITLE"))
        newAccountPanel = new NewAccountPanel(accounts, accountTypes)
        setContentPane(newAccountPanel)
        pack()
    }

    void setAccount(Account account) {
        newAccountPanel.setAccount(account)
    }
}