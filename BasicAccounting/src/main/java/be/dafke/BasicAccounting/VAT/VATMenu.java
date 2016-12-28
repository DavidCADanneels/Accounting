package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class VATMenu extends JMenu {
    private static JMenuItem taxOverview;
    private static VATTransactions vatTransactions;

    public VATMenu() {
        super(getBundle("VAT").getString("VAT"));
        setMnemonic(KeyEvent.VK_P);
        taxOverview = new JMenuItem(getBundle("VAT").getString("VAT_OVERVIEW"));
        taxOverview.addActionListener(e -> VATGUI.getInstance(vatTransactions).setVisible(true));
        taxOverview.setEnabled(false);
        add(taxOverview);
    }

    public static void setAccounting(Accounting accounting) {
        taxOverview.setEnabled(accounting!=null);
        if(accounting!=null){
            vatTransactions = accounting.getVatTransactions();
        }
    }
}
