package be.dafke.BasicAccounting.VAT;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.util.ArrayList;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class VATTransactionsPopupMenu extends JPopupMenu {
    private final JMenuItem book;
    private final SelectableTable<VATBooking> table;
    private final Accounting accounting;

    public VATTransactionsPopupMenu(SelectableTable<VATBooking> table, Accounting accounting) {
        this.accounting = accounting;
        this.table = table;
        book = new JMenuItem("book");
//        book = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        book.addActionListener(e -> book());

        add(book);
    }

    private void book() {
        setVisible(false);
        ArrayList<VATBooking> selectedObjects = table.getSelectedObjects();
//        selectedObjects.forEach(vatBooking -> vatBooking.getVatTransaction());
        ArrayList<VATTransaction> transactions = new ArrayList<>();
        selectedObjects.forEach(vatBooking -> {
            VATTransaction vatTransaction = vatBooking.getVatTransaction();
            if(!transactions.contains(vatTransaction)){
                transactions.add(vatTransaction);
            }
        });
        VATFieldsGUI.getInstance(transactions, accounting).setVisible(true);
    }
}
