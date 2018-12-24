package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import java.awt.*;

import static be.dafke.BasicAccounting.MainApplication.Main.setAccounting;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 22/01/2017.
 */
public class NewAccountingPanel extends RefreshableDialog {

    private JButton add;
    private JTextField nameField;
    private Accountings accountings;
    private Accounting accounting;
    private AccountingSettingsPanel accountingSettingsPanel;
    private AccountingCopyPanel accountingCopyPanel;
    private JComboBox<Accounting> accountingToCopyFrom;

    public NewAccountingPanel(Accountings accountings) {
        super(getBundle("Accounting").getString("NEW_ACCOUNTING_GUI_TITLE"));
        this.accountings = accountings;
        accounting = new Accounting("tmp");
        setContentPane(createContentPanel());
        pack();
    }

    private JPanel createContentPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        accountingToCopyFrom = new JComboBox<>();
        for(Accounting accounting:accountings.getBusinessObjects()) {
            accountingToCopyFrom.addItem(accounting);
        }

        nameField = new JTextField(10);
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);
        namePanel.add(new JLabel("copy data from:"));
        namePanel.add(accountingToCopyFrom);

        panel.add(namePanel, BorderLayout.NORTH);

        accountingCopyPanel = new AccountingCopyPanel();
        accountingSettingsPanel = new AccountingSettingsPanel(new Accounting("tmp"), accountingCopyPanel);
        accountingCopyPanel.setSettingsPanel(accountingSettingsPanel);
//        JSplitPane splitPane = Main.createSplitPane(accountingCopyPanel, accountingSettingsPanel, JSplitPane.HORIZONTAL_SPLIT);
        panel.add(accountingSettingsPanel, BorderLayout.CENTER);

        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNTING"));
        add.addActionListener(e -> addAccounting());
        panel.add(add, BorderLayout.SOUTH);

        return panel;
    }

    private void addAccounting() {
        String name = nameField.getText().trim();
        if(name!=null && !name.isEmpty()) {
            saveAccounting(name);
        }
    }

    public void saveAccounting(String name) {
        accounting.setName(name);
        accounting.getAccountTypes().addDefaultTypes();
        accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes());
        accounting.getBalances().addDefaultBalances();
        accounting.getVatFields().addDefaultFields();
        Accounting source = (Accounting)accountingToCopyFrom.getSelectedItem();
        if(accountingCopyPanel.isCopyAccountsSelected()){
            accounting.copyAccounts(source.getAccounts());
        }
        if(accountingCopyPanel.isCopyJournalsSelected()){
            accounting.copyJournals(source.getJournals());
            accounting.copyJournalTypes(source.getJournalTypes());
        }
        if(accountingCopyPanel.isCopyContactsSelected()){
            accounting.copyContacts(source.getContacts());
        }
        try{
            accountings.addBusinessObject(accounting);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_DUPLICATE_NAME);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_NAME_EMPTY);
        }
        Accountings.setActiveAccounting(accounting);
        setAccounting(accounting);
        accounting = new Accounting("tmp");
    }
}
