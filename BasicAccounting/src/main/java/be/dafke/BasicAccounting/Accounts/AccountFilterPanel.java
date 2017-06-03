package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountsList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class AccountFilterPanel extends JPanel {
    private final AccountTypesFilterPanel types;
    private final JPanel name, number;
    private JTextField nameField, numberField;

    private AccountDataModel model;
    private JLabel nameLabel, numberLabel;

    public AccountFilterPanel(AccountDataModel model) {
        this.model = model;

        types = new AccountTypesFilterPanel(model);
        name = createNamePanel();
        number = createNumberPanel();

        setLayout(new BorderLayout());
        JPanel south = new JPanel();
        south.add(name);
//        south.add(number);
        add(south, BorderLayout.SOUTH);
        add(types, BorderLayout.CENTER);
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
        nameLabel = new JLabel((getBundle("Utils").getString("SEARCH")));
        panel.add(nameLabel);
        panel.add(nameField);
        return panel;
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
        numberLabel = new JLabel((getBundle("Utils").getString("SEARCH")));
        panel.add(numberLabel);
        panel.add(numberField);
        return panel;
    }

    private void filter() {
        String namePrefix = nameField.getText();
        String numberPrefix = numberField.getText();
        Predicate<Account> predicate = Account.namePrefix(namePrefix).and(Account.numberPrefix(numberPrefix));
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

    public void setAccountList(AccountsList accountList) {
        clearSearchFields();
        types.setAccountList(accountList);
        setEnabled(accountList!=null && !accountList.isSingleAccount());
    }
}
