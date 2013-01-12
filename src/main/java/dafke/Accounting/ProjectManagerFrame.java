package be.dafke.Accounting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.dafke.AlfabetischeLijstModel;
import be.dafke.PrefixZoeker;
import be.dafke.RefreshableFrame;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Project;
import be.dafke.Accounting.Objects.Projects;

/**
 * @author David Danneels
 */
public class ProjectManagerFrame extends RefreshableFrame implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrefixZoeker zoeker;
	private final AlfabetischeLijstModel allAccountsModel, projectAccountsModel;
	private final JList allAccounts, projectAccounts;
	private final JButton moveTo, moveBack, newProject;
	private final JComboBox combo;
	private Project project;
//	private final AccountingGUIFrame parent;

	private static ProjectManagerFrame projectManagerFrame;

	public static ProjectManagerFrame getInstance(AccountingGUIFrame parent) {
		if (projectManagerFrame == null) {
			projectManagerFrame = new ProjectManagerFrame(parent);
		}
		parent.addChildFrame(projectManagerFrame);
		return projectManagerFrame;
	}

	private ProjectManagerFrame(AccountingGUIFrame parent) {
		super(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("PROJECTMANAGER"), parent);
		this.parent = parent;
		JPanel hoofdPaneel = new JPanel();
		hoofdPaneel.setLayout(new BoxLayout(hoofdPaneel, BoxLayout.X_AXIS));
		//
		// midden
		JPanel onder = new JPanel();
		moveTo = new JButton(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("VOEG_TOE"));
		moveTo.addActionListener(this);
		moveTo.setEnabled(false);
		onder.add(moveTo);
		moveBack = new JButton(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("VERWIJDER"));
		moveBack.addActionListener(this);
		moveBack.setEnabled(false);
		onder.add(moveBack);
		//
		// links
		JPanel paneelLinks = new JPanel();
		allAccountsModel = new AlfabetischeLijstModel();
		allAccounts = new JList(allAccountsModel);
		allAccounts.addListSelectionListener(this);
		allAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixZoeker(allAccounts, onder, Accountings.getCurrentAccounting().getAccounts().getAccounts());
		paneelLinks.add(zoeker);
		paneelLinks.setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"be/dafke/Accounting/Bundle").getString("REKENINGEN")));
		hoofdPaneel.add(paneelLinks);
		//
		// rechts
		JPanel paneelRechts = new JPanel(new BorderLayout());
		// paneelRechts.setLayout(new BoxLayout(paneelRechts,BoxLayout.Y_AXIS));
		projectAccountsModel = new AlfabetischeLijstModel();
		projectAccounts = new JList(projectAccountsModel);
		projectAccounts.addListSelectionListener(this);
		projectAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrol = new JScrollPane(projectAccounts);
		Projects projects = Accountings.getCurrentAccounting().getProjects();
		Project[] result = new Project[projects.size()];
		projects.values().toArray(result);
		combo = new JComboBox(result);
		combo.addActionListener(this);
		if (result.length != 0) {
			System.out.println("voor init");
			combo.setSelectedItem(result[0]);
			System.out.println("na init");
		} else {
			project = null;
			ArrayList<Account> noProjectlijst = Accountings.getCurrentAccounting().getAccounts().getAccounts(
					AccountType.getList());
			Iterator<Account> it2 = noProjectlijst.iterator();
			allAccountsModel.removeAllElements();
			while (it2.hasNext()) {
				Account account = it2.next();
				System.out.println("No Project: " + project + " | account" + account);
				allAccountsModel.addElement(account);
			}
		}
		newProject = new JButton(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"NIEUW_PROJECT"));
		newProject.addActionListener(this);
		JPanel noord = new JPanel();
		noord.add(combo);
		noord.add(newProject);
		paneelRechts.add(noord, BorderLayout.NORTH);

		paneelRechts.add(scrol);

		paneelRechts.setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"be/dafke/Accounting/Bundle").getString("PROJECTEN")));
		hoofdPaneel.add(paneelRechts);
		//
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(hoofdPaneel);
		pack();
		setVisible(true);
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
			Account account = (Account) allAccounts.getSelectedValue();
			projectAccountsModel.addElement(account);
			Project p = account.getProject();
			if (p != null) p.removeAccount(account);
			project.addAccount(account);
			allAccountsModel.removeElement(account);
		} else if (ae.getSource() == moveBack) {
			Account account = (Account) projectAccounts.getSelectedValue();
			allAccountsModel.addElement(account);
			project.removeAccount(account);
			projectAccountsModel.removeElement(account);
		} else if (ae.getSource() == newProject) {
			String naam = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
					"GEEF_NAAM"));
			while (naam != null && naam.equals(""))
				naam = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
						"GEEF_NAAM"));
			if (naam != null) {
				project = new Project(naam);
				project.setAccounting(Accountings.getCurrentAccounting());
				Accountings.getCurrentAccounting().getProjects().put(naam, project);
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
			ArrayList<Account> noProjectlijst = Accountings.getCurrentAccounting().getAccounts().getAccountNoMatchProject(
					project);
			zoeker.resetMap(noProjectlijst);
		}
	}

	@Override
	public void refresh() {
		zoeker.resetMap(Accountings.getCurrentAccounting().getAccounts().getAccounts());
	}
}
