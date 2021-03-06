package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class TestBalanceGUI extends JFrame {
    static HashMap<Accounts,TestBalanceGUI> testBalanceMap = new HashMap<>()
    final TestBalancePanel testBalancePanel

    TestBalanceGUI(Accounting accounting, Accounts accounts) {
        super(getBundle("BusinessModel").getString("TESTBALANCE"))
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
        testBalancePanel = new TestBalancePanel(accounting, accounts)
        setContentPane(testBalancePanel)
        pack()
    }

    static TestBalanceGUI getInstance(Accounting accounting) {
        Accounts accounts = accounting.accounts
        TestBalanceGUI testBalanceGUI = testBalanceMap.get(accounts)
        if(testBalanceGUI ==null){
            testBalanceGUI = new TestBalanceGUI(accounting, accounts)
            testBalanceMap.put(accounts, testBalanceGUI)
            Main.addFrame(testBalanceGUI)
        }
        testBalanceGUI
    }

    static void fireAccountDataChangedForAll(){
        for (TestBalanceGUI testBalanceGUI :testBalanceMap.values()) {
            testBalanceGUI.fireAccountDataChanged()
        }
    }

    void fireAccountDataChanged() {
        testBalancePanel.fireAccountDataChanged()
    }
}
