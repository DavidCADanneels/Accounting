package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accountings;

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
    private JMenuItem projects;

    public ProjectsMenu(Accountings accountings, AccountingMenuBar menuBar) {
        projects = new JMenu(getBundle("Projects").getString("PROJECTS"));
        projects.setMnemonic(KeyEvent.VK_P);
        JMenuItem projects = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        projects.addActionListener(new ShowProjectsActionListener(accountings));
        projects.setEnabled(false);
        this.projects.add(projects);
        menuBar.addRefreshableMenuItem(projects);
        menuBar.add(this.projects);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
