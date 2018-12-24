package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
        accountingToCopyFrom.addActionListener(e -> selectedAccountChanged());

        nameField = new JTextField(10);
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);

            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String name = nameField.getText();
                if(name!=null && !name.trim().isEmpty()){
                    add.setEnabled(true);
                    accounting.setName(name.trim());
                } else {
                    accounting.setName(name);
                }
            }
        });

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);
        namePanel.add(new JLabel("copy data from:"));
        namePanel.add(accountingToCopyFrom);

        panel.add(namePanel, BorderLayout.NORTH);

        accountingCopyPanel = new AccountingCopyPanel();
        accounting = accountingCopyPanel.getAccounting();
        accountingSettingsPanel = new AccountingSettingsPanel(accounting, accountingCopyPanel);
        accountingCopyPanel.setSettingsPanel(accountingSettingsPanel);
//        JSplitPane splitPane = Main.createSplitPane(accountingCopyPanel, accountingSettingsPanel, JSplitPane.HORIZONTAL_SPLIT);
        panel.add(accountingSettingsPanel, BorderLayout.CENTER);

        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNTING"));
        add.setEnabled(false);
        add.addActionListener(e -> saveAccounting());
        panel.add(add, BorderLayout.SOUTH);

        return panel;
    }

    private void selectedAccountChanged() {
        Accounting accounting = (Accounting) accountingToCopyFrom.getSelectedItem();
        accountingCopyPanel.setCopyFrom(accounting);
//        accountingCopyPanel.enableCopyContacts(accounting.isContactsAccounting());

    }

    public void saveAccounting() {
//        if(accounting.isVatAccounting()) {
//            accounting.getVatFields().addDefaultFields();
//        }
        try{
            accountings.addBusinessObject(accounting);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_DUPLICATE_NAME);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_NAME_EMPTY);
        }
        Accountings.setActiveAccounting(accounting);
        setAccounting(accounting);
//        do not clear yet, check details
//        accountingCopyPanel.createAccounting();
    }
}
