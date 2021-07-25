package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Balance

import javax.swing.*

class BalanceGUI extends JFrame {
    static HashMap<Balance,BalanceGUI> otherBalanceMap = new HashMap<>()
    final BalancePanel balancePanel

    BalanceGUI(Balance balance) {
        super(balance.name)
        balancePanel = new BalancePanel(balance, false, true)
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
        setContentPane(balancePanel)
        pack()

    }

    static BalanceGUI getBalance(Balance balance) {
        BalanceGUI balanceGUI = otherBalanceMap.get(balance)
        if(balanceGUI==null){
            balanceGUI = new BalanceGUI(balance)
            otherBalanceMap.put(balance,balanceGUI)
            Main.addFrame(balanceGUI)
        }
        balanceGUI
    }

    static void fireAccountDataChangedForAll(){
        for (BalanceGUI testBalance:otherBalanceMap.values()) {
            testBalance.fireAccountDataChanged()
        }
    }

    void fireAccountDataChanged() {
        balancePanel.fireTableDataChanged()
    }
}
