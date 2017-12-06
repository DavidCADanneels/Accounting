package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static be.dafke.BusinessModel.AccountsList.DEBIT;
import static be.dafke.BusinessModel.AccountsList.CREDIT;

/**
 * Created by ddanneels on 14/05/2017.
 */
public class AccountsListConfigPanel extends JPanel {
    private JRadioButton byType, singleAccount;
    private AccountsListSingleAccountSelectorPanel accountSelectorPanel;
    private JPanel north;
    private AccountsList accountsList;
    private JTextField taxType;
    private JCheckBox leftButton, rightButton;
    private JComboBox<String> leftActions, rightActions;
    private JTextField leftButtonLabel, rightButtonLabel;

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
        JPanel south = createButtonConfigPanel();
        add(south, BorderLayout.SOUTH);
//        refresh();
    }

    private JPanel createButtonConfigPanel() {
        JPanel panel = new JPanel(new GridLayout(0,1));

        leftButtonLabel = new JTextField(DEBIT);
        rightButtonLabel = new JTextField(CREDIT);

        leftButton = new JCheckBox("Left Button");
        rightButton = new JCheckBox("Right Button");

        leftButton.setSelected(true);
        rightButton.setSelected(true);

        leftActions = new JComboBox<>();
        rightActions = new JComboBox<>();

        leftActions.addItem(DEBIT);
        leftActions.addItem(CREDIT);

        rightActions.addItem(CREDIT);
        rightActions.addItem(DEBIT);

        leftActions.setSelectedItem(DEBIT);
        rightActions.setSelectedItem(CREDIT);

        leftActions.addActionListener(e -> updateLeftAction());
        rightActions.addActionListener(e -> updateRightAction());
        leftButtonLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newName = leftButtonLabel.getText();
                accountsList.setLeftButton(newName);
            }
        });
        rightButtonLabel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newName = rightButtonLabel.getText();
                accountsList.setRightButton(newName);
            }
        });

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel leftCenter = new JPanel(new GridLayout(0,2));
        JPanel rightCenter = new JPanel(new GridLayout(0,2));

        leftCenter.add(new JLabel("Action:"));
        leftCenter.add(leftActions);
        leftCenter.add(new JLabel("Label:"));
        leftCenter.add(leftButtonLabel);

        rightCenter.add(new JLabel("Action:"));
        rightCenter.add(rightActions);
        rightCenter.add(new JLabel("Label:"));
        rightCenter.add(rightButtonLabel);

        leftPanel.add(leftButton, BorderLayout.NORTH);
        leftPanel.add(leftCenter, BorderLayout.SOUTH);

        rightPanel.add(rightButton, BorderLayout.NORTH);
        rightPanel.add(rightCenter, BorderLayout.SOUTH);

        leftPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Left Button"));
        rightPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Right Button"));

        panel.add(leftPanel);
        panel.add(rightPanel);

        return panel;
    }

    private void updateRightAction() {
        String selectedItem = (String)rightActions.getSelectedItem();
        accountsList.setRightAction(DEBIT.equals(selectedItem));
    }

    private void updateLeftAction() {
        String selectedItem = (String)leftActions.getSelectedItem();
        accountsList.setLeftAction(DEBIT.equals(selectedItem));
    }


    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
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
