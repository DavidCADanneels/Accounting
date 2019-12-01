package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.VATTransactions

import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.GridLayout

class VATSettingsPanel extends JPanel {
    private Accounting accounting
    private final JComboBox<Account> debitAccountSelection, creditAccountSelection, debitCnAccountSelection, creditCnAccountSelection
    private final DefaultComboBoxModel<Account> debitAccountModel, creditAccountModel, debitCnAccountModel, creditCnAccountModel

    VATSettingsPanel(Accounting accounting) {
        this.accounting = accounting
        debitAccountModel = new DefaultComboBoxModel<>()
        creditAccountModel = new DefaultComboBoxModel<>()
        debitCnAccountModel = new DefaultComboBoxModel<>()
        creditCnAccountModel = new DefaultComboBoxModel<>()
        accounting.getAccounts().getBusinessObjects().forEach({ account ->
            debitAccountModel.addElement(account)
            creditAccountModel.addElement(account)
            debitCnAccountModel.addElement(account)
            creditCnAccountModel.addElement(account)
        })

        debitAccountSelection = new JComboBox<>(debitAccountModel)
        creditAccountSelection = new JComboBox<>(creditAccountModel)
        debitCnAccountSelection = new JComboBox<>(debitCnAccountModel)
        creditCnAccountSelection = new JComboBox<>(creditCnAccountModel)

        VATTransactions vatTransactions = accounting.getVatTransactions()
        Account debitAccount = vatTransactions.getDebitAccount()
        Account creditAccount = vatTransactions.getCreditAccount()
        Account debitCNAccount = vatTransactions.getDebitCNAccount()
        Account creditCNAccount = vatTransactions.getCreditCNAccount()

        debitAccountSelection.setSelectedItem(debitAccount)
        debitAccountSelection.addActionListener({ e -> updateSelectedDebitAccount() })
        debitAccountSelection.setEnabled(accounting.isVatAccounting())

        creditAccountSelection.setSelectedItem(creditAccount)
        creditAccountSelection.addActionListener({ e -> updateSelectedCreditAccount() })
        creditAccountSelection.setEnabled(accounting.isVatAccounting())

        debitCnAccountSelection.setSelectedItem(debitCNAccount)
        debitCnAccountSelection.addActionListener({ e -> updateSelectedDebitCnAccount() })
        debitCnAccountSelection.setEnabled(accounting.isVatAccounting())

        creditCnAccountSelection.setSelectedItem(creditCNAccount)
        creditCnAccountSelection.addActionListener({ e -> updateSelectedCreditCnAccount() })
        creditCnAccountSelection.setEnabled(accounting.isVatAccounting())

        JPanel panel = new JPanel()
        panel.setLayout(new GridLayout(0, 2))

        panel.add(new JLabel("Debit Account"))
        panel.add(debitAccountSelection)
        panel.add(new JLabel("Credit Account"))
        panel.add(creditAccountSelection)
        panel.add(new JLabel("Debit CN Account"))
        panel.add(debitCnAccountSelection)
        panel.add(new JLabel("Credit CN Account"))
        panel.add(creditCnAccountSelection)

        add(panel)
    }

    void updateSelectedDebitAccount() {
        Account account = (Account) debitAccountSelection.getSelectedItem()
        VATTransactions vatTransactions = accounting.getVatTransactions()
        vatTransactions.setDebitAccount(account)
    }

    void updateSelectedCreditAccount() {
        Account account = (Account) creditAccountSelection.getSelectedItem()
        VATTransactions vatTransactions = accounting.getVatTransactions()
        vatTransactions.setCreditAccount(account)
    }

    void updateSelectedDebitCnAccount() {
        Account account = (Account) debitCnAccountSelection.getSelectedItem()
        VATTransactions vatTransactions = accounting.getVatTransactions()
        vatTransactions.setDebitCNAccount(account)
    }

    void updateSelectedCreditCnAccount() {
        Account account = (Account) creditCnAccountSelection.getSelectedItem()
        VATTransactions vatTransactions = accounting.getVatTransactions()
        vatTransactions.setCreditCNAccount(account)
    }

    @Override
    void setEnabled(boolean enabled){
        super.setEnabled(enabled)
        debitAccountSelection.setEnabled(enabled)
        creditAccountSelection.setEnabled(enabled)
        debitCnAccountSelection.setEnabled(enabled)
        creditCnAccountSelection.setEnabled(enabled)
        if(!enabled){
            debitAccountSelection.setSelectedItem(null)
            creditAccountSelection.setSelectedItem(null)
            debitCnAccountSelection.setSelectedItem(null)
            creditCnAccountSelection.setSelectedItem(null)
            updateSelectedDebitAccount()
            updateSelectedCreditAccount()
            updateSelectedDebitCnAccount()
            updateSelectedCreditCnAccount()
        }
    }

    void copyVatSettings(Accounting copyFrom) {
        debitAccountModel.removeAllElements()
        creditAccountModel.removeAllElements()
        debitCnAccountModel.removeAllElements()
        creditCnAccountModel.removeAllElements()

        if (copyFrom != null) {
            VATTransactions vatTransactions = copyFrom.getVatTransactions()

            Account debitAccount = vatTransactions.getDebitAccount()
            Account creditAccount = vatTransactions.getCreditAccount()
            Account debitCNAccount = vatTransactions.getDebitCNAccount()
            Account creditCNAccount = vatTransactions.getCreditCNAccount()

            Accounts accounts = accounting.getAccounts()
            if (accounts != null) {
                accounts.getBusinessObjects().forEach({ account ->
                    debitAccountModel.addElement(account)
                    creditAccountModel.addElement(account)
                    debitCnAccountModel.addElement(account)
                    creditCnAccountModel.addElement(account)
                })
            }

            if (debitAccount != null) {
                Account account = accounts.getBusinessObject(debitAccount.getName())
                vatTransactions.setDebitAccount(account)
                debitAccountSelection.setSelectedItem(account)
            } else {
                vatTransactions.setDebitAccount(null)
                debitAccountSelection.setSelectedItem(null)
            }

            if (creditAccount != null) {
                Account account = accounts.getBusinessObject(creditAccount.getName())
                vatTransactions.setCreditAccount(account)
                creditAccountSelection.setSelectedItem(account)
            } else {
                vatTransactions.setCreditAccount(null)
                creditAccountSelection.setSelectedItem(null)
            }

            if (debitCNAccount != null) {
                Account account = accounts.getBusinessObject(debitCNAccount.getName())
                vatTransactions.setDebitCNAccount(account)
                debitCnAccountSelection.setSelectedItem(account)
            } else {
                vatTransactions.setDebitCNAccount(null)
                debitCnAccountSelection.setSelectedItem(null)
            }

            if (creditCNAccount != null) {
                Account account = accounts.getBusinessObject(creditCNAccount.getName())
                vatTransactions.setCreditCNAccount(account)
                creditCnAccountSelection.setSelectedItem(account)
            } else {
                vatTransactions.setCreditCNAccount(null)
                creditCnAccountSelection.setSelectedItem(null)
            }
        } else {
            debitAccountModel.setSelectedItem(null)
            creditAccountModel.setSelectedItem(null)
            debitCnAccountModel.setSelectedItem(null)
            creditCnAccountModel.setSelectedItem(null)
        }
    }
}
