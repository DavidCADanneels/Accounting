package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Projects

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectManagementGUI extends JFrame {
    private static final HashMap<Projects, ProjectManagementGUI> projectManagementGuis = new HashMap<>()
    private ProjectManagementPanel projectManagementPanel

    private ProjectManagementGUI(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        super(getBundle("Projects").getString("PROJECTMANAGER"))
        projectManagementPanel = new ProjectManagementPanel(accounts, accountTypes, projects)
        setContentPane(projectManagementPanel)
        pack()
        refresh()
    }

    static ProjectManagementGUI showManager(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        ProjectManagementGUI gui = projectManagementGuis.get(projects)
        if (gui == null) {
            gui = new ProjectManagementGUI(accounts, accountTypes, projects)
            projectManagementGuis.put(projects, gui)
            Main.addFrame(gui)
        }
        gui
    }

    void refresh() {
        projectManagementPanel.refresh()
    }
}
