package be.dafke.Project;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
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
    public static final String PROJECTS = "Projects";

    public ProjectExtension(AccountingMenuBar menuBar){
        if(projecten == null) createMenu(menuBar);
    }

    private void createMenu(AccountingMenuBar menuBar) {
        projecten = new JMenu(getBundle("Projects").getString("PROJECTS"));
        projecten.setMnemonic(KeyEvent.VK_P);
        JMenuItem projects = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        //projects.addActionListener(actionListener);
        projects.setActionCommand(PROJECTS);
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

    public void extendAccountingComponentMap(Accounting accounting){
        ComponentMap.addDisposableComponent(accounting.toString() + PROJECTS, new ProjectManagementGUI(accounting, projects));
    }

    public void extendWriteCollection(Accounting accounting, File xmlFolder){

    }

}
