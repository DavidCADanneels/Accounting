package be.dafke.BasicAccounting.Projects;

import be.dafke.Accounting.BusinessModel.AccountTypes;
import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.Accounting.BusinessModel.Accounts;
import be.dafke.Accounting.BusinessModel.Projects;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class ProjectsMenu extends JMenu {
    private JMenuItem manage, project;
    private Projects projects;
    private Accounts accounts;
    private AccountTypes accountTypes;

    public ProjectsMenu() {
        super(getBundle("Projects").getString("PROJECTS"));
//        setMnemonic(KeyEvent.VK_P);
        manage = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        manage.addActionListener(e -> {
            ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(accounts, accountTypes, projects);
            projectManagementGUI.setLocation(getLocationOnScreen());
            projectManagementGUI.setVisible(true);
        });
        manage.setEnabled(false);

        project = new JMenuItem(getBundle("Projects").getString(
                "PROJECTS"));
        project.addActionListener(e -> {
            ProjectGUI projectGUI = ProjectGUI.showProjects(accounts, accountTypes, projects);
            projectGUI.setLocation(getLocationOnScreen());
            projectGUI.setVisible(true);
        });
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
