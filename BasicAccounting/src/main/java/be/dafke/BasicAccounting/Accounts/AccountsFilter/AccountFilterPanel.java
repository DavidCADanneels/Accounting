package be.dafke.BasicAccounting.Accounts.AccountsFilter;

import be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel;
import be.dafke.Accounting.BusinessModel.AccountType;
import be.dafke.Accounting.BusinessModel.AccountsList;
import be.dafke.BusinessModelDao.JournalSession;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

//import java.util.function.Predicate;

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
        hideEmptyCheckbox.addActionListener(e -> hideEmpty());

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
//        TODO: update Session
        invalidate();
    }

    private void hideEmpty() {
        boolean selected = hideEmptyCheckbox.isSelected();
        model.setHideEmpty(selected);
//        TODO: update Session
         invalidate();
    }

    private JPanel createNamePanel(){
        JPanel panel = new JPanel();
        nameField = new JTextField(20);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateNamePrefix(); }
            public void insertUpdate(DocumentEvent e) {
                updateNamePrefix();
            }
            public void removeUpdate(DocumentEvent e) {
                updateNamePrefix();
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

    private void updateNamePrefix(){
        model.setNamePrefix(nameField.getText());
    }

    private void updateNumberPrefix(){
        model.setNumberPrefix(numberField.getText());
    }

    public void setJournalSession(JournalSession journalSession){
        types.setJournalSession(journalSession);
    }

    private JPanel createNumberPanel(){
        JPanel panel = new JPanel();
        numberField = new JTextField(20);
        numberField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateNumberPrefix();
            }
            public void insertUpdate(DocumentEvent e) {
                updateNumberPrefix();
            }
            public void removeUpdate(DocumentEvent e) {
                updateNumberPrefix();
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
//        filter();
    }
}
