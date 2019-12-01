package be.dafke.Accounting.BasicAccounting.Accounts.AccountsFilter

import be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.AccountsList
import be.dafke.Accounting.BusinessModelDao.JournalSession

import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import java.awt.BorderLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import static java.util.ResourceBundle.getBundle

class AccountFilterPanel extends JPanel {
    final AccountTypesFilterPanel types
    private final JPanel name, number
    JTextField nameField, numberField
    JCheckBox hideEmptyCheckbox
    JCheckBox showNumbersCheckbox

    AccountDataTableModel model
    JLabel nameLabel, numberLabel

    AccountFilterPanel(AccountDataTableModel model, boolean left) {
        this.model = model

        types = new AccountTypesFilterPanel(model, left)
        name = createNamePanel()
        number = createNumberPanel()
        hideEmptyCheckbox = new JCheckBox("Hide Empty Accounts")
        hideEmptyCheckbox.addActionListener({ e -> hideEmpty() })

        showNumbersCheckbox = new JCheckBox("Show Numbers")
        showNumbersCheckbox.addActionListener({ e -> showNumbers() })
        showNumbersCheckbox.selected = true

        setLayout(new BorderLayout())
        JPanel south = new JPanel()
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS))
        south.add(new JLabel("Search on ..."))
        south.add(name)
        south.add(number)
        south.add(hideEmptyCheckbox)
        south.add(showNumbersCheckbox)
        add(south, BorderLayout.SOUTH)
        add(types, BorderLayout.CENTER)
    }

    void showNumbers() {
        boolean selected = showNumbersCheckbox.selected
        model.setShowNumbers(selected)
        number.setVisible(selected)
//        TODO: update Session
        invalidate()
    }

    void hideEmpty() {
        boolean selected = hideEmptyCheckbox.selected
        model.setHideEmpty(selected)
//        TODO: update Session
        invalidate()
    }

    JPanel createNamePanel(){
        JPanel panel = new JPanel()
        nameField = new JTextField(20)
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            void changedUpdate(DocumentEvent e) { updateNamePrefix() }
            void insertUpdate(DocumentEvent e) {
                updateNamePrefix()
            }
            void removeUpdate(DocumentEvent e) {
                updateNamePrefix()
            }
        })
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            void focusGained(FocusEvent e) {
                nameField.selectAll()
            }
        })
        nameLabel = new JLabel((getBundle("Accounting").getString("NAME_LABEL")))
        panel.add(nameLabel)
        panel.add(nameField)
        panel
    }

    void updateNamePrefix(){
        model.setNamePrefix(nameField.getText())
    }

    void updateNumberPrefix(){
        model.setNumberPrefix(numberField.getText())
    }

    void setJournalSession(JournalSession journalSession){
        types.setJournalSession(journalSession)
    }

    JPanel createNumberPanel(){
        JPanel panel = new JPanel()
        numberField = new JTextField(20)
        numberField.getDocument().addDocumentListener(new DocumentListener() {
            void changedUpdate(DocumentEvent e) {
                updateNumberPrefix()
            }
            void insertUpdate(DocumentEvent e) {
                updateNumberPrefix()
            }
            void removeUpdate(DocumentEvent e) {
                updateNumberPrefix()
            }
        })
        numberField.addFocusListener(new FocusAdapter() {
            @Override
            void focusGained(FocusEvent e) {
                numberField.selectAll()
            }
        })
        numberLabel = new JLabel((getBundle("Accounting").getString("NUMBER_LABEL")))
        panel.add(numberLabel)
        panel.add(numberField)
        panel
    }

    void clearSearchFields(){
        nameField.setText("")
        numberField.setText("")
    }

    @Override
    void setEnabled(boolean enabled){
        nameLabel.setVisible(enabled)
        nameField.setVisible(enabled)
        numberLabel.setVisible(enabled)
        numberField.setVisible(enabled)
    }


    void setAccountTypesList(ArrayList<AccountType> accountTypes) {
        types.setAvailableAccountTypes(accountTypes)

    }
    void setAccountList(AccountsList accountList) {
        clearSearchFields()
        types.setAccountList(accountList)
        boolean enabled = accountList!=null && (!accountList.isSingleAccount() || accountList.account==null)
        setEnabled(enabled)
//        filter()
    }
}
