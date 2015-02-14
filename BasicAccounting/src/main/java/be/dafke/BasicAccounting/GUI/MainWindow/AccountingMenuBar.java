package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.Actions.OpenAccountingActionListener;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.RefreshableComponent;

import javax.swing.*;
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
    private final ActionListener actionListener;
    private boolean active = false;

    public AccountingMenuBar(ActionListener actionListener) {
        this.actionListener = actionListener;

        file = new JMenu(getBundle("Accounting").getString("ACCOUNTING"));
        startNew = new JMenuItem(getBundle("Accounting").getString("NEW_ACCOUNTING"));
        startNew.addActionListener(actionListener);
        startNew.setActionCommand(AccountingActionListener.NEW_ACCOUNTING);
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

    public void setAccounting(Accounting accounting, Accountings accountings) {
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
