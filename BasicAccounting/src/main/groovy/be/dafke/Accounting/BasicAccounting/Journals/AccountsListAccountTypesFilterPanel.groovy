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
    private final Map<AccountType, JCheckBox> boxes
    private AccountsList accountsList
    private JournalType journalType

    AccountsListAccountTypesFilterPanel(AccountTypes accountTypes, boolean left) {
        setLayout(new GridLayout(0, 3))
        boxes = new HashMap<>()
        if (accountTypes != null) {
            accountTypes.getBusinessObjects().forEach({ accountType ->

                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(accountType.getName().toUpperCase()))
                // TODO: save selections per Journal in xml file
//                boolean enabled = accountsList.isTypeAvailable(accountType)
                boolean enabled = false
                checkBox.setSelected(enabled)
                checkBox.setActionCommand(accountType.getName())
                checkBox.addActionListener({ e -> checkBoxAction(accountType, checkBox, left) })
                boxes.put(accountType, checkBox)
                add(checkBox)
            })
            revalidate()
        }
    }

    private void checkBoxAction(AccountType accountType, JCheckBox checkBox, boolean left){
        accountsList.setTypeAvailable(accountType, checkBox.isSelected())
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes()
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
        boxes.forEach({ accountType, checkBox -> checkBox.setEnabled(enabled) })
    }

    void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList
        refresh()
    }

    void setJournalType(JournalType journalType) {
        this.journalType = journalType
    }

    void refresh() {
        boxes.forEach({ accountType, checkBox -> checkBox.setSelected(accountsList.isTypeAvailable(accountType)) })
    }
}
