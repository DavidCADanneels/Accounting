package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.MainApplication.AccountingMenuBar;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class ProjectsMenu extends JMenu implements ActionListener {
    private JMenuItem manage, project;
    private final Accountings accountings;
    public static final String MANAGE = "ManageProjects";
    public static final String PROJECTS = "Projects";

    public ProjectsMenu(Accountings accountings, AccountingMenuBar menuBar) {
        super(getBundle("Projects").getString("PROJECTS"));
        this.accountings = accountings;
        setMnemonic(KeyEvent.VK_P);
        manage = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        manage.addActionListener(this);
        manage.setEnabled(false);

        project = new JMenuItem(getBundle("Projects").getString(
                "PROJECTS"));
        project.addActionListener(this);
        project.setEnabled(false);

        add(project);
        add(manage);
        menuBar.addRefreshableMenuItem(manage);
        menuBar.addRefreshableMenuItem(project);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==manage) {
            Accounting accounting = accountings.getCurrentObject();
            String key = accounting.toString() + MANAGE;
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if (gui == null) {
                gui = new ProjectManagementGUI(accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(ae.getSource()==project){
            Accounting accounting = accountings.getCurrentObject();
            String key = accounting.toString() + PROJECTS;
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if (gui == null) {
                gui = new ProjectGUI(accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        }
    }
}
