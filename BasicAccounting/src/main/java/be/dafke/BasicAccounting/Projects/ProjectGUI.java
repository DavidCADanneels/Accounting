package be.dafke.BasicAccounting.Projects;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Project;
import be.dafke.BusinessModel.Projects;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
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
    private TransactionsDataModel transactionsDataModel;

    public ProjectGUI(Accounting accounting) {
        super(getBundle("Projects").getString("PROJECTS"));
        this.accounting = accounting;
        setLayout(new BorderLayout());

        JPanel noord = new JPanel();
        combo = new JComboBox<>();
        combo.addActionListener(this);

        manage = new JButton(getBundle("Projects").getString("PROJECTMANAGER"));
        manage.addActionListener(this);

        noord.add(combo);
        noord.add(manage);
        add(noord, BorderLayout.NORTH);
        add(createTransactionsPanel(),BorderLayout.CENTER);
        pack();
        refresh();
    }

    private JScrollPane createTransactionsPanel() {
        transactionsDataModel = new TransactionsDataModel();

        RefreshableTable<Booking> table = new RefreshableTable<>(transactionsDataModel);
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
        transactionsDataModel.setTransactions(project.getTransactions());
        transactionsDataModel.fireTableDataChanged();
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
