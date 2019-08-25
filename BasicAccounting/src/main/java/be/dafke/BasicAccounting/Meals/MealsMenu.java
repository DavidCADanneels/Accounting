package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.BusinessModel.Ingredients;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

public class MealsMenu extends JMenu {
    private JMenuItem ordersOverview, meals, allergenesMenuView, allergenesMenuEdit, ingredientsMenuView, ingredientsMenuEdit;
//    allergenesMenu, ingredientsMenu

    private Accounting accounting;
//    private Ingredients ingredients;
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

//        ingredientsMenu = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS"));
//        ingredientsMenu.setMnemonic(KeyEvent.VK_I);
//        ingredientsMenu.addActionListener(e -> {
//            IngredientsViewGUI ingredientsViewGUI = IngredientsViewGUI.showIngredients(accounting);
//            ingredientsViewGUI.setLocation(getLocationOnScreen());
//            ingredientsViewGUI.setVisible(true);
//        });
//        ingredientsMenu.setEnabled(false);

//        allergenesMenu = new JMenuItem(getBundle("Accounting").getString("ALLERGENES"));
//        allergenesMenu.setMnemonic(KeyEvent.VK_A);
//        allergenesMenu.addActionListener(e -> {
//            AllergenesEditGUI allergenesEditGUI = AllergenesEditGUI.showAllergenes(allergenes);
//            allergenesEditGUI.setLocation(getLocationOnScreen());
//            allergenesEditGUI.setVisible(true);
//        });
//        allergenesMenu.setEnabled(false);

        ingredientsMenuView = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_VIEW"));
        ingredientsMenuView.setMnemonic(KeyEvent.VK_I);
        ingredientsMenuView.addActionListener(e -> {
            IngredientsViewGUI ingredientsViewGUI = IngredientsViewGUI.showIngredients(accounting);
            ingredientsViewGUI.setLocation(getLocationOnScreen());
            ingredientsViewGUI.setVisible(true);
        });
        ingredientsMenuView.setEnabled(false);

        ingredientsMenuEdit = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_EDIT"));
        ingredientsMenuEdit.setMnemonic(KeyEvent.VK_I);
        ingredientsMenuEdit.addActionListener(e -> {
            IngredientsEditGUI ingredientsEditGUI = IngredientsEditGUI.showIngredients(accounting);
            ingredientsEditGUI.setLocation(getLocationOnScreen());
            ingredientsEditGUI.setVisible(true);
        });
        ingredientsMenuEdit.setEnabled(false);

        allergenesMenuEdit = new JMenuItem(getBundle("Accounting").getString("ALLERGENES_EDIT"));
        allergenesMenuEdit.setMnemonic(KeyEvent.VK_E);
        allergenesMenuEdit.addActionListener(e -> {
            AllergenesEditGUI allergenesEditGUI = AllergenesEditGUI.showAllergenes(allergenes);
            allergenesEditGUI.setLocation(getLocationOnScreen());
            allergenesEditGUI.setVisible(true);
        });
        allergenesMenuEdit.setEnabled(false);

        allergenesMenuView = new JMenuItem(getBundle("Accounting").getString("ALLERGENES_VIEW"));
        allergenesMenuView.setMnemonic(KeyEvent.VK_V);
        allergenesMenuView.addActionListener(e -> {
            AllergenesViewGUI allergenesViewGUI = AllergenesViewGUI.showAllergenes(allergenes);
            allergenesViewGUI.setLocation(getLocationOnScreen());
            allergenesViewGUI.setVisible(true);
        });
        allergenesMenuView.setEnabled(false);


//        add(ingredientsMenu);
        add(ingredientsMenuView);
        add(ingredientsMenuEdit);
//        add(allergenesMenu);
        add(allergenesMenuView);
        add(allergenesMenuEdit);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setIngredients(accounting==null?null:accounting.getIngredients());
        setAllergenes(accounting==null?null:accounting.getAllergenes());
    }

    private void setIngredients(Ingredients ingredients){
//        ingredientsMenu.setEnabled(ingredients!=null);
        ingredientsMenuView.setEnabled(ingredients!=null);
        ingredientsMenuEdit.setEnabled(ingredients!=null);
    }

    private void setAllergenes(Allergenes allergenes){
        this.allergenes = allergenes;
//        allergenesMenu.setEnabled(allergenes!=null);
        allergenesMenuView.setEnabled(allergenes!=null);
        allergenesMenuEdit.setEnabled(allergenes!=null);
    }
}
