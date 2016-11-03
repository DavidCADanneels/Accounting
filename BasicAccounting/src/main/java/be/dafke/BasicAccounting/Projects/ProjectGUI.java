package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.Balances.BalanceDataModel;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static be.dafke.BasicAccounting.Projects.ProjectsMenu.MANAGE;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 2/11/2016.
 */
public class ProjectGUI extends RefreshableFrame implements ActionListener {
    private final JButton manage;
    private final JComboBox<Project> combo;
    private Project project;
    private final Accounting accounting;
    private JournalDetailsDataModel journalDetailsDataModel;
    private BalanceDataModel resultBalanceDataModel, relationsBalanceDataModel;

    public ProjectGUI(Accounting accounting) {
        super(getBundle("Projects").getString("PROJECTS"));
        this.accounting = accounting;
        setLayout(new BorderLayout());

        JPanel noord = new JPanel();
        //
        combo = new JComboBox<>();
        combo.addActionListener(this);
        //
        manage = new JButton(getBundle("Projects").getString("PROJECTMANAGER"));
        manage.addActionListener(this);
        //
        noord.add(combo);
        noord.add(manage);
        add(noord, BorderLayout.NORTH);

//------------------------------------------------------------------------------------------
        resultBalanceDataModel = new BalanceDataModel(
                getBundle("BusinessModel").getString("COSTS"),
                getBundle("BusinessModel").getString("REVENUES"));
        JScrollPane resultBalance = createBalancePanel(resultBalanceDataModel);
        resultBalance.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "BusinessModel").getString("RESULTBALANCE")));
//------------------------------------------------------------------------------------------
        relationsBalanceDataModel = new BalanceDataModel(
                getBundle("BusinessModel").getString("FUNDS_FROM_CUSTOMERS"),
                getBundle("BusinessModel").getString("DEBTS_TO_SUPPLIERS"));
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

    @Override
    public void refresh() {
        Projects projects = accounting.getProjects();
        combo.removeAllItems();
        for(Project project : projects.getBusinessObjects()) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
        }
        if (!projects.getBusinessObjects().isEmpty()) {
            if(project==null) project = projects.getBusinessObjects().get(0);
            combo.setSelectedItem(project);
        }
        journalDetailsDataModel.setJournal(project.getJournal());
        journalDetailsDataModel.fireTableDataChanged();
//        resultBalanceDataModel.setBalance(project.getResultBalance());
        resultBalanceDataModel.fireTableDataChanged();
//        relationsBalanceDataModel.setBalance(project.getRelationsBalance());
        relationsBalanceDataModel.fireTableDataChanged();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == combo) {
            project = (Project) combo.getSelectedItem();
            refresh();
        } else if(ae.getSource()==manage) {
            String key = accounting.toString() + MANAGE;
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if (gui == null) {
                gui = new ProjectManagementGUI(accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        }
    }
}
