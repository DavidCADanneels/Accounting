package be.dafke.Accounting.BasicAccounting.Projects


import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectsMenu extends JMenu {
    JMenuItem manage, project

    ProjectsMenu() {
        super(getBundle("Projects").getString("PROJECTS"))
//        setMnemonic(KeyEvent.VK_P)
        manage = new JMenuItem(getBundle("Projects").getString("PROJECTMANAGER"))
        manage.addActionListener({ e -> showProjectManagement() })
        manage.enabled = false

        project = new JMenuItem(getBundle("Projects").getString("PROJECTS"))
        project.addActionListener({ e -> showProjects() })
        project.enabled = false

        add(project)
        add(manage)
    }

    void showProjectManagement(){
        ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(Session.activeAccounting)
        projectManagementGUI.setLocation(getLocationOnScreen())
        projectManagementGUI.visible = true
    }

    void showProjects(){
        ProjectGUI projectGUI = ProjectGUI.showProjects()
        projectGUI.setLocation(getLocationOnScreen())
        projectGUI.visible = true
    }

    void refresh() {
        Accounting accounting = Session.activeAccounting
        manage.enabled = accounting&&accounting.projects
        project.enabled = accounting&&accounting.projects
    }
}
