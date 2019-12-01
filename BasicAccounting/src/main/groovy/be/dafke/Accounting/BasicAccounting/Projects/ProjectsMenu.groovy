package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Projects

import javax.swing.JMenu
import javax.swing.JMenuItem

import static java.util.ResourceBundle.getBundle

class ProjectsMenu extends JMenu {
    JMenuItem manage, project
    Projects projects
    Accounts accounts
    AccountTypes accountTypes

    ProjectsMenu() {
        super(getBundle("Projects").getString("PROJECTS"))
//        setMnemonic(KeyEvent.VK_P)
        manage = new JMenuItem(getBundle("Projects").getString(
                "PROJECTMANAGER"))
        manage.addActionListener({ e ->
            ProjectManagementGUI projectManagementGUI = ProjectManagementGUI.showManager(accounts, accountTypes, projects)
            projectManagementGUI.setLocation(getLocationOnScreen())
            projectManagementGUI.visible = true
        })
        manage.enabled = false

        project = new JMenuItem(getBundle("Projects").getString(
                "PROJECTS"))
        project.addActionListener({ e ->
            ProjectGUI projectGUI = ProjectGUI.showProjects(accounts, accountTypes, projects)
            projectGUI.setLocation(getLocationOnScreen())
            projectGUI.visible = true
        })
        project.enabled = false

        add(project)
        add(manage)
    }

    void setAccounting(Accounting accounting) {
        accounts=accounting?accounting.accounts:null
        accountTypes=accounting?accounting.accountTypes:null
        projects=accounting?accounting.getProjects():null
        manage.enabled = projects!=null
        project.enabled = projects!=null
    }
}
