package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.VATTransactions;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class VATTransactionsGUI extends JFrame implements WindowListener {
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

    public void windowClosing(WindowEvent we) {
        vatTransactionsPanel.closePopups();
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

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
