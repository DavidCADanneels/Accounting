package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Contacts.ContactsSettingsPanel;
import be.dafke.BasicAccounting.Deliveroo.DeliverooSettingsPanel;
import be.dafke.BasicAccounting.Trade.TradeSettingsPanel;
import be.dafke.BasicAccounting.VAT.VATSettingsPanel;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingSettingsPanel extends JTabbedPane {
    public static final String TRADE = getBundle("Accounting").getString("TRADE");
    public static final String VAT = getBundle("VAT").getString("VAT");
    public static final String CONTACTS = getBundle("Contacts").getString("CONTACTS");
    public static final String PROJECTS = getBundle("Projects").getString("PROJECTS");
    public static final String DELIVEROO = "Deliveroo";
    public static final String MORTGAGES = getBundle("Mortgage").getString("MORTGAGES");
    private JCheckBox vatAccounting;
    private JCheckBox tradeAccounting;
    private JCheckBox contacts;
    private JCheckBox projects;
    private JCheckBox deliveroo;
    private JCheckBox mortgages;
    private Accounting accounting;
    private JPanel contactsTab, vatTab, tradeTab, deliverooTab;
    private JPanel copyPanel;

    public AccountingSettingsPanel(Accounting accounting, JPanel copyPanel) {
        this.accounting = accounting;
        this.copyPanel = copyPanel;

        setTabPlacement(JTabbedPane.TOP);
        JComponent mainPanel = createCenterPanel();
        addTab("Modules", mainPanel);

        contactsTab = new ContactsSettingsPanel(accounting);
        vatTab = new VATSettingsPanel(accounting);
        tradeTab = new TradeSettingsPanel(accounting);
        deliverooTab = new DeliverooSettingsPanel(accounting);

        updateProjectSetting();
        updateMortgageSetting();
        updateContactSetting();
        updateVatSetting();
        updateTradeSetting();
        updateDeliverooSetting();
    }

    private void updateDeliverooSetting(){
        boolean deliverooSelected = deliveroo.isSelected();
        deliverooTab.setEnabled(deliverooSelected);
        if(deliverooSelected){
            vatAccounting.setSelected(true);
            updateVatSetting();
            int index = 3;
            if(tradeAccounting.isSelected()){
                index = 4;
            }
            insertTab("Deliveroo", null, deliverooTab, "", index);
        } else {
            int indexOfComponent = indexOfComponent(deliverooTab);
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent);
            }
        }
        accounting.setDeliverooAccounting(deliverooSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    public void setContactsSelected(boolean selected){
        contacts.setSelected(selected);
        updateContactSetting();
    }

    public void setVatSelected(boolean selected) {
        vatAccounting.setSelected(selected);
        updateVatSetting();
    }

    public void setDeliveooSelected(boolean selected) {
        deliveroo.setSelected(selected);
        updateDeliverooSetting();
    }

    public void setTradeSelected(boolean selected) {
        tradeAccounting.setSelected(selected);
        updateTradeSetting();
    }


    private void updateVatSetting(){
        boolean vatAccountingSelected = vatAccounting.isSelected();
        vatTab.setEnabled(vatAccountingSelected);
        if(vatAccountingSelected) {
            setContactsSelected(true);
//            contacts.setSelected(true);
//            updateContactSetting();
            insertTab("VAT", null, vatTab, "", 2);
        } else {
            setTradeSelected(false);
//            tradeAccounting.setSelected(false);
//            updateTradeSetting();
            setDeliveooSelected(false);
//            deliveroo.setSelected(false);
//            updateDeliverooSetting();
            int indexOfComponent = indexOfComponent(vatTab);
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent);
            }
        }
        accounting.setVatAccounting(vatAccountingSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateContactSetting(){
        boolean contactsSelected = contacts.isSelected();
        contactsTab.setEnabled(contactsSelected);
        if(!contactsSelected){
            accounting.setCompanyContact(null);
            setVatSelected(false);
//            vatAccounting.setSelected(false);
//            updateVatSetting();
            int indexOfComponent = indexOfComponent(contactsTab);
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent);
            }
        } else {
            insertTab("Contacts", null, contactsTab, "", 1);
        }
        accounting.setContactsAccounting(contactsSelected);
        Main.fireAccountingTypeChanged(accounting);
    }

    private void updateTradeSetting(){
        boolean tradeAccountingSelected = tradeAccounting.isSelected();
        tradeTab.setEnabled(tradeAccountingSelected);
        if(tradeAccountingSelected){
            setVatSelected(true);
//            vatAccounting.setSelected(true);
//            updateVatSetting();
            insertTab("Trade", null, tradeTab, "", 3);
        } else {
            int indexOfComponent = indexOfComponent(tradeTab);
            if(indexOfComponent!=-1) {
                removeTabAt(indexOfComponent);
            }
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

    private JComponent createCenterPanel(){
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        projects = new JCheckBox(PROJECTS);
        mortgages = new JCheckBox(MORTGAGES);
        contacts = new JCheckBox(CONTACTS);
        vatAccounting = new JCheckBox(VAT);
        tradeAccounting = new JCheckBox(TRADE);
        deliveroo = new JCheckBox(DELIVEROO);

        projects.setSelected(accounting==null||accounting.isProjectsAccounting());
        mortgages.setSelected(accounting==null||accounting.isMortgagesAccounting());
        contacts.setSelected(accounting==null||accounting.isContactsAccounting());
        vatAccounting.setSelected(accounting==null||accounting.isVatAccounting());
        tradeAccounting.setSelected(accounting==null||accounting.isTradeAccounting());
        deliveroo.setSelected(accounting==null||accounting.isDeliverooAccounting());

        projects.addActionListener(e -> updateProjectSetting());
        mortgages.addActionListener(e -> updateMortgageSetting());
        contacts.addActionListener(e -> updateContactSetting());
        vatAccounting.addActionListener(e -> updateVatSetting());
        tradeAccounting.addActionListener(e -> updateTradeSetting());
        deliveroo.addActionListener(e -> updateDeliverooSetting());

        panel.add(projects);
        panel.add(mortgages);
        panel.add(contacts);
        panel.add(vatAccounting);
        panel.add(tradeAccounting);
        panel.add(deliveroo);

        if(copyPanel==null) {
            return panel;
        } else {
            return Main.createSplitPane(panel, copyPanel, JSplitPane.HORIZONTAL_SPLIT);
        }
    }
}
