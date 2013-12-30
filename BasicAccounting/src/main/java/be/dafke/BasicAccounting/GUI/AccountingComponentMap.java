package be.dafke.BasicAccounting.GUI;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.GUI.JournalManagement.JournalManagementGUI;
import be.dafke.BasicAccounting.GUI.JournalManagement.JournalTypeManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;

import java.awt.event.ActionListener;

/**
 * User: Dafke
 * Date: 25/02/13
 * Time: 6:05
 */
public class AccountingComponentMap extends ComponentMap{

    public static void addAccountingComponents(Accounting accounting, ActionListener actionListener){
        addDisposableComponent(accounting.toString() + AccountingActionListener.ACCOUNT_MANAGEMENT, new AccountManagementGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + AccountingActionListener.JOURNAL_MANAGEMENT, new JournalManagementGUI(accounting, actionListener));
        addDisposableComponent(accounting.toString() + AccountingActionListener.JOURNAL_TYPE_MANAGEMENT, new JournalTypeManagementGUI(accounting));
    }

}
