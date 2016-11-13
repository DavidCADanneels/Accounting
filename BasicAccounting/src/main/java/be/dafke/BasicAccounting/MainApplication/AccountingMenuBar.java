package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableComponent;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements RefreshableComponent, AccountingListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JMenu file;
    private final JMenuItem startNew;
    private final Accountings accountings;

    public AccountingMenuBar(final Accountings accountings) {
        this.accountings = accountings;
        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(e -> Main.newAccounting(accountings));
        add(file);
    }

    @Override
    public void setAccounting(final Accounting accounting) {
        file.removeAll();
        file.add(startNew);
        accountings.getBusinessObjects().stream()
                .filter(acc -> acc != accounting)
                .forEach(acc -> {
                    JMenuItem item = new JMenuItem(acc.toString());
                    item.addActionListener(e -> {
                        Main.setAccounting(acc);
                    });
                    file.add(item);
                });
    }
}
