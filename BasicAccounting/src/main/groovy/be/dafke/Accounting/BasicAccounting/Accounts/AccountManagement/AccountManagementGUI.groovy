package be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement

import be.dafke.Accounting.BasicAccounting.Accounts.New.NewAccountDialog
import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.TableColumn
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountManagementGUI extends JFrame implements ListSelectionListener {
    JButton newAccount, delete, edit
    final AccountManagementTableModel accountManagementTableModel
    final SelectableTable<Account> tabel
    Accounts accounts
    ArrayList<AccountType> accountTypes
    static final HashMap<Accounts, AccountManagementGUI> accountManagementGuis = new HashMap<>()

    AccountManagementGUI(final Accounts accounts, ArrayList<AccountType> accountTypes) {
        super(getBundle("Accounting").getString("ACCOUNT_MANAGEMENT_TITLE"))
        this.accounts = accounts
        this.accountTypes = accountTypes
        this.accountManagementTableModel = new AccountManagementTableModel(this, accounts)

        // COMPONENTS
        //
        // Table
        tabel = new SelectableTable<>(accountManagementTableModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))
        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener(this)
        tabel.setSelectionModel(selection)

        JComboBox<AccountType> comboBox=createComboBox()

        TableColumn column = tabel.getColumnModel().getColumn(AccountManagementTableModel.TYPE_COL)
        column.setCellEditor(new DefaultCellEditor(comboBox))

        JScrollPane scrollPane = new JScrollPane(tabel)
        //
        JPanel panel = new JPanel(new BorderLayout())
        panel.add(scrollPane, BorderLayout.CENTER)

        JPanel south = createContentPanel()

        panel.add(south, BorderLayout.SOUTH)
        setContentPane(panel)
        pack()
    }

    JComboBox<AccountType> createComboBox() {
        JComboBox<AccountType> comboBox = new JComboBox<>()
        accountTypes.forEach({ accountType -> comboBox.addItem(accountType) })
        comboBox
    }

    static AccountManagementGUI getInstance(Accounts accounts, ArrayList<AccountType> accountTypes) {
        AccountManagementGUI gui = accountManagementGuis.get(accounts)
        if(gui == null){
            gui = new AccountManagementGUI(accounts, accountTypes)
            accountManagementGuis.put(accounts, gui)

            Main.addFrame(gui)
        }
        gui
    }

    JPanel createContentPanel(){
        JPanel south = new JPanel()
        delete = new JButton(getBundle("Accounting").getString("DELETE_ACCOUNT"))
        edit = new JButton(getBundle("Accounting").getString("EDIT_ACCOUNT"))
        newAccount = new JButton(getBundle("Accounting").getString("ADD_ACCOUNT"))
        delete.addActionListener({ e -> deleteAccounts(tabel.selectedObjects, accounts) })
        edit.addActionListener({ e ->
            Account account = tabel.selectedObject
            if (account != null) {
                NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes)
                newAccountDialog.setLocation(getLocationOnScreen())
                newAccountDialog.setAccount(account)
                newAccountDialog.visible = true
            }
        })
        newAccount.addActionListener({ e ->
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounts, accountTypes)
            newAccountDialog.setLocation(getLocationOnScreen())
            newAccountDialog.visible = true
        })
        delete.enabled = false
        edit.enabled = false
        south.add(delete)
        south.add(edit)
        south.add(newAccount)
        south
    }

    /**
     * Herlaadt de data van de tabel
     * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
     */

    static void fireAccountDataChangedForAll() {
        for(AccountManagementGUI accountManagementGUI:accountManagementGuis.values()){
            accountManagementGUI.fireAccountDataChanged()
        }
    }

    void fireAccountDataChanged() {
        accountManagementTableModel.fireTableDataChanged()
    }

    void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            ArrayList<Account> accounts = tabel.selectedObjects
            if (accounts && accounts.size() > 0) {
                delete.enabled = true
                edit.enabled = true
            } else {
                delete.enabled = false
                edit.enabled = false
            }
        }
    }

    void deleteAccounts(ArrayList<Account> accountList, Accounts accounts){
        if(!accountList.empty) {
            ArrayList<String> failed = new ArrayList<>()
            for(Account account : accountList) {
                try{
                    accounts.removeBusinessObject(account)
                }catch (NotEmptyException e){
                    failed.add(account.name)
                }
            }
            if (failed.size() > 0) {
                if (failed.size() == 1) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_NOT_EMPTY, failed.get(0))
                } else {
                    StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_ACCOUNTS_NOT_EMPTY")+"\n")
                    for(String s : failed){
                        builder.append("- ").append(s).append("\n")
                    }
                    JOptionPane.showMessageDialog(null, builder.toString())
                }
            }
        }
        fireAccountDataChanged()
    }
}
