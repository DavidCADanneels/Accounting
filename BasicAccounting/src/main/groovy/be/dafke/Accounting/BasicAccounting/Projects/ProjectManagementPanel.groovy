package be.dafke.Accounting.BasicAccounting.Projects

import be.dafke.Accounting.BasicAccounting.Accounts.NewAccountDialog
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException
import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Utils.AlphabeticListModel
import be.dafke.Utils.PrefixFilterPanel

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import java.awt.*

import static java.util.ResourceBundle.getBundle

class ProjectManagementPanel extends JPanel implements ListSelectionListener {
    final PrefixFilterPanel<Account> zoeker
    final AlphabeticListModel<Account> allAccountsModel, projectAccountsModel
    final JList<Account> allAccounts, projectAccounts
    final JButton moveTo
    final JButton moveBack
    final JComboBox<Project> combo
    Accounting accounting
    Project project
//    Accounts accounts
//    AccountTypes accountTypes
//    Projects projects

    ProjectManagementPanel(Accounting accounting) {
        this.accounting = accounting
//        this.accounts = accounting.accounts
//        this.accountTypes = accounting.accountTypes
//        this.projects = accounting.projects
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS))
        //
        // onder
        JPanel onder = new JPanel()
        moveTo = new JButton(getBundle("Projects").getString("ADD"))
        moveTo.addActionListener({ e -> moveToProject() })
        moveTo.enabled = false
        onder.add(moveTo)
        //
        moveBack = new JButton(getBundle("Projects").getString("DELETE"))
        moveBack.addActionListener({ e -> removeFromProject() })
        moveBack.enabled = false
        onder.add(moveBack)
        //
        JButton addAccount = new JButton("Add Account")
        addAccount.addActionListener({ e ->
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.accounts, accounting.accountTypes.businessObjects)
            newAccountDialog.setLocation(getLocationOnScreen())
            newAccountDialog.visible = true
        })
        onder.add(addAccount)
        //
        // links
        JPanel paneelLinks = new JPanel()
        allAccountsModel = new AlphabeticListModel<>()
        allAccounts = new JList<>(allAccountsModel)
        allAccounts.addListSelectionListener(this)
        allAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        zoeker = new PrefixFilterPanel<>(allAccountsModel, allAccounts, new ArrayList<>())
        zoeker.add(onder, BorderLayout.SOUTH)
        paneelLinks.add(zoeker)
        paneelLinks.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")))
        add(paneelLinks)
        //
        // rechts
        JPanel paneelRechts = new JPanel(new BorderLayout())
        projectAccountsModel = new AlphabeticListModel<>()
        projectAccounts = new JList<>(projectAccountsModel)
        projectAccounts.addListSelectionListener(this)
        projectAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
        //
        JButton newProject = new JButton(getBundle("Projects").getString("NEW_PROJECT"))
        newProject.addActionListener({ e -> createNewProject() })
        JPanel noord = new JPanel()
        combo = new JComboBox<>()
        combo.addActionListener({ e -> comboAction() })
        noord.add(combo)
        noord.add(newProject)
        paneelRechts.add(noord, BorderLayout.NORTH)
        JScrollPane scrol = new JScrollPane(projectAccounts)
        paneelRechts.add(scrol)

        paneelRechts.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Projects").getString("PROJECTS")))
        add(paneelRechts)
    }

    void valueChanged(ListSelectionEvent lse) {
        JList list = (JList) lse.getSource()
        if (list == allAccounts) {
            if (!lse.getValueIsAdjusting() && allAccounts.selectedIndex != -1 && project != null) {
                moveTo.enabled = true
            } else {
                moveTo.enabled = false
            }
        } else if (list == projectAccounts) {
            if (!lse.getValueIsAdjusting() && projectAccounts.selectedIndex != -1) {
                moveBack.enabled = true
            } else {
                moveBack.enabled = false
            }
        }
    }

    void moveToProject(){
        for(Account account : allAccounts.getSelectedValuesList()) {
            projectAccountsModel.addElement(account)
            // TODO check if account belongs to another project (and remove it there ?)
            try {
                project.addBusinessObject(account)
            } catch (EmptyNameException | DuplicateNameException e) {
                // should not occur since projectAccounts are already filtered out from allAccounts
                e.printStackTrace()
            }
            allAccountsModel.removeElement(account)
        }
    }

    void removeFromProject(){
        for(Account account : projectAccounts.getSelectedValuesList()) {
            allAccountsModel.addElement(account)
            try {
                project.removeBusinessObject(account)
            } catch (NotEmptyException e) {
                e.printStackTrace()
            }
            projectAccountsModel.removeElement(account)
        }
    }

    void createNewProject(){
        String name = JOptionPane.showInputDialog(this, getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"))
        while (name != null && name.equals(""))
            name = JOptionPane.showInputDialog(this, getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"))
        if (name != null) {
            Project project = new Project(name, accounting.accounts, accounting.accountTypes)
            try {
                accounting.projects.addBusinessObject(project)
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(this, ActionUtils.PROJECT_NAME_EMPTY)
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(this, ActionUtils.PROJECT_DUPLICATE_NAME, name.trim())
            }
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project)
            (combo.getModel()).setSelectedItem(project)
            ProjectGUI.refreshAll()
        }
    }

    void comboAction() {
        project = (Project) combo.selectedItem
        projectAccountsModel.removeAllElements()
        if(project) {
            for (Account account : project.businessObjects) {
                // System.out.println("Project: " + project + " | account" + account)
                projectAccountsModel.addElement(account)
            }
            ArrayList<Account> noProjectlijst = getAccountNoMatchProject(project)
            zoeker.resetMap(noProjectlijst)
        }
    }

    ArrayList<Account> getAccountNoMatchProject(Project project) {
        ArrayList<Account> result = new ArrayList<>()
        for(Account account : accounting.accounts.businessObjects) {
            if (!project.businessObjects.contains(account)){
                result.add(account)
            }
        }
        result
    }

    void refresh() {
        zoeker.resetMap(accounting.accounts.businessObjects)
        combo.removeAllItems()
        for(Project project : accounting.projects.businessObjects) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project)
        }
        Project[] result = new Project[accounting.projects.businessObjects.size()]
        accounting.projects.businessObjects.toArray(result)
        if (accounting.projects.businessObjects.size() != 0) {
            combo.setSelectedItem(project)
        } else {
            project = null
            allAccountsModel.removeAllElements()
            for(Account account : accounting.accounts.businessObjects){
                allAccountsModel.addElement(account)
            }
        }
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
//        accounts=accounting?accounting.accounts:null
//        accountTypes=accounting?accounting.accountTypes:null
//        projects=accounting?accounting.getProjects():null
    }
}
