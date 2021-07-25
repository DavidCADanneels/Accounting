package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class ProjectGUI extends JFrame {
    static ProjectGUI gui = null
    final ProjectPanel projectPanel

    ProjectGUI() {
        super(getBundle("Projects").getString("PROJECTS"))
        projectPanel = new ProjectPanel()
        setContentPane(projectPanel)
        pack()
        refresh()
    }


    static ProjectGUI showProjects() {
        if (gui == null) {
            gui = new ProjectGUI()
            Main.addFrame(gui)
        }
        gui
    }

    void refresh() {
        projectPanel.refresh()
    }

//    void setAccounting(Projects projects){
//        this.projects = projects
//    }
}
