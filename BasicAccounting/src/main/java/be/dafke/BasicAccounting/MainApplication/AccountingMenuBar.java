package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessActions.AccountingActions;
import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableComponent;

import javax.swing.*;
import java.util.ArrayList;

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
    private final Accountings accountings;
    private ArrayList<AccountingListener> accountingListeners = new ArrayList<>();

    public AccountingMenuBar(final Accountings accountings) {
        this.accountings = accountings;
        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(e -> AccountingActions.newAccounting(accountings));
        add(file);
    }

    public void setAccounting(final Accounting accounting) {
        file.removeAll();
        file.add(startNew);
        accountings.getBusinessObjects().stream()
                .filter(acc -> acc != accounting)
                .forEach(acc -> {
                    JMenuItem item = new JMenuItem(acc.toString());
                    item.addActionListener(e -> {
                        accountings.setCurrentObject(acc);
                        for (AccountingListener accountingListener : accountingListeners) {
                            accountingListener.setAccounting(acc);
                        }
                    });
                    file.add(item);
                });
        for(AccountingListener accountingListener:accountingListeners){
            accountingListener.setAccounting(accounting);
        }
    }

    public void setAccountingListeners(ArrayList<AccountingListener> accountingListeners) {
        this.accountingListeners = accountingListeners;
    }
}
