package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.BalancesIO

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class BalancesMenu extends JMenu {
    JMenuItem manage
    JMenuItem pdfGeneration
    Journals journals
    Accounts accounts
    Balances balances
    Accounting accounting
    AccountTypes accountTypes

    BalancesMenu(){
        super(getBundle("BusinessModel").getString("BALANCES"))
        manage = new JMenuItem(getBundle("BusinessModel").getString("MANAGE_BALANCES"))
        manage.addActionListener({ e ->
            BalancesManagementGUI balancesManagementGUI = BalancesManagementGUI.getInstance(balances, accounts, accountTypes)
            balancesManagementGUI.setLocation(getLocationOnScreen())
            balancesManagementGUI.visible = true
        })
        pdfGeneration = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"))
        pdfGeneration.addActionListener({ e -> BalancesIO.writeBalancePdfFiles(accounting) })
        add(manage)
        add(pdfGeneration)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        journals = accounting?accounting.journals:null
        accounts = accounting?accounting.accounts:null
        balances = accounting?accounting.balances:null
        accountTypes = accounting?accounting.accountTypes:null

        fireBalancesChanged()
    }

    void fireBalancesChanged(){
        removeAll()
        if(balances) {
            balances.businessObjects.forEach({ balance ->
                String name = balance.name
                JMenuItem item = new JMenuItem(name)
                item.addActionListener({ e ->
                    BalanceGUI balanceGUI = BalanceGUI.getBalance(accounting, balances.getBusinessObject(name))
                    balanceGUI.setLocation(getLocationOnScreen())
                    balanceGUI.visible = true
                })
                add(item)
            })
            add(manage)
            add(pdfGeneration)
        }
    }
}
