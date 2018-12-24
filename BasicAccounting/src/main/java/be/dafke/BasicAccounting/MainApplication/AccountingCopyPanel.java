package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingCopyPanel extends JPanel {

    private final JCheckBox copyAccounts, copyJournals, copyContacts, copyVat, copyTrade, copyDeliveroo;
    private AccountingSettingsPanel accountingSettingsPanel;
    private Accounting copyFrom;
    private Accounting newAccounting;

    public AccountingCopyPanel(){
        copyAccounts = new JCheckBox("copy Accounts");
        copyJournals = new JCheckBox("copy Journals");
        copyContacts = new JCheckBox("copy Contacts");
        copyVat = new JCheckBox("copy VAT Settings");
        copyTrade = new JCheckBox("copy Trade Settings");
        copyDeliveroo = new JCheckBox("copy Deliveroo Settings");

        copyAccounts.addActionListener(e -> updateCopyAccountsSelected());
        copyJournals.addActionListener(e -> updateCopyJournalsSelected());
        copyContacts.addActionListener(e -> updateCopyContactsSelected());
        copyVat.addActionListener(e -> updateCopyVATSelected());
        copyTrade.addActionListener(e -> updateCopyTradeSelected());
        copyDeliveroo.addActionListener(e -> updateCopyDeliverooSelected());

        copyAccounts.setEnabled(false);
        copyJournals.setEnabled(false);
        copyContacts.setEnabled(false);
        copyVat.setEnabled(false);
        copyTrade.setEnabled(false);
        copyDeliveroo.setEnabled(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(copyAccounts);
        panel.add(copyJournals);
        panel.add(copyContacts);
        panel.add(copyVat);
        panel.add(copyTrade);
        panel.add(copyDeliveroo);

        add(panel);
    }

    // SET

    public void setAccounting(Accounting accounting){
        newAccounting = accounting;
    }

    public void setSettingsPanel(AccountingSettingsPanel accountingSettingsPanel) {
        this.accountingSettingsPanel = accountingSettingsPanel;
    }

    public void setCopyFrom(Accounting copyFrom) {
        this.copyFrom = copyFrom;
        selectCopyAccounts(copyAccounts.isSelected());
        selectCopyJournals(copyJournals.isSelected());
        selectCopyContacts(copyContacts.isSelected());
        selectCopyVat(copyVat.isSelected());
        selectCopyTrade(copyTrade.isSelected());
        selectCopyDeliveroo(copyDeliveroo.isSelected());
    }

    // CREATE

    public Accounting createAccounting(){
        Accounting accounting = new Accounting("New Accounting");
        accounting.getAccountTypes().addDefaultTypes();
        accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes());
        accounting.getBalances().addDefaultBalances();
        return accounting;
    }

    // COPY

    public void copyAccounts(){
        newAccounting.copyAccounts(copyFrom.getAccounts());
    }

    public void copyJournals(){
        newAccounting.copyJournals(copyFrom.getJournals());
        newAccounting.copyJournalTypes(copyFrom.getJournalTypes());
    }

    public void copyContacts(){
        newAccounting.copyContacts(copyFrom.getContacts());
    }

    public void copyVatSettings(){
        newAccounting.copyVatSettings(copyFrom.getVatTransactions());
    }

    // UPDATE

    private void updateCopyAccountsSelected() {
        boolean copyAccountsSelected = copyAccounts.isSelected();
        if(copyAccountsSelected){
            copyAccounts();
        } else {
            selectCopyContacts(false);
        }
    }

    private void updateCopyJournalsSelected() {
        boolean copyJournalsSelected = copyJournals.isSelected();
        if(copyJournalsSelected){
            copyJournals();
        } else {

        }
    }

    private void updateCopyContactsSelected() {
        boolean enabled = copyFrom.isContactsAccounting();
        boolean selected = enabled && copyContacts.isSelected();
        if(selected){
            selectCopyAccounts(true);
            accountingSettingsPanel.setContactsSelected(true);
            copyContacts();
            if(copyFrom!=null){
                accountingSettingsPanel.copyContacts(copyFrom);
            }
        } else {
            accountingSettingsPanel.setVatSelected(false);
            selectCopyVat(false);
        }
    }

    private void updateCopyVATSelected() {
        boolean selected = copyVat.isSelected();
        if(selected){
            selectCopyAccounts(true);
            selectCopyJournals(true);
            selectCopyContacts(true);
            accountingSettingsPanel.setVatSelected(true);
            copyVatSettings();
            if(copyFrom!=null){
                accountingSettingsPanel.copyVatSettings(copyFrom);
            }
        } else {
            selectCopyTrade(false);
            selectCopyDeliveroo(false);
        }
    }

    private void updateCopyTradeSelected() {
        boolean selected = copyTrade.isSelected();
        if(selected){
            selectCopyVat(true);
            accountingSettingsPanel.setTradeSelected(true);
            if(copyFrom!=null){
                accountingSettingsPanel.copyTradeSettings(copyFrom);
            }
        } else {

        }

    }

    private void updateCopyDeliverooSelected() {
        boolean selected = copyDeliveroo.isSelected();
        if(selected){
            selectCopyVat(true);
            accountingSettingsPanel.setDeliveooSelected(true);
            if(copyFrom!=null){
                accountingSettingsPanel.copyDeliverooSettings(copyFrom);
            }
        } else {

        }
    }

    // SELECT

    public void selectCopyAccounts(boolean selected){
        boolean enabled = copyFrom != null;
        copyAccounts.setEnabled(enabled);
        copyAccounts.setSelected(enabled && selected);
        updateCopyAccountsSelected();
    }

    public void selectCopyJournals(boolean selected){
        boolean enabled = copyFrom != null;
        copyJournals.setEnabled(enabled);
        copyJournals.setSelected(enabled && selected);
        updateCopyJournalsSelected();
    }

    public void selectCopyContacts(boolean selected){
        boolean enabled = copyFrom!=null && copyFrom.isContactsAccounting();
        copyContacts.setEnabled(enabled);
        copyContacts.setSelected(enabled && selected);
        updateCopyContactsSelected();
    }

    private void selectCopyVat(boolean selected) {
        boolean enabled = copyFrom!=null && copyFrom.isVatAccounting();
        copyVat.setEnabled(enabled);
        copyVat.setSelected(enabled && selected);
        updateCopyVATSelected();
    }

    private void selectCopyTrade(boolean selected) {
        boolean enabled = copyFrom!=null && copyFrom.isTradeAccounting();
        copyTrade.setEnabled(enabled);
        copyTrade.setSelected(enabled && selected);
        updateCopyTradeSelected();
    }

    private void selectCopyDeliveroo(boolean selected) {
        boolean enabled = copyFrom!=null && copyFrom.isDeliverooAccounting();
        copyDeliveroo.setEnabled(enabled);
        copyDeliveroo.setSelected(enabled && selected);
        updateCopyDeliverooSelected();
    }
}
