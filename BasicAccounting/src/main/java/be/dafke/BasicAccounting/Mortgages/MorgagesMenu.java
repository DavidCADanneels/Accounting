package be.dafke.BasicAccounting.Mortgages;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Mortgages;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class MorgagesMenu extends JMenu {
    private JMenuItem mortgage;
    private Mortgages mortgages;
    private Accounts accounts;

    public MorgagesMenu() {
        super(getBundle("Mortgage").getString("MORTGAGES"));
//        setMnemonic(KeyEvent.VK_M);
        mortgage = new JMenuItem("Mortgages");
        mortgage.addActionListener(e -> {
            MortgageGUI mortgageGUI = MortgageGUI.showMortgages(mortgages, accounts);
            mortgageGUI.setLocation(getLocationOnScreen());
            mortgageGUI.setVisible(true);
        });
        mortgage.setEnabled(false);
        add(mortgage);
    }

    public void setAccounting(Accounting accounting) {
        setMortgages(accounting==null?null:accounting.getMortgages());
        setAccounts(accounting==null?null:accounting.getAccounts());
    }
    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }
    public void setMortgages(Mortgages mortgages) {
        this.mortgages=mortgages;
        mortgage.setEnabled(mortgages!=null);
    }
}
