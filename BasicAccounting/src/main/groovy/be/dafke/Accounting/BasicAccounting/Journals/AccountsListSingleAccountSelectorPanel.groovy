package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.NewAccountDialog
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.AccountsList

import javax.swing.*

class AccountsListSingleAccountSelectorPanel extends JPanel {
    JButton create
    JComboBox<Account> combo
    DefaultComboBoxModel<Account> model
    AccountsList accountsList

    AccountsListSingleAccountSelectorPanel(Accounts accounts, AccountTypes accountTypes) {
        model = new DefaultComboBoxModel<>()
        combo = new JComboBox<>(model)
        combo.addActionListener({ e -> selectionChanged() })
        create = new JButton("Add account(s) ...")
        create.addActionListener({ e ->
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes.businessObjects)
            newAccountDialog.setLocation(getLocationOnScreen())
            newAccountDialog.visible = true
        })
        add(combo)
        add(create)
        accounts.businessObjects.stream().forEach({ account -> model.addElement(account) })
    }
    void selectionChanged(){
        Account account = (Account) combo.selectedItem
        if(accountsList!=null) accountsList.setAccount(account)
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
    }

    @Override
    void setEnabled(boolean enabled){
        combo.enabled = enabled
        create.enabled = enabled
    }

    void refresh() {
        boolean singleAccount = accountsList.isSingleAccount()
        Account account = singleAccount?accountsList.account:null
        combo.setSelectedItem(account)
    }
}
