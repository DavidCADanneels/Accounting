package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATField;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransactionsGUI extends JFrame {
    private static final HashMap<VATTransactions, VATTransactionsGUI> vatGuis = new HashMap<>();
    private final VATTransactionsPanel vatTransactionsPanel;

    public static VATTransactionsGUI getInstance(VATTransactions vatTransactions) {
        VATTransactionsGUI gui;
        gui = vatGuis.get(vatTransactions);
        if(gui==null){
            gui = new VATTransactionsGUI(vatTransactions);
            vatGuis.put(vatTransactions,gui);
            Main.addFrame(gui);
        }
        return gui;
    }


    private VATTransactionsGUI(VATTransactions vatTransactions) {
        super(getBundle("VAT").getString("VAT_TRANSACTIONS"));

        vatTransactionsPanel = new VATTransactionsPanel(vatTransactions);


        setContentPane(vatTransactionsPanel);
        updateVATTransactions();
        pack();
    }



    public void updateVATTransactions(){
        vatTransactionsPanel.updateVATTransactions();
    }

    public static void fireVATTransactionsUpdated(){
        for(VATTransactionsGUI vatTransactionsGUI : vatGuis.values()) {
            if (vatTransactionsGUI != null) {
                vatTransactionsGUI.updateVATTransactions();
            }
        }
    }
}
