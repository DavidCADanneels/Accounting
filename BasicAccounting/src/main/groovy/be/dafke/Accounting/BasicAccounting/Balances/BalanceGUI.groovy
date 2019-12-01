package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Balance

import javax.swing.*

class BalanceGUI extends JFrame {
    private static HashMap<Balance,BalanceGUI> otherBalanceMap = new HashMap<>()
    private final BalancePanel balancePanel

    private BalanceGUI(Accounting accounting, Balance balance) {
        super(balance.getName())
        balancePanel = new BalancePanel(accounting, balance)
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
        setContentPane(balancePanel)
        pack()

    }

    static BalanceGUI getBalance(Accounting accounting, Balance balance) {
        BalanceGUI balanceGUI = otherBalanceMap.get(balance)
        if(balanceGUI==null){
            balanceGUI = new BalanceGUI(accounting, balance)
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
        balancePanel.fireAccountDataChanged()
    }
}
