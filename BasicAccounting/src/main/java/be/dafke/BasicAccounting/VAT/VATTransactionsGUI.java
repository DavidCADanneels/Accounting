package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransactionsGUI extends JFrame {
    private static final HashMap<VATTransactions, VATTransactionsGUI> vatGuis = new HashMap<>();
    private final SelectableTable tabel;

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

    private VATTransactions vatTransactions;

    private VATTransactionsGUI(VATTransactions vatTransactions) {
        super(vatTransactions.getAccounting().getName() + " / " + getBundle("VAT").getString("VAT_TRANSACTIONS"));
        this.vatTransactions = vatTransactions;
        VATTransactionsDataModel vatTransactionsDataModel = new VATTransactionsDataModel(vatTransactions);

        tabel = new SelectableTable<>(vatTransactionsDataModel);
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
        //tabel.setAutoCreateRowSorter(true);
        tabel.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(tabel);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPanel);

        updateVATTransactions();
        pack();
    }



    public void updateVATTransactions(){

    }

    public static void fireVATTransactionsUpdated(){
        for(VATTransactionsGUI vatTransactionsGUI : vatGuis.values()) {
            if (vatTransactionsGUI != null) {
                vatTransactionsGUI.updateVATTransactions();
            }
        }
    }
}
