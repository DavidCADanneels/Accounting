package be.dafke.BasicAccounting.VAT;

import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.Accounting.BusinessModel.Contact;
import be.dafke.Accounting.BusinessModel.VATFields;
import be.dafke.Accounting.BusinessModel.VATTransactions;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class VATMenu extends JMenu {
    private Accounting accounting;
    private JMenuItem vatFieldsMenuItem;
    private VATFields vatFields;

    public VATMenu() {
        super(getBundle("VAT").getString("VAT"));
//        setMnemonic(KeyEvent.VK_P);

        vatFieldsMenuItem = new JMenuItem(getBundle("VAT").getString("VAT_FIELDS"));
        vatFieldsMenuItem.addActionListener(e -> {
            VATFieldsGUI vatFieldsGUI = VATFieldsGUI.getInstance(vatFields, accounting);
            vatFieldsGUI.setLocation(getLocationOnScreen());
            vatFieldsGUI.setVisible(true);
        });
        vatFieldsMenuItem.setEnabled(false);

        add(vatFieldsMenuItem);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        vatFieldsMenuItem.setEnabled(accounting!=null);
        if(accounting!=null){
            vatFields = accounting.getVatFields();
        }
    }
}
