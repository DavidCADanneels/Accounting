package be.dafke.Accounting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import be.dafke.Accounting.Balances.RelationsBalance;
import be.dafke.Accounting.Balances.ResultBalance;
import be.dafke.Accounting.Balances.TestBalance;
import be.dafke.Accounting.Balances.YearBalance;
import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Coda.CounterPartyTable;
import be.dafke.Coda.MovementTable;
import be.dafke.Mortgage.MortgageGUI;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JMenu balances, projecten, file, banking;
	private final JMenuItem test, year, result, relate;
	private final JMenuItem projects;
	private final JMenuItem open, startNew, close, save, saveAs, transfer;
	private final JMenuItem movements, counterParties, mortgage;
	private final AccountingGUIFrame parent;

	// private Accounting currentAccounting = null;

	public AccountingMenuBar(AccountingGUIFrame parent) {
		this.parent = parent;
		file = new JMenu("Bestand");
		open = new JMenuItem("Open");
		close = new JMenuItem("Close");
		startNew = new JMenuItem("New");
		save = new JMenuItem("Save (to XML)");
		saveAs = new JMenuItem("Save As ... (rename)");
		transfer = new JMenuItem("Transfer accounting");
		open.addActionListener(this);
		close.addActionListener(this);
		startNew.addActionListener(this);
		save.addActionListener(this);
		saveAs.addActionListener(this);
		transfer.addActionListener(this);
		// close.setEnabled(false);
		// save.setEnabled(false);
		// saveAs.setEnabled(false);
		file.add(startNew);
		file.add(open);
		file.add(close);
		file.add(save);
		file.add(saveAs);
		// file.add(transfer);

		for(Accounting acc : Accountings.getAccountings()) {
			JMenuItem item = new JMenuItem(acc.toString());
			item.addActionListener(this);
			item.setActionCommand(acc.toString());
			file.add(item);
		}
		add(file);

		balances = new JMenu(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("BALANSEN"));
		balances.setMnemonic(KeyEvent.VK_B);
		test = new JMenuItem(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"PROEF_EN_SALDI-BALANS"));
		year = new JMenuItem(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("EINDBALANS"));
		result = new JMenuItem(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"RESULTATENBALANS"));
		relate = new JMenuItem(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"RELATIES-BALANS"));
		test.addActionListener(this);
		year.addActionListener(this);
		result.addActionListener(this);
		relate.addActionListener(this);
		// relate.setEnabled(false);
		// result.setEnabled(false);
		// test.setEnabled(false);
		// year.setEnabled(false);
		balances.add(test);
		balances.add(result);
		balances.add(year);
		balances.add(relate);
		add(balances);

		projecten = new JMenu(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("PROJECTEN"));
		projecten.setMnemonic(KeyEvent.VK_P);
		projects = new JMenuItem(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"PROJECTMANAGER"));
		projects.addActionListener(this);
		// projects.setEnabled(false);
		projecten.add(projects);
		add(projecten);

		banking = new JMenu("Banking");
		movements = new JMenuItem("Show movements");
		movements.addActionListener(this);
		// movements.setEnabled(false);
		counterParties = new JMenuItem("Show Counterparties");
		counterParties.addActionListener(this);
		mortgage = new JMenuItem("Mortgages");
		mortgage.addActionListener(this);
		banking.add(movements);
		banking.add(counterParties);
		banking.add(mortgage);
		add(banking);

		activateButtons();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		JMenuItem item = (JMenuItem) ae.getSource();
		if (item == test) {
			TestBalance.getInstance(parent).setVisible(true);
		} else if (item == year) {
			YearBalance.getInstance(parent).setVisible(true);
		} else if (item == result) {
			ResultBalance.getInstance(parent).setVisible(true);
		} else if (item == relate) {
			RelationsBalance.getInstance(parent).setVisible(true);
		} else if (item == projects) {
			ProjectManagerFrame.getInstance(parent).setVisible(true);
		} else if (item == startNew) {
			Accountings.newAccounting();
			parent.refresh();
//			setAccounting(Accounting.newInstance(false));
		} else if (item == open) {
			Accountings.openInstance();
//			Accountings.openAccounting();
			parent.refresh();
//			setAccounting(Accounting.openInstance());
		} else if (item == close) {
			System.out.println("Menu.close() --> we don't need this ?");
//			setAccounting(Accounting.closeInstance());
		} else if (item == save) {
//			setAccounting(Accounting.saveInstance());
		} else if (item == saveAs) {
//			setAccounting(Accounting.saveInstanceAs());
		} else if (item == transfer) {
//			setAccounting(Accounting.newInstance(true));
		} else if (item == movements) {
			MovementTable.getInstance(parent).setVisible(true);
		} else if (item == counterParties) {
			CounterPartyTable.getInstance(parent).setVisible(true);
		} else if (item == mortgage) {
			MortgageGUI.getInstance(parent).setVisible(true);
			// MortgageCalculatorGUI.getInstance(parent).setVisible(true);
		} else {
			Accountings.open(ae.getActionCommand());
			parent.refresh();
		}
		activateButtons();
	}

//	private void setAccounting(Accounting accounting) {
//		Accountings.setCurrentAccounting(accounting);
//		parent.refresh();
//	}

	private void activateButtons() {
		Accounting accounting = Accountings.getCurrentAccounting();
		boolean active = (accounting != null);
		startNew.setEnabled(!active);
		open.setEnabled(!active);
		close.setEnabled(active);
		save.setEnabled(active);// && !accounting.isSaved());
		saveAs.setEnabled(active);
		projects.setEnabled(active);
		relate.setEnabled(active);
		result.setEnabled(active);
		test.setEnabled(active);
		year.setEnabled(active);
		// movements.setEnabled(active);
	}
}
