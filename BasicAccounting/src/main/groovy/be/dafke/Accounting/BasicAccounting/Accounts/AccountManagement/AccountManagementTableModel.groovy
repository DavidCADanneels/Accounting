package be.dafke.Accounting.BasicAccounting.Accounts.AccountManagement

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.ComponentModel.SelectableTableModel
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

import java.awt.*

import static java.util.ResourceBundle.getBundle

class AccountManagementTableModel extends SelectableTableModel<Account> {
    static final int NAME_COL = 0
    static final int NUMBER_COL = 1
    static final int TYPE_COL = 2
    static final int SALDO_COL = 3
    static final int DEFAULT_AMOUNT_COL = 4
    private HashMap<Integer,String> columnNames = new HashMap<>()
    private HashMap<Integer,Class> columnClasses = new HashMap<>()
    private ArrayList<Integer> nonEditableColumns = new ArrayList<>()

    private final Accounts accounts
    private final Component parent

    AccountManagementTableModel(Component parent, Accounts accounts) {
        this.parent = parent
        this.accounts = accounts
        nonEditableColumns.add(SALDO_COL)
//		nonEditableColumns.add(TYPE_COL)
        setColumnNames()
        setColumnClasses()
    }

    private void setColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(NUMBER_COL, BigInteger.class)
        columnClasses.put(TYPE_COL, AccountType.class)
        columnClasses.put(SALDO_COL, BigDecimal.class)
        columnClasses.put(DEFAULT_AMOUNT_COL, BigDecimal.class)
    }

    private void setColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ACCOUNT_NAME"))
        columnNames.put(NUMBER_COL, getBundle("Accounting").getString("ACCOUNT_NUMBER"))
        columnNames.put(TYPE_COL, getBundle("Accounting").getString("TYPE"))
        columnNames.put(SALDO_COL, getBundle("Accounting").getString("SALDO"))
        columnNames.put(DEFAULT_AMOUNT_COL, getBundle("BusinessActions").getString("DEFAULT_AMOUNT"))
    }

    int getColumnCount() {
        columnNames.size()
    }

    int getRowCount() {
        accounts.getBusinessObjects().size()
    }

    Object getValueAt(int row, int col) {
        Account account = accounts.getBusinessObjects().get(row)
        if (col == NAME_COL) {
            account.getName()
        } else if (col == NUMBER_COL) {
            account.getNumber()
        } else if (col == TYPE_COL) {
            account.getType()
        } else if (col == SALDO_COL) {
            AccountType type = account.getType()
            BigDecimal saldo = account.getSaldo()
            if(type.isInverted()){
                saldo = saldo.negate()
            }
            saldo
        } else if (col == DEFAULT_AMOUNT_COL) {
            BigDecimal defaultAmount = account.getDefaultAmount()
            if(defaultAmount!=null){
                defaultAmount
            } else {
                null
            }
        } else account
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
        !nonEditableColumns.contains(col)
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Account account = getObject(row, col)
        if(isCellEditable(row, col) && account!=null){
            if(col== NAME_COL){
                String oldName = account.getName()
                String newName = (String)value
                if (newName != null && !oldName.trim().equals(newName.trim())) {
                    try {
                        accounts.modifyName(oldName, newName)
                        Main.fireAccountDataChanged(account)
                    } catch (DuplicateNameException e) {
                        ActionUtils.showErrorMessage(parent, ActionUtils.ACCOUNT_DUPLICATE_NAME,newName.trim())
                    } catch (EmptyNameException e) {
                        ActionUtils.showErrorMessage(parent, ActionUtils.ACCOUNT_NAME_EMPTY)
                    }
                }
            }else if(col== TYPE_COL){
                AccountType accountType = (AccountType)value
                account.setType(accountType)
            }else if(col== NUMBER_COL){
                account.setNumber((BigInteger)value)
            } else if(col== DEFAULT_AMOUNT_COL) {
                if (value == null || BigDecimal.ZERO.compareTo((BigDecimal) value) == NAME_COL) {
                    account.setDefaultAmount(null)
                } else {
                    account.setDefaultAmount(((BigDecimal) value).setScale(TYPE_COL))
                }
            }
        }
    }

    @Override
    Account getObject(int row, int col) {
        accounts.getBusinessObjects().get(row)
    }

}
