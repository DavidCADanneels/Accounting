package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

public class DeliverooMenu extends JMenu {
    private JMenuItem ordersOverview, meals;
    private Accounting accounting;

    public DeliverooMenu() {
        super("Deliveroo");
        meals = new JMenuItem("Meals");
        meals.addActionListener(e -> {
            MealsGUI mealsGUI = MealsGUI.showMeals(accounting);
            mealsGUI.setLocation(getLocationOnScreen());
            mealsGUI.setVisible(true);
        });
        ordersOverview = new JMenuItem("Daily Orders");
        ordersOverview.addActionListener(e -> {
            DeliverooOrdersOverviewGUI deliverooOrders = DeliverooOrdersOverviewGUI.getInstance(accounting);
            deliverooOrders.setLocation(getLocationOnScreen());
            deliverooOrders.setVisible(true);
        });
        add(meals);
        add(ordersOverview);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
