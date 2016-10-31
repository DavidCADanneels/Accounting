package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BasicAccounting.AccountsTablePopupMenu;
import be.dafke.BasicAccounting.GUIActions;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsTableGUI extends AccountingPanel implements ListSelectionListener, MouseListener, ActionListener {
    private final RefreshableTable<Account> table;
	private final JButton debet, credit, accountManagement, accountDetails, addAccount;
    private final AccountDataModel accountDataModel;

    private AccountsTablePopupMenu popup;

    public final String DEBIT = "debit";
    public final String CREDIT = "credit";
    public final String ADD = "add";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private Account selectedAccount = null;

    public AccountsTableGUI() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));

        // CENTER
        //
        accountDataModel = new AccountDataModel();
        table = new RefreshableTable<>(accountDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new AccountsTablePopupMenu(accounting, table);
        table.addMouseListener(new PopupForTableActivator(popup, table));

        JScrollPane scrollPane1 = new JScrollPane(table);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
        add(center, BorderLayout.CENTER);

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
        debet.setActionCommand(DEBIT);
        credit.setActionCommand(CREDIT);
        addAccount.setActionCommand(ADD);
        accountManagement.setActionCommand(MANAGE);
        accountDetails.setActionCommand(DETAILS);

        debet.addActionListener(this);
        credit.addActionListener(this);
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
        noord.add(addAccount);
		JPanel midden = new JPanel();
		// midden.setLayout(new BoxLayout(midden,BoxLayout.Y_AXIS));
		midden.add(accountManagement);
		midden.add(accountDetails);
		hoofdPaneel.add(noord, BorderLayout.NORTH);
		hoofdPaneel.add(midden, BorderLayout.CENTER);
	}

	public void valueChanged(ListSelectionEvent lse) {
        selectedAccount = table.getSelectedObject();
        accountDetails.setEnabled(selectedAccount !=null);
        boolean active = (selectedAccount !=null && accounting!=null && accounting.getJournals()!=null && accounting.getJournals().getCurrentObject()!=null);
        debet.setEnabled(active);
        credit.setEnabled(active);
	}

	public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if(accounting!=null) {
            if (MANAGE.equals(actionCommand)) {
                GUIActions.showAccountManager(accounting);
            } else if (DETAILS.equals(actionCommand)) {
                GUIActions.showDetails(table.getSelectedObject(), accounting.getJournals());
            } else if (ADD.equals(actionCommand)) {
                new NewAccountGUI(accounting).setVisible(true);
            } else {
                Transaction transaction = accounting.getJournals().getCurrentObject().getCurrentObject();
                if (DEBIT.equals(actionCommand)) {
                    TransactionActions.addBookingToTransaction(accounting.getAccounts(), selectedAccount, transaction, true);
                } else if (CREDIT.equals(actionCommand)) {
                    TransactionActions.addBookingToTransaction(accounting.getAccounts(), selectedAccount, transaction, false);
                }
            }
        }
    }

    public void setAccounting(Accounting accounting) {
        super.setAccounting(accounting);
        accountDataModel.setAccounts(accounting.getAccounts());

        // could be popup.setAccounting() with constructor call in this.constructor
        popup = new AccountsTablePopupMenu(accounting, table);
        table.addMouseListener(new PopupForTableActivator(popup, table, 0,2,3,4));
    }

	public void refresh() {
        boolean active = accounting != null && accounting.getAccounts() != null;

		accountManagement.setEnabled(active);
        addAccount.setEnabled(active);
	}

    public void mouseClicked(MouseEvent me) {
        int clickCount = me.getClickCount();
        int button = me.getButton();
        if(popup!=null) {
            popup.setVisible(false);
            if (clickCount == 2) {
                if(accounting!=null) GUIActions.showDetails(selectedAccount, accounting.getJournals());
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