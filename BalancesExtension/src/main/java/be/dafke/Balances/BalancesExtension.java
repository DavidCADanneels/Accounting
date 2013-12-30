package be.dafke.Balances;

import be.dafke.Balances.Dao.BalancesSAXParser;
import be.dafke.Balances.GUI.BalanceGUI;
import be.dafke.Balances.GUI.TestBalance;
import be.dafke.Balances.Objects.Balance;
import be.dafke.Balances.Objects.Balances;
import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.Dao.AccountingsSAXParser;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 30-12-13
 * Time: 11:03
 */
public class BalancesExtension implements AccountingExtension {
    private final ActionListener actionListener;
    private static JMenu balancesMenu = null;
    public static final String TEST_BALANCE = "TestBalance";
    public static final String YEAR_BALANCE = "YearBalance";
    public static final String RELATIONS_BALANCE = "BalanceGUI";
    public static final String RESULT_BALANCE = "ResultBalance";
    private Balances balances;


    public BalancesExtension(ActionListener actionListener, AccountingMenuBar menuBar){
        this.actionListener = actionListener;
        if(balancesMenu == null) createMenu(menuBar, actionListener);
    }

    private void createMenu(AccountingMenuBar menuBar, ActionListener actionListener) {
        balancesMenu = new JMenu(getBundle("Accounting").getString("BALANSEN"));
        balancesMenu.setMnemonic(KeyEvent.VK_B);
        JMenuItem testBalance = new JMenuItem(getBundle("Accounting").getString(
                "PROEF_EN_SALDI-BALANS"));
        JMenuItem yearBalance = new JMenuItem(getBundle("Accounting").getString("EINDBALANS"));
        JMenuItem resultBalance = new JMenuItem(getBundle("Accounting").getString(
                "RESULTATENBALANS"));
        JMenuItem relationsBalance = new JMenuItem(getBundle("Accounting").getString(
                "RELATIES-BALANS"));
        testBalance.addActionListener(actionListener);
        yearBalance.addActionListener(actionListener);
        resultBalance.addActionListener(actionListener);
        relationsBalance.addActionListener(actionListener);
        testBalance.setActionCommand(TEST_BALANCE);
        yearBalance.setActionCommand(YEAR_BALANCE);
        resultBalance.setActionCommand(RESULT_BALANCE);
        relationsBalance.setActionCommand(RELATIONS_BALANCE);
        relationsBalance.setEnabled(false);
        resultBalance.setEnabled(false);
        testBalance.setEnabled(false);
        yearBalance.setEnabled(false);
        balancesMenu.add(testBalance);
        balancesMenu.add(resultBalance);
        balancesMenu.add(yearBalance);
        balancesMenu.add(relationsBalance);
        menuBar.addRefreshableMenuItem(testBalance);
        menuBar.addRefreshableMenuItem(resultBalance);
        menuBar.addRefreshableMenuItem(yearBalance);
        menuBar.addRefreshableMenuItem(relationsBalance);
        menuBar.add(balancesMenu);
    }
    public void extendConstructor(Accounting accounting){
        balances = new Balances();
        balances.setBusinessCollection(accounting.getAccounts());
        balances.setBusinessTypeCollection(accounting.getAccountTypes());
        balances.addDefaultBalances(accounting);
        balances.setName(balances.getBusinessObjectType());
        try{
            accounting.addBusinessObject((BusinessCollection) balances);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        accounting.addKey(balances.getBusinessObjectType());
    }

    public void extendReadCollection(Accounting accounting, File xmlFolder){

    }

    public void extendAccountingComponentMap(Accounting accounting){
        AccountingComponentMap.addDisposableComponent(accounting.toString() + RELATIONS_BALANCE, new BalanceGUI(accounting, balances.getBusinessObject(Balances.RELATIONS_BALANCE)));
        AccountingComponentMap.addDisposableComponent(accounting.toString() + RESULT_BALANCE, new BalanceGUI(accounting, balances.getBusinessObject(Balances.RESULT_BALANCE)));
        AccountingComponentMap.addDisposableComponent(accounting.toString() + YEAR_BALANCE, new BalanceGUI(accounting, balances.getBusinessObject(Balances.YEAR_BALANCE)));
        AccountingComponentMap.addDisposableComponent(accounting.toString() + TEST_BALANCE, new TestBalance(accounting));

    }

    public void extendWriteCollection(Accounting accounting, File xmlFolder){
        File balancesFolder = new File(xmlFolder, "Balances");
        for(Balance balance : balances.getBusinessObjects()){
            BalancesSAXParser.writeBalance(balance, balancesFolder, AccountingsSAXParser.getXmlHeader(balance, 2));
        }

    }
}
