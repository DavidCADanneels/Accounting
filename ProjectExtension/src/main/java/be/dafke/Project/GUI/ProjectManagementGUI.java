package be.dafke.Project.GUI;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.Project.Objects.Project;
import be.dafke.Project.Objects.Projects;
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
import java.util.Iterator;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class ProjectManagementGUI extends RefreshableFrame implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrefixFilterPanel<Account> zoeker;
	private final AlphabeticListModel<Account> allAccountsModel, projectAccountsModel;
	private final JList<Account> allAccounts, projectAccounts;
	private final JButton moveTo, moveBack, newProject;
	private final JComboBox<Project> combo;
	private Project project;
	private final Accounting accounting;
    private final Projects projects;

    public ProjectManagementGUI(Accounting accounting, Projects projects) {
		super(getBundle("Accounting").getString("PROJECTMANAGER") + " (" + accounting.toString() + ")");
		this.accounting = accounting;
        this.projects = projects;
		JPanel hoofdPaneel = new JPanel();
		hoofdPaneel.setLayout(new BoxLayout(hoofdPaneel, BoxLayout.X_AXIS));
		//
		// midden
		JPanel onder = new JPanel();
		moveTo = new JButton(getBundle("Accounting").getString("VOEG_TOE"));
		moveTo.addActionListener(this);
		moveTo.setEnabled(false);
		onder.add(moveTo);
		moveBack = new JButton(getBundle("Accounting").getString("VERWIJDER"));
		moveBack.addActionListener(this);
		moveBack.setEnabled(false);
		onder.add(moveBack);
		//
		// links
		JPanel paneelLinks = new JPanel();
		allAccountsModel = new AlphabeticListModel<Account>();
		allAccounts = new JList<Account>(allAccountsModel);
		allAccounts.addListSelectionListener(this);
		allAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		Accounting accounting = accountings.getCurrentObject();
//		Accounts accounts = accounting.getBusinessObjects();
		zoeker = new PrefixFilterPanel<Account>(allAccountsModel, allAccounts, new ArrayList<Account>());
        zoeker.add(onder, BorderLayout.SOUTH);
		paneelLinks.add(zoeker);
		paneelLinks.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("REKENINGEN")));
		hoofdPaneel.add(paneelLinks);
		//
		// rechts
		JPanel paneelRechts = new JPanel(new BorderLayout());
		// paneelRechts.setLayout(new BoxLayout(paneelRechts,BoxLayout.Y_AXIS));
		projectAccountsModel = new AlphabeticListModel<Account>();
		projectAccounts = new JList<Account>(projectAccountsModel);
		projectAccounts.addListSelectionListener(this);
		projectAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		Projects projects = accounting.getProjects();
//		JScrollPane scrol = new JScrollPane(projectAccounts);
//		Project[] result = new Project[projects.size()];
//		projects.values().toArray(result);
//		combo = new JComboBox(result);
//		combo.addActionListener(this);
//		if (result.length != 0) {
//			System.out.println("voor init");
//			combo.setSelectedItem(result[0]);
//			System.out.println("na init");
//		} else {
//			project = null;
//			ArrayList<Account> noProjectlijst = accounts.getBusinessObjects(AccountType.getList());
//			Iterator<Account> it2 = noProjectlijst.iterator();
//			allAccountsModel.removeAllElements();
//			while (it2.hasNext()) {
//				Account account = it2.next();
//				System.out.println("No Project: " + project + " | account" + account);
//				allAccountsModel.addElement(account);
//			}
//		}
		newProject = new JButton(getBundle("Accounting").getString(
				"NIEUW_PROJECT"));
		newProject.addActionListener(this);
		JPanel noord = new JPanel();
		combo = new JComboBox<Project>();
		combo.addActionListener(this);
		noord.add(combo);
		noord.add(newProject);
		paneelRechts.add(noord, BorderLayout.NORTH);
		JScrollPane scrol = new JScrollPane(projectAccounts);

		paneelRechts.add(scrol);

		paneelRechts.setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("PROJECTEN")));
		hoofdPaneel.add(paneelRechts);
		//
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(hoofdPaneel);
		pack();
		// setVisible(true);
	}

	@Override
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

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == moveTo) {
			for(Account account : allAccounts.getSelectedValuesList()) {
				projectAccountsModel.addElement(account);
                // TODO check if account belongs to another project (and remove it there ?)
				project.addAccount(account);
				allAccountsModel.removeElement(account);
			}
		} else if (ae.getSource() == moveBack) {
			for(Account account : projectAccounts.getSelectedValuesList()) {
				allAccountsModel.addElement(account);
				project.removeAccount(account);
				projectAccountsModel.removeElement(account);
			}
		} else if (ae.getSource() == newProject) {
			String naam = JOptionPane.showInputDialog(getBundle("Accounting").getString(
					"GEEF_NAAM"));
			while (naam != null && naam.equals(""))
				naam = JOptionPane.showInputDialog(getBundle("Accounting").getString(
						"GEEF_NAAM"));
			if (naam != null) {
				project = new Project(naam);
				projects.put(naam, project);
				((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
				(combo.getModel()).setSelectedItem(project);
			}
		} else if (ae.getSource() == combo) {
			System.out.println("action");
			project = (Project) combo.getSelectedItem();
			projectAccountsModel.removeAllElements();
			for(Account account : project.getAccounts()) {
				// System.out.println("Project: " + project + " | account" + account);
				projectAccountsModel.addElement(account);
			}
			ArrayList<Account> noProjectlijst = getAccountNoMatchProject(project);
			zoeker.resetMap(noProjectlijst);
		}
	}

    public ArrayList<Account> getAccountNoMatchProject(Project project) {
		ArrayList<Account> result = new ArrayList<Account>();
		for(Account account : accounting.getAccounts().getBusinessObjects()) {
            if (!project.getAccounts().contains(account)){
                result.add(account);
            }
		}
		return result;
	}

    @Override
    public void refresh() {
        Accounts accounts = accounting.getAccounts();
        zoeker.resetMap(accounts.getBusinessObjects());
        for(Project project : projects.values()) {
            combo.addItem(project);
        }
        Project[] result = new Project[projects.size()];
        projects.values().toArray(result);
        if (result.length != 0) {
            System.out.println("voor init");
            combo.setSelectedItem(result[0]);
            System.out.println("na init");
        } else {
            project = null;
            ArrayList<Account> noProjectlijst = accounts.getAccounts(accounting.getAccountTypes().getBusinessObjects());
            Iterator<Account> it2 = noProjectlijst.iterator();
            allAccountsModel.removeAllElements();
            while (it2.hasNext()) {
                Account account = it2.next();
//				System.out.println("No Project: " + project + " | account" + account);
                allAccountsModel.addElement(account);
            }
        }
	}
}
