package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

public class DeliverooMenu extends JMenu {
    private JMenuItem dailyOrders, ordersOverview, meals;
    private Accounting accounting;

    public DeliverooMenu() {
        super("Deliveroo");
        meals = new JMenuItem("Meals");
        meals.addActionListener(e -> {
            MealsGUI mealsGUI = MealsGUI.showMeals(accounting.getDeliverooMeals());
            mealsGUI.setLocation(getLocationOnScreen());
            mealsGUI.setVisible(true);
        });
        dailyOrders = new JMenuItem("Daily Order Input");
        dailyOrders.addActionListener(e -> {
            DeliverooOrderCreateGUI deliverooOrders = DeliverooOrderCreateGUI.getInstance(accounting);
            deliverooOrders.setLocation(getLocationOnScreen());
            deliverooOrders.setVisible(true);
        });
        ordersOverview = new JMenuItem("Daily Order Overview");
        ordersOverview.addActionListener(e -> {
            DeliverooOrdersOverviewGUI deliverooOrders = DeliverooOrdersOverviewGUI.getInstance(accounting);
            deliverooOrders.setLocation(getLocationOnScreen());
            deliverooOrders.setVisible(true);
        });
        add(meals);
        add(dailyOrders);
        add(ordersOverview);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
