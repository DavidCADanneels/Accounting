package be.dafke.Project;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.Project.Actions.ShowProjectsActionListener;
import be.dafke.Project.GUI.ProjectManagementGUI;
import be.dafke.Project.Objects.Projects;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 10:04
 */
public class ProjectExtension implements AccountingExtension{
    private static JMenu projecten = null;
    private Projects projects;
    private Accountings accountings;

    public ProjectExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(projecten == null) createMenu(accountings, menuBar);
    }

    private void createMenu(Accountings accountings, AccountingMenuBar menuBar) {
        projecten = new JMenu(getBundle("Projects").getString("PROJECTS"));
        projecten.setMnemonic(KeyEvent.VK_P);
        JMenuItem projects = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        projects.addActionListener(new ShowProjectsActionListener(accountings));
        projects.setEnabled(false);
        projecten.add(projects);
        menuBar.addRefreshableMenuItem(projects);
        menuBar.add(projecten);
    }

    public void extendConstructor(Accounting accounting){
        projects = new Projects();
    }

    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    public void extendWriteCollection(Accounting accounting, File xmlFolder){

    }

}
