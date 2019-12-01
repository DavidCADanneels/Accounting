package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.VATFields

import javax.swing.JMenu
import javax.swing.JMenuItem

import static java.util.ResourceBundle.getBundle

class VATMenu extends JMenu {
    private Accounting accounting
    private JMenuItem vatFieldsMenuItem
    private VATFields vatFields

    VATMenu() {
        super(getBundle("VAT").getString("VAT"))
//        setMnemonic(KeyEvent.VK_P)

        vatFieldsMenuItem = new JMenuItem(getBundle("VAT").getString("VAT_FIELDS"))
        vatFieldsMenuItem.addActionListener({ e ->
            VATFieldsGUI vatFieldsGUI = VATFieldsGUI.getInstance(vatFields, accounting)
            vatFieldsGUI.setLocation(getLocationOnScreen())
            vatFieldsGUI.setVisible(true)
        })
        vatFieldsMenuItem.setEnabled(false)

        add(vatFieldsMenuItem)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        vatFieldsMenuItem.setEnabled(accounting!=null)
        if(accounting!=null){
            vatFields = accounting.getVatFields()
        }
    }
}
