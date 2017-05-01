package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.VATTransaction;
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

import static be.dafke.BasicAccounting.Accounts.AccountManagementGUI.showAccountManager;
import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsListGUI extends JPanel {
    private final PrefixFilterPanel<Account> zoeker;
    private final AlphabeticListModel<Account> model;
    private final JList<Account> lijst;
    private final AccountInputPanel accountInputPanel;
    private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType, Boolean> selectedAccountTypes;

    private final JPanel filter;
    private AccountsPopupMenu popup;
    private AccountsTableButtons accountsTableButtons;

    private Account selectedAccount = null;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;

    public AccountsListGUI(AccountInputPanel accountInputPanel) {
        this.accountInputPanel = accountInputPanel;

        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle("Accounting").getString("ACCOUNTS")));

        selectedAccountTypes = new HashMap<>();

        accountsTableButtons = new AccountsTableButtons(this);
        // CENTER
        //
        model = new AlphabeticListModel<>();
        lijst = new JList<>(model);
        lijst.addListSelectionListener(e ->  {
            selectedAccount = null;
            if (!e.getValueIsAdjusting() && lijst.getSelectedIndex() != -1) {
                selectedAccount = lijst.getSelectedValue();
            }
            boolean accountSelected = (selectedAccount != null);
            boolean transaction = (accountInputPanel.getTransaction()!=null);
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
                        if (journals != null) accountInputPanel.getAccountDetails(selectedAccount, journals);
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
        accountInputPanel.getAccountDetails(lijst.getSelectedValue(), journals);
    }

    public void manageAccount(){
        showAccountManager(accounts, accountTypes).setVisible(true);
        popup.setVisible(false);
    }

    public void addAccount(){
        new NewAccountGUI(accounts, accountTypes).setVisible(true);
        popup.setVisible(false);
    }

//    public void setFirstButton(String text,ActionListener actionListener){
//        debet.removeActionListener();
//        debet.addActionListener(actionListener);
//        debet.setText(text);
//    }
    private VATTransaction.VATType vatType = null;

//    public VATTransaction.VATType getVatType() {
//        return vatType;
//    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    public void book(boolean debit) {
        if (selectedAccount != null) {
            accountInputPanel.book(selectedAccount, debit, vatType);
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

    public void setAccounting(Accounting accounting) {
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
        setAccounts(accounting == null ? null : accounting.getAccounts());
        setJournals(accounting == null ? null : accounting.getJournals());
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
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
        this.accounts = accounts;
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