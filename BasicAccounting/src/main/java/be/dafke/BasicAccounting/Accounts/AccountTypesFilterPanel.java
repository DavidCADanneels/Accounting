package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class AccountTypesFilterPanel extends JPanel {
    private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType, Boolean> selectedAccountTypes;
    private AccountDataModel model;

    public AccountTypesFilterPanel(AccountDataModel model) {
        this.model = model;
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
                    model.setAccountTypes(getSelectedAccountTypes());
                    for (AccountType accountType : boxes.keySet()) {
                        JCheckBox checkBox2 = boxes.get(accountType);
                        selectedAccountTypes.remove(accountType);
                        selectedAccountTypes.put(accountType, checkBox2.isSelected());
                    }
                });
                boxes.put(type, checkBox);
                add(checkBox);
            }
            revalidate();
            model.setAccountTypes(getSelectedAccountTypes());
        }
    }

    public List<AccountType> getSelectedAccountTypes() {
        ArrayList<AccountType> types = new ArrayList<>();
        for (AccountType type : selectedAccountTypes.keySet()) {
            JCheckBox checkBox = boxes.get(type);
            if (checkBox.isSelected()) {
                types.add(type);
            }
        }
        return types;
    }

}
