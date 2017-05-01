package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsTableGUI extends AccountsGUI {//implements MouseListener {
    private final SelectableTable<Account> table;
    private final AccountDataModel accountDataModel;

    private AccountsTablePopupMenu popup;
    private JournalInputGUI journalInputGUI;
    private Journals journals;

    public AccountsTableGUI(JournalInputGUI journalInputGUI) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("Accounting").getString("ACCOUNTS")));

        // CENTER
        //
        accountDataModel = new AccountDataModel();
        table = new SelectableTable<>(accountDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(100, 600));

        this.journalInputGUI=journalInputGUI;
        popup = new AccountsTablePopupMenu(this);
        setPopup(popup);
        // TODO: register popup menu as TransactionListener and remove TransactionListener from 'this'.
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        JScrollPane scrollPane1 = new JScrollPane(table);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
        add(center, BorderLayout.CENTER);
	}

	public void showDetails(){
        for(int i: table.getSelectedRows()){
            Account account = accounts.getBusinessObjects().get(i);
            AccountDetails.getAccountDetails(account, journals, journalInputGUI);
        }
        popup.setVisible(false);
    }

    public void setAccounting(Accounting accounting) {
        accountDataModel.setAccounts(accounting==null?null:accounting.getAccounts());
        setJournals(accounting==null?null:accounting.getJournals());
        setAccounts(accounting==null?null:accounting.getAccounts());
        setAccountTypes(accounting==null?null:accounting.getAccountTypes());
        // if setAccounts() is used here, popup.setAccounts() will be called twice
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));  // TODO: Needed?
        fireAccountDataChanged();
    }

    public void book(boolean debit) {
        for(int i: table.getSelectedRows()){
            Account account = accounts.getBusinessObjects().get(i);
            if (account != null) {
                BigDecimal amount = journalInputGUI.askAmount(account, debit);
                if (amount != null) {
                    journalInputGUI.addBooking(new Booking(account, amount, debit));
                }
            }
        }
        popup.setVisible(false);
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }


    public void setAccounts(Accounts accounts) {
        super.setAccounts(accounts);
        accountDataModel.setAccounts(accounts);
        fireAccountDataChanged();
    }

    public void fireAccountDataChanged() {
        accountDataModel.fireTableDataChanged();
    }
}