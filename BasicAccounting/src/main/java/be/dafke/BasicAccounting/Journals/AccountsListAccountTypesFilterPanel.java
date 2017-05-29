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
public class AccountsListAccountTypesFilterPanel extends JPanel {
    private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType, Boolean> selectedAccountTypes;
    private AccountsList accountsList;

    public AccountsListAccountTypesFilterPanel(AccountsList accountsList) {
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
            boxes.clear();
            removeAll();

            accountTypes.getBusinessObjects().forEach(type -> {

                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(type.getName().toUpperCase()));
                // TODO: save selections per Journal in xml file
                checkBox.setSelected(true);
                checkBox.setActionCommand(type.getName());
                checkBox.addActionListener(e -> {
                    accountsList.setTypeAvailable(type, checkBox.isSelected());
                });
                boxes.put(type, checkBox);
                add(checkBox);
            });
            revalidate();
        }
    }

    public void refresh() {
        boxes.forEach((accountType, checkBox) -> {
            checkBox.setSelected(accountsList.isTypeAvailable(accountType));
        });
    }
}
