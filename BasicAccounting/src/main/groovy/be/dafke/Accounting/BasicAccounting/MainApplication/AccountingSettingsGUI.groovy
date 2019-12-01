package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.JFrame
import javax.swing.JTabbedPane

import static java.util.ResourceBundle.getBundle

class AccountingSettingsGUI extends JFrame {
    static final String title = getBundle("Accounting").getString("SETTINGS")
    private final JTabbedPane tabbedPane
    private static HashMap<Accounting,AccountingSettingsGUI> accountingSettingsMap = new HashMap<>()

    AccountingSettingsGUI(Accounting accounting, AccountingCopyPanel copyPanel) {
        super(accounting.getName() + " / " + title)
        tabbedPane = new AccountingSettingsPanel(accounting, copyPanel)
        setContentPane(tabbedPane)
        pack()
    }

    static AccountingSettingsGUI showPanel(Accounting accounting){
        AccountingSettingsGUI accountingSettingsPanel = accountingSettingsMap.get(accounting)
        if(accountingSettingsPanel == null){
            accountingSettingsPanel = new AccountingSettingsGUI(accounting, null)
            accountingSettingsMap.put(accounting,accountingSettingsPanel)
            Main.addFrame(accountingSettingsPanel)
        }
        accountingSettingsPanel
    }
}
