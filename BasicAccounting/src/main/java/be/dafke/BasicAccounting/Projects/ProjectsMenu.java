package be.dafke.BasicAccounting.Projects;

import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Projects;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class ProjectsMenu extends JMenu {
    private JMenuItem manage, project;
    private Projects projects;
    private Accounts accounts;
    private AccountTypes accountTypes;

    public ProjectsMenu() {
        super(getBundle("Projects").getString("PROJECTS"));
        setMnemonic(KeyEvent.VK_P);
        manage = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        manage.addActionListener(e -> ProjectManagementGUI.showManager(accounts, accountTypes, projects).setVisible(true));
        manage.setEnabled(false);

        project = new JMenuItem(getBundle("Projects").getString(
                "PROJECTS"));
        project.addActionListener(e -> ProjectGUI.showProjects(accounts, accountTypes, projects).setVisible(true));
        project.setEnabled(false);

        add(project);
        add(manage);
    }

    public void setAccounting(Accounting accounting) {
        accounts=accounting==null?null:accounting.getAccounts();
        accountTypes=accounting==null?null:accounting.getAccountTypes();
        projects=accounting==null?null:accounting.getProjects();
        manage.setEnabled(projects!=null);
        project.setEnabled(projects!=null);
    }
}
