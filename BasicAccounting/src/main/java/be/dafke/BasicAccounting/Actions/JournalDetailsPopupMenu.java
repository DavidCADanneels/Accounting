package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Journal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalDetailsPopupMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem move, delete, edit;

    public JournalDetailsPopupMenu(Accounting accounting, JournalDetails gui, Journal journal) {
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        delete.addActionListener(this);
        move.addActionListener(this);
        edit.addActionListener(this);
        delete.addActionListener(new DeleteTransactionActionListener(gui, journal));
        move.addActionListener(new MoveTransactionActionListener(accounting, gui, journal));
        edit.addActionListener(new EditTransactionActionListener(accounting, gui, journal));
        add(delete);
        add(move);
        add(edit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
