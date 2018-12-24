package be.dafke.BasicAccounting.MainApplication;

import javax.swing.*;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingCopyPanel extends JPanel {

    private JCheckBox copyAccounts, copyJournals, copyContacts;
    private AccountingSettingsPanel accountingSettingsPanel;

    public AccountingCopyPanel(){
        copyAccounts = new JCheckBox("copy Accounts");
        copyJournals = new JCheckBox("copy Journals");
        copyContacts = new JCheckBox("copy Contacts");

        copyAccounts.addActionListener(e -> updateCopyAccountsSelected());
        copyJournals.addActionListener(e -> updateCopyJournalsSelected());
        copyContacts.addActionListener(e -> updateCopyContactsSelected());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(copyAccounts);
        panel.add(copyJournals);
        panel.add(copyContacts);

        add(panel);
    }

    private void updateCopyAccountsSelected() {
        boolean copyAccountsSelected = copyAccounts.isSelected();
//        accountsTab.setEnabled(copyAccountsSelected);
        if(copyAccountsSelected){

        } else {

        }
    }

    private void updateCopyJournalsSelected() {
        boolean copyJournalsSelected = copyJournals.isSelected();
//        journalsTab.setEnabled(copyJournalsSelected);
        if(copyJournalsSelected){

        } else {

        }
    }

    private void updateCopyContactsSelected() {
        boolean copyContactsSelected = copyContacts.isSelected();
//        contactsTab.setEnabled(copyContactsSelected);
        if(copyContactsSelected){
            copyAccounts.setSelected(true);
            updateCopyAccountsSelected();
            accountingSettingsPanel.setContactsSelected(true);
        } else {
            accountingSettingsPanel.setVatSelected(false);
        }
    }

    public boolean isCopyAccountsSelected(){
        return copyAccounts.isSelected();
    }

    public boolean isCopyJournalsSelected(){
        return copyJournals.isSelected();
    }

    public boolean isCopyContactsSelected(){
        return copyContacts.isSelected();
    }

    public void setSettingsPanel(AccountingSettingsPanel accountingSettingsPanel) {
        this.accountingSettingsPanel = accountingSettingsPanel;
    }
}
