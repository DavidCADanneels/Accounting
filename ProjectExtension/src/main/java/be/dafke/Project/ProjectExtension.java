package be.dafke.Project;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.Project.Actions.ShowProjectsActionListener;
import be.dafke.Project.Objects.Projects;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 10:04
 */
public class ProjectExtension {
    private static JMenu projecten = null;

    public ProjectExtension(Accountings accountings, AccountingMenuBar menuBar){
        if(projecten == null) createMenu(accountings, menuBar);
        for(Accounting accounting: accountings.getBusinessObjects()) {
            new Projects(accounting);
        }
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
}
