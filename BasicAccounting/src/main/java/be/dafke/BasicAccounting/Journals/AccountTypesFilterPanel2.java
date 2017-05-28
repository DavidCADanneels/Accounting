package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.AccountsList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class AccountTypesFilterPanel2 extends JPanel {
    private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType, Boolean> selectedAccountTypes;
    private AccountsList accountsList;

    public AccountTypesFilterPanel2(AccountsList accountsList) {
        this.accountsList = accountsList;
        setLayout(new GridLayout(0, 3));
        boxes = new HashMap<>();
        selectedAccountTypes = new HashMap<>();
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        if (accountTypes != null) {
            selectedAccountTypes.clear();
            for (AccountType type : accountTypes.getBusinessObjects()) {
                selectedAccountTypes.put(type, Boolean.TRUE);
            }
//        }
            boxes.clear();
            removeAll();

            for (AccountType type : accountTypes.getBusinessObjects()) {
                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(type.getName().toUpperCase()));
                // TODO: save selections per Journal in xml file
                checkBox.setSelected(true);
                checkBox.setActionCommand(type.getName());
                checkBox.addActionListener(e -> {

                });
                boxes.put(type, checkBox);
                add(checkBox);
            }
            revalidate();
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
}
