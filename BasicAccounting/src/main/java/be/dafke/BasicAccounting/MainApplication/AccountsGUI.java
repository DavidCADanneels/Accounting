package be.dafke.BasicAccounting.MainApplication;

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
	private final JButton debet, credit, accountManagement, accountDetails;
	private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType,Boolean> selectedAccountTypes;

    private Accounting accounting;
    private final JPanel filter;
    private AccountsPopupMenu popup;

    public final String DEBIT = "debit";
    public final String CREDIT = "credit";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private Account selectedAccount = null;

    public AccountsGUI(Accounting accounting) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));

        // BUTTONS
        //
		debet = new JButton(getBundle("Accounting").getString("DEBIT_ACTION"));
        credit = new JButton(getBundle("Accounting").getString("CREDIT_ACTION"));
        accountManagement = new JButton(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"));
        debet.setMnemonic(KeyEvent.VK_D);
        credit.setMnemonic(KeyEvent.VK_C);
        accountManagement.setMnemonic(KeyEvent.VK_M);
        accountDetails.setMnemonic(KeyEvent.VK_T);
        //
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

        // PANEL
        //
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

        selectedAccountTypes = new HashMap<AccountType, Boolean>();

        // CENTER
        //
		model = new AlphabeticListModel<>();
		lijst = new JList<>(model);
		lijst.addListSelectionListener(this);

        popup = new AccountsPopupMenu(accounting);

        lijst.addMouseListener(this);//new PopupForListActivator(popup, lijst));//, new AccountDetailsLauncher(accountings)));
		lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		zoeker = new PrefixFilterPanel<>(model, lijst, new ArrayList<Account>());
        zoeker.add(hoofdPaneel, BorderLayout.SOUTH);
		add(zoeker, BorderLayout.CENTER);

		filter = new JPanel();
		filter.setLayout(new GridLayout(0, 2));
        boxes = new HashMap<AccountType,JCheckBox>();

        setAccounting(accounting);

        add(filter, BorderLayout.NORTH);
	}

	public void valueChanged(ListSelectionEvent lse) {
        selectedAccount = null;
		if (!lse.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
            selectedAccount = lijst.getSelectedValue();
        }
        accountDetails.setEnabled(selectedAccount !=null);
        boolean active = (selectedAccount !=null && accounting!=null && accounting.getJournals()!=null && accounting.getJournals().getCurrentObject()!=null);
        debet.setEnabled(active);
        credit.setEnabled(active);
	}

    public void buttonClicked(String actionCommand){
        if (MANAGE.equals(actionCommand)){
            GUIActions.showAccountManager(accounting);
        } else if (DETAILS.equals(actionCommand)){
            GUIActions.showDetails(lijst.getSelectedValue(),accounting.getJournals());
        } else {
            Transaction transaction = accounting.getJournals().getCurrentObject().getCurrentObject();
            if(DEBIT.equals(actionCommand)){
                TransactionActions.addBookingToTransaction(accounting.getAccounts(), selectedAccount, transaction, true);
            } else if (CREDIT.equals(actionCommand)){
                TransactionActions.addBookingToTransaction(accounting.getAccounts(), selectedAccount, transaction, false);
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
		ArrayList<Account> map = accounting.getAccounts().getAccounts(types);
		zoeker.resetMap(map);
	}

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;

        if(accounting!=null) {
            selectedAccountTypes.clear();
            AccountTypes accountTypes = accounting.getAccountTypes();
            for(AccountType type : accountTypes.getBusinessObjects()){
                selectedAccountTypes.put(type, Boolean.TRUE);
            }
            boxes.clear();
            filter.removeAll();

            for (AccountType type : accounting.getAccountTypes().getBusinessObjects()) {
                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(type.getName().toUpperCase()));
                checkBox.setSelected(selectedAccountTypes.get(type));
                checkBox.setEnabled(false);
                checkBox.setActionCommand(type.getName());
                checkBox.addActionListener(this);
                boxes.put(type,checkBox);
                filter.add(checkBox);
            }
        }
    }

	public void refresh() {
        boolean active = accounting!=null && accounting.getAccounts()!=null;
        for (AccountType type : accounting.getAccountTypes().getBusinessObjects()) {
            JCheckBox checkBox = boxes.get(type);
            checkBox.setSelected(selectedAccountTypes.get(type));
            checkBox.setEnabled(active);
        }
		accountManagement.setEnabled(active);
		if (active) {
			updateListOfShownAccounts();
		}
	}

    public void mouseClicked(MouseEvent me) {
        int clickCount = me.getClickCount();
        int button = me.getButton();
        popup.setVisible(false);
        if(clickCount==2){
            GUIActions.showDetails(selectedAccount, accounting.getJournals());
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