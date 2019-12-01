package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Projects

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectGUI extends JFrame {
    static final HashMap<Projects, ProjectGUI> projectGuis = new HashMap<>()
    final ProjectPanel projectPanel

    ProjectGUI(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        super(getBundle("Projects").getString("PROJECTS"))
        projectPanel = new ProjectPanel(accounts, accountTypes, projects)
        setContentPane(projectPanel)
        pack()
        refresh()
    }


    static ProjectGUI showProjects(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        ProjectGUI gui = projectGuis.get(projects)
        if (gui == null) {
            gui = new ProjectGUI(accounts, accountTypes, projects)
            projectGuis.put(projects, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void refreshAll(){
        for (ProjectGUI gui : projectGuis.values()){
            gui.refresh()
        }
    }

    void refresh() {
        projectPanel.refresh()
    }

//    void setAccounting(Projects projects){
//        this.projects = projects
//    }
}
