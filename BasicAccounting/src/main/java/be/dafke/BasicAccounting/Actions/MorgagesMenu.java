package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.GUI.Mortgages.MortgageGUI;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.BusinessModel.Mortgages;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 26/12/2015.
 */
public class MorgagesMenu extends JMenu implements ActionListener {
    private JMenuItem mortgage;
    private Accountings accountings;

    public MorgagesMenu(Accountings accountings, AccountingMenuBar menuBar) {
        super(getBundle("Mortgage").getString("MORTGAGES"));
        this.accountings = accountings;
        setMnemonic(KeyEvent.VK_M);
        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(this);
        mortgage.setEnabled(false);
        add(mortgage);
        menuBar.addRefreshableMenuItem(mortgage);
    }

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        Mortgages mortgages = accounting.getMortgages();
        String key = accounting.toString() + Mortgages.MORTGAGES;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new MortgageGUI(accounting, mortgages);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
