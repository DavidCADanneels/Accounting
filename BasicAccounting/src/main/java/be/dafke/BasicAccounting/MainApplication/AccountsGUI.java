package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BasicAccounting.AccountsPopupMenu;
import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.*;
import be.dafke.Utils.AlphabeticListModel;
import be.dafke.Utils.PrefixFilterPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsGUI extends AccountingPanel implements ListSelectionListener, MouseListener, ActionListener {
    private final PrefixFilterPanel<Account> zoeker;
	private final AlphabeticListModel<Account> model;
	private final JList<Account> lijst;
	private final JButton debet, credit, accountManagement, accountDetails, addAccount;
	private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType,Boolean> selectedAccountTypes;

    private final JPanel filter;
    private AccountsPopupMenu popup;

    public final String ADD = "add";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private Account selectedAccount = null;
    private ArrayList<AddBookingListener> addBookingListeners = new ArrayList<>();
    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;


    public AccountsGUI() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));

        // BUTTONS
        //
		debet = new JButton(getBundle("Accounting").getString("DEBIT_ACTION"));
        credit = new JButton(getBundle("Accounting").getString("CREDIT_ACTION"));
        addAccount = new JButton("AddAccount");
        accountManagement = new JButton(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        debet.setMnemonic(KeyEvent.VK_D);
        credit.setMnemonic(KeyEvent.VK_C);
        addAccount.setMnemonic(KeyEvent.VK_A);
        accountManagement.setMnemonic(KeyEvent.VK_M);
        accountDetails.setMnemonic(KeyEvent.VK_T);
        //
        addAccount.setActionCommand(ADD);
        accountManagement.setActionCommand(MANAGE);
        accountDetails.setActionCommand(DETAILS);

        debet.addActionListener(e -> {
            for(AddBookingListener addBookingListener : addBookingListeners){
                Transaction transaction = addBookingListener.getCurrentTransaction();
                Booking booking = TransactionActions.addBookingToTransaction(selectedAccount, transaction, true);
                addBookingListener.addBookingToTransaction(booking, transaction);
            }
        });
        credit.addActionListener(e -> {
            for(AddBookingListener addBookingListener : addBookingListeners){
                Transaction transaction = addBookingListener.getCurrentTransaction();
                Booking booking = TransactionActions.addBookingToTransaction(selectedAccount, transaction, false);
                addBookingListener.addBookingToTransaction(booking, transaction);
            }
        });
        addAccount.addActionListener(this);
        accountManagement.addActionListener(this);
        accountDetails.addActionListener(this);

		debet.setEnabled(false);
		credit.setEnabled(false);
        addAccount.setEnabled(false);
		accountDetails.setEnabled(false);
        accountManagement.setEnabled(false);

        // PANEL
        //
		JPanel hoofdPaneel = new JPanel(new BorderLayout());
		JPanel noord = new JPanel();
		noord.add(debet);
		noord.add(credit);
        noord.add(accountDetails);
		JPanel midden = new JPanel();
		// midden.setLayout(new BoxLayout(midden,BoxLayout.Y_AXIS));
		midden.add(accountManagement);
		midden.add(addAccount);
		hoofdPaneel.add(noord, BorderLayout.NORTH);
		hoofdPaneel.add(midden, BorderLayout.CENTER);

        selectedAccountTypes = new HashMap<AccountType, Boolean>();

        // CENTER
        //
		model = new AlphabeticListModel<>();
		lijst = new JList<>(model);
		lijst.addListSelectionListener(this);

        lijst.addMouseListener(this);//new PopupForListActivator(popup, lijst));//, new AccountDetailsLauncher(accountings)));
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixFilterPanel<>(model, lijst, new ArrayList<Account>());
        zoeker.add(hoofdPaneel, BorderLayout.SOUTH);
		add(zoeker, BorderLayout.CENTER);

		filter = new JPanel();
		filter.setLayout(new GridLayout(0, 2));
        boxes = new HashMap<>();

        add(filter, BorderLayout.NORTH);
	}

	public void addAddBookingLister(AddBookingListener addBookingListener){
        addBookingListeners.add(addBookingListener);
    }

	public void valueChanged(ListSelectionEvent lse) {
        selectedAccount = null;
		if (!lse.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
            selectedAccount = lijst.getSelectedValue();
        }
        boolean accountSelected = (selectedAccount != null);
        accountDetails.setEnabled(accountSelected);
        boolean active = false;
        if(accountSelected) {
            for (AddBookingListener addBookingListener : addBookingListeners) {
                active = active || addBookingListener.getCurrentTransaction() != null;
            }
        }
        debet.setEnabled(active);
        credit.setEnabled(active);
	}

    public void buttonClicked(String actionCommand){
        if(accounting!=null) {
            if (MANAGE.equals(actionCommand)) {
                GUIActions.showAccountManager(accounts, accountTypes);
            } else if (DETAILS.equals(actionCommand)) {
                GUIActions.showDetails(lijst.getSelectedValue(), accounting.getJournals());
            } else if (ADD.equals(actionCommand)) {
                new NewAccountGUI(accounts, accountTypes).setVisible(true);
            }
        }
    }

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() instanceof JCheckBox) {
            updateListOfShownAccounts();
            updateListOfCheckedBoxes();
        } else{
            buttonClicked(ae.getActionCommand());
        }
    }

    private void updateListOfCheckedBoxes() {
        for(AccountType type : boxes.keySet()){
            JCheckBox checkBox = boxes.get(type);
            selectedAccountTypes.remove(type);
            selectedAccountTypes.put(type, checkBox.isSelected());
        }
    }

    private void updateListOfShownAccounts() {
        ArrayList<AccountType> types = new ArrayList<>();
        for(AccountType type : selectedAccountTypes.keySet()){
            JCheckBox checkBox = boxes.get(type);
            if(checkBox.isSelected()){
				types.add(type);
			}
		}
		if(accounts!=null) {
            ArrayList<Account> map = accounts.getAccounts(types);
            zoeker.resetMap(map);
        }
	}

    public void setAccounting(Accounting accounting) {
        accounts = accounting.getAccounts();
        accountTypes = accounting.getAccountTypes();
        journals = accounting.getJournals();

        // could be popup.setAccounting() with constructor call in this.constructor
        popup = new AccountsPopupMenu(accounts, accountTypes);

        if(accounting!=null) {
            selectedAccountTypes.clear();
            for(AccountType type : accountTypes.getBusinessObjects()){
                selectedAccountTypes.put(type, Boolean.TRUE);
            }
            boxes.clear();
            filter.removeAll();

            for (AccountType type : accountTypes.getBusinessObjects()) {
                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(type.getName().toUpperCase()));
                checkBox.setSelected(true);
                checkBox.setActionCommand(type.getName());
                checkBox.addActionListener(this);
                boxes.put(type,checkBox);
                filter.add(checkBox);
            }
        }
    }

	public void refresh() {
        boolean active = accounts != null;
        if(accountTypes!=null) {
            for (AccountType type : accountTypes.getBusinessObjects()) {
                JCheckBox checkBox = boxes.get(type);
                checkBox.setSelected(selectedAccountTypes.get(type));
                checkBox.setEnabled(active);
            }
        }
		accountManagement.setEnabled(active);
        addAccount.setEnabled(active);
		if (active) {
			updateListOfShownAccounts();
		}
	}

    public void mouseClicked(MouseEvent me) {
        int clickCount = me.getClickCount();
        int button = me.getButton();
        if(popup!=null) {
            popup.setVisible(false);
            if (clickCount == 2) {
                if(journals!=null) GUIActions.showDetails(selectedAccount, journals);
            } else if (button == 3) {
                Point location = me.getLocationOnScreen();
                popup.show(null, location.x, location.y);
            }
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