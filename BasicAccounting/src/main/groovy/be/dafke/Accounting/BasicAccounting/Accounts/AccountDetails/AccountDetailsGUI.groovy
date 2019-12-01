package be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journals

import javax.swing.*
import java.awt.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

import static java.util.ResourceBundle.getBundle

class AccountDetailsGUI extends JFrame implements WindowListener {
    private static HashMap<Account,AccountDetailsGUI> accountDetailsMap = new HashMap<>()
    private final AccountDetailsPanel accountDetailsPanel

    private AccountDetailsGUI(Point location, Account account, Journals journals) {
        super(getBundle("Accounting").getString("ACCOUNT_DETAILS") + account.getName())
        accountDetailsPanel = new AccountDetailsPanel(account, journals)
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
        setLocation(location)
        setContentPane(accountDetailsPanel)
        pack()
    }

    static AccountDetailsGUI getAccountDetails(Point location, Account account, Journals journals){
        AccountDetailsGUI accountDetailsGUI = accountDetailsMap.get(account)
        if(accountDetailsGUI ==null){
            accountDetailsGUI = new AccountDetailsGUI(location, account, journals)
            accountDetailsMap.put(account, accountDetailsGUI)
            Main.addFrame(accountDetailsGUI)
        }
        accountDetailsGUI.setVisible(true)
        accountDetailsGUI
    }

    void selectObject(Booking object){
        accountDetailsPanel.selectObject(object)
    }

    void windowClosing(WindowEvent we) {
        accountDetailsPanel.closePopups()
    }
    void windowOpened(WindowEvent e) {}
    void windowClosed(WindowEvent e) {}
    void windowIconified(WindowEvent e) {}
    void windowDeiconified(WindowEvent e) {}
    void windowActivated(WindowEvent e) {}
    void windowDeactivated(WindowEvent e) {}

    static void fireAccountDataChangedForAll(Account account) {
        AccountDetailsGUI accountDetailsGUI = accountDetailsMap.get(account)
        if(accountDetailsGUI !=null) {
            accountDetailsGUI.fireAccountDataChanged()
        }
    }

    void fireAccountDataChanged() {
        accountDetailsPanel.fireAccountDataChanged()
    }
}