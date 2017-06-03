package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * Created by ddanneels on 14/05/2017.
 */
public class AccountsListConfigPanel extends JPanel {
    private JRadioButton byType, singleAccount;
    private AccountsListSingleAccountSelectorPanel accountSelectorPanel;
    private JPanel north;
    private AccountsList accountsList;
    private AccountsListAccountTypesFilterPanel accountTypesFilterPanel;

    public AccountsListConfigPanel(Accounts accounts, AccountTypes accountTypes) {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Accounts"));
        ButtonGroup group = new ButtonGroup();
        byType = new JRadioButton("select by type:", true);
        singleAccount = new JRadioButton("single account:", false);
        byType.addActionListener(e -> refresh());
        singleAccount.addActionListener(e -> refresh());
        group.add(byType);
        group.add(singleAccount);
        north = new JPanel();
        north.add(byType);
        north.add(singleAccount);

        accountsList = new AccountsList(accountTypes);
        accountTypesFilterPanel = new AccountsListAccountTypesFilterPanel(accountTypes);
        accountSelectorPanel = new AccountsListSingleAccountSelectorPanel(accounts,accountTypes);
        add(north,BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        center.add(accountSelectorPanel, BorderLayout.NORTH);
        center.add(accountTypesFilterPanel, BorderLayout.CENTER);
        add(center,BorderLayout.CENTER);
//        refresh();
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

}
