package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.GUI.Details.AccountDetails;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.AlphabeticListModel;
import be.dafke.DisposableComponent;
import be.dafke.PrefixFilterPanel;

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

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsGUI extends JPanel implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PrefixFilterPanel<Account> zoeker;
	private final AlphabeticListModel<Account> model;
	private final JList<Account> lijst;
	private final JButton debet, credit, accountManagement, details;
	private final JCheckBox[] boxes;
	private Accounting accounting;

	public AccountsGUI(Accounting accounting) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("REKENINGEN")));
		this.accounting = accounting;
		debet = new JButton(getBundle("Accounting").getString("DEBITEER"));
		debet.setMnemonic(KeyEvent.VK_D);
		credit = new JButton(getBundle("Accounting").getString("CREDITEER"));
		credit.setMnemonic(KeyEvent.VK_C);
		accountManagement = new JButton(getBundle("Accounting").getString(
				"BEHEER_REKENING"));
		accountManagement.setMnemonic(KeyEvent.VK_N);
		accountManagement.setEnabled(false);
		details = new JButton(getBundle("Accounting").getString(
				"BEKIJK_REKENING"));
		debet.addActionListener(this);
		credit.addActionListener(this);
		accountManagement.addActionListener(this);
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
		midden.add(accountManagement);
		midden.add(details);
		hoofdPaneel.add(noord, BorderLayout.NORTH);
		hoofdPaneel.add(midden, BorderLayout.CENTER);

		model = new AlphabeticListModel<Account>();
		lijst = new JList<Account>(model);
		lijst.addListSelectionListener(this);
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixFilterPanel<Account>(model, lijst, new ArrayList<Account>());
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
		} else if (ae.getSource() == accountManagement) {
            String key = accounting.toString()+ComponentMap.ACCOUNT_MANAGEMENT;
            ComponentMap.getDisposableComponent(key).setVisible(true);
        } else if (ae.getSource() == details) {
            Account account = lijst.getSelectedValue();
            String key = accounting.toString() + ComponentMap.ACCOUNT_DETAILS + account.getName();
            DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
            if(gui == null){
                gui = new AccountDetails(account, accounting);
                ComponentMap.addDisposableComponent(key, gui); // DETAILS
            }
			gui.setVisible(true);
		} else if (ae.getSource() instanceof JCheckBox) {
			checkBoxes();
		}
	}

	private void book(boolean debit) {
		Account rekening = lijst.getSelectedValue();
		boolean ok = false;
		while (!ok) {
			String s = JOptionPane.showInputDialog(getBundle("Accounting").getString(
					"GEEF_BEDRAG"));
			if (s == null || s.equals("")) {
				ok = true;
			} else {
				try {
					BigDecimal amount = new BigDecimal(s);
					amount = amount.setScale(2);
                    boolean merge = false;
                    Transaction transaction = accounting.getCurrentTransaction();
                    if(transaction.contains(rekening)){
                        merge = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Merge Bookings?", "The current transaction already contains bookings for "
                                + rekening +". Do you want to merge them?", JOptionPane.YES_NO_OPTION);
                    }
					if (debit) {
						// TODO: veranderen naar Transaction.addBooking(newBooking)?
						transaction.debiteer(rekening, amount, merge);
					} else {
						transaction.crediteer(rekening, amount, merge);
					}
					ok = true;
                    ComponentMap.refreshAllFrames();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(this,
							getBundle("Accounting").getString("INVALID_INPUT"));
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
		ArrayList<Account> map = accounting.getAccounts().getAccounts(types);
		zoeker.resetMap(map);
	}

    public void setAccounting(Accounting accounting){
        this.accounting = accounting;
        refresh();
    }

	private void refresh() {
		boolean active = accounting!=null;
		for(JCheckBox checkBox: boxes) {
			checkBox.setEnabled(active);
		}
		accountManagement.setEnabled(active);
		if (active) {
			checkBoxes();
		}
	}
}