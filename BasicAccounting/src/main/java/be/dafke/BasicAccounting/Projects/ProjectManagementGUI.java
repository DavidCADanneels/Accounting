package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class ProjectManagementGUI extends JFrame {
	private static final HashMap<Projects, ProjectManagementGUI> projectManagementGuis = new HashMap<>();
	private ProjectManagementPanel projectManagementPanel;

	private ProjectManagementGUI(Accounts accounts, AccountTypes accountTypes, Projects projects) {
		super(getBundle("Projects").getString("PROJECTMANAGER"));
		projectManagementPanel = new ProjectManagementPanel(accounts, accountTypes, projects);
		setContentPane(projectManagementPanel);
		pack();
		refresh();
	}

	public static ProjectManagementGUI showManager(Accounts accounts, AccountTypes accountTypes, Projects projects) {
		ProjectManagementGUI gui = projectManagementGuis.get(projects);
		if (gui == null) {
			gui = new ProjectManagementGUI(accounts, accountTypes, projects);
			projectManagementGuis.put(projects, gui);
			Main.addFrame(gui);
		}
		return gui;
	}

    public void refresh() {
        projectManagementPanel.refresh();
	}
}
