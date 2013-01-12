package be.dafke.Accounting;

import be.dafke.Accounting.Details.AccountDetails;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Transaction;
import be.dafke.AlfabetischeLijstModel;
import be.dafke.PrefixZoeker;
import be.dafke.RefreshableFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author David Danneels
 */

public class AccountsGUI extends JPanel implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrefixZoeker zoeker;
	private final AlfabetischeLijstModel model;
	private final JList lijst;
	private final JButton debet, credit, nieuw, details;
	private final JournalGUI dagboekGUI;
	private final AccountingGUIFrame parent;
	private RefreshableFrame detailsGui;

	private final JCheckBox[] boxes;

	public AccountsGUI(JournalGUI journalGUI, AccountingGUIFrame parent) {
		this.parent = parent;
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
				"Accounting").getString("REKENINGEN")));
		dagboekGUI = journalGUI;
		debet = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("DEBITEER"));
		debet.setMnemonic(KeyEvent.VK_D);
		credit = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString("CREDITEER"));
		credit.setMnemonic(KeyEvent.VK_C);
		nieuw = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString(
				"BEHEER_REKENING"));
		nieuw.setMnemonic(KeyEvent.VK_N);
		nieuw.setEnabled(false);
		details = new JButton(java.util.ResourceBundle.getBundle("Accounting").getString(
				"BEKIJK_REKENING"));
		debet.addActionListener(this);
		credit.addActionListener(this);
		nieuw.addActionListener(this);
		details.addActionListener(this);
		debet.setEnabled(false);
		credit.setEnabled(false);
		details.setEnabled(false);
		JPanel hoofdPaneel = new JPanel(new BorderLayout());
		JPanel noord = new JPanel();
		noord.add(debet);
		noord.add(credit);
		JPanel midden = new JPanel();
		// midden.setLayout(new BoxLayout(midden,BoxLayout.Y_AXIS));
		midden.add(nieuw);
		midden.add(details);
		hoofdPaneel.add(noord, BorderLayout.NORTH);
		hoofdPaneel.add(midden, BorderLayout.CENTER);

		model = new AlfabetischeLijstModel();
		lijst = new JList(model);
		lijst.addListSelectionListener(this);
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixZoeker(lijst, hoofdPaneel, new ArrayList<Account>());
		// zoeker=new PrefixZoeker(lijst,hoofdPaneel,parent.getAccounting().getAccounts());
		add(zoeker, BorderLayout.CENTER);

		JPanel filter = new JPanel();
		filter.setLayout(new GridLayout(0, 2));
		AccountType[] types = AccountType.values();
		boxes = new JCheckBox[types.length];
		for(int i = 0; i < types.length; i++) {
			boxes[i] = new JCheckBox(types[i].name());
			boxes[i].setSelected(true);
			boxes[i].setEnabled(false);
			boxes[i].setActionCommand(types[i].name());
			boxes[i].addActionListener(this);
			filter.add(boxes[i]);
		}

		add(filter, BorderLayout.NORTH);
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		if (!lse.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
			debet.setEnabled(true);
			credit.setEnabled(true);
			details.setEnabled(true);
		} else {
			debet.setEnabled(false);
			credit.setEnabled(false);
			details.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == debet || ae.getSource() == credit) {
			book(ae.getSource() == debet);
		} else if (ae.getSource() == nieuw) {
			NewAccountGUI.getInstance(parent).setVisible(true);
		} else if (ae.getSource() == details) {
			// if(!parent.containsFrame(detailsGui)){
			// TODO: if parent contains frame detail_THIS_ACCOUNT
			Account account = (Account) lijst.getSelectedValue();
			detailsGui = new AccountDetails(account, parent);
			parent.addChildFrame(detailsGui);
			// }else detailsGui.setVisible(true);
		} else if (ae.getSource() instanceof JCheckBox) {
			checkBoxes();
		}
	}

	private void book(boolean debit) {
		Account rekening = (Account) lijst.getSelectedValue();
		boolean ok = false;
		while (!ok) {
			String s = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
					"GEEF_BEDRAG"));
			if (s == null || s.equals("")) {
				ok = true;
			} else {
				try {
					BigDecimal amount = new BigDecimal(s);
					amount = amount.setScale(2);
					if (debit) {
						// veranderen naar Transaction.addBooking(newBooking)?
						Transaction.getInstance().debiteer(rekening, amount);
					} else {
						Transaction.getInstance().crediteer(rekening, amount);
					}
					dagboekGUI.refresh();
					ok = true;
					BigDecimal debettotaal = Transaction.getInstance().getDebetTotaal();
					BigDecimal credittotaal = Transaction.getInstance().getCreditTotaal();
					if (debettotaal.compareTo(credittotaal) == 0) dagboekGUI.setOK();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(this,
							java.util.ResourceBundle.getBundle("Accounting").getString("INVALID_INPUT"));
				}
			}
		}
	}

	private void checkBoxes() {
		ArrayList<AccountType> types = new ArrayList<AccountType>();
		for(JCheckBox box : boxes) {
			if (box.isSelected()) {
				types.add(AccountType.valueOf(box.getActionCommand()));
			}
		}
		ArrayList<Account> map = Accountings.getCurrentAccounting().getAccounts().getAccounts(types);
		zoeker.resetMap(map);
	}

	public void activateButtons(/*boolean active*/) {
		boolean active = Accountings.isActive();
		/*
		 * if (active) { zoeker.resetMap(parent.getAccounting().getAccounts()); } else { zoeker.resetMap(new
		 * ArrayList<Account>()); }
		 */for(int i = 0; i < boxes.length; i++) {
			// boxes[i].setSelected(active);
			boxes[i].setEnabled(active);
		}
		nieuw.setEnabled(active);
		if (active) {
			checkBoxes();
		}
	}
}