package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.AccountingActions;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.RefreshableComponent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public AccountingMenuBar(final Accountings accountings) {
        this.accountings = accountings;

        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccountingActions.newAccounting(accountings);
            }
        });
        add(file);

        itemsToRefresh = new ArrayList<JMenuItem>();
    }

    public void addRefreshableMenuItem(JMenuItem item){
        itemsToRefresh.add(item);
    }

    public void refresh(){
        for(JMenuItem item:itemsToRefresh){
            item.setEnabled(active);
        }
    }

    public void setAccounting(final Accounting accounting) {
        active = accounting!=null;
        file.removeAll();
        file.add(startNew);
        for(final Accounting acc : accountings.getBusinessObjects()) {
            if(acc!=accounting){
                JMenuItem item = new JMenuItem(acc.toString());
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        AccountingActions.openAccounting(accountings, acc);
                    }
                });
                file.add(item);
            }
        }
        refresh();
    }
}
