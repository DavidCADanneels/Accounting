package be.dafke.Accounting.BasicAccounting.Projects


import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectsMenu extends JMenu {
    JMenuItem manage, project
    Accounting accounting

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
        ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(accounting)
        projectManagementGUI.setLocation(getLocationOnScreen())
        projectManagementGUI.visible = true
    }

    void showProjects(){
        ProjectGUI projectGUI = ProjectGUI.showProjects(accounting)
        projectGUI.setLocation(getLocationOnScreen())
        projectGUI.visible = true
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        manage.enabled = accounting&&accounting.projects
        project.enabled = accounting&&accounting.projects
    }
}
