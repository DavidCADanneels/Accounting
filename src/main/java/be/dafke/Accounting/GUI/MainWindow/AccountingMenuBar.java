package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.Balances.RelationsBalance;
import be.dafke.Accounting.GUI.Balances.ResultBalance;
import be.dafke.Accounting.GUI.Balances.TestBalance;
import be.dafke.Accounting.GUI.Balances.YearBalance;
import be.dafke.Accounting.GUI.CodaManagement.CounterPartyTable;
import be.dafke.Accounting.GUI.CodaManagement.MovementTable;
import be.dafke.Accounting.GUI.MortgageManagement.MortgageGUI;
import be.dafke.Accounting.GUI.Projects.ProjectManagerFrame;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.RefreshEvent;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class AccountingMenuBar extends JMenuBar implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String OPEN_TEST_BALANCE = "test";
	private static final String OPEN_YEAR_BALANCE = "year";
	private static final String OPEN_RELATIONS_BALANCE = "relations";
	private static final String OPEN_RESULT_BALANCE = "result";
	private static final String OPEN_MOVEMENTS = "movements";
	private static final String OPEN_COUNTERPARTIES = "counterparties";
	private static final String OPEN_MORTGAGES = "mortgages";
	private static final String OPEN_PROJECTS = "projects";
	private final JMenu balances, projecten, file, banking;
	private final JMenuItem test, year, result, relate;
	private final JMenuItem projects;
	private final JMenuItem open, startNew, close, save, saveAs, transfer;
	private final JMenuItem movements, counterParties, mortgage;
	private final Accountings accountings;
	private final HashMap<String, RefreshableFrame> frames;

	public AccountingMenuBar(Accountings accountings) {
		this.accountings = accountings;

		// Menu1
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

		for(Accounting acc : accountings.getAccountings()) {
			JMenuItem item = new JMenuItem(acc.toString());
			item.addActionListener(this);
			item.setActionCommand(acc.toString());
			file.add(item);
		}
		add(file);

		frames = new HashMap<String, RefreshableFrame>();
		frames.put(OPEN_RELATIONS_BALANCE, new RelationsBalance(accountings));
		frames.put(OPEN_RESULT_BALANCE, new ResultBalance(accountings));
		frames.put(OPEN_TEST_BALANCE, new TestBalance(accountings));
		frames.put(OPEN_YEAR_BALANCE, new YearBalance(accountings));
		frames.put(OPEN_PROJECTS, new ProjectManagerFrame(accountings));
		frames.put(OPEN_MOVEMENTS, new MovementTable(accountings));
		frames.put(OPEN_COUNTERPARTIES, new CounterPartyTable(accountings));
		frames.put(OPEN_MORTGAGES, new MortgageGUI(accountings));

		// Menu2
		balances = new JMenu(java.util.ResourceBundle.getBundle("Accounting").getString("BALANSEN"));
		balances.setMnemonic(KeyEvent.VK_B);
		test = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
				"PROEF_EN_SALDI-BALANS"));
		year = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString("EINDBALANS"));
		result = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
				"RESULTATENBALANS"));
		relate = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
				"RELATIES-BALANS"));
		test.addActionListener(this);
		year.addActionListener(this);
		result.addActionListener(this);
		relate.addActionListener(this);
		test.setActionCommand(OPEN_TEST_BALANCE);
		year.setActionCommand(OPEN_YEAR_BALANCE);
		result.setActionCommand(OPEN_RESULT_BALANCE);
		relate.setActionCommand(OPEN_RELATIONS_BALANCE);
		// relate.setEnabled(false);
		// result.setEnabled(false);
		// test.setEnabled(false);
		// year.setEnabled(false);
		balances.add(test);
		balances.add(result);
		balances.add(year);
		balances.add(relate);
		add(balances);

		projecten = new JMenu(java.util.ResourceBundle.getBundle("Accounting").getString("PROJECTEN"));
		projecten.setMnemonic(KeyEvent.VK_P);
		projects = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString(
				"PROJECTMANAGER"));
		projects.addActionListener(this);
		// projects.setEnabled(false);
		projects.setActionCommand(OPEN_PROJECTS);
		projecten.add(projects);
		add(projecten);

		banking = new JMenu("Banking");
		movements = new JMenuItem("Show movements");
		movements.setActionCommand(OPEN_MOVEMENTS);
		movements.addActionListener(this);
		// movements.setEnabled(false);
		counterParties = new JMenuItem("Show Counterparties");
		counterParties.setActionCommand(OPEN_COUNTERPARTIES);
		counterParties.addActionListener(this);

		mortgage = new JMenuItem("Mortgages");
		mortgage.addActionListener(this);
		mortgage.setActionCommand(OPEN_MORTGAGES);
		banking.add(movements);
		banking.add(counterParties);
		banking.add(mortgage);
		add(banking);

		// activateButtons();
	}

	public void actionPerformed(ActionEvent ae) {
		JMenuItem item = (JMenuItem) ae.getSource();
//		if (item == test) {
//			TestBalance.getInstance(accountings).setVisible(true);
//		} else if (item == year) {
//			YearBalance.getInstance(accountings).setVisible(true);
//		} else if (item == result) {
//			ResultBalance.getInstance(accountings).setVisible(true);
//		} else if (item == relate) {
//			if (relationsBalance == null) {
//				relationsBalance = new RelationsBalance(accountings);
//			}
//			relationsBalance.setVisible(true);
////			RelationsBalance.getInstance(accountings).setVisible(true);
//		} else if (item == projects) {
//			Accounting accounting = accountings.getCurrentAccounting();
//			ProjectManagerFrame.getInstance(accountings).setVisible(true);
//		} else
		if (item == startNew) {
			Accounting accounting = accountings.newAccounting();
			RefreshEvent event = new RefreshEvent(this);
			System.out.println("notifyAll called in " + this.getClass());
			event.notifyAll();
//			setAccounting(Accounting.newInstance(false));
		} else if (item == open) {
			Accounting accounting = accountings.openInstance();
//			Accountings.openAccounting();
			RefreshEvent event = new RefreshEvent(this);
			System.out.println("notifyAll called in " + this.getClass());
			event.notifyAll();
//			setAccounting(Accounting.openInstance());
		} else if (item == close) {
			System.out.println("Menu.close() --> we don't need this ?");
//			setAccounting(Accounting.closeInstance());
		} else if (item == save) {
//			setAccounting(Accounting.saveInstance());
		} else if (item == saveAs) {
//			setAccounting(Accounting.saveInstanceAs());
//		} else if (item == transfer) {
//			setAccounting(Accounting.newInstance(true));
//		} else if (item == movements) {
//			MovementTable gui = new MovementTable(accountings.getCurrentAccounting().getMovements(), accountings);
//			gui.setVisible(true);
//		} else if (item == counterParties) {
//			CounterPartyTable gui = new CounterPartyTable(accountings);
//			gui.setVisible(true);
//		} else if (item == mortgage) {
//			MortgageGUI.getInstance(accountings).setVisible(true);
//			// MortgageCalculatorGUI.getInstance(parent).setVisible(true);
		} else if (frames.containsKey(item.getActionCommand())) {
			frames.get(item.getActionCommand()).setVisible(true);
		} else {
			accountings.open(ae.getActionCommand());
			RefreshEvent event = new RefreshEvent(this);
//			System.out.println("notifyAll called in " + this.getClass());
			accountings.getApplicationEventPublisher().publishEvent(event);
//			event.notify();
//			event.notifyAll();
		}
//		activateButtons();
	}

//	private void setAccounting(Accounting accounting) {
//		Accountings.setCurrentAccounting(accounting);
//		parent.refresh();
//	}

	private void activateButtons() {
		Accounting accounting = accountings.getCurrentAccounting();
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
