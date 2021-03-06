package be.dafke.Accounting.BasicAccounting.Accounts.AccountsFilter

import be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.AccountsList
import be.dafke.Accounting.BusinessModelDao.JournalSession

import javax.swing.*
import java.awt.*
import java.util.List

import static java.util.ResourceBundle.getBundle

class AccountTypesFilterPanel extends JPanel {
    final Map<AccountType, JCheckBox> boxes
    AccountDataTableModel model
    JournalSession journalSession
    boolean left

    AccountTypesFilterPanel(AccountDataTableModel model, boolean left) {
        this.model = model
        this.left = left
        setLayout(new GridLayout(0, 3))
        boxes = new HashMap<>()
    }

    void setAccountList(AccountsList accountList) {
        setAvailableAccountTypes(accountList.accountTypes)
        refreshSelectedAccounts()
    }

    void refreshSelectedAccounts(){
        if(journalSession!=null) {
            if (left) {
                setSelectedAccountTypes(journalSession.getCheckedTypesLeft())
            } else {
                setSelectedAccountTypes(journalSession.getCheckedTypesRight())
            }
        }
        revalidate()
    }

    void setAvailableAccountTypes(ArrayList<AccountType> accountTypes) {
        boxes.clear()
        removeAll()
        if (accountTypes != null) {
            for (AccountType accountType : accountTypes) {
                addCheckBox(accountType)
            }
        }
    }

    void addCheckBox(AccountType accountType){
        JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(accountType.name.toUpperCase()))
        checkBox.setActionCommand(accountType.name)
        checkBox.addActionListener({ e -> checkBoxAction() })
        boxes.put(accountType, checkBox)
        add(checkBox)
    }

    void setSelectedAccountTypes(ArrayList<AccountType> accountTypes) {
        for (Map.Entry<AccountType,JCheckBox> entry:boxes.entrySet()){
            AccountType accountType = entry.getKey()
            JCheckBox checkBox = entry.getValue()
            checkBox.setSelected(accountTypes==null||accountTypes.contains(accountType))
        }
    }

    void checkBoxAction(){
        List<AccountType> selectedAccountTypes = getSelectedAccountTypes()
        model.accountTypes = selectedAccountTypes
        // TODO: can't we just remove and re-add only 'type' i.s.o. all types?
        if(journalSession!=null) {
            for (AccountType accountType : boxes.keySet()) {
                if (left) {
                    journalSession.setTypeCheckedLeft(accountType, selectedAccountTypes.contains(accountType))
                } else {
                    journalSession.setTypeCheckedRight(accountType, selectedAccountTypes.contains(accountType))
                }
            }
        }
    }

//    void setAccounts(Accounts accounts) {
//        boolean active = accounts != null
//        if (accountTypes != null) {
//            for (AccountType type : accountTypes.businessObjects) {
//                JCheckBox checkBox = boxes.get(type)
//                checkBox.setSelected(selectedAccountTypes.get(type))
//                checkBox.enabled = active
//            }
//        }
//        if (active) {
//            fireAccountDataChanged()
//        }
//    }

    List<AccountType> getSelectedAccountTypes() {
        ArrayList<AccountType> types = new ArrayList<>()
        for (Map.Entry<AccountType,JCheckBox> entry : boxes.entrySet()){
            AccountType accountType = entry.getKey()
            JCheckBox checkBox = entry.getValue()
            if (checkBox.selected) {
                types.add(accountType)
            }
        }
        types
    }

    void setJournalSession(JournalSession journalSession) {
        this.journalSession = journalSession
        refreshSelectedAccounts()
    }
}
