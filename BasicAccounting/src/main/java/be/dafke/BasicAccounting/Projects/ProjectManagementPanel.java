package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.Accounts.NewAccountDialog;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.Utils.AlphabeticListModel;
import be.dafke.Utils.PrefixFilterPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class ProjectManagementPanel extends JPanel implements ListSelectionListener {
    private final PrefixFilterPanel<Account> zoeker;
    private final AlphabeticListModel<Account> allAccountsModel, projectAccountsModel;
    private final JList<Account> allAccounts, projectAccounts;
    private final JButton moveTo, moveBack, newProject, addAccount;
    private final JComboBox<Project> combo;
    private Project project;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private Projects projects;

    public ProjectManagementPanel(Accounts accounts, AccountTypes accountTypes, Projects projects) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        this.projects = projects;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //
        // onder
        JPanel onder = new JPanel();
        moveTo = new JButton(getBundle("Projects").getString("ADD"));
        moveTo.addActionListener(e -> moveToProject());
        moveTo.setEnabled(false);
        onder.add(moveTo);
        //
        moveBack = new JButton(getBundle("Projects").getString("DELETE"));
        moveBack.addActionListener(e -> removeFromProject());
        moveBack.setEnabled(false);
        onder.add(moveBack);
        //
        addAccount = new JButton("Add Account");
        addAccount.addActionListener(e -> new NewAccountDialog(accounts, accountTypes.getBusinessObjects()).setVisible(true));
        onder.add(addAccount);
        //
        // links
        JPanel paneelLinks = new JPanel();
        allAccountsModel = new AlphabeticListModel<>();
        allAccounts = new JList<>(allAccountsModel);
        allAccounts.addListSelectionListener(this);
        allAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        zoeker = new PrefixFilterPanel<>(allAccountsModel, allAccounts, new ArrayList<>());
        zoeker.add(onder, BorderLayout.SOUTH);
        paneelLinks.add(zoeker);
        paneelLinks.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));
        add(paneelLinks);
        //
        // rechts
        JPanel paneelRechts = new JPanel(new BorderLayout());
        projectAccountsModel = new AlphabeticListModel<>();
        projectAccounts = new JList<>(projectAccountsModel);
        projectAccounts.addListSelectionListener(this);
        projectAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //
        newProject = new JButton(getBundle("Projects").getString("NEW_PROJECT"));
        newProject.addActionListener(e -> createNewProject());
        JPanel noord = new JPanel();
        combo = new JComboBox<>();
        combo.addActionListener(e -> comboAction());
        noord.add(combo);
        noord.add(newProject);
        paneelRechts.add(noord, BorderLayout.NORTH);
        JScrollPane scrol = new JScrollPane(projectAccounts);
        paneelRechts.add(scrol);

        paneelRechts.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Projects").getString("PROJECTS")));
        add(paneelRechts);
    }

    public void valueChanged(ListSelectionEvent lse) {
        JList list = (JList) lse.getSource();
        if (list == allAccounts) {
            if (!lse.getValueIsAdjusting() && allAccounts.getSelectedIndex() != -1 && project != null) {
                moveTo.setEnabled(true);
            } else {
                moveTo.setEnabled(false);
            }
        } else if (list == projectAccounts) {
            if (!lse.getValueIsAdjusting() && projectAccounts.getSelectedIndex() != -1) {
                moveBack.setEnabled(true);
            } else {
                moveBack.setEnabled(false);
            }
        }
    }

    public void moveToProject(){
        for(Account account : allAccounts.getSelectedValuesList()) {
            projectAccountsModel.addElement(account);
            // TODO check if account belongs to another project (and remove it there ?)
            try {
                project.addBusinessObject(account);
            } catch (EmptyNameException | DuplicateNameException e) {
                // should not occur since projectAccounts are already filtered out from allAccounts
                e.printStackTrace();
            }
            allAccountsModel.removeElement(account);
        }
    }

    public void removeFromProject(){
        for(Account account : projectAccounts.getSelectedValuesList()) {
            allAccountsModel.addElement(account);
            try {
                project.removeBusinessObject(account);
            } catch (NotEmptyException e) {
                e.printStackTrace();
            }
            projectAccountsModel.removeElement(account);
        }
    }

    public void createNewProject(){
        String name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
        while (name != null && name.equals(""))
            name = JOptionPane.showInputDialog(getBundle("Projects").getString("ENTER_NAME_FOR_PROJECT"));
        if (name != null) {
            Project project = new Project(name, accounts, accountTypes);
            try {
                projects.addBusinessObject(project);
            } catch (EmptyNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.PROJECT_NAME_EMPTY);
            } catch (DuplicateNameException e) {
                ActionUtils.showErrorMessage(ActionUtils.PROJECT_DUPLICATE_NAME, name.trim());
            }
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
            (combo.getModel()).setSelectedItem(project);
            ProjectGUI.refreshAll();
        }
    }

    public void comboAction() {
        project = (Project) combo.getSelectedItem();
        projectAccountsModel.removeAllElements();
        if(project!=null) {
            for (Account account : project.getBusinessObjects()) {
                // System.out.println("Project: " + project + " | account" + account);
                projectAccountsModel.addElement(account);
            }
            ArrayList<Account> noProjectlijst = getAccountNoMatchProject(project);
            zoeker.resetMap(noProjectlijst);
        }
    }

    public ArrayList<Account> getAccountNoMatchProject(Project project) {
        ArrayList<Account> result = new ArrayList<>();
        for(Account account : accounts.getBusinessObjects()) {
            if (!project.getBusinessObjects().contains(account)){
                result.add(account);
            }
        }
        return result;
    }

    public void refresh() {
        zoeker.resetMap(accounts.getBusinessObjects());
        combo.removeAllItems();
        for(Project project : projects.getBusinessObjects()) {
            ((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
        }
        Project[] result = new Project[projects.getBusinessObjects().size()];
        projects.getBusinessObjects().toArray(result);
        if (projects.getBusinessObjects().size() != 0) {
            combo.setSelectedItem(project);
        } else {
            project = null;
            allAccountsModel.removeAllElements();
            for(Account account : accounts.getBusinessObjects()){
                allAccountsModel.addElement(account);
            }
        }
    }

    public void setAccounting(Accounting accounting) {
        accounts=accounting==null?null:accounting.getAccounts();
        accountTypes=accounting==null?null:accounting.getAccountTypes();
        projects=accounting==null?null:accounting.getProjects();
    }
}