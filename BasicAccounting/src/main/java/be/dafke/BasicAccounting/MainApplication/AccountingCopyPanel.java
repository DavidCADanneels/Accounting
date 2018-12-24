package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingCopyPanel extends JPanel {

    private JCheckBox copyAccounts, copyJournals, copyContacts;
    private AccountingSettingsPanel accountingSettingsPanel;
    private Accounting copyFrom;
    private Accounting newAccounting;

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

    public void setAccounting(Accounting accounting){
        newAccounting = accounting;
    }

    public Accounting createAccounting(){
        Accounting accounting = new Accounting("New Accounting");
        accounting.getAccountTypes().addDefaultTypes();
        accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes());
        accounting.getBalances().addDefaultBalances();
        return accounting;
    }

    private void updateCopyAccountsSelected() {
        boolean copyAccountsSelected = copyAccounts.isSelected();
//        accountsTab.setEnabled(copyAccountsSelected);
        if(copyAccountsSelected){
            newAccounting.copyAccounts(copyFrom.getAccounts());
        } else {

        }
    }

    private void updateCopyJournalsSelected() {
        boolean copyJournalsSelected = copyJournals.isSelected();
//        journalsTab.setEnabled(copyJournalsSelected);
        if(copyJournalsSelected){
            newAccounting.copyJournals(copyFrom.getJournals());
            newAccounting.copyJournalTypes(copyFrom.getJournalTypes());
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
            newAccounting.copyContacts(copyFrom.getContacts());
            if(copyFrom!=null){
                accountingSettingsPanel.copyContacts(copyFrom);
            }
        } else {
            accountingSettingsPanel.setVatSelected(false);
        }
    }

    public void setSettingsPanel(AccountingSettingsPanel accountingSettingsPanel) {
        this.accountingSettingsPanel = accountingSettingsPanel;
    }

    public void setCopyFrom(Accounting copyFrom) {
        this.copyFrom = copyFrom;
        updateCopyContactsSelected();
    }

    public void enableCopyContacts(boolean enabled) {
        if(!enabled) copyContacts.setSelected(false);
        copyContacts.setEnabled(enabled);
    }
}
