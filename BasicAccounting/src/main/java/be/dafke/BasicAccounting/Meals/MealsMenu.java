package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.BusinessModel.Ingredients;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

public class MealsMenu extends JMenu {
    private JMenuItem ordersOverview, meals, ingredientsMenu, allergenesMenu;

    private Accounting accounting;
    private Ingredients ingredients;
    private Allergenes allergenes;

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

        ingredientsMenu = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS"));
        ingredientsMenu.setMnemonic(KeyEvent.VK_I);
        ingredientsMenu.addActionListener(e -> {
            IngredientsGUI ingredientsGUI = IngredientsGUI.showIngredients(accounting);
            ingredientsGUI.setLocation(getLocationOnScreen());
            ingredientsGUI.setVisible(true);
        });
        ingredientsMenu.setEnabled(false);

        allergenesMenu = new JMenuItem(getBundle("Accounting").getString("ALLERGENES"));
        allergenesMenu.setMnemonic(KeyEvent.VK_A);
        allergenesMenu.addActionListener(e -> {
            AllergenesGUI allergenesGUI = AllergenesGUI.showAllergenes(allergenes);
            allergenesGUI.setLocation(getLocationOnScreen());
            allergenesGUI.setVisible(true);
        });
        allergenesMenu.setEnabled(false);

        add(ingredientsMenu);
        add(allergenesMenu);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setIngredients(accounting==null?null:accounting.getIngredients());
        setAllergenes(accounting==null?null:accounting.getAllergenes());
    }

    public void setIngredients(Ingredients ingredients){
        this.ingredients = ingredients;
        ingredientsMenu.setEnabled(ingredients!=null);
    }

    public void setAllergenes(Allergenes allergenes){
        this.allergenes = allergenes;
        allergenesMenu.setEnabled(allergenes!=null);
    }
}
