package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.Balances.BalanceDataModel;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 2/11/2016.
 */
public class ProjectGUI extends RefreshableFrame {
    private final JButton manage;
    private final JComboBox<Project> combo;
    private final Accounts accounts;
    private final AccountTypes accountTypes;
    private Project project;
    private JournalDetailsDataModel journalDetailsDataModel;
    private BalanceDataModel resultBalanceDataModel, relationsBalanceDataModel;
    private Projects projects;
    public static final String PROJECTS = "Projects";
    private static final HashMap<Projects, ProjectGUI> projectGuis = new HashMap<>();
    private static final HashMap<Projects, ProjectManagementGUI> projectManagentGuis = new HashMap<>();

    private ProjectGUI(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        super(getBundle("Projects").getString("PROJECTS"));
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        this.projects = projects;
        setLayout(new BorderLayout());

        JPanel noord = new JPanel();
        //
        combo = new JComboBox<>();
        combo.addActionListener(e -> {
            project = (Project) combo.getSelectedItem();
            refresh();
        });
        //
        manage = new JButton(getBundle("Projects").getString("PROJECTMANAGER"));
        manage.addActionListener(e -> ProjectManagementGUI.showManager(accounts, accountTypes, projects).setVisible(true));
        //
        noord.add(combo);
        noord.add(manage);
        add(noord, BorderLayout.NORTH);

//------------------------------------------------------------------------------------------
        resultBalanceDataModel = new BalanceDataModel(
                getBundle("BusinessModel").getString("COSTS"),
                getBundle("BusinessModel").getString("REVENUES"), true);
        JScrollPane resultBalance = createBalancePanel(resultBalanceDataModel);
        resultBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "BusinessModel").getString("RESULTBALANCE")));
//------------------------------------------------------------------------------------------
        relationsBalanceDataModel = new BalanceDataModel(
                getBundle("BusinessModel").getString("FUNDS_FROM_CUSTOMERS"),
                getBundle("BusinessModel").getString("DEBTS_TO_SUPPLIERS"), true);
        JScrollPane partnerBalance = createBalancePanel(relationsBalanceDataModel);
        partnerBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "BusinessModel").getString("RELATIONSBALANCE")));
//------------------------------------------------------------------------------------------
        JScrollPane transactionsPanel = createTransactionsPanel();
        transactionsPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("TRANSACTIONS")));
//------------------------------------------------------------------------------------------
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(resultBalance);
        center.add(partnerBalance);
        center.add(transactionsPanel);
//------------------------------------------------------------------------------------------
        add(center,BorderLayout.CENTER);
        pack();
        refresh();
    }
    private JScrollPane createBalancePanel(BalanceDataModel dataModel){
        RefreshableTable<Account> table = new RefreshableTable<>(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        return new JScrollPane(table);
    }

    private JScrollPane createTransactionsPanel() {
        journalDetailsDataModel = new JournalDetailsDataModel();
        RefreshableTable<Booking> table = new RefreshableTable<>(journalDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));
        return new JScrollPane(table);
    }

    public static ProjectGUI showProjects(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        ProjectGUI gui = projectGuis.get(projects);
        if (gui == null) {
            gui = new ProjectGUI(accounts, accountTypes, projects);
            projectGuis.put(projects, gui);
            Main.addJFrame(gui);
        }
        return gui;
    }


//    @Override
    public void refresh() {
        combo.removeAllItems();
        for(Project project : projects.getBusinessObjects()) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
        }
        if (!projects.getBusinessObjects().isEmpty()) {
            if(project==null) project = projects.getBusinessObjects().get(0);
            combo.setSelectedItem(project);
        }
        if(project!=null) {
            journalDetailsDataModel.setJournal(project.getJournal());
            resultBalanceDataModel.setBalance(project.getResultBalance());
            relationsBalanceDataModel.setBalance(project.getRelationsBalance());
        }
        journalDetailsDataModel.fireTableDataChanged();
        resultBalanceDataModel.fireTableDataChanged();
        relationsBalanceDataModel.fireTableDataChanged();
    }

    public void setAccounting(Projects projects){
        this.projects = projects;
    }
}
