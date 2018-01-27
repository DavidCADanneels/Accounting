package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.VATFields;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class VATMenu extends JMenu {
    private Accounting accounting;
    private JMenuItem vatFieldsMenuItem;
    private JMenuItem vatTransactionsMenuItem;
    private VATFields vatFields;
    private VATTransactions vatTransactions;
    private Contact declarant;

    public VATMenu() {
        super(getBundle("VAT").getString("VAT"));
//        setMnemonic(KeyEvent.VK_P);

        vatFieldsMenuItem = new JMenuItem(getBundle("VAT").getString("VAT_FIELDS"));
        vatFieldsMenuItem.addActionListener(e -> VATFieldsGUI.getInstance(vatFields, accounting).setVisible(true));
        vatFieldsMenuItem.setEnabled(false);

        vatTransactionsMenuItem = new JMenuItem(getBundle("VAT").getString("VAT_TRANSACTIONS"));
        vatTransactionsMenuItem.addActionListener(e -> VATTransactionsGUI.getInstance(vatTransactions).setVisible(true));
        vatTransactionsMenuItem.setEnabled(false);

        add(vatFieldsMenuItem);
        add(vatTransactionsMenuItem);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        vatFieldsMenuItem.setEnabled(accounting!=null);
        vatTransactionsMenuItem.setEnabled(accounting!=null);
        if(accounting!=null){
            vatFields = accounting.getVatFields();
            vatTransactions = accounting.getVatTransactions();
            declarant = accounting.getCompanyContact();
        }
    }
}
