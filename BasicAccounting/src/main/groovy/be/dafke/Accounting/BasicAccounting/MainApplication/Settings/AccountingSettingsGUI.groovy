package be.dafke.Accounting.BasicAccounting.MainApplication.Settings

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.JFrame
import javax.swing.JTabbedPane

import static java.util.ResourceBundle.getBundle

class AccountingSettingsGUI extends JFrame {
    static final String title = getBundle("Accounting").getString("SETTINGS")
    final JTabbedPane tabbedPane
    static AccountingSettingsGUI accountingSettingsPanel = null

    AccountingSettingsGUI(Accounting accounting, AccountingCopyPanel copyPanel) {
        super(accounting.name + " / " + title)
        tabbedPane = new AccountingSettingsPanel(accounting, copyPanel)
        setContentPane(tabbedPane)
        pack()
    }

    static AccountingSettingsGUI showPanel(){
        Accounting accounting = Session.activeAccounting
        if(accountingSettingsPanel == null){
            accountingSettingsPanel = new AccountingSettingsGUI(accounting, null)
            Main.addFrame(accountingSettingsPanel)
        }
        accountingSettingsPanel
    }
}
