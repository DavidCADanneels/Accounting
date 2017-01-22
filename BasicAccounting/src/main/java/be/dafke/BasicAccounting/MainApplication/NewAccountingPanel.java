package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableDialog;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import static be.dafke.BasicAccounting.MainApplication.Main.accountings;
import static be.dafke.BasicAccounting.MainApplication.Main.setAccounting;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 22/01/2017.
 */
public class NewAccountingPanel extends RefreshableDialog {

    private JButton add;
    private JTextField nameField;

    public NewAccountingPanel(Accountings accountings) {
        super(getBundle("Accounting").getString("NEW_ACCOUNTING_GUI_TITLE"));
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

        add = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNT"));
        add.addActionListener(e -> addAccounting());
        panel.add(add);

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
                accountings.addBusinessObject(accounting);
                accountings.setCurrentObject(accounting);
                setAccounting(accounting);
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.ACCOUNTING_DUPLICATE_NAME);
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.ACCOUNTING_NAME_EMPTY);
            }
        }
    }
}
