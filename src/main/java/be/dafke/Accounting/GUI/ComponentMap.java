package be.dafke.Accounting.GUI;

import be.dafke.Accounting.GUI.Balances.RelationsBalance;
import be.dafke.Accounting.GUI.Balances.ResultBalance;
import be.dafke.Accounting.GUI.Balances.TestBalance;
import be.dafke.Accounting.GUI.Balances.YearBalance;
import be.dafke.Accounting.GUI.CodaManagement.CounterPartyTable;
import be.dafke.Accounting.GUI.CodaManagement.MovementTable;
import be.dafke.Accounting.GUI.MainWindow.AccountingGUIFrame;
import be.dafke.Accounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.Accounting.GUI.MortgageManagement.MortgageGUI;
import be.dafke.Accounting.GUI.Projects.ProjectManagerFrame;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.DisposableComponent;
import be.dafke.RefreshableComponent;

import java.util.Collection;
import java.util.HashMap;

/**
 * User: Dafke
 * Date: 25/02/13
 * Time: 6:05
 */
public class ComponentMap {
    public static final String OPEN_TEST_BALANCE = "test";
    public static final String OPEN_YEAR_BALANCE = "year";
    public static final String OPEN_RELATIONS_BALANCE = "relations";
    public static final String OPEN_RESULT_BALANCE = "result";
    public static final String OPEN_MOVEMENTS = "movements";
    public static final String OPEN_COUNTERPARTIES = "counterparties";
    public static final String OPEN_MORTGAGES = "mortgages";
    public static final String OPEN_PROJECTS = "projects";
    public static final String MAIN = "mainPanel";
    public static final String MENU = "menuBar";
    private static final HashMap<String, RefreshableComponent> refreshableComponents = new HashMap<String, RefreshableComponent>();
    private static final HashMap<String, DisposableComponent> disposableComponents = new HashMap<String, DisposableComponent>();

    public ComponentMap(Accountings accountings, AccountingGUIFrame mainPanel, AccountingMenuBar menuBar) {
        for(Accounting accounting : accountings.getAccountings()){
            disposableComponents.put(accounting.toString()+OPEN_RELATIONS_BALANCE, new RelationsBalance(accounting));
            disposableComponents.put(accounting.toString()+OPEN_RESULT_BALANCE, new ResultBalance(accounting));
            disposableComponents.put(accounting.toString()+OPEN_TEST_BALANCE, new TestBalance(accounting));
            disposableComponents.put(accounting.toString()+OPEN_YEAR_BALANCE, new YearBalance(accounting));
        }
        disposableComponents.put(OPEN_PROJECTS, new ProjectManagerFrame(accountings));
        disposableComponents.put(OPEN_MOVEMENTS, new MovementTable(accountings));
        disposableComponents.put(OPEN_COUNTERPARTIES, new CounterPartyTable(accountings));
        disposableComponents.put(OPEN_MORTGAGES, new MortgageGUI(accountings));
        disposableComponents.put(MAIN, mainPanel);
        refreshableComponents.putAll(disposableComponents);
        refreshableComponents.put(MENU, menuBar);
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
