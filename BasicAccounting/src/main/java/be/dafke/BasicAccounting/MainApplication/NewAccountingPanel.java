package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import static be.dafke.BasicAccounting.MainApplication.Main.setAccounting;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 22/01/2017.
 */
public class NewAccountingPanel extends RefreshableDialog {

    private JButton add;
    private JTextField nameField;
    private JCheckBox copyAccounts, copyJournals;
    private JComboBox<Accounting> accountingToCopyFrom;
    private Accountings accountings;

    public NewAccountingPanel(Accountings accountings) {
        super(getBundle("Accounting").getString("NEW_ACCOUNTING_GUI_TITLE"));
        this.accountings = accountings;
        setContentPane(createContentPanel());
        pack();
    }

    private JPanel createContentPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        nameField = new JTextField(10);
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);
        panel.add(namePanel);

        panel.add(createOptionsPanel());

        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNTING"));
        add.addActionListener(e -> addAccounting());
        panel.add(add);

        return panel;
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel();
        copyAccounts = new JCheckBox("copy Accounts");
        copyJournals = new JCheckBox("copy Journals");
        JLabel from = new JLabel("from:");
        accountingToCopyFrom = new JComboBox<>();
        for(Accounting accounting:accountings.getBusinessObjects()) {
            accountingToCopyFrom.addItem(accounting);
        }
        panel.add(copyAccounts);
        panel.add(copyJournals);
        panel.add(from);
        panel.add(accountingToCopyFrom);
        return panel;
    }

    private void addAccounting() {
        String name = nameField.getText().trim();
        if(name!=null && !name.isEmpty()) {
            try{
                Accounting accounting = new Accounting(name);
                accounting.getAccountTypes().addDefaultTypes();
                accounting.getJournalTypes().addDefaultType(accounting.getAccountTypes());
                accounting.getBalances().addDefaultBalances();
                accounting.getVatFields().addDefaultFields();
                Accounting source = (Accounting)accountingToCopyFrom.getSelectedItem();
                if(copyAccounts.isSelected()){
                    accounting.copyAccounts(source.getAccounts());
                }
                if(copyJournals.isSelected()){
                    accounting.copyJournals(source.getJournals());
                    accounting.copyJournalTypes(source.getJournalTypes());
                }
                accountings.addBusinessObject(accounting);
                Accountings.setActiveAccounting(accounting);
                setAccounting(accounting);
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_DUPLICATE_NAME);
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_NAME_EMPTY);
            }
        }
    }
}
