package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

public class DeliverooMenu extends JMenu {
    private JMenuItem dailyOrders, meals;
    private Accounting accounting;

    public DeliverooMenu() {
        super("Deliveroo");
        meals = new JMenuItem("Meals");
        meals.addActionListener(e -> {
            MealsGUI mealsGUI = MealsGUI.showMeals(accounting.getDeliverooMeals());
            mealsGUI.setLocation(getLocationOnScreen());
            mealsGUI.setVisible(true);
        });
        dailyOrders = new JMenuItem("Daily Orders");
        dailyOrders.addActionListener(e -> {
            DeliverooGUI deliverooOrders = DeliverooGUI.getInstance(accounting);
            deliverooOrders.setLocation(getLocationOnScreen());
            deliverooOrders.setVisible(true);
        });
        add(meals);
        add(dailyOrders);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
