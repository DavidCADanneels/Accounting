package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessActions.AccountingActions;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements RefreshableComponent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JMenu file;
    private final JMenuItem startNew;
    private List<JMenuItem> itemsToRefresh;
    private boolean active = false;
    private Accountings accountings;
    private AccountingPanelInterface parent;

    public AccountingMenuBar(final Accountings accountings) {
        this.accountings = accountings;

        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(e -> AccountingActions.newAccounting(accountings));
        add(file);

        itemsToRefresh = new ArrayList<>();
    }

    public void addRefreshableMenuItem(JMenuItem item){
        itemsToRefresh.add(item);
    }

    public void refresh(){
        for(JMenuItem item:itemsToRefresh){
            item.setEnabled(active);
        }
    }

    public void setParent(AccountingPanelInterface parent) {
        this.parent = parent;
    }

    public void setAccounting(final Accounting accounting) {
        active = accounting!=null;
        file.removeAll();
        file.add(startNew);
        accountings.getBusinessObjects().stream()
                .filter(acc -> acc != accounting)
                .forEach(acc -> {
            JMenuItem item = new JMenuItem(acc.toString());
            item.addActionListener(e -> {
                accountings.setCurrentObject(acc);
                parent.setAccounting(acc);
                ComponentMap.refreshAllFrames();
            });
            file.add(item);
        });
        refresh();
    }
}
