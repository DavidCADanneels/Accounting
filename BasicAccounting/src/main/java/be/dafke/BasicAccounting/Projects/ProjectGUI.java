package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.Balances.BalanceDataModel;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 2/11/2016.
 */
public class ProjectGUI extends JFrame {
    private static final HashMap<Projects, ProjectGUI> projectGuis = new HashMap<>();
    private final ProjectPanel projectPanel;

    private ProjectGUI(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        super(getBundle("Projects").getString("PROJECTS"));
        projectPanel = new ProjectPanel(accounts, accountTypes, projects);
        setContentPane(projectPanel);
        pack();
        refresh();
    }


    public static ProjectGUI showProjects(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        ProjectGUI gui = projectGuis.get(projects);
        if (gui == null) {
            gui = new ProjectGUI(accounts, accountTypes, projects);
            projectGuis.put(projects, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void refreshAll(){
        for (ProjectGUI gui : projectGuis.values()){
            gui.refresh();
        }
    }

    public void refresh() {
        projectPanel.refresh();
    }

//    public void setAccounting(Projects projects){
//        this.projects = projects;
//    }
}
