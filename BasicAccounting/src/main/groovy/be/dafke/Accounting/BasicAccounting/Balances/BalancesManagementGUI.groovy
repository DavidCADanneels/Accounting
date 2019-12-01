package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Balances

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class BalancesManagementGUI extends JFrame {
    private static final HashMap<Balances, BalancesManagementGUI> balancesManagementGuis = new HashMap<>()

    private BalancesManagementGUI(Balances balances, Accounts accounts, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("BALANCE_MANAGEMENT_TITLE"))
        setContentPane(new BalancesManagementPanel(balances, accounts, accountTypes))
        pack()
    }

    static BalancesManagementGUI getInstance(Balances balances, Accounts accounts, AccountTypes accountTypes) {
        BalancesManagementGUI gui = balancesManagementGuis.get(balances)
        if(gui == null){
            gui = new BalancesManagementGUI(balances, accounts, accountTypes)
            balancesManagementGuis.put(balances,gui)
            Main.addFrame(gui)
        }
        gui
    }
}