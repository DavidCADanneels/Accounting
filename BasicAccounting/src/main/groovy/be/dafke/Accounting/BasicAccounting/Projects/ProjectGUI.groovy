package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectGUI extends JFrame {
    static final HashMap<Accounting, ProjectGUI> projectGuis = [:]
    final ProjectPanel projectPanel

    ProjectGUI(Accounting accounting) {
        super(getBundle("Projects").getString("PROJECTS"))
        projectPanel = new ProjectPanel(accounting)
        setContentPane(projectPanel)
        pack()
        refresh()
    }


    static ProjectGUI showProjects(Accounting accounting) {
        ProjectGUI gui = projectGuis.get(accounting)
        if (gui == null) {
            gui = new ProjectGUI(accounting)
            projectGuis.put(accounting, gui)
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
