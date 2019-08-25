package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.BusinessModel.Ingredients;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

public class MealsMenu extends JMenu {
    private JMenuItem ordersOverview, meals, ingredientsMenu, allergenesMenuView, allergenesMenuEdit;
//    allergenesMenu

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

//        allergenesMenu = new JMenuItem(getBundle("Accounting").getString("ALLERGENES"));
//        allergenesMenu.setMnemonic(KeyEvent.VK_A);
//        allergenesMenu.addActionListener(e -> {
//            AllergenesEditGUI allergenesEditGUI = AllergenesEditGUI.showAllergenes(allergenes);
//            allergenesEditGUI.setLocation(getLocationOnScreen());
//            allergenesEditGUI.setVisible(true);
//        });
//        allergenesMenu.setEnabled(false);

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


        add(ingredientsMenu);
//        add(allergenesMenu);
        add(allergenesMenuView);
        add(allergenesMenuEdit);
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
//        allergenesMenu.setEnabled(allergenes!=null);
        allergenesMenuView.setEnabled(allergenes!=null);
        allergenesMenuEdit.setEnabled(allergenes!=null);
    }
}
