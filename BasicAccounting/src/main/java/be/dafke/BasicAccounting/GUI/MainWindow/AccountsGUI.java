package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Actions.AccountDetailsLauncher;
import be.dafke.BasicAccounting.Actions.AccountManagementLauncher;
import be.dafke.BasicAccounting.Actions.AccountsPopupMenu;
import be.dafke.BasicAccounting.Actions.AddBookingToTransactionLauncher;
import be.dafke.BasicAccounting.GUI.AccountingPanel;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.Utils.AlphabeticListModel;
import be.dafke.Utils.PrefixFilterPanel;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsGUI extends AccountingPanel implements ListSelectionListener, MouseListener, ActionListener {
	private final PrefixFilterPanel<Account> zoeker;
	private final AlphabeticListModel<Account> model;
	private final JList<Account> lijst;
	private final JButton debet, credit, accountManagement, accountDetails;
	private final List<JCheckBox> boxes;

    private Journal journal;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private final JPanel filter;
    private AccountsPopupMenu popup;

    private Accounting accounting;

    public final String DEBIT = "debit";
    public final String CREDIT = "credit";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private Account selectedAccount = null;
    final AddBookingToTransactionLauncher addBookingToTransactionLauncher = new AddBookingToTransactionLauncher();
    final AccountManagementLauncher accountManagementLauncher = new AccountManagementLauncher();
    final AccountDetailsLauncher accountDetailsLauncher = new AccountDetailsLauncher();

    public AccountsGUI(final Accounting accounting) {
        this.accounting = accounting;
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

        debet.setActionCommand(DEBIT);
        credit.setActionCommand(CREDIT);
        accountManagement.setActionCommand(MANAGE);
        accountDetails.setActionCommand(DETAILS);
        accountManagement.setEnabled(false);
        debet.addActionListener(this);
        credit.addActionListener(this);
        accountManagement.addActionListener(this);
        accountDetails.addActionListener(this);
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

        popup = new AccountsPopupMenu(accounting);

        lijst.addMouseListener(this);//new PopupForListActivator(popup, lijst));//, new AccountDetailsLauncher(accountings)));
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixFilterPanel<Account>(model, lijst, new ArrayList<Account>());
        zoeker.add(hoofdPaneel, BorderLayout.SOUTH);
		add(zoeker, BorderLayout.CENTER);

		filter = new JPanel();
		filter.setLayout(new GridLayout(0, 2));
        boxes = new ArrayList<JCheckBox>();

        setAccounting(accounting);

        for(AccountType type : accountTypes.getBusinessObjects()) {
            JCheckBox checkBox = new JCheckBox(getBundle("Accounting").getString(type.getName().toUpperCase()));
            checkBox.setSelected(true);
            checkBox.setEnabled(false);
            checkBox.setActionCommand(type.getName());
            checkBox.addActionListener(this);
            boxes.add(checkBox);
            filter.add(checkBox);
        }

        add(filter, BorderLayout.NORTH);
	}

	public void valueChanged(ListSelectionEvent lse) {
        selectedAccount = null;
		if (!lse.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
            selectedAccount = lijst.getSelectedValue();
        }
        accountDetails.setEnabled(selectedAccount !=null);
        boolean active = (selectedAccount !=null && journal!=null);
        debet.setEnabled(active);
        credit.setEnabled(active);
	}

    public void buttonClicked(String actionCommand){
        Transaction transaction = accounting.getJournals().getCurrentObject().getCurrentObject();

        if(DEBIT.equals(actionCommand)){
            addBookingToTransactionLauncher.addBookingToTransaction(selectedAccount, transaction, true);
        } else if (CREDIT.equals(actionCommand)){
            addBookingToTransactionLauncher.addBookingToTransaction(selectedAccount, transaction, false);
        } else if (MANAGE.equals(actionCommand)){
            accountManagementLauncher.showAccountManager(accounting);
        } else if (DETAILS.equals(actionCommand)){
            accountDetailsLauncher.showDetails(accounting,lijst.getSelectedValue());
        }
    }

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() instanceof JCheckBox) {
            checkBoxes();
        } else{
            buttonClicked(ae.getActionCommand());
        }
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
        for(JCheckBox checkBox: boxes) {
			checkBox.setEnabled(active);
		}
		accountManagement.setEnabled(active);
		if (active) {
			checkBoxes();
		}
	}

    public void mouseClicked(MouseEvent me) {
        int clickCount = me.getClickCount();
        int button = me.getButton();
        popup.setVisible(false);
        if(clickCount==2){
            accountDetailsLauncher.showDetails(accounting,selectedAccount);
        } else if (button == 3){
            Point location = me.getLocationOnScreen();
            popup.show(null, location.x, location.y);
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}