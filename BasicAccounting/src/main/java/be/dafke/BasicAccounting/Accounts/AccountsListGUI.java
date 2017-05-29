package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.AccountsList;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsListGUI extends AccountsGUI {
    private final AccountDataListModel model;
    private final JList<Account> lijst;
    private final JournalInputGUI journalInputGUI;

    private final AccountFilterPanel filterPanel;
    private AccountsPopupMenu popup;
    private AccountsTableButtons accountsTableButtons;

    private Journals journals;

    private VATTransaction.VATType vatType = null;
    private VATTransactions vatTransactions = null;
    private Contacts contacts = null;

    public AccountsListGUI(JournalInputGUI journalInputGUI) {
        this.journalInputGUI = journalInputGUI;

        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("Accounting").getString("ACCOUNTS")));

        accountsTableButtons = new AccountsTableButtons(this);
        // CENTER
        //
        model = new AccountDataListModel();
        lijst = new JList<>(model);
        lijst.addListSelectionListener(e ->  {
            Account selectedAccount = null;
            if (!e.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
                selectedAccount = lijst.getSelectedValue();
            }
            boolean accountSelected = (selectedAccount != null);
            boolean transaction = (journalInputGUI.getTransaction()!=null);
            boolean active = accountSelected && transaction;
            accountsTableButtons.setActive(active);
        });

        lijst.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int clickCount = me.getClickCount();
                int button = me.getButton();
                if (popup != null) {
                    popup.setVisible(false);
                    if (clickCount == 2) {
                        if (journals != null) showDetails();;
                    } else if (button == 3) {
                        Point location = me.getLocationOnScreen();
                        popup.show(null, location.x, location.y);
                    }
                }
            }
        });//new PopupForListActivator(popup, lijst));//, new AccountDetailsLauncher(accountings)));
        lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        popup = new AccountsPopupMenu(this);
        setPopup(popup);

        // PANEL
        //

        JScrollPane scrollPane1 = new JScrollPane(lijst);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);

        filterPanel = new AccountFilterPanel(model);

        add(filterPanel, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(accountsTableButtons, BorderLayout.SOUTH);
    }

    public void showDetails(){
        if(lijst.getSelectedValue()!=null) {
            AccountDetails.getAccountDetails(lijst.getSelectedValue(), journals, journalInputGUI);
        }
    }

//    public void setFirstButton(String text,ActionListener actionListener){
//        debet.removeActionListener();
//        debet.addActionListener(actionListener);
//        debet.setText(text);
//    }


//    public VATTransaction.VATType getVatType() {
//        return vatType;
//    }

    public void setAccounting(Accounting accounting) {
        model.setAccounts(accounting == null ? null : accounting.getAccounts());
        model.setAccountTypes(accounting == null ? null : accounting.getAccountTypes().getBusinessObjects());
        model.setFilter(null);
        // TODO: remove this method
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
        setAccounts(accounting == null ? null : accounting.getAccounts());
        setJournals(accounting == null ? null : accounting.getJournals());
        setVatTransactions(accounting == null ? null : accounting.getVatTransactions());
        setContacts(accounting == null ? null : accounting.getContacts());
    }

    public void setVatTransactions(VATTransactions vatTransactions) {
        this.vatTransactions = vatTransactions;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    @Override
    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
        filterPanel.setAccountList(accountsList);
    }

    public void book(boolean debit) {
        if (lijst.getSelectedValue() != null) {
            ArrayList<AccountType> accountTypes = accountsList.getAccountTypes();
            AccountActions.book(journalInputGUI, lijst.getSelectedValue(), debit, vatType, vatTransactions, accounts, accountTypes, contacts);
        }
    }

    @Override
    public void fireAccountDataChanged() {
        model.filter();
    }

    public void setAccounts(Accounts accounts) {
        super.setAccounts(accounts);
        filterPanel.clearSearchFields();
    }


    public void setAccountTypes(AccountTypes accountTypes) {
        model.setAccountTypes(accountTypes.getBusinessObjects());
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }
}