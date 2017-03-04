package be.dafke.BasicAccounting.MainApplication;

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
        allContacts.removeAllItems();
        accounting.getContacts().getBusinessObjects().stream().forEach(contact -> model.addElement(contact));
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
        panel.add(new JLabel("Company Contact"));
        panel.add(allContacts);
        return panel;
    }

    private JPanel createCenterPanel(){
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
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
