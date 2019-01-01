package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class PromoOrderCreateGUI extends JFrame {
    private final PromoOrderCreatePanel orderPanel;

    private static PromoOrderCreateGUI salesOrderCreateGui = null;

    private PromoOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_PR"));
        orderPanel = new PromoOrderCreatePanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static PromoOrderCreateGUI showPromoOrderGUI(Accounting accounting) {
        if (salesOrderCreateGui == null) {
            salesOrderCreateGui = new PromoOrderCreateGUI(accounting);
            Main.addFrame(salesOrderCreateGui);
        }
        return salesOrderCreateGui;
    }
}