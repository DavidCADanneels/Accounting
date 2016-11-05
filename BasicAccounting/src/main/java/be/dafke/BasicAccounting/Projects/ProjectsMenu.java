package be.dafke.BasicAccounting.Projects;

import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Projects;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class ProjectsMenu extends JMenu implements ActionListener, AccountingListener {
    private JMenuItem manage, project;
    public static final String MANAGE = "ManageProjects";
    public static final String PROJECTS = "Projects";
    private Projects projects;
    private Accounts accounts;
    private AccountTypes accountTypes;

    public ProjectsMenu() {
        super(getBundle("Projects").getString("PROJECTS"));
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
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==manage) {
            String key = MANAGE + projects.hashCode();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if (gui == null) {
                gui = new ProjectManagementGUI(accounts, accountTypes, projects);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        } else if(ae.getSource()==project){
            String key = MANAGE + projects.hashCode();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if (gui == null) {
                gui = new ProjectGUI(accounts, accountTypes, projects);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
            gui.setVisible(true);
        }
    }

    @Override
    public void setAccounting(Accounting accounting) {
        accounts=accounting==null?null:accounting.getAccounts();
        accountTypes=accounting==null?null:accounting.getAccountTypes();
        projects=accounting==null?null:accounting.getProjects();
        manage.setEnabled(projects!=null);
        project.setEnabled(projects!=null);
    }
}
