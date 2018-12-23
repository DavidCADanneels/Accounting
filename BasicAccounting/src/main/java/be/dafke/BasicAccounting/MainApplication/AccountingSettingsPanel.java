package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Contacts.ContactsSettingsPanel;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.awt.BorderLayout.*;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingSettingsPanel extends JFrame {
    public static final String title = getBundle("Accounting").getString("SETTINGS");
    public static final String TRADE = getBundle("Accounting").getString("TRADE");
    public static final String VAT = getBundle("VAT").getString("VAT");
    public static final String CONTACTS = getBundle("Contacts").getString("CONTACTS");
    public static final String PROJECTS = getBundle("Projects").getString("PROJECTS");
    public static final String DELIVEROO = "Deliveroo";
    public static final String MORTGAGES = getBundle("Mortgage").getString("MORTGAGES");
    private final JTabbedPane tabbedPane;
    private JCheckBox vatAccounting;
    private JCheckBox tradeAccounting;
    private JCheckBox contacts;
    private JCheckBox projects;
    private JCheckBox deliveroo;
    private JCheckBox mortgages;
    private Accounting accounting;
    private static HashMap<Accounting,AccountingSettingsPanel> accountingSettingsMap = new HashMap<>();
    private JPanel contactsTab;

    private AccountingSettingsPanel(Accounting accounting) {
        super(accounting.getName() + " / " + title);
        this.accounting = accounting;

        contactsTab = new ContactsSettingsPanel(accounting);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Modules",createCenterPanel());

        setContentPane(tabbedPane);
        pack();
    }

    public static AccountingSettingsPanel showPanel(Accounting accounting){
        AccountingSettingsPanel accountingSettingsPanel = accountingSettingsMap.get(accounting);
        if(accountingSettingsPanel == null){
            accountingSettingsPanel = new AccountingSettingsPanel(accounting);
            accountingSettingsMap.put(accounting,accountingSettingsPanel);
            Main.addFrame(accountingSettingsPanel);
        }
        return accountingSettingsPanel;
    }

    private void updateDeliverooSetting(){
        boolean deliverooSelected = deliveroo.isSelected();
        if(deliverooSelected){
            vatAccounting.setSelected(true);
            updateVatSetting();
        }
        accounting.setDeliverooAccounting(deliverooSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateVatSetting(){
        boolean vatAccountingSelected = vatAccounting.isSelected();
        if(vatAccountingSelected) {
            contacts.setSelected(true);
            updateContactSetting();
        } else {
            tradeAccounting.setSelected(false);
            updateTradeSetting();
            deliveroo.setSelected(false);
            updateDeliverooSetting();
        }
        accounting.setVatAccounting(vatAccountingSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateContactSetting(){
        boolean contactsSelected = contacts.isSelected();
        contactsTab.setEnabled(contactsSelected);
        if(!contactsSelected){
            accounting.setCompanyContact(null);
            vatAccounting.setSelected(false);
            updateVatSetting();
            int indexOfComponent = tabbedPane.indexOfComponent(contactsTab);
            if(indexOfComponent!=-1) {
                tabbedPane.removeTabAt(indexOfComponent);
            }
        } else {
            tabbedPane.insertTab("Contacts", null, contactsTab, "", 1);
        }
        accounting.setContactsAccounting(contactsSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateTradeSetting(){
        boolean tradeAccountingSelected = tradeAccounting.isSelected();
        if(tradeAccountingSelected){
            vatAccounting.setSelected(true);
            updateVatSetting();
        }
        accounting.setTradeAccounting(tradeAccountingSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateMortgageSetting(){
        accounting.setMortgagesAccounting(mortgages.isSelected());
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateProjectSetting(){
        accounting.setProjectsAccounting(projects.isSelected());
        Main.fireAccountingTypeChanged(accounting);
    }

    private JPanel createCenterPanel(){
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        projects = new JCheckBox(PROJECTS);
        mortgages = new JCheckBox(MORTGAGES);
        contacts = new JCheckBox(CONTACTS);
        vatAccounting = new JCheckBox(VAT);
        tradeAccounting = new JCheckBox(TRADE);
        deliveroo = new JCheckBox(DELIVEROO);

        projects.setSelected(accounting.isProjectsAccounting());
        mortgages.setSelected(accounting.isMortgagesAccounting());
        contacts.setSelected(accounting.isContactsAccounting());
        vatAccounting.setSelected(accounting.isVatAccounting());
        tradeAccounting.setSelected(accounting.isTradeAccounting());
        deliveroo.setSelected(accounting.isDeliverooAccounting());

        projects.addActionListener(e -> updateProjectSetting());
        mortgages.addActionListener(e -> updateMortgageSetting());
        contacts.addActionListener(e -> updateContactSetting());
        vatAccounting.addActionListener(e -> updateVatSetting());
        tradeAccounting.addActionListener(e -> updateTradeSetting());
        deliveroo.addActionListener(e -> updateDeliverooSetting());

        updateProjectSetting();
        updateMortgageSetting();
        updateContactSetting();
        updateVatSetting();
        updateTradeSetting();
        updateDeliverooSetting();

        panel.add(projects);
        panel.add(mortgages);
        panel.add(contacts);
        panel.add(vatAccounting);
        panel.add(tradeAccounting);
        panel.add(deliveroo);

        return panel;
    }
}
