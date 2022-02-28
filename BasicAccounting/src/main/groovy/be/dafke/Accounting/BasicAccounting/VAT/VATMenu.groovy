package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.JMenu
import javax.swing.JMenuItem

import static java.util.ResourceBundle.getBundle

class VATMenu extends JMenu {
    JMenuItem vatFieldsMenuItem

    VATMenu() {
        super(getBundle("VAT").getString("VAT"))
//        setMnemonic(KeyEvent.VK_P)

        vatFieldsMenuItem = new JMenuItem(getBundle("VAT").getString("VAT_FIELDS"))
        vatFieldsMenuItem.addActionListener({ e ->
            VATFieldsGUI vatFieldsGUI = VATFieldsGUI.getInstance()
            vatFieldsGUI.setLocation(getLocationOnScreen())
            vatFieldsGUI.visible = true
        })
        vatFieldsMenuItem.enabled = false

        add(vatFieldsMenuItem)
    }

    void refresh() {
        vatFieldsMenuItem.enabled = Session.activeAccounting.vatAccounting!=null
    }
}
