package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountSelectorPanel;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;

/**
 * Created by ddanneels on 14/05/2017.
 */
public class AccountsListConfigPanel extends JPanel {
    private JRadioButton byType, singleAccount;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private AccountSelectorPanel accountSelectorPanel;

    public AccountsListConfigPanel(Accounts accounts, AccountTypes accountTypes, String title) {
        this.accounts=accounts;
        this.accountTypes=accountTypes;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), title));
        ButtonGroup group = new ButtonGroup();
        byType = new JRadioButton("select by type:", false);
        singleAccount = new JRadioButton("single account:", true);
        byType.addActionListener(e -> refresh());
        singleAccount.addActionListener(e -> refresh());
        group.add(byType);
        group.add(singleAccount);
        accountSelectorPanel = new AccountSelectorPanel(accounts,accountTypes);
        refresh();
    }

    public void refresh(){
        removeAll();
        add(getByTypePanel(byType.isSelected()));
        add(getSingleAccountPanel(singleAccount.isSelected()));
//        revalidate();
        invalidate();
    }

    private JPanel getByTypePanel(boolean selected){
        JPanel panel = new JPanel();
        panel.add(byType);
        if(selected){
            // add more
        }

        return panel;
    }

    private JPanel getSingleAccountPanel(boolean selected){
        JPanel panel = new JPanel();
        panel.add(singleAccount);
        if(selected){
            panel.add(accountSelectorPanel);
        }
        return panel;
    }
}
