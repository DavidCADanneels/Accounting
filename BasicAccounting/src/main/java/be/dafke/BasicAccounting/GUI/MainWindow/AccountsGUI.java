package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.AccountDetailsActionListener;
import be.dafke.BasicAccounting.Actions.AccountManagementActionListener;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;
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
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsGUI extends AccountingPanel implements ListSelectionListener, ActionListener {
	private final PrefixFilterPanel<Account> zoeker;
	private final AlphabeticListModel<Account> model;
	private final JList<Account> lijst;
	private final JButton debet, credit, accountManagement, accountDetails;
	private final List<JCheckBox> boxes;

    private Journal journal;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private final JPanel filter;

    public AccountsGUI(ActionListener actionListener, Accountings accountings) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));
		debet = new JButton(getBundle("Accounting").getString("DEBIT_ACTION"));
        credit = new JButton(getBundle("Accounting").getString("CREDIT_ACTION"));
        accountManagement = new JButton(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"));
        debet.setMnemonic(KeyEvent.VK_D);
        credit.setMnemonic(KeyEvent.VK_C);
        accountManagement.setMnemonic(KeyEvent.VK_M);
        accountDetails.setMnemonic(KeyEvent.VK_T);
        accountManagement.setEnabled(false);
		debet.addActionListener(this);
		credit.addActionListener(this);
		accountManagement.addActionListener(new AccountManagementActionListener(accountings));
        accountDetails.addActionListener(new AccountDetailsActionListener(accountings));
		debet.setEnabled(false);
		credit.setEnabled(false);
		accountDetails.setEnabled(false);
		JPanel hoofdPaneel = new JPanel(new BorderLayout());
		JPanel noord = new JPanel();
		noord.add(debet);
		noord.add(credit);
		JPanel midden = new JPanel();
		// midden.setLayout(new BoxLayout(midden,BoxLayout.Y_AXIS));
		midden.add(accountManagement);
		midden.add(accountDetails);
		hoofdPaneel.add(noord, BorderLayout.NORTH);
		hoofdPaneel.add(midden, BorderLayout.CENTER);

		model = new AlphabeticListModel<Account>();
		lijst = new JList<Account>(model);
		lijst.addListSelectionListener(this);
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixFilterPanel<Account>(model, lijst, new ArrayList<Account>());
        zoeker.add(hoofdPaneel, BorderLayout.SOUTH);
		add(zoeker, BorderLayout.CENTER);

		filter = new JPanel();
		filter.setLayout(new GridLayout(0, 2));
        boxes = new ArrayList<JCheckBox>();
		add(filter, BorderLayout.NORTH);
	}

	@Override
	public void valueChanged(ListSelectionEvent lse) {
        Account account = null;
		if (!lse.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
            account = lijst.getSelectedValue();
            accounts.setCurrentObject(account);
        }
        accountDetails.setEnabled(account!=null);
        boolean active = (account!=null && journal!=null);
        debet.setEnabled(active);
        credit.setEnabled(active);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == debet || ae.getSource() == credit) {
			book(ae.getSource() == debet);
		} else if (ae.getSource() instanceof JCheckBox) {
			checkBoxes();
		}
	}

	private void book(boolean debit) {
		Account account = accounts.getCurrentObject();
        Transaction transaction = journal.getCurrentObject();
        BigDecimal creditTotal = transaction.getCreditTotaal();
        BigDecimal debitTotal = transaction.getDebetTotaal();
        boolean suggestion = false;
        BigDecimal suggestedAmount = null;
        if(creditTotal.compareTo(debitTotal)>0 && debit){
            suggestion = true;
            suggestedAmount = creditTotal.subtract(debitTotal);
        } else if(debitTotal.compareTo(creditTotal)>0 && !debit){
            suggestion = true;
            suggestedAmount = debitTotal.subtract(creditTotal);
        } else {
            BigDecimal defaultAmount = account.getDefaultAmount();
            if(defaultAmount!=null){
                suggestion = true;
                suggestedAmount = defaultAmount;
            }
        }
        boolean ok = false;
		while (!ok) {
			String s;
            if(suggestion){
                s = JOptionPane.showInputDialog(getBundle("Accounting").getString(
					"ENTER_AMOUNT"), suggestedAmount.toString());
            } else {
                s = JOptionPane.showInputDialog(getBundle("Accounting").getString(
                        "ENTER_AMOUNT"));
            }
			if (s == null || s.equals("")) {
				ok = true;
			} else {
				try {
					BigDecimal amount = new BigDecimal(s);
					amount = amount.setScale(2);
                    Booking booking = new Booking(account);
                    booking.addBusinessObject(new Movement(amount,debit));
                    transaction.addBusinessObject(booking);
					ok = true;
                    ComponentMap.refreshAllFrames();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(this,
							getBundle("Accounting").getString("INVALID_INPUT"));
				}
			}
		}
	}

    private void addCheckBoxes(){
        filter.removeAll();
        boxes.clear();
        if(accountTypes!=null){
            for(AccountType type : accountTypes.getBusinessObjects()) {
                if(!type.getName().equals("Mortgage")){
                    JCheckBox checkBox = new JCheckBox(getBundle("Accounting").getString(type.getName().toUpperCase()));
                    checkBox.setSelected(true);
                    checkBox.setEnabled(false);
                    checkBox.setActionCommand(type.getName());
                    checkBox.addActionListener(this);
                    boxes.add(checkBox);
                    filter.add(checkBox);
                }
            }
        }
//        revalidate();
    }

	private void checkBoxes() {
        ArrayList<AccountType> types = new ArrayList<AccountType>();
		for(JCheckBox box : boxes) {
			if (box.isSelected()) {
				types.add(accountTypes.getBusinessObject(box.getActionCommand()));
			}
		}
		ArrayList<Account> map = accounts.getAccounts(types);
		zoeker.resetMap(map);
	}

    public void setAccounting(Accounting accounting){
        if(accounting == null){
            setAccountTypes(null);
            setAccounts(null);
            setJournal(null);
        } else {
            setAccountTypes(accounting.getAccountTypes());
            setAccounts(accounting.getAccounts());
            if(accounting.getJournals()==null){
                setJournal(null);
            } else {
                setJournal(accounting.getJournals().getCurrentObject());
            }
        }
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void setAccounts(Accounts accounts){
        this.accounts = accounts;
    }

    public void setJournal(Journal journal){
        this.journal = journal;
    }

	public void refresh() {
        boolean active = accounts!=null;
        addCheckBoxes();
        for(JCheckBox checkBox: boxes) {
			checkBox.setEnabled(active);
		}
		accountManagement.setEnabled(active);
		if (active) {
			checkBoxes();
		}
	}
}