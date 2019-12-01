package be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.AccountsList
import be.dafke.Accounting.BusinessModelDao.JournalSession
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle
/**
 * @author David Danneels
 */

class AccountDataTableModel extends SelectableTableModel<Account> {
    int ACCOUNT_COL
    int SALDO_COL
    int NUMBER_COL
    int NR_OF_COL
    boolean showNumbers = true
    boolean hideEmpty = false
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    String namePrefix
    String numberPrefix
    Account account = null
    Accounts accounts
    JournalSession journalSession
    boolean left
    List<AccountType> accountTypes
    boolean singleAccount = false

    AccountDataTableModel(boolean left) {
        this.left = left
        initialize()
    }

    void initialize(){
        setColumnNumbers()
        setColumnNames()
        setColumnClasses()
    }

    void setColumnNumbers() {
        if(showNumbers){
            NUMBER_COL = 0
            ACCOUNT_COL = 1
            SALDO_COL = 2
            //
            NR_OF_COL = 3
        } else {
            ACCOUNT_COL = 0
            SALDO_COL = 1
            NUMBER_COL = 3
            //
            NR_OF_COL = 2
        }
    }

    void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix
        fireTableDataChanged()
    }

    void setNumberPrefix(String numberPrefix) {
        this.numberPrefix = numberPrefix
        fireTableDataChanged()
    }

    boolean isShowNumbers() {
        showNumbers
    }

    void setShowNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers
        initialize()
        fireTableStructureChanged()
    }

    boolean isHideEmpty() {
        return hideEmpty
    }

    void setHideEmpty(boolean hideEmpty) {
        this.hideEmpty = hideEmpty
        fireTableStructureChanged()
    }

    void setColumnClasses() {
        columnClasses.clear()
        if(showNumbers) {
            columnClasses.put(ACCOUNT_COL, Account.class)
            columnClasses.put(SALDO_COL, BigDecimal.class)
            columnClasses.put(NUMBER_COL, BigInteger.class)
        } else {
            columnClasses.put(ACCOUNT_COL, Account.class)
            columnClasses.put(SALDO_COL, BigDecimal.class)
        }
    }

    void setColumnNames() {
        columnNames.clear()
        if(showNumbers) {
            columnNames.put(ACCOUNT_COL, getBundle("Accounting").getString("ACCOUNT_NAME"))
            columnNames.put(SALDO_COL, getBundle("Accounting").getString("SALDO"))
            columnNames.put(NUMBER_COL, getBundle("Accounting").getString("ACCOUNT_NUMBER"))
        } else {
            columnNames.put(ACCOUNT_COL, getBundle("Accounting").getString("ACCOUNT_NAME"))
            columnNames.put(SALDO_COL, getBundle("Accounting").getString("SALDO"))
        }
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Account account = getFilteredAccounts().get(row)
        if (col == ACCOUNT_COL) return account
        if (col == NUMBER_COL) {
            if(account==null) return null
            return account.number
        }
        if (col == SALDO_COL) {
            if(account==null) return null
            if(account.type.isInverted())
                return account.saldo.negate()
            else return account.saldo
            // TODO: use this isInverted() switch to call negate() in other places
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(accounts == null){
            return 0
        }
        getFilteredAccounts().size()
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        col==NUMBER_COL
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        if(col == NUMBER_COL){
            Account account = getObject(row,col)
            account.number = (BigInteger)value
        }
    }

    void setAccounts(Accounts accounts) {
        this.accounts = accounts
    }

    List<Account> getFilteredAccounts(){
        if (accounts == null) return []
        List<Account> accountsList
        if(accountTypes!=null) {
            accountsList = accounts.getAccountsByType(accountTypes)
        } else {
            accountsList = accounts.businessObjects
        }
        def filteredList = accountsList.stream()
                .filter(Account.namePrefix(namePrefix))
                .filter(Account.numberPrefix(numberPrefix))
                .collect().toList()
        if(hideEmpty) {
            filteredList.stream().filter(Account.saldoNotZero()).collect().toList()
        } else {
            filteredList.stream().sorted().collect().toList()
        }
    }

//    void setFilter(Predicate<Account> filter) {
//        if(!singleAccount || account == null) {
//            this.filter = filter
//            fireTableDataChanged()
//        }
//    }

    void setAccountList(AccountsList accountList) {
        singleAccount = accountList.isSingleAccount()
        if(singleAccount){
            accountTypes = null
            account = accountList.account
//            filter = account==null?null:Account.name(accountList.account.name)
        } else if(journalSession!=null){
            if(left) {
                accountTypes = journalSession.getCheckedTypesLeft()
            } else {
                accountTypes = journalSession.getCheckedTypesRight()
            }
//            filter = null
        }
        fireTableDataChanged()
    }

    void setJournalSession(JournalSession journalSession) {
        this.journalSession = journalSession
        fireTableDataChanged()
    }

    @Deprecated
    void setAccountTypes(List<AccountType> accountTypes) {
        this.accountTypes = accountTypes
        fireTableDataChanged()
    }

    @Override
    Account getObject(int row, int col) {
        getFilteredAccounts().get(row)
    }

}