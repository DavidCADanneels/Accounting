package be.dafke.BasicAccounting.Projects;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class ProjectManagementGUI extends JFrame implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrefixFilterPanel<Account> zoeker;
	private final AlphabeticListModel<Account> allAccountsModel, projectAccountsModel;
	private final JList<Account> allAccounts, projectAccounts;
	private final JButton moveTo, moveBack, newProject, addAccount;
	private final JComboBox<Project> combo;
	private Project project;
	private Accounts accounts;
	private AccountTypes accountTypes;
	private Projects projects;
	private static final HashMap<Projects, ProjectManagementGUI> projectManagementGuis = new HashMap<>();
	public static final String MANAGE = "ManageProjects";


	private ProjectManagementGUI(Accounts accounts, AccountTypes accountTypes, Projects projects) {
		super(getBundle("Projects").getString("PROJECTMANAGER"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.projects = projects;
		JPanel hoofdPaneel = new JPanel();
		hoofdPaneel.setLayout(new BoxLayout(hoofdPaneel, BoxLayout.X_AXIS));
		//
		// onder
		JPanel onder = new JPanel();
		moveTo = new JButton(getBundle("Projects").getString("ADD"));
		moveTo.addActionListener(this);
		moveTo.setEnabled(false);
		onder.add(moveTo);
		//
		moveBack = new JButton(getBundle("Projects").getString("DELETE"));
		moveBack.addActionListener(this);
		moveBack.setEnabled(false);
		onder.add(moveBack);
		//
		addAccount = new JButton("Add Account");
		addAccount.addActionListener(this);
		onder.add(addAccount);
		//
		// links
		JPanel paneelLinks = new JPanel();
		allAccountsModel = new AlphabeticListModel<>();
		allAccounts = new JList<>(allAccountsModel);
		allAccounts.addListSelectionListener(this);
		allAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

//		Accounting accounting = accountings.getCurrentObject();
//		Accounts accounts = accounting.getBusinessObjects();
		zoeker = new PrefixFilterPanel<>(allAccountsModel, allAccounts, new ArrayList<>());
        zoeker.add(onder, BorderLayout.SOUTH);
		paneelLinks.add(zoeker);
		paneelLinks.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));
		hoofdPaneel.add(paneelLinks);
		//
		// rechts
		JPanel paneelRechts = new JPanel(new BorderLayout());
		projectAccountsModel = new AlphabeticListModel<>();
		projectAccounts = new JList<>(projectAccountsModel);
		projectAccounts.addListSelectionListener(this);
		projectAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//
		newProject = new JButton(getBundle("Projects").getString(
				"NEW_PROJECT"));
		newProject.addActionListener(this);
		JPanel noord = new JPanel();
		combo = new JComboBox<>();
		combo.addActionListener(this);
		noord.add(combo);
		noord.add(newProject);
		paneelRechts.add(noord, BorderLayout.NORTH);
		JScrollPane scrol = new JScrollPane(projectAccounts);
		paneelRechts.add(scrol);

		paneelRechts.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Projects").getString("PROJECTS")));
		hoofdPaneel.add(paneelRechts);
		//
		setContentPane(hoofdPaneel);
		pack();
		refresh();
	}

	public static ProjectManagementGUI showManager(Accounts accounts, AccountTypes accountTypes, Projects projects) {
		ProjectManagementGUI gui = projectManagementGuis.get(projects);
		if (gui == null) {
			gui = new ProjectManagementGUI(accounts, accountTypes, projects);
			projectManagementGuis.put(projects, gui);
			SaveAllActionListener.addFrame(gui);
		}
		return gui;
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

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == moveTo) {
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
		} else if (ae.getSource() == moveBack) {
			for(Account account : projectAccounts.getSelectedValuesList()) {
				allAccountsModel.addElement(account);
				try {
					project.removeBusinessObject(account);
				} catch (NotEmptyException e) {
					e.printStackTrace();
				}
				projectAccountsModel.removeElement(account);
			}
		} else if (ae.getSource() == newProject) {
			String name = JOptionPane.showInputDialog(getBundle("Projects").getString(
					"ENTER_NAME_FOR_PROJECT"));
			while (name != null && name.equals(""))
				name = JOptionPane.showInputDialog(getBundle("Projects").getString(
						"ENTER_NAME_FOR_PROJECT"));
			if (name != null) {
				Project project = new Project(name, accounts, accountTypes);
				try {
					projects.addBusinessObject(project);
				} catch (EmptyNameException e) {
					e.printStackTrace();
				} catch (DuplicateNameException e) {
					e.printStackTrace();
				}
				((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
				(combo.getModel()).setSelectedItem(project);
			}
		} else if (ae.getSource() == combo) {
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
		} else if (ae.getSource()==addAccount) {
			new NewAccountGUI(accounts, accountTypes).setVisible(true);
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
