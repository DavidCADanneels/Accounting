package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.JOptionPane;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AccountingActions {

    public static void newAccounting(Accountings accountings) {
        String name = JOptionPane.showInputDialog(null, "Enter a name");
        try {
            Accounting accounting = new Accounting();
            accounting.setName(name);
            accountings.addBusinessObject(accounting);
            accountings.setCurrentObject(name);
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNTING_DUPLICATE_NAME);
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(ActionUtils.ACCOUNTING_NAME_EMPTY);
        }
        ComponentMap.refreshAllFrames();
    }

    public static void openAccounting(Accountings accountings, Accounting accounting) {
        accountings.setCurrentObject(accounting.getName());
        ComponentMap.refreshAllFrames();
    }
}
