package be.dafke.Accounting;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Accounts;
import be.dafke.Accounting.Objects.Project;
import be.dafke.Accounting.Objects.Projects;
import be.dafke.AlfabeticListModel;
import be.dafke.PrefixFilter;
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

/**
 * @author David Danneels
 */
public class ProjectManagerFrame extends RefreshableFrame implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrefixFilter zoeker;
	private final AlfabeticListModel allAccountsModel, projectAccountsModel;
	private final JList<Account> allAccounts, projectAccounts;
	private final JButton moveTo, moveBack, newProject;
	private final JComboBox combo;
	private Project project;

	private final Accountings accountings;

	public ProjectManagerFrame(Accountings accountings) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("PROJECTMANAGER"));
		this.accountings = accountings;
		JPanel hoofdPaneel = new JPanel();
		hoofdPaneel.setLayout(new BoxLayout(hoofdPaneel, BoxLayout.X_AXIS));
		//
		// midden
		JPanel onder = new JPanel();
		moveTo = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("VOEG_TOE"));
		moveTo.addActionListener(this);
		moveTo.setEnabled(false);
		onder.add(moveTo);
		moveBack = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("VERWIJDER"));
		moveBack.addActionListener(this);
		moveBack.setEnabled(false);
		onder.add(moveBack);
		//
		// links
		JPanel paneelLinks = new JPanel();
		allAccountsModel = new AlfabeticListModel();
		allAccounts = new JList(allAccountsModel);
		allAccounts.addListSelectionListener(this);
		allAccounts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		Accounting accounting = accountings.getCurrentAccounting();
//		Accounts accounts = accounting.getAccounts();
		zoeker = new PrefixFilter(allAccounts, onder, new ArrayList<Account>());
		paneelLinks.add(zoeker);
		paneelLinks.setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"Accounting").getString("REKENINGEN")));
		hoofdPaneel.add(paneelLinks);
		//
		// rechts
		JPanel paneelRechts = new JPanel(new BorderLayout());
		// paneelRechts.setLayout(new BoxLayout(paneelRechts,BoxLayout.Y_AXIS));
		projectAccountsModel = new AlfabeticListModel();
		projectAccounts = new JList(projectAccountsModel);
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
//			ArrayList<Account> noProjectlijst = accounts.getAccounts(AccountType.getList());
//			Iterator<Account> it2 = noProjectlijst.iterator();
//			allAccountsModel.removeAllElements();
//			while (it2.hasNext()) {
//				Account account = it2.next();
//				System.out.println("No Project: " + project + " | account" + account);
//				allAccountsModel.addElement(account);
//			}
//		}
		newProject = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString(
				"NIEUW_PROJECT"));
		newProject.addActionListener(this);
		JPanel noord = new JPanel();
		combo = new JComboBox();
		combo.addActionListener(this);
		noord.add(combo);
		noord.add(newProject);
		paneelRechts.add(noord, BorderLayout.NORTH);
		JScrollPane scrol = new JScrollPane(projectAccounts);

		paneelRechts.add(scrol);

		paneelRechts.setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"Accounting").getString("PROJECTEN")));
		hoofdPaneel.add(paneelRechts);
		//
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		Accounting accounting = accountings.getCurrentAccounting();
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
			String naam = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
					"GEEF_NAAM"));
			while (naam != null && naam.equals(""))
				naam = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
						"GEEF_NAAM"));
			if (naam != null) {
				project = new Project(naam);
				project.setAccounting(accounting);
				accounting.getProjects().put(naam, project);
				((DefaultComboBoxModel) combo.getModel()).addElement(project);
				((DefaultComboBoxModel) combo.getModel()).setSelectedItem(project);
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
		Accounting accounting = accountings.getCurrentAccounting();
		Accounts accounts = accounting.getAccounts();
		zoeker.resetMap(accounts.getAccounts());
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
				System.out.println("No Project: " + project + " | account" + account);
				allAccountsModel.addElement(account);
			}
		}
	}
}
