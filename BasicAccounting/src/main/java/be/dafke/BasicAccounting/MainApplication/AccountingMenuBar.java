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
public class AccountingMenuBar extends JMenuBar implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JMenu file;
    private JMenuItem startNew;
    private JMenuItem settings;
    private Accountings accountings;
    private Accounting accounting;

    public AccountingMenuBar(final Accountings accountings) {
        this.accountings = accountings;
        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(e -> new NewAccountingPanel(accountings).setVisible(true));
        settings = new JMenuItem(getBundle("Accounting").getString("SETTINGS"));
        settings.addActionListener(this);
//        file.add(startNew);
        add(file);
    }

    public void setAccounting(final Accounting accounting) {
        file.removeAll();
        file.add(startNew);
        file.add(settings);
        file.addSeparator();
        this.accounting=accounting;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        AccountingSettingsPanel.showPanel(accounting);
    }
}
