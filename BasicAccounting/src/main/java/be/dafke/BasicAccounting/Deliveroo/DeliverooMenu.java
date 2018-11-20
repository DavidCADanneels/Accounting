package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.awt.*;

public class DeliverooMenu extends JMenu {
    private JMenuItem dailyOrders;
    private Accounting accounting;

    public DeliverooMenu() {
        super("Deliveroo");
        dailyOrders = new JMenuItem("Daily Orders");
        dailyOrders.addActionListener(e -> {
            DeliverooGUI instance = DeliverooGUI.getInstance(accounting);
            instance.setVisible(true);
        });
        add(dailyOrders);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
