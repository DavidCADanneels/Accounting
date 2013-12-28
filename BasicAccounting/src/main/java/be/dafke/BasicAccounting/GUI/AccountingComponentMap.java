package be.dafke.BasicAccounting.GUI;

import be.dafke.BasicAccounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.GUI.Balances.BalanceGUI;
import be.dafke.BasicAccounting.GUI.Balances.TestBalance;
import be.dafke.BasicAccounting.GUI.JournalManagement.JournalManagementGUI;
import be.dafke.BasicAccounting.GUI.JournalManagement.JournalTypeManagementGUI;
import be.dafke.BasicAccounting.GUI.Projects.ProjectManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Balances;
import be.dafke.Coda.GUI.CounterPartyTable;
import be.dafke.Coda.GUI.StatementTable;
import be.dafke.ComponentModel.ComponentMap;

import java.awt.event.ActionListener;

/**
 * User: Dafke
 * Date: 25/02/13
 * Time: 6:05
 */
public class AccountingComponentMap extends ComponentMap{
    public static final String TEST_BALANCE = "TestBalance";
    public static final String YEAR_BALANCE = "YearBalance";
    public static final String RELATIONS_BALANCE = "BalanceGUI";
    public static final String RESULT_BALANCE = "ResultBalance";
    public static final String MOVEMENTS = "Statements";
    public static final String COUNTERPARTIES = "Counterparties";
    public static final String PROJECTS = "Projects";
    public static final String MAIN = "MainPanel";
    public static final String MENU = "MenuBar";
    public static final String ACCOUNT_MANAGEMENT = "AccountManagement";
    public static final String JOURNAL_MANAGEMENT = "JournalManagement";
    public static final String JOURNAL_TYPE_MANAGEMENT = "JournalTypeManagement";

    public static final String JOURNAL_DETAILS = "JournalDetails";
    public static final String ACCOUNT_DETAILS = "AccountDetails";
    public static final String NEW_ACCOUNTING = "NewAccounting";
    public static final String OPEN_ACCOUNTING = "OpenAccounting";
    public static final String NEW_ACCOUNT = "NewAccount";


    public static void addAccountingComponents(Accounting accounting, ActionListener actionListener){
        addDisposableComponent(accounting.toString() + RELATIONS_BALANCE, new BalanceGUI(accounting,accounting.getBalances().getBusinessObject(Balances.RELATIONS_BALANCE)));
        addDisposableComponent(accounting.toString() + RESULT_BALANCE, new BalanceGUI(accounting,accounting.getBalances().getBusinessObject(Balances.RESULT_BALANCE)));
        addDisposableComponent(accounting.toString() + YEAR_BALANCE, new BalanceGUI(accounting,accounting.getBalances().getBusinessObject(Balances.YEAR_BALANCE)));
        addDisposableComponent(accounting.toString() + TEST_BALANCE, new TestBalance(accounting));
        addDisposableComponent(accounting.toString() + PROJECTS, new ProjectManagementGUI(accounting));
        addDisposableComponent(accounting.toString() + MOVEMENTS, new StatementTable(accounting, actionListener));
        addDisposableComponent(accounting.toString() + COUNTERPARTIES, new CounterPartyTable(accounting, actionListener));

        addDisposableComponent(accounting.toString() + ACCOUNT_MANAGEMENT, new AccountManagementGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + JOURNAL_MANAGEMENT, new JournalManagementGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + JOURNAL_TYPE_MANAGEMENT, new JournalTypeManagementGUI(accounting));
    }

}
