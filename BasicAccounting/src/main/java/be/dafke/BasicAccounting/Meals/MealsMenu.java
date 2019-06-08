package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

public class MealsMenu extends JMenu {
    private JMenuItem ordersOverview, meals;
    private Accounting accounting;

    public MealsMenu() {
        super("Meals");
        meals = new JMenuItem("Available Meals");
        meals.addActionListener(e -> {
            MealsGUI mealsGUI = MealsGUI.showMeals(accounting);
            mealsGUI.setLocation(getLocationOnScreen());
            mealsGUI.setVisible(true);
        });
        ordersOverview = new JMenuItem("Meal Orders");
        ordersOverview.addActionListener(e -> {
            MealOrdersOverviewGUI mealOrdersOverviewGUI = MealOrdersOverviewGUI.getInstance(accounting);
            mealOrdersOverviewGUI.setLocation(getLocationOnScreen());
            mealOrdersOverviewGUI.setVisible(true);
        });
        add(meals);
        add(ordersOverview);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
