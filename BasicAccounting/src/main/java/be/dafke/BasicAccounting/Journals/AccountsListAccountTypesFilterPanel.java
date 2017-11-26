package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.BusinessModel.JournalType;

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
    private AccountsList accountsList;
    private JournalType journalType;

    public AccountsListAccountTypesFilterPanel(AccountTypes accountTypes, boolean left) {
        setLayout(new GridLayout(0, 3));
        boxes = new HashMap<>();
        if (accountTypes != null) {
            accountTypes.getBusinessObjects().forEach(accountType -> {

                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(accountType.getName().toUpperCase()));
                // TODO: save selections per Journal in xml file
//                boolean enabled = accountsList.isTypeAvailable(accountType);
                boolean enabled = false;
                checkBox.setSelected(enabled);
                checkBox.setActionCommand(accountType.getName());
                checkBox.addActionListener(e -> {
                    accountsList.setTypeAvailable(accountType, checkBox.isSelected());
                    if(left) {
                        Main.setAccountsListLeft(journalType, accountsList);
                    } else {
                        Main.setAccountsListRight(journalType, accountsList);
                    }
                });
                boxes.put(accountType, checkBox);
                add(checkBox);
            });
            revalidate();
        }
    }

    @Override
    public void setEnabled(boolean enabled){
        boxes.forEach((accountType, checkBox) -> checkBox.setEnabled(enabled));
    }

    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
        refresh();
    }

    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
    }

    public void refresh() {
        boxes.forEach((accountType, checkBox) -> checkBox.setSelected(accountsList.isTypeAvailable(accountType)));
    }
}
