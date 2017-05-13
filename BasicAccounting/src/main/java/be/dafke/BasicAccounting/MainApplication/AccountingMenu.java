package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountingMenu extends JMenu implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JMenuItem startNew;
    private JMenuItem settings;
    private Accountings accountings;
    private Accounting accounting;

    public AccountingMenu(final Accountings accountings) {
        super(getBundle("Accounting").getString("ACCOUNTING"));
        this.accountings = accountings;
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(e -> new NewAccountingPanel(accountings).setVisible(true));
        settings = new JMenuItem(getBundle("Accounting").getString("SETTINGS"));
        settings.addActionListener(this);
//        add(startNew);
    }

    public void setAccounting(final Accounting accounting) {
        removeAll();
        add(startNew);
        add(settings);
        addSeparator();
        this.accounting=accounting;

        accountings.getBusinessObjects().stream()
                .filter(acc -> acc != accounting)
                .forEach(acc -> {
                    JMenuItem item = new JMenuItem(acc.toString());
                    item.addActionListener(e -> {
                        Main.setAccounting(acc);
                    });
                    add(item);
                });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AccountingSettingsPanel.showPanel(accounting);
    }
}
