package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.Details.AccountDetails;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Transaction;
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
	private final PrefixFilterPanel zoeker;
	private final AlphabeticListModel model;
	private final JList lijst;
	private final JButton debet, credit, nieuw, details;
	private final JournalGUI journalGUI;
	private RefreshableFrame detailsGui;
	private final JCheckBox[] boxes;
	private final Accountings accountings;

	public AccountsGUI(Accountings accountings, JournalGUI journalGUI) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), java.util.ResourceBundle.getBundle(
                "Accounting").getString("REKENINGEN")));
		this.accountings = accountings;
		this.journalGUI = journalGUI;
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

		model = new AlphabeticListModel();
		lijst = new JList(model);
		lijst.addListSelectionListener(this);
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixFilterPanel(model, lijst, new ArrayList<Account>());
        zoeker.add(hoofdPaneel, BorderLayout.SOUTH);
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
            RefreshableFrame frame = AccountingMenuBar.getFrame(AccountingMenuBar.NEW_ACCOUNT);
            if(frame == null){
                System.err.println("frame not found");
            }
            frame.setVisible(true);
        } else if (ae.getSource() == details) {
            Account account = (Account) lijst.getSelectedValue();
            RefreshableFrame gui = AccountingMenuBar.getFrame("ACCOUNT_"+account.getName());
            if(gui == null){
                gui = new AccountDetails(account);
                AccountingMenuBar.addFrame("ACCOUNT_"+account.getName(), gui);
            }
			gui.setVisible(true);
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
                    boolean merge = false;
                    if(Transaction.getInstance().contains(rekening)){
                        merge = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Merge Bookings?", "The current transaction already contains bookings for "
                                + rekening +". Do you want to merge them?", JOptionPane.YES_NO_OPTION);
                    }
					if (debit) {
						// TODO: veranderen naar Transaction.addBooking(newBooking)?
						Transaction.getInstance().debiteer(rekening, amount, merge);
					} else {
						Transaction.getInstance().crediteer(rekening, amount, merge);
					}
					journalGUI.refresh();
					ok = true;
					BigDecimal debettotaal = Transaction.getInstance().getDebetTotaal();
					BigDecimal credittotaal = Transaction.getInstance().getCreditTotaal();
					if (debettotaal.compareTo(credittotaal) == 0) journalGUI.setOK();
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
		Accounting accounting = accountings.getCurrentAccounting();
		ArrayList<Account> map = accounting.getAccounts().getAccounts(types);
		zoeker.resetMap(map);
	}

	public void refresh() {
		boolean active = accountings.isActive();
		for(int i = 0; i < boxes.length; i++) {
			boxes[i].setEnabled(active);
		}
		nieuw.setEnabled(active);
		if (active) {
			checkBoxes();
		}
	}
}