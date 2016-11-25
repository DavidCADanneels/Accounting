package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.MainApplication.Main;
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
    private static JMenuItem manage, project;
    public static final String MANAGE = "ManageProjects";
    public static final String PROJECTS = "Projects";
    private static Projects projects;
    private static Accounts accounts;
    private static AccountTypes accountTypes;

    public ProjectsMenu() {
        super(getBundle("Projects").getString("PROJECTS"));
        setMnemonic(KeyEvent.VK_P);
        manage = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        manage.addActionListener(e -> showManager());
        manage.setEnabled(false);

        project = new JMenuItem(getBundle("Projects").getString(
                "PROJECTS"));
        project.addActionListener(e -> showProjects());
        project.setEnabled(false);

        add(project);
        add(manage);
    }

    private void showProjects() {
        String key = PROJECTS + projects.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if (gui == null) {
            gui = new ProjectGUI(accounts, accountTypes, projects);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    private void showManager() {
        String key = MANAGE + projects.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if (gui == null) {
            gui = new ProjectManagementGUI(accounts, accountTypes, projects);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void setAccounting(Accounting accounting) {
        accounts=accounting==null?null:accounting.getAccounts();
        accountTypes=accounting==null?null:accounting.getAccountTypes();
        projects=accounting==null?null:accounting.getProjects();
        manage.setEnabled(projects!=null);
        project.setEnabled(projects!=null);
    }
}
