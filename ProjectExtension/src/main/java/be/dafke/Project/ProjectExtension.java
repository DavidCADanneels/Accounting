package be.dafke.Project;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Project.Actions.ShowProjectsActionListener;
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
        try {
            accounting.addBusinessObject((BusinessCollection)projects);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(projects.getBusinessObjectType());
    }

    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    public void extendWriteCollection(Accounting accounting, File xmlFolder){

    }

}
