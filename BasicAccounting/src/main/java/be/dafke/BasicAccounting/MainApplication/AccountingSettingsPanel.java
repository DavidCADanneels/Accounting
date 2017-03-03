package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingSettingsPanel extends JFrame {
    public static final String title = getBundle("Accounting").getString("SETTINGS");
    public static final String VAT = getBundle("VAT").getString("VAT");
    public static final String CONTACTS = getBundle("Contacts").getString("CONTACTS");
    public static final String PROJECTS = getBundle("Projects").getString("PROJECTS");
    public static final String MORTGAGES = getBundle("Mortgage").getString("MORTGAGES");
    private JCheckBox vatAccounting;
    private JCheckBox contacts;
    private JCheckBox projects;
    private JCheckBox mortgages;
    private Accounting accounting;
    private static HashMap<Accounting,AccountingSettingsPanel> accountingSettingsMap = new HashMap<>();

    private AccountingSettingsPanel(Accounting accounting) {
        super(accounting.getName() + " / " + title);
        this.accounting = accounting;
        JPanel center = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(center);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPanel);
        setAccounting(accounting);
        setActions();
        pack();
    }

    public static void showPanel(Accounting accounting){
        AccountingSettingsPanel accountingSettingsPanel = accountingSettingsMap.get(accounting);
        if(accountingSettingsPanel == null){
            accountingSettingsPanel = new AccountingSettingsPanel(accounting);
            accountingSettingsMap.put(accounting,accountingSettingsPanel);
        }
        accountingSettingsPanel.setVisible(true);
    }

    private void setActions() {
        vatAccounting.addActionListener(e -> {
            accounting.setVatAccounting(vatAccounting.isSelected());
        });
        contacts.addActionListener(e -> {
            accounting.setContactsAccounting(contacts.isSelected());
        });
        projects.addActionListener(e -> {
            accounting.setProjectsAccounting(projects.isSelected());
        });
        mortgages.addActionListener(e -> {
            accounting.setMortgagesAccounting(mortgages.isSelected());
        });
    }

    private void setAccounting(Accounting accounting) {
        vatAccounting.setSelected(accounting.isVatAccounting());
        contacts.setSelected(accounting.isContactsAccounting());
        projects.setSelected(accounting.isProjectsAccounting());
        mortgages.setSelected(accounting.isMortgagesAccounting());
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();

        vatAccounting = new JCheckBox(VAT);
        contacts = new JCheckBox(CONTACTS);
        projects = new JCheckBox(PROJECTS);
        mortgages = new JCheckBox(MORTGAGES);

        panel.add(vatAccounting);
        panel.add(contacts);
        panel.add(projects);
        panel.add(mortgages);

        return panel;
    }
}
