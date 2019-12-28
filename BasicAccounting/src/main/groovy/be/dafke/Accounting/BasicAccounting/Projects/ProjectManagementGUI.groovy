package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectManagementGUI extends JFrame {
    static final HashMap<Accounting, ProjectManagementGUI> projectManagementGuis = [:]
    ProjectManagementPanel projectManagementPanel

    ProjectManagementGUI(Accounting accounting) {
        super(getBundle("Projects").getString("PROJECTMANAGER"))
        projectManagementPanel = new ProjectManagementPanel(accounting)
        setContentPane(projectManagementPanel)
        pack()
        refresh()
    }

    static ProjectManagementGUI showManager(Accounting accounting) {
        ProjectManagementGUI gui = projectManagementGuis.get(accounting)
        if (gui == null) {
            gui = new ProjectManagementGUI(accounting)
            projectManagementGuis.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    void refresh() {
        projectManagementPanel.refresh()
    }
}
