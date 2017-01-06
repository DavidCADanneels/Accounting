package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.VATFields;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class VATMenu extends JMenu {
    private JMenuItem taxOverview;
    private VATFields vatFields;

    public VATMenu() {
        super(getBundle("VAT").getString("VAT"));
        setMnemonic(KeyEvent.VK_P);
        taxOverview = new JMenuItem(getBundle("VAT").getString("VAT_OVERVIEW"));
        taxOverview.addActionListener(e -> VATGUI.getInstance(vatFields).setVisible(true));
        taxOverview.setEnabled(false);
        add(taxOverview);
    }

    public void setAccounting(Accounting accounting) {
        taxOverview.setEnabled(accounting!=null);
        if(accounting!=null){
            vatFields = accounting.getVatFields();
        }
    }
}
