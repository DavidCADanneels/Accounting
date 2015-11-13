package be.dafke.Project.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Project.GUI.ProjectManagementGUI;
import be.dafke.Project.Objects.Projects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class ShowProjectsActionListener implements ActionListener {
    private Accountings accountings;
    public static final String PROJECTS = "Projects";

    public ShowProjectsActionListener(Accountings accountings) {
        this.accountings = accountings;
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
