package be.dafke.Accounting.BasicAccounting.Mortgages

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Mortgages

import javax.swing.JMenu
import javax.swing.JMenuItem

import static java.util.ResourceBundle.getBundle

class MortgagesMenu extends JMenu {
    JMenuItem mortgage
    Mortgages mortgages
    Accounts accounts

    MortgagesMenu() {
        super(getBundle("Mortgage").getString("MORTGAGES"))
//        setMnemonic(KeyEvent.VK_M)
        mortgage = new JMenuItem("Mortgages")
        mortgage.addActionListener({ e ->
            MortgageGUI mortgageGUI = MortgageGUI.showMortgages(mortgages, accounts)
            mortgageGUI.setLocation(getLocationOnScreen())
            mortgageGUI.visible = true
        })
        mortgage.enabled = false
        add(mortgage)
    }

    void setAccounting(Accounting accounting) {
        setMortgages(accounting?accounting.mortgages:null)
        setAccounts(accounting?accounting.accounts:null)
    }
    void setAccounts(Accounts accounts) {
        this.accounts = accounts
    }
    void setMortgages(Mortgages mortgages) {
        this.mortgages=mortgages
        mortgage.enabled = mortgages!=null
    }
}
