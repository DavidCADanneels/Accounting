package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.GUI.Projects.ProjectManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Projects;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

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
    private final Accountings accountings;
    public static final String PROJECTS = "Projects";

    public ProjectsMenu(Accountings accountings, AccountingMenuBar menuBar) {
        super(getBundle("Projects").getString("PROJECTS"));
        this.accountings = accountings;
        setMnemonic(KeyEvent.VK_P);
        projects = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"));
        projects.addActionListener(this);
        projects.setEnabled(false);
        add(projects);
        menuBar.addRefreshableMenuItem(projects);
    }

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        BusinessCollection<BusinessObject> projects = accounting.getBusinessObject(PROJECTS);
        String key = accounting.toString() + PROJECTS;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new ProjectManagementGUI(accounting, (Projects)projects);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
