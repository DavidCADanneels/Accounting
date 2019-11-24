package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.MealsPDF;
import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.Accounting.BusinessModel.Allergenes;
import be.dafke.Accounting.BusinessModel.Ingredients;
import be.dafke.BusinessModelDao.MealsIO;
import be.dafke.BusinessModelDao.SalesOrderIO;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

public class MealsMenu extends JMenu {
    private JMenuItem ordersOverview, allergenesMenuView, allergenesMenuEdit, ingredientsMenuView, ingredientsMenuEdit,
            mealIngredientsEdit,mealRecipeEdit, mealIngredientsView, mealRecipeView, generatePdf;

    private Accounting accounting;
    private Allergenes allergenes;

    public MealsMenu() {
        super("Meals");

        mealIngredientsView = new JMenuItem(getBundle("Accounting").getString("MEAL_INGREDIENTS_VIEW"));
        new JMenuItem("Meal Ingredients (View)");
        mealIngredientsView.addActionListener(e -> {
            MealIngredientsViewGUI gui = MealIngredientsViewGUI.showMeals(accounting);
            gui.setLocation(getLocationOnScreen());
            gui.setVisible(true);
        });

        mealIngredientsEdit = new JMenuItem(getBundle("Accounting").getString("MEAL_INGREDIENTS_EDIT"));
        new JMenuItem("Meal Ingredients (Edit)");
        mealIngredientsEdit.addActionListener(e -> {
            MealIngredientsEditGUI gui = MealIngredientsEditGUI.showMeals(accounting);
            gui.setLocation(getLocationOnScreen());
            gui.setVisible(true);
        });

        mealRecipeView = new JMenuItem(getBundle("Accounting").getString("MEAL_RECIPE_VIEW"));
        new JMenuItem("Meal Recipe (View)");
        mealRecipeView.addActionListener(e -> {
            MealRecipeViewGUI gui = MealRecipeViewGUI.showMeals(accounting);
            gui.setLocation(getLocationOnScreen());
            gui.setVisible(true);
        });

        mealRecipeEdit = new JMenuItem(getBundle("Accounting").getString("MEAL_RECIPE_EDIT"));
        new JMenuItem("Meal Recipe (Edit)");
        mealRecipeEdit.addActionListener(e -> {
            MealRecipeEditGUI gui = MealRecipeEditGUI.showMeals(accounting);
            gui.setLocation(getLocationOnScreen());
            gui.setVisible(true);
        });

        ordersOverview = new JMenuItem(getBundle("Accounting").getString("MEAL_ORDERS"));
        ordersOverview.addActionListener(e -> {
            MealOrdersOverviewGUI mealOrdersOverviewGUI = MealOrdersOverviewGUI.getInstance(accounting);
            mealOrdersOverviewGUI.setLocation(getLocationOnScreen());
            mealOrdersOverviewGUI.setVisible(true);
        });

        add(mealIngredientsView);
        add(mealIngredientsEdit);
        add(mealRecipeView);
        add(mealRecipeEdit);
        add(ordersOverview);

        ingredientsMenuView = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_VIEW"));
        ingredientsMenuView.setMnemonic(KeyEvent.VK_I);
        ingredientsMenuView.addActionListener(e -> {
            IngredientsViewGUI ingredientsViewGUI = IngredientsViewGUI.showIngredients(accounting);
            ingredientsViewGUI.setLocation(getLocationOnScreen());
            ingredientsViewGUI.setVisible(true);
        });
        ingredientsMenuView.setEnabled(false);

        ingredientsMenuEdit = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_EDIT"));
        ingredientsMenuEdit.setMnemonic(KeyEvent.VK_D);
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

        add(ingredientsMenuView);
        add(ingredientsMenuEdit);
        add(allergenesMenuView);
        add(allergenesMenuEdit);

        generatePdf = new JMenuItem(getBundle("Accounting").getString("MEAL_PDF"));
        generatePdf.setMnemonic(KeyEvent.VK_P);
        generatePdf.addActionListener(e -> {
            String xmlPath = MealsIO.writeMeals(accounting, true);
            String pdfPath = MealsIO.calculatePdfPath(accounting);
            MealsPDF.createPdf(xmlPath, pdfPath);
        });
        add(generatePdf);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setIngredients(accounting==null?null:accounting.getIngredients());
        setAllergenes(accounting==null?null:accounting.getAllergenes());
    }

    private void setIngredients(Ingredients ingredients){
        ingredientsMenuView.setEnabled(ingredients!=null);
        ingredientsMenuEdit.setEnabled(ingredients!=null);
    }

    private void setAllergenes(Allergenes allergenes){
        this.allergenes = allergenes;
        allergenesMenuView.setEnabled(allergenes!=null);
        allergenesMenuEdit.setEnabled(allergenes!=null);
    }
}
