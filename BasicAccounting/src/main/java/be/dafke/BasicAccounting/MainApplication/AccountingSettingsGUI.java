package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingSettingsGUI extends JFrame {
    public static final String title = getBundle("Accounting").getString("SETTINGS");
    private final JTabbedPane tabbedPane;
    private static HashMap<Accounting,AccountingSettingsGUI> accountingSettingsMap = new HashMap<>();

    public AccountingSettingsGUI(Accounting accounting, JPanel copyPanel) {
        super(accounting.getName() + " / " + title);
        tabbedPane = new AccountingSettingsPanel(accounting, copyPanel);
        setContentPane(tabbedPane);
        pack();
    }

    public static AccountingSettingsGUI showPanel(Accounting accounting){
        AccountingSettingsGUI accountingSettingsPanel = accountingSettingsMap.get(accounting);
        if(accountingSettingsPanel == null){
            accountingSettingsPanel = new AccountingSettingsGUI(accounting, null);
            accountingSettingsMap.put(accounting,accountingSettingsPanel);
            Main.addFrame(accountingSettingsPanel);
        }
        return accountingSettingsPanel;
    }


}
