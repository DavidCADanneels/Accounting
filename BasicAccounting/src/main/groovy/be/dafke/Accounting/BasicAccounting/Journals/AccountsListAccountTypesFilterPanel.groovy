package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.AccountsList
import be.dafke.Accounting.BusinessModel.JournalType

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountsListAccountTypesFilterPanel extends JPanel {
    final Map<AccountType, JCheckBox> boxes
    AccountsList accountsList
    JournalType journalType

    AccountsListAccountTypesFilterPanel(AccountTypes accountTypes, boolean left) {
        setLayout(new GridLayout(0, 3))
        boxes = new HashMap<>()
        if (accountTypes != null) {
            accountTypes.businessObjects.forEach({ accountType ->

                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(accountType.name.toUpperCase()))
                // TODO: save selections per Journal in xml file
//                boolean enabled = accountsList.isTypeAvailable(accountType)
                boolean enabled = false
                checkBox.setSelected(enabled)
                checkBox.setActionCommand(accountType.name)
                checkBox.addActionListener({ e -> checkBoxAction(accountType, checkBox, left) })
                boxes.put(accountType, checkBox)
                add(checkBox)
            })
            revalidate()
        }
    }

    void checkBoxAction(AccountType accountType, JCheckBox checkBox, boolean left){
        accountsList.setTypeAvailable(accountType, checkBox.selected)
        ArrayList<AccountType> accountTypes = accountsList.accountTypes
        if(left) {
//            Main.setAccountsListLeft(journalType, accountsList)
            Main.setAccountsTypesLeft(journalType, accountTypes)
        } else {
//            Main.setAccountsListRight(journalType, accountsList)
            Main.setAccountsTypesRight(journalType, accountTypes)
        }
    }

    @Override
    void setEnabled(boolean enabled){
        boxes.forEach({ accountType, checkBox -> checkBox.enabled = enabled })
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
        refresh()
    }

    void refresh() {
        boxes.forEach({ accountType, checkBox -> checkBox.setSelected(accountsList.isTypeAvailable(accountType)) })
    }
}
