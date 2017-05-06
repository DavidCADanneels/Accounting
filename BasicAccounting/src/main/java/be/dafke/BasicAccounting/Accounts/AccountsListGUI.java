package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.Utils.AlphabeticListModel;
import be.dafke.Utils.PrefixFilterPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsListGUI extends AccountsGUI {
    private final PrefixFilterPanel<Account> zoeker;
    private final AlphabeticListModel<Account> model;
    private final JList<Account> lijst;
    private final JournalInputGUI journalInputGUI;
    private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType, Boolean> selectedAccountTypes;

    private final JPanel filter;
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

        selectedAccountTypes = new HashMap<>();

        accountsTableButtons = new AccountsTableButtons(this);
        // CENTER
        //
        model = new AlphabeticListModel<>();
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
        zoeker = new PrefixFilterPanel<>(model, lijst, new ArrayList<>());

        popup = new AccountsPopupMenu(this);
        setPopup(popup);

        // PANEL
        //
        zoeker.add(accountsTableButtons, BorderLayout.SOUTH);
        add(zoeker, BorderLayout.CENTER);

        filter = new JPanel();
        filter.setLayout(new GridLayout(0, 2));
        boxes = new HashMap<>();

        add(filter, BorderLayout.NORTH);
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

    public void book(boolean debit) {
        if (lijst.getSelectedValue() != null) {
            AccountActions.book(journalInputGUI, lijst.getSelectedValue(), debit, vatType, vatTransactions, accounts, accountTypes, contacts);
        }
    }

    private void updateListOfCheckedBoxes() {
        for (AccountType type : boxes.keySet()) {
            JCheckBox checkBox = boxes.get(type);
            selectedAccountTypes.remove(type);
            selectedAccountTypes.put(type, checkBox.isSelected());
        }
    }

    public void fireAccountDataChanged() {
        ArrayList<AccountType> types = new ArrayList<>();
        for (AccountType type : selectedAccountTypes.keySet()) {
            JCheckBox checkBox = boxes.get(type);
            if (checkBox.isSelected()) {
                types.add(type);
            }
        }
        if (accounts != null) {
            ArrayList<Account> map = accounts.getAccounts(types);
            zoeker.resetMap(map);
        }
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        super.setAccountTypes(accountTypes);
        if (accountTypes != null) {
            selectedAccountTypes.clear();
            for (AccountType type : accountTypes.getBusinessObjects()) {
                selectedAccountTypes.put(type, Boolean.TRUE);
            }
//        }
            boxes.clear();
            filter.removeAll();

            for (AccountType type : accountTypes.getBusinessObjects()) {
                JCheckBox checkBox = new JCheckBox(getBundle("BusinessModel").getString(type.getName().toUpperCase()));
                checkBox.setSelected(true);
                checkBox.setActionCommand(type.getName());
                checkBox.addActionListener(e -> {
                    fireAccountDataChanged();
                    updateListOfCheckedBoxes();
                });
                boxes.put(type, checkBox);
                filter.add(checkBox);
            }
            revalidate();
            fireAccountDataChanged();
        }
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
    }

    public void setAccounts(Accounts accounts) {
        super.setAccounts(accounts);
        boolean active = accounts != null;
        if (accountTypes != null) {
            for (AccountType type : accountTypes.getBusinessObjects()) {
                JCheckBox checkBox = boxes.get(type);
                checkBox.setSelected(selectedAccountTypes.get(type));
                checkBox.setEnabled(active);
            }
        }
        if (active) {
            fireAccountDataChanged();
        }
    }
}