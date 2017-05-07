package be.dafke.BasicAccounting.VAT;

import javax.swing.*;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class VATTransactionsPopupMenu extends JPopupMenu {
    private final JMenuItem book;

    public VATTransactionsPopupMenu() {
        book = new JMenuItem("book");
//        book = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        book.addActionListener(e -> book());

        add(book);
    }

    private void book() {
        setVisible(false);

    }
}
