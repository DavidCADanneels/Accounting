package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.*;
import be.dafke.Utils.AlphabeticListModel;
import be.dafke.Utils.PrefixFilterPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsGUI extends JPanel {
    private final PrefixFilterPanel<Account> zoeker;
    private final AlphabeticListModel<Account> model;
    private final JList<Account> lijst;
    private JButton debet, credit, accountDetails;
    private final Map<AccountType, JCheckBox> boxes;
    private final Map<AccountType, Boolean> selectedAccountTypes;

    private final JPanel filter;
    private AccountsPopupMenu popup;

    private Account selectedAccount = null;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private Journals journals;
    private JournalInputGUI journalInputGUI;
    private boolean tax = true;

    public AccountsGUI(JournalInputGUI journalInputGUI) {
        this.journalInputGUI = journalInputGUI;

        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));

        selectedAccountTypes = new HashMap<>();

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
                accountDetails.setEnabled(accountSelected);
                debet.setEnabled(accountSelected);
                credit.setEnabled(accountSelected);
            });

        lijst.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int clickCount = me.getClickCount();
                int button = me.getButton();
                if (popup != null) {
                    popup.setVisible(false);
                    if (clickCount == 2) {
                        if (journals != null) AccountDetails.getAccountDetails(selectedAccount, journals, journalInputGUI);
                    } else if (button == 3) {
                        Point location = me.getLocationOnScreen();
                        popup.show(null, location.x, location.y);
                    }
                }
            }
        });//new PopupForListActivator(popup, lijst));//, new AccountDetailsLauncher(accountings)));
        lijst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        zoeker = new PrefixFilterPanel<>(model, lijst, new ArrayList<>());

        popup = new AccountsPopupMenu();

        // BUTTONS
        //
        debet = new JButton(getBundle("Accounting").getString("DEBIT_ACTION"));
        credit = new JButton(getBundle("Accounting").getString("CREDIT_ACTION"));
        accountDetails = new JButton(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        debet.setMnemonic(KeyEvent.VK_D);
        credit.setMnemonic(KeyEvent.VK_C);
        accountDetails.setMnemonic(KeyEvent.VK_T);

        debet.addActionListener(e -> {book(true);});
        credit.addActionListener(e -> {book(false);});
        accountDetails.addActionListener(e -> AccountDetails.getAccountDetails(lijst.getSelectedValue(), journals, journalInputGUI));

        debet.setEnabled(false);
        credit.setEnabled(false);
        accountDetails.setEnabled(false);

        // PANEL
        //
        JPanel noord = new JPanel();
        noord.add(debet);
        noord.add(credit);
        noord.add(accountDetails);

        zoeker.add(noord, BorderLayout.SOUTH);
        add(zoeker, BorderLayout.CENTER);

        filter = new JPanel();
        filter.setLayout(new GridLayout(0, 2));
        boxes = new HashMap<>();

        add(filter, BorderLayout.NORTH);
    }

    public boolean isTax() {
        return tax;
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }

    public void book(boolean debit) {
        if(selectedAccount!=null){
            BigDecimal amount = journalInputGUI.askAmount(selectedAccount,debit);
            if(amount!=null) {
                journalInputGUI.addBooking(new Booking(selectedAccount, amount, debit));
                if(tax){
                    Integer[] percentages = new Integer[]{0, 6, 21};
                    int nr = JOptionPane.showOptionDialog(null, "BTW %", "BTW %",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, percentages, null);
                    Account btwAccount = null;
                    if (nr == 1) {
                        btwAccount = accounts.getBusinessObject("BTW - te betalen - 6pct");
                    } else if(nr == 2){
                        btwAccount = accounts.getBusinessObject("BTW - te betalen - 21pct");
                    }
                    if(btwAccount!=null){
                        BigDecimal percentage = new BigDecimal(percentages[nr]).divide(new BigDecimal(100));
                        BigDecimal suggestedAmount = amount.multiply(percentage);
                        BigDecimal btwAmount = journalInputGUI.askAmount(btwAccount, suggestedAmount);
                        btwAmount.setScale(2);
                        if(btwAmount!=null) {
                            journalInputGUI.addBooking(new Booking(btwAccount, btwAmount, debit));
                        }
                    }
                }
            }
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
        popup.setAccounting(accounting);
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
        if (accountTypes != null) {
            selectedAccountTypes.clear();
            for (AccountType type : accountTypes.getBusinessObjects()) {
                selectedAccountTypes.put(type, Boolean.TRUE);
            }
        }
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