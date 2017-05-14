package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountSelectorPanel;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;

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
    private AccountSelectorPanel accountSelectorPanel;
    private JPanel north;

    public AccountsListConfigPanel(Accounts accounts, AccountTypes accountTypes, String title) {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), title));
        ButtonGroup group = new ButtonGroup();
        byType = new JRadioButton("select by type:", false);
        singleAccount = new JRadioButton("single account:", true);
        byType.addActionListener(e -> refresh());
        singleAccount.addActionListener(e -> refresh());
        group.add(byType);
        group.add(singleAccount);
        north = new JPanel();
        north.add(byType);
        north.add(singleAccount);
        accountSelectorPanel = new AccountSelectorPanel(accounts,accountTypes);
        refresh();
    }

    public void refresh(){
        removeAll();
        add(north,BorderLayout.NORTH);
        if(singleAccount.isSelected()){
            add(accountSelectorPanel, BorderLayout.CENTER);
        } else if(byType.isSelected()){
            add(new JPanel(), BorderLayout.CENTER);
        }
        repaint();
    }

}
