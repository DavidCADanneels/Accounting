package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.NewAccountingActionListener;
import be.dafke.BasicAccounting.Actions.OpenAccountingActionListener;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
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

    public AccountingMenuBar(Accountings accountings) {
        this.accountings = accountings;

        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(new NewAccountingActionListener(accountings));
        add(file);

        itemsToRefresh = new ArrayList<JMenuItem>();
    }

    public void addRefreshableMenuItem(JMenuItem item){
        itemsToRefresh.add(item);
    }

    @Override
    public void refresh(){
        for(JMenuItem item:itemsToRefresh){
            item.setEnabled(active);
        }
    }

    public void setAccounting(Accounting accounting) {
        active = accounting!=null;
        file.removeAll();
        file.add(startNew);
        for(Accounting acc : accountings.getBusinessObjects()) {
            if(acc!=accounting){
                JMenuItem item = new JMenuItem(acc.toString());
                OpenAccountingActionListener openAccountingActionListener = new OpenAccountingActionListener(accountings, acc);
                item.addActionListener(openAccountingActionListener);
                file.add(item);
            }
        }
        refresh();
    }
}
