package be.dafke.Accounting.BasicAccounting.MainApplication.New

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Settings.AccountingCopyPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Settings.AccountingSettingsPanel
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accountings
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Accounting.BusinessModelDao.XMLWriter
import be.dafke.ComponentModel.RefreshableDialog

import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import java.awt.BorderLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import static java.util.ResourceBundle.getBundle

class NewAccountingPanel extends RefreshableDialog {

    JButton addButton
    JTextField nameField
    Accountings accountings
    Accounting accounting
    AccountingSettingsPanel accountingSettingsPanel
    AccountingCopyPanel accountingCopyPanel
    JComboBox<Accounting> accountingToCopyFrom

    NewAccountingPanel(Accountings accountings) {
        super(getBundle("Accounting").getString("NEW_ACCOUNTING_GUI_TITLE"))
        this.accountings = accountings
        setContentPane(createContentPanel())
        pack()
    }

    JPanel createContentPanel(){
        JPanel panel = new JPanel()
        panel.setLayout(new BorderLayout())

        accountingToCopyFrom = new JComboBox<>()
        for(Accounting accounting:accountings.businessObjects) {
            accountingToCopyFrom.addItem(accounting)
        }
        accountingToCopyFrom.setSelectedItem(Session.activeAccounting)
        accountingToCopyFrom.addActionListener({ e -> selectedAccountChanged() })

        nameField = new JTextField(10)
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            void focusGained(FocusEvent e) {
                super.focusGained(e)

            }

            @Override
            void focusLost(FocusEvent e) {
                super.focusLost(e)
                String name = nameField.getText()
                if(name && !name.trim().empty){
                    addButton.enabled = true
                    accounting.setName(name.trim())
                } else {
                    accounting.setName(name)
                }
            }
        })

        JPanel namePanel = new JPanel()
        namePanel.add(new JLabel("Name:"))
        namePanel.add(nameField)
        namePanel.add(new JLabel("copy data from:"))
        namePanel.add(accountingToCopyFrom)

        panel.add(namePanel, BorderLayout.NORTH)

        accountingCopyPanel = new AccountingCopyPanel()
        accounting = accountingCopyPanel.accounting
        accountingSettingsPanel = new AccountingSettingsPanel(accounting, accountingCopyPanel)
        accountingCopyPanel.setSettingsPanel(accountingSettingsPanel)
//        JSplitPane splitPane = Main.createSplitPane(accountingCopyPanel, accountingSettingsPanel, JSplitPane.HORIZONTAL_SPLIT)
        panel.add(accountingSettingsPanel, BorderLayout.CENTER)

        addButton = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_ACCOUNTING"))
        addButton.enabled = false
        addButton.addActionListener({ e -> saveAccounting() })
        panel.add(addButton, BorderLayout.SOUTH)

        panel
    }

    void selectedAccountChanged() {
        Accounting accounting = (Accounting) accountingToCopyFrom.selectedItem
        accountingCopyPanel.setCopyFrom(accounting)
//        accountingCopyPanel.enableCopyContacts(accounting.isContactsAccounting())

    }

    void saveAccounting() {
//        if(accounting.vatAccounting) {
//            accounting.vatFields.addDefaultFields()
//        }
        try{
            accountings.addBusinessObject(accounting)
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_DUPLICATE_NAME)
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNTING_NAME_EMPTY)
        }
//        Accountings.setActiveAccounting(accounting)
        XMLWriter.writeAccounting(accounting, false)
//        XMLReader.readAccountingSkeleton(accounting)
        setAccounting(accounting, false)
//        do not clear yet, check details
//        accountingCopyPanel.createAccounting()
    }
}
