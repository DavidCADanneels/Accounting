package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * Created by ddanneels on 14/05/2017.
 */
public class AccountsListConfigPanel extends JPanel {
    private final ButtonConfigPanel buttonConfigPanel;
    private JRadioButton byType, singleAccount;
    private AccountsListSingleAccountSelectorPanel accountSelectorPanel;
    private JPanel north;
    private AccountsList accountsList;
    private JTextField taxType;

    private AccountsListAccountTypesFilterPanel accountTypesFilterPanel;

    public AccountsListConfigPanel(Accounts accounts, AccountTypes accountTypes, boolean left) {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Accounts"));
        taxType = new JTextField(20);
        taxType.setEditable(false);
        ButtonGroup group = new ButtonGroup();
        byType = new JRadioButton("select by type:", true);
        singleAccount = new JRadioButton("single account:", false);
        byType.addActionListener(e -> refresh());
        singleAccount.addActionListener(e -> refresh());
        group.add(byType);
        group.add(singleAccount);
        north = new JPanel();
        north.setLayout(new GridLayout(2,0));
        north.add(new JLabel("VatType:"));
        north.add(taxType);
        north.add(byType);
        north.add(singleAccount);

        accountsList = new AccountsList();
        accountTypesFilterPanel = new AccountsListAccountTypesFilterPanel(accountTypes, left);
        accountSelectorPanel = new AccountsListSingleAccountSelectorPanel(accounts,accountTypes);
        add(north,BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        center.add(accountSelectorPanel, BorderLayout.NORTH);
        center.add(accountTypesFilterPanel, BorderLayout.CENTER);
        add(center,BorderLayout.CENTER);
//
        buttonConfigPanel = new ButtonConfigPanel();
        add(buttonConfigPanel, BorderLayout.SOUTH);
//        refresh();
    }




    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
        buttonConfigPanel.setAccountsList(accountsList);
        singleAccount.setSelected(accountsList.isSingleAccount());
        accountTypesFilterPanel.setAccountsList(accountsList);
        accountSelectorPanel.setAccountsList(accountsList);
    }

    public void refresh(){
        boolean singleAccountSelected = singleAccount.isSelected();
        accountsList.setSingleAccount(singleAccountSelected);
        accountSelectorPanel.setEnabled(singleAccountSelected);
        accountSelectorPanel.refresh();

        boolean byTypeSelected = byType.isSelected();
        accountTypesFilterPanel.setEnabled(byTypeSelected);
        accountTypesFilterPanel.refresh();
    }

    public void setJournalType(JournalType journalType) {
        accountTypesFilterPanel.setJournalType(journalType);
    }

    public void setVatType(VATTransaction.VATType vatType){
        taxType.setText(vatType==null?"":vatType.toString());
    }
}
