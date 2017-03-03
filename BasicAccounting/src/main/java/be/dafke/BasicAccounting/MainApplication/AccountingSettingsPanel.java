package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.awt.*;

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
    JCheckBox vatAccounting;
    JCheckBox contacts;
    JCheckBox projects;
    JCheckBox mortgages;

    public AccountingSettingsPanel(Accounting accounting) {
        super(accounting.getName() + " / " + title);

        JPanel center = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(center);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPanel);
        pack();
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
