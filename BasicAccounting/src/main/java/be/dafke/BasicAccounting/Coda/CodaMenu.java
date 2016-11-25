package be.dafke.BasicAccounting.Coda;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.CounterParties;
import be.dafke.BusinessModel.Statements;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class CodaMenu extends JMenu implements ActionListener {
    private static JMenuItem movementsItem, counterPartiesItem;
    private static BusinessCollection<BusinessObject> counterParties, statements;

    public CodaMenu(){
        movementsItem = new JMenuItem("Show movements");
        movementsItem.addActionListener(this);
        movementsItem.setEnabled(false);
        counterPartiesItem = new JMenuItem("Show Counterparties");
        counterPartiesItem.addActionListener(this);
        counterPartiesItem.setEnabled(false);

        add(movementsItem);
        add(counterPartiesItem);
    }

    public void actionPerformed(ActionEvent e) {
        String key = Statements.STATEMENTS + statements.hashCode();
        JFrame gui = Main.getJFrame(key); // DETAILS
        if(gui == null){
            gui = new StatementTableFrame((Statements)statements, (CounterParties)counterParties);
            Main.addJFrame(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static void setAccounting(Accounting accounting) {
        counterParties = accounting.getBusinessObject(CounterParties.COUNTERPARTIES);
        statements = accounting.getBusinessObject(Statements.STATEMENTS);
        counterPartiesItem.setEnabled(counterParties!=null);
        movementsItem.setEnabled(statements!=null);
    }
}
