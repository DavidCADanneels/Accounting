package be.dafke.Accounting.GUI.Projects;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Project;
import be.dafke.Accounting.Objects.Accounting.Projects;
import be.dafke.AlphabeticListModel;
import be.dafke.PrefixFilterPanel;
import be.dafke.RefreshableFrame;

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

	public ProjectManagementGUI(Accounting accounting) {
		super(getBundle("Accounting").getString("PROJECTMANAGER") + " (" + accounting.toString() + ")");
		this.accounting = accounting;
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
//		Accounting accounting = accountings.getCurrentAccounting();
//		Accounts accounts = accounting.getAllAccounts();
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
//			ArrayList<Account> noProjectlijst = accounts.getAllAccounts(AccountType.getList());
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
				Project p = account.getProject();
				if (p != null) p.removeAccount(account);
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
				accounting.getProjects().put(naam, project);
				((DefaultComboBoxModel<Project>) combo.getModel()).addElement(project);
				(combo.getModel()).setSelectedItem(project);
			}
		} else if (ae.getSource() == combo) {
			System.out.println("action");
			project = (Project) combo.getSelectedItem();
			projectAccountsModel.removeAllElements();
			for(Account account : project.getAccounts().getAccounts(AccountType.getList())) {
				// System.out.println("Project: " + project + " | account" + account);
				projectAccountsModel.addElement(account);
			}
			ArrayList<Account> noProjectlijst = accounting.getAccounts().getAccountNoMatchProject(project);
			zoeker.resetMap(noProjectlijst);
		}
	}

    @Override
    public void refresh() {
        Accounts accounts = accounting.getAccounts();
        zoeker.resetMap(accounts.getAllAccounts());
        Projects projects = accounting.getProjects();
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
            ArrayList<Account> noProjectlijst = accounts.getAccounts(AccountType.getList());
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
