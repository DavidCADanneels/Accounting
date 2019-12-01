package be.dafke.Accounting.BasicAccounting.Accounts

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts

import javax.swing.*

class AccountSelectorPanel extends JPanel {
    Account account
    JComboBox<Account> combo
    DefaultComboBoxModel<Account> model
    Accounts accounts
    ArrayList<AccountType> accountTypes

    AccountSelectorPanel(Accounts accounts, ArrayList<AccountType> accountTypes) {
        this.accountTypes = accountTypes
        model = new DefaultComboBoxModel<>()
        combo = new JComboBox<>(model)
        combo.addActionListener({ e -> account = (Account) combo.selectedItem })
        JButton create = new JButton("Add account(s) ...")
        create.addActionListener({ e ->
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes)
            newAccountDialog.setLocation(getLocationOnScreen())
            newAccountDialog.visible = true
        })
        add(combo)
        add(create)
        setAccounts(accounts)
    }

    Account getSelection() {
        account
    }

    void fireAccountDataChanged() {
        model.removeAllElements()
        accounts.getAccountsByType(accountTypes).forEach({ account -> model.addElement(account) })
        invalidate()
        combo.invalidate()
    }

    void setAccounts(Accounts accounts) {
        this.accounts = accounts
        fireAccountDataChanged()
    }
}
