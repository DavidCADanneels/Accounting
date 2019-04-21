package be.dafke.BasicAccounting.Accounts.AccountsFilter;

import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.BusinessModel.Journal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

public class AccountFilterPanel extends JPanel {
    private final AccountTypesFilterPanel types;
    private final JPanel name, number;
    private JTextField nameField, numberField;
    private JCheckBox hideEmptyCheckbox;
    private JCheckBox showNumbersCheckbox;

    private AccountDataTableModel model;
    private JLabel nameLabel, numberLabel;

    public AccountFilterPanel(AccountDataTableModel model, boolean left) {
        this.model = model;

        types = new AccountTypesFilterPanel(model, left);
        name = createNamePanel();
        number = createNumberPanel();
        hideEmptyCheckbox = new JCheckBox("Hide Empty Accounts");
        hideEmptyCheckbox.addActionListener(e -> filter());

        showNumbersCheckbox = new JCheckBox("Show Numbers");
        showNumbersCheckbox.addActionListener(e -> showNumbers());
        showNumbersCheckbox.setSelected(true);

        setLayout(new BorderLayout());
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(new JLabel("Search on ..."));
        south.add(name);
        south.add(number);
        south.add(hideEmptyCheckbox);
        south.add(showNumbersCheckbox);
        add(south, BorderLayout.SOUTH);
        add(types, BorderLayout.CENTER);
    }

    private void showNumbers() {
        boolean selected = showNumbersCheckbox.isSelected();
        model.setShowNumbers(selected);
        number.setVisible(selected);
        invalidate();
    }

    private JPanel createNamePanel(){
        JPanel panel = new JPanel();
        nameField = new JTextField(20);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
            public void insertUpdate(DocumentEvent e) {
                filter();
            }
            public void removeUpdate(DocumentEvent e) {
                filter();
            }
        });
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                nameField.selectAll();
            }
        });
        nameLabel = new JLabel((getBundle("Accounting").getString("NAME_LABEL")));
        panel.add(nameLabel);
        panel.add(nameField);
        return panel;
    }

    public void setJournal(Journal journal){
        types.setJournal(journal);
    }

    private JPanel createNumberPanel(){
        JPanel panel = new JPanel();
        numberField = new JTextField(20);
        numberField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
            public void insertUpdate(DocumentEvent e) {
                filter();
            }
            public void removeUpdate(DocumentEvent e) {
                filter();
            }
        });
        numberField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                numberField.selectAll();
            }
        });
        numberLabel = new JLabel((getBundle("Accounting").getString("NUMBER_LABEL")));
        panel.add(numberLabel);
        panel.add(numberField);
        return panel;
    }

    private void filter() {
        String namePrefix = nameField.getText();
        String numberPrefix = numberField.getText();
        Predicate<Account> predicate = Account.namePrefix(namePrefix).and(Account.numberPrefix(numberPrefix));
        if(hideEmptyCheckbox.isSelected()){
            predicate =  predicate.and(Account.saldoNotZero());
        }
        model.setFilter(predicate);
    }

    public void clearSearchFields(){
        nameField.setText("");
        numberField.setText("");
    }

    @Override
    public void setEnabled(boolean enabled){
        nameLabel.setVisible(enabled);
        nameField.setVisible(enabled);
        numberLabel.setVisible(enabled);
        numberField.setVisible(enabled);
    }


    public void setAccountTypesList(ArrayList<AccountType> accountTypes) {
        types.setAvailableAccountTypes(accountTypes);

    }
    public void setAccountList(AccountsList accountList) {
        clearSearchFields();
        types.setAccountList(accountList);
        boolean enabled = accountList!=null && (!accountList.isSingleAccount() || accountList.getAccount()==null);
        setEnabled(enabled);
        filter();
    }
}
