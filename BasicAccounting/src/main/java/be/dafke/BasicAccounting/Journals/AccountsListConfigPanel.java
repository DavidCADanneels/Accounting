package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountDataListModel;
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
    private AccountSelectorPanel2 accountSelectorPanel;
    private JPanel north;
    private AccountsList accountsList;
    private AccountTypesFilterPanel2 accountTypesFilterPanel;

    public AccountsListConfigPanel(Accounts accounts, AccountTypes accountTypes, String title) {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), title));
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

        AccountDataListModel model = new AccountDataListModel();
        model.setAccounts(accounts);
        model.setAccountTypes(accountTypes.getBusinessObjects());

        accountsList = new AccountsList(accounts);
        accountTypesFilterPanel = new AccountTypesFilterPanel2(accountsList);
        accountTypesFilterPanel.setAccountTypes(accountTypes);
        accountSelectorPanel = new AccountSelectorPanel2(accountsList, accounts,accountTypes);
        add(north,BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        center.add(accountSelectorPanel, BorderLayout.NORTH);
        center.add(accountTypesFilterPanel, BorderLayout.CENTER);
        add(center,BorderLayout.CENTER);
//        refresh();
    }

    public void refresh(){
        if(singleAccount.isSelected()){
            accountsList.setSingleAccount(true);
            accountsList.setAccount(accountSelectorPanel.getSelection());
            accountTypesFilterPanel.setVisible(false);
            accountSelectorPanel.setVisible(true);
        } else if(byType.isSelected()){
            accountsList.setSingleAccount(false);
            accountTypesFilterPanel.setVisible(true);
            accountSelectorPanel.setVisible(false);
        }
    }

}
