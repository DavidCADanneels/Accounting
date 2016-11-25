package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgages;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 26/12/2015.
 */
public class MorgagesMenu extends JMenu {
    private static JMenuItem mortgage;
    private static Mortgages mortgages;
    private static Accounts accounts;

    public MorgagesMenu() {
        super(getBundle("Mortgage").getString("MORTGAGES"));
        setMnemonic(KeyEvent.VK_M);
        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(e -> MortgageGUI.showMortgages(mortgages, accounts).setVisible(true));
        mortgage.setEnabled(false);
        add(mortgage);
    }

    public static void setAccounting(Accounting accounting) {
        setMortgages(accounting==null?null:accounting.getMortgages());
        setAccounts(accounting==null?null:accounting.getAccounts());
    }
    public static void setAccounts(Accounts accounts) {
        MorgagesMenu.accounts = accounts;
    }
    public static void setMortgages(Mortgages mortgages) {
        MorgagesMenu.mortgages=mortgages;
        mortgage.setEnabled(mortgages!=null);
    }
}
