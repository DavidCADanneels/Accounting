package be.dafke.Accounting.GUI;

import be.dafke.Accounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.Accounting.GUI.Balances.BalanceGUI;
import be.dafke.Accounting.GUI.Balances.TestBalance;
import be.dafke.Accounting.GUI.CodaManagement.CounterPartyTable;
import be.dafke.Accounting.GUI.CodaManagement.MovementTable;
import be.dafke.Accounting.GUI.JournalManagement.JournalManagementGUI;
import be.dafke.Accounting.GUI.JournalManagement.JournalTypeManagementGUI;
import be.dafke.Accounting.GUI.MortgageManagement.MortgageGUI;
import be.dafke.Accounting.GUI.Projects.ProjectManagementGUI;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Balances;
import be.dafke.DisposableComponent;
import be.dafke.RefreshableComponent;

import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;

/**
 * User: Dafke
 * Date: 25/02/13
 * Time: 6:05
 */
public class ComponentMap {
    public static final String TEST_BALANCE = "TestBalance";
    public static final String YEAR_BALANCE = "YearBalance";
    public static final String RELATIONS_BALANCE = "BalanceGUI";
    public static final String RESULT_BALANCE = "ResultBalance";
    public static final String MOVEMENTS = "Movements";
    public static final String COUNTERPARTIES = "Counterparties";
    public static final String MORTGAGES = "Mortgages";
    public static final String PROJECTS = "Projects";
    public static final String MAIN = "MainPanel";
    public static final String MENU = "MenuBar";
    public static final String ACCOUNT_MANAGEMENT = "AccountManagement";
    public static final String JOURNAL_MANAGEMENT = "JournalManagement";
    public static final String JOURNAL_TYPE_MANAGEMENT = "JournalTypeManagement";

    public static final String JOURNAL_DETAILS = "JournalDetails";
    public static final String ACCOUNT_DETAILS = "AccountDetails";
    public static final String MORTGAGE_CALCULATOR = "MortgageCalculator";
    public static final String MORTGAGE_TABLE = "MortgageTable";
    public static final String NEW_ACCOUNTING = "NewAccounting";
    public static final String OPEN_ACCOUNTING = "OpenAccounting";
    public static final String NEW_ACCOUNT = "NewAccount";

    private static final HashMap<String, RefreshableComponent> refreshableComponents = new HashMap<String, RefreshableComponent>();
    private static final HashMap<String, DisposableComponent> disposableComponents = new HashMap<String, DisposableComponent>();

    public static void addAccountingComponents(Accounting accounting, ActionListener actionListener){
        addDisposableComponent(accounting.toString() + RELATIONS_BALANCE, new BalanceGUI(accounting.getBalances().get(Balances.RELATIONS_BALANCE)));
        addDisposableComponent(accounting.toString() + RESULT_BALANCE, new BalanceGUI(accounting.getBalances().get(Balances.RESULT_BALANCE)));
        addDisposableComponent(accounting.toString() + TEST_BALANCE, new TestBalance(accounting));
        addDisposableComponent(accounting.toString() + YEAR_BALANCE, new BalanceGUI(accounting.getBalances().get(Balances.YEAR_BALANCE)));
        addDisposableComponent(accounting.toString() + PROJECTS, new ProjectManagementGUI(accounting));
        addDisposableComponent(accounting.toString() + MOVEMENTS, new MovementTable(accounting, actionListener));
        addDisposableComponent(accounting.toString() + COUNTERPARTIES, new CounterPartyTable(accounting, actionListener));
        addDisposableComponent(accounting.toString() + MORTGAGES, new MortgageGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + ACCOUNT_MANAGEMENT, new AccountManagementGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + JOURNAL_MANAGEMENT, new JournalManagementGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + JOURNAL_TYPE_MANAGEMENT, new JournalTypeManagementGUI(accounting));
    }

    public static void closeAllFrames(){
        Collection<DisposableComponent> collection = disposableComponents.values();
        for(DisposableComponent frame: collection){
            frame.dispose();
        }
    }

    public static DisposableComponent getDisposableComponent(String name){
        return disposableComponents.get(name);
    }

    public static void refreshAllFrames(){
        Collection<RefreshableComponent> collection = refreshableComponents.values();
        for(RefreshableComponent frame: collection){
            frame.refresh();
        }
    }

    public static void addRefreshableComponent(String key, RefreshableComponent frame) {
        refreshableComponents.put(key, frame);
    }

    public static void addDisposableComponent(String key, DisposableComponent frame) {
        refreshableComponents.put(key, frame);
        disposableComponents.put(key, frame);
    }
}