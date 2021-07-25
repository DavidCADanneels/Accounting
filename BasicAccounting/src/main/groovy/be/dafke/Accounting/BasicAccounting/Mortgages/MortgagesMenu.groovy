package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.JMenu
import javax.swing.JMenuItem

import static java.util.ResourceBundle.getBundle

class MortgagesMenu extends JMenu {
    JMenuItem mortgage

    MortgagesMenu() {
        super(getBundle("Mortgage").getString("MORTGAGES"))
//        setMnemonic(KeyEvent.VK_M)
        mortgage = new JMenuItem("Mortgages")
        mortgage.addActionListener({ e ->
            MortgageGUI mortgageGUI = MortgageGUI.showMortgages()
            mortgageGUI.setLocation(getLocationOnScreen())
            mortgageGUI.visible = true
        })
        mortgage.enabled = false
        add(mortgage)
    }

    void refresh() {
        mortgage.enabled = Session.activeAccounting.mortgages!=null
    }
}
