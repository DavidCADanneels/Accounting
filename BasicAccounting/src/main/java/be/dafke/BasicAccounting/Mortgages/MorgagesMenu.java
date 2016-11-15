package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 26/12/2015.
 */
public class MorgagesMenu extends JMenu implements ActionListener, AccountingListener {
    private JMenuItem mortgage;
    private Mortgages mortgages;
    private Accounts accounts;

    public MorgagesMenu() {
        super(getBundle("Mortgage").getString("MORTGAGES"));
        setMnemonic(KeyEvent.VK_M);
        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(this);
        mortgage.setEnabled(false);
        add(mortgage);
    }

    public void actionPerformed(ActionEvent e) {
        String key = Mortgages.MORTGAGES + mortgages.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if(gui == null){
            gui = new MortgageGUI(mortgages, accounts);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    @Override
    public void setAccounting(Accounting accounting) {
        mortgages=accounting==null?null:accounting.getMortgages();
        mortgage.setEnabled(mortgages!=null);
        accounts=accounting==null?null:accounting.getAccounts();
    }
}
