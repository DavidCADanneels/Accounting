package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.AccountsTablePopupMenu;
import be.dafke.BusinessActions.ActionUtils;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsTableGUI extends AccountingPanel implements MouseListener {
    private final RefreshableTable<Account> table;
    private final AccountDataModel accountDataModel;

    private AccountsTablePopupMenu popup;
    private Accounts accounts;
//    private Journals journals;

    public AccountsTableGUI() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));

        // CENTER
        //
        accountDataModel = new AccountDataModel();
        table = new RefreshableTable<>(accountDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(100, 600));

        popup = new AccountsTablePopupMenu(table);
        table.addMouseListener(new PopupForTableActivator(popup, table));

        JScrollPane scrollPane1 = new JScrollPane(table);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
        add(center, BorderLayout.CENTER);
	}

    public void setAccounting(Accounting accounting) {
        accounts = accounting.getAccounts();
//        journals = accounting.getJournals();

        accountDataModel.setAccounts(accounts);

        // could be popup.setAccounting() with constructor call in this.constructor
        popup.setAccounting(accounting);
        table.addMouseListener(new PopupForTableActivator(popup, table));
    }

	public void refresh() {
        accountDataModel.fireTableDataChanged();
    }

    public void mouseClicked(MouseEvent me) {
        int clickCount = me.getClickCount();
        int button = me.getButton();
        Point location = me.getLocationOnScreen();
        if (clickCount == 2) {
            // TODO pick selected Object or take Object under Pointer?
            // Doubleclick should already select an Object
            // Doubleclick does not trigger anything right now
//            Account selectedAccount = table.getSelectedObject();
//            if (journals != null) GUIActions.showDetails(selectedAccount, journals);
        } else if (button == 3) {
            popup.show(null, location.x, location.y);
        } else{
            popup.setVisible(false);
        }
    }

    public ArrayList<Account> getSelectedAccounts() {
        ArrayList<Account> selectedObjects = table.getSelectedObjects();
        if (selectedObjects.isEmpty()) {
            ActionUtils.showErrorMessage(ActionUtils.SELECT_ACCOUNT_FIRST);
        }
        return selectedObjects;
    }


    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void addAddBookingLister(AddBookingListener addBookingListener) {
        popup.addAddBookingListener(addBookingListener);
    }
}