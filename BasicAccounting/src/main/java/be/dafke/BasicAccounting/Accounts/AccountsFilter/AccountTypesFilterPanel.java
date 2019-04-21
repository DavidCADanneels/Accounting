package be.dafke.BasicAccounting.Accounts.AccountsFilter;

import be.dafke.BasicAccounting.Accounts.AccountDataModel;
import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.BusinessModel.Journal;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;

public class AccountTypesFilterPanel extends JPanel {
    private final Map<AccountType, JCheckBox> boxes;
    private AccountDataModel model;
    private Journal journal;
    private boolean left;

    public AccountTypesFilterPanel(AccountDataTableModel model, boolean left) {
        this.model = model;
        this.left = left;
        setLayout(new GridLayout(0, 3));
        boxes = new HashMap<>();
    }

    public void setAccountList(AccountsList accountList) {
        setAvailableAccountTypes(accountList.getAccountTypes());
        if(journal!=null) {
            if (left) {
                setSelectedAccountTypes(journal.getCheckedTypesLeft());
            } else {
                setSelectedAccountTypes(journal.getCheckedTypesRight());
            }
        }
        revalidate();
    }

    public void setAvailableAccountTypes(ArrayList<AccountType> accountTypes) {
        boxes.clear();
        removeAll();
        if (accountTypes != null) {
            for (AccountType accountType : accountTypes) {
                addCheckBox(accountType);
            }
        }
    }

    private void addCheckBox(AccountType accountType){
        JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(accountType.getName().toUpperCase()));
        checkBox.setActionCommand(accountType.getName());
        checkBox.addActionListener(e -> checkBoxAction());
        boxes.put(accountType, checkBox);
        add(checkBox);
    }

    private void setSelectedAccountTypes(ArrayList<AccountType> accountTypes) {
        for (Map.Entry<AccountType,JCheckBox> entry:boxes.entrySet()){
            AccountType accountType = entry.getKey();
            JCheckBox checkBox = entry.getValue();
            checkBox.setSelected(accountTypes==null||accountTypes.contains(accountType));
        }
    }

    private void checkBoxAction(){
        List<AccountType> selectedAccountTypes = getSelectedAccountTypes();
        model.setAccountTypes(selectedAccountTypes);
        // TODO: can't we just remove and re-add only 'type' i.s.o. all types?
        if(journal!=null) {
            for (AccountType accountType : boxes.keySet()) {
                if (left) {
                    journal.setTypeCheckedLeft(accountType, selectedAccountTypes.contains(accountType));
                } else {
                    journal.setTypeCheckedRight(accountType, selectedAccountTypes.contains(accountType));
                }
            }
        }
    }

//    public void setAccounts(Accounts accounts) {
//        boolean active = accounts != null;
//        if (accountTypes != null) {
//            for (AccountType type : accountTypes.getBusinessObjects()) {
//                JCheckBox checkBox = boxes.get(type);
//                checkBox.setSelected(selectedAccountTypes.get(type));
//                checkBox.setEnabled(active);
//            }
//        }
//        if (active) {
//            fireAccountDataChanged();
//        }
//    }

    public List<AccountType> getSelectedAccountTypes() {
        ArrayList<AccountType> types = new ArrayList<>();
        for (Map.Entry<AccountType,JCheckBox> entry : boxes.entrySet()){
            AccountType accountType = entry.getKey();
            JCheckBox checkBox = entry.getValue();
            if (checkBox.isSelected()) {
                types.add(accountType);
            }
        }
        return types;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }
}
