package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static java.awt.BorderLayout.*;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class AccountingSettingsPanel extends JFrame implements ActionListener{
    public static final String title = getBundle("Accounting").getString("SETTINGS");
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
    private static HashMap<Accounting,AccountingSettingsPanel> accountingSettingsMap = new HashMap<>();
    private JComboBox<Contact> allContacts;
    private DefaultComboBoxModel<Contact> model;

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

    public static AccountingSettingsPanel showPanel(Accounting accounting){
        AccountingSettingsPanel accountingSettingsPanel = accountingSettingsMap.get(accounting);
        if(accountingSettingsPanel == null){
            accountingSettingsPanel = new AccountingSettingsPanel(accounting);
            accountingSettingsMap.put(accounting,accountingSettingsPanel);
            Main.addFrame(accountingSettingsPanel);
        }
        return accountingSettingsPanel;
    }

    private void setActions() {
        vatAccounting.addActionListener(e -> {
            accounting.setVatAccounting(vatAccounting.isSelected());
            Main.fireAccountingTypeChanged(accounting);
        });
        tradeAccounting.addActionListener(e -> {
            accounting.setTradeAccounting(tradeAccounting.isSelected());
            Main.fireAccountingTypeChanged(accounting);
        });
        contacts.addActionListener(e -> {
            accounting.setContactsAccounting(contacts.isSelected());
            Main.fireAccountingTypeChanged(accounting);
        });
        projects.addActionListener(e -> {
            accounting.setProjectsAccounting(projects.isSelected());
            Main.fireAccountingTypeChanged(accounting);
        });
        deliveroo.addActionListener(e -> {
            accounting.setDeliverooAccounting(deliveroo.isSelected());
            Main.fireAccountingTypeChanged(accounting);
        });
        mortgages.addActionListener(e -> {
            accounting.setMortgagesAccounting(mortgages.isSelected());
            Main.fireAccountingTypeChanged(accounting);
        });
    }

    private void setAccounting(Accounting accounting) {
        tradeAccounting.setSelected(accounting.isTradeAccounting());
        vatAccounting.setSelected(accounting.isVatAccounting());
        contacts.setSelected(accounting.isContactsAccounting());
        projects.setSelected(accounting.isProjectsAccounting());
        deliveroo.setSelected(accounting.isDeliverooAccounting());
        mortgages.setSelected(accounting.isMortgagesAccounting());
        allContacts.removeActionListener(this);
        allContacts.removeAllItems();
        accounting.getContacts().getBusinessObjects().stream().forEach(contact -> model.addElement(contact));
        Contact companyContact = accounting.getCompanyContact();
        allContacts.setSelectedItem(companyContact);
        allContacts.addActionListener(this);
    }

    private JPanel createContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel checkBoxes = createCenterPanel();
        JPanel comboBox = createNorthPanel();

        mainPanel.add(checkBoxes,CENTER);
        mainPanel.add(comboBox,NORTH);
        return mainPanel;
    }

    private JPanel createNorthPanel(){
        JPanel panel = new JPanel();
        model = new DefaultComboBoxModel<>();
        allContacts = new JComboBox<>(model);
        allContacts.setSelectedItem(null);
        allContacts.addActionListener(this);
        panel.add(new JLabel("Company Contact"));
        panel.add(allContacts);
        return panel;
    }

    private JPanel createCenterPanel(){
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        tradeAccounting = new JCheckBox(TRADE);
        vatAccounting = new JCheckBox(VAT);
        contacts = new JCheckBox(CONTACTS);
        projects = new JCheckBox(PROJECTS);
        deliveroo = new JCheckBox(DELIVEROO);
        mortgages = new JCheckBox(MORTGAGES);

        panel.add(tradeAccounting);
        panel.add(vatAccounting);
        panel.add(contacts);
        panel.add(projects);
        panel.add(deliveroo);
        panel.add(mortgages);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Contact contact = (Contact)allContacts.getSelectedItem();
        accounting.setCompanyContact(contact);
    }
}
