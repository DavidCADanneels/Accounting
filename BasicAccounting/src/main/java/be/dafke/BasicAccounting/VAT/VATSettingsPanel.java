package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class VATSettingsPanel extends JPanel {
    private Accounting accounting;
    private final JComboBox<Account> debitAccountSelection, creditAccountSelection, debitCnAccountSelection, creditCnAccountSelection;
    private final DefaultComboBoxModel<Account> debitAccountModel, creditAccountModel, debitCnAccountModel, creditCnAccountModel;

    public VATSettingsPanel(Accounting accounting) {
        this.accounting = accounting;
        debitAccountModel = new DefaultComboBoxModel<>();
        creditAccountModel = new DefaultComboBoxModel<>();
        debitCnAccountModel = new DefaultComboBoxModel<>();
        creditCnAccountModel = new DefaultComboBoxModel<>();
        accounting.getAccounts().getBusinessObjects().forEach(account -> {
            debitAccountModel.addElement(account);
            creditAccountModel.addElement(account);
            debitCnAccountModel.addElement(account);
            creditCnAccountModel.addElement(account);
        });

        debitAccountSelection = new JComboBox<>(debitAccountModel);
        creditAccountSelection = new JComboBox<>(creditAccountModel);
        debitCnAccountSelection = new JComboBox<>(debitCnAccountModel);
        creditCnAccountSelection = new JComboBox<>(creditCnAccountModel);

        VATTransactions vatTransactions = accounting.getVatTransactions();
        Account debitAccount = vatTransactions.getDebitAccount();
        Account creditAccount = vatTransactions.getCreditAccount();
        Account debitCNAccount = vatTransactions.getDebitCNAccount();
        Account creditCNAccount = vatTransactions.getCreditCNAccount();

        debitAccountSelection.setSelectedItem(debitAccount);
        debitAccountSelection.addActionListener(e -> updateSelectedDebitAccount());
        debitAccountSelection.setEnabled(accounting.isVatAccounting());

        creditAccountSelection.setSelectedItem(creditAccount);
        creditAccountSelection.addActionListener(e -> updateSelectedCreditAccount());
        creditAccountSelection.setEnabled(accounting.isVatAccounting());

        debitCnAccountSelection.setSelectedItem(debitCNAccount);
        debitCnAccountSelection.addActionListener(e -> updateSelectedDebitCnAccount());
        debitCnAccountSelection.setEnabled(accounting.isVatAccounting());

        creditCnAccountSelection.setSelectedItem(creditCNAccount);
        creditCnAccountSelection.addActionListener(e -> updateSelectedCreditCnAccount());
        creditCnAccountSelection.setEnabled(accounting.isVatAccounting());


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Debit Account"));
        panel1.add(debitAccountSelection);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Credit Account"));
        panel2.add(creditAccountSelection);

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("Debit CN Account"));
        panel3.add(debitCnAccountSelection);

        JPanel panel4 = new JPanel();
        panel4.add(new JLabel("Credit CN Account"));
        panel4.add(creditCnAccountSelection);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);

        add(panel);
    }

    public void updateSelectedDebitAccount() {
        Account account = (Account) debitAccountSelection.getSelectedItem();
        VATTransactions vatTransactions = accounting.getVatTransactions();
        vatTransactions.setDebitAccount(account);
    }

    public void updateSelectedCreditAccount() {
        Account account = (Account) creditAccountSelection.getSelectedItem();
        VATTransactions vatTransactions = accounting.getVatTransactions();
        vatTransactions.setCreditAccount(account);
    }

    public void updateSelectedDebitCnAccount() {
        Account account = (Account) debitCnAccountSelection.getSelectedItem();
        VATTransactions vatTransactions = accounting.getVatTransactions();
        vatTransactions.setDebitCNAccount(account);
    }

    public void updateSelectedCreditCnAccount() {
        Account account = (Account) creditCnAccountSelection.getSelectedItem();
        VATTransactions vatTransactions = accounting.getVatTransactions();
        vatTransactions.setCreditCNAccount(account);
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        debitAccountSelection.setEnabled(enabled);
        creditAccountSelection.setEnabled(enabled);
        debitCnAccountSelection.setEnabled(enabled);
        creditCnAccountSelection.setEnabled(enabled);
        if(!enabled){
            debitAccountSelection.setSelectedItem(null);
            creditAccountSelection.setSelectedItem(null);
            debitCnAccountSelection.setSelectedItem(null);
            creditCnAccountSelection.setSelectedItem(null);
            updateSelectedDebitAccount();
            updateSelectedCreditAccount();
            updateSelectedDebitCnAccount();
            updateSelectedCreditCnAccount();
        }
    }
}
