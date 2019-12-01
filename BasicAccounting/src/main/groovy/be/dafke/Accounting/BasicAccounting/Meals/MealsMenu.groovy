package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.BusinessModel.Ingredients

import javax.swing.*
import java.awt.event.KeyEvent

import static java.util.ResourceBundle.getBundle

class MealsMenu extends JMenu {
    JMenuItem ordersOverview, allergenesMenuView, allergenesMenuEdit, ingredientsMenuView, ingredientsMenuEdit,
                      mealIngredientsEdit, mealRecipeEdit, mealIngredientsView, mealRecipeView, generatePdf

    Accounting accounting
    Allergenes allergenes

    MealsMenu() {
        super("Meals")

        mealIngredientsView = new JMenuItem(getBundle("Accounting").getString("MEAL_INGREDIENTS_VIEW"))
        new JMenuItem("Meal Ingredients (View)")
        mealIngredientsView.addActionListener({ e ->
            MealIngredientsViewGUI gui = MealIngredientsViewGUI.showMeals(accounting)
            gui.setLocation(getLocationOnScreen())
            gui.visible = true
        })

        mealIngredientsEdit = new JMenuItem(getBundle("Accounting").getString("MEAL_INGREDIENTS_EDIT"))
        new JMenuItem("Meal Ingredients (Edit)")
        mealIngredientsEdit.addActionListener({ e ->
            MealIngredientsEditGUI gui = MealIngredientsEditGUI.showMeals(accounting)
            gui.setLocation(getLocationOnScreen())
            gui.visible = true
        })

        mealRecipeView = new JMenuItem(getBundle("Accounting").getString("MEAL_RECIPE_VIEW"))
        new JMenuItem("Meal Recipe (View)")
        mealRecipeView.addActionListener({ e ->
            MealRecipeViewGUI gui = MealRecipeViewGUI.showMeals(accounting)
            gui.setLocation(getLocationOnScreen())
            gui.visible = true
        })

        mealRecipeEdit = new JMenuItem(getBundle("Accounting").getString("MEAL_RECIPE_EDIT"))
        new JMenuItem("Meal Recipe (Edit)")
        mealRecipeEdit.addActionListener({ e ->
            MealRecipeEditGUI gui = MealRecipeEditGUI.showMeals(accounting)
            gui.setLocation(getLocationOnScreen())
            gui.visible = true
        })

        ordersOverview = new JMenuItem(getBundle("Accounting").getString("MEAL_ORDERS"))
        ordersOverview.addActionListener({ e ->
            MealOrdersOverviewGUI mealOrdersOverviewGUI = MealOrdersOverviewGUI.getInstance(accounting)
            mealOrdersOverviewGUI.setLocation(getLocationOnScreen())
            mealOrdersOverviewGUI.visible = true
        })

        add(mealIngredientsView)
        add(mealIngredientsEdit)
        add(mealRecipeView)
        add(mealRecipeEdit)
        add(ordersOverview)

        ingredientsMenuView = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_VIEW"))
        ingredientsMenuView.setMnemonic(KeyEvent.VK_I)
        ingredientsMenuView.addActionListener({ e ->
            IngredientsViewGUI ingredientsViewGUI = IngredientsViewGUI.showIngredients(accounting)
            ingredientsViewGUI.setLocation(getLocationOnScreen())
            ingredientsViewGUI.visible = true
        })
        ingredientsMenuView.enabled = false

        ingredientsMenuEdit = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_EDIT"))
        ingredientsMenuEdit.setMnemonic(KeyEvent.VK_D)
        ingredientsMenuEdit.addActionListener({ e ->
            IngredientsEditGUI ingredientsEditGUI = IngredientsEditGUI.showIngredients(accounting)
            ingredientsEditGUI.setLocation(getLocationOnScreen())
            ingredientsEditGUI.visible = true
        })
        ingredientsMenuEdit.enabled = false

        allergenesMenuEdit = new JMenuItem(getBundle("Accounting").getString("ALLERGENES_EDIT"))
        allergenesMenuEdit.setMnemonic(KeyEvent.VK_E)
        allergenesMenuEdit.addActionListener({ e ->
            AllergenesEditGUI allergenesEditGUI = AllergenesEditGUI.showAllergenes(allergenes)
            allergenesEditGUI.setLocation(getLocationOnScreen())
            allergenesEditGUI.visible = true
        })
        allergenesMenuEdit.enabled = false

        allergenesMenuView = new JMenuItem(getBundle("Accounting").getString("ALLERGENES_VIEW"))
        allergenesMenuView.setMnemonic(KeyEvent.VK_V)
        allergenesMenuView.addActionListener({ e ->
            AllergenesViewGUI allergenesViewGUI = AllergenesViewGUI.showAllergenes(allergenes)
            allergenesViewGUI.setLocation(getLocationOnScreen())
            allergenesViewGUI.visible = true
        })
        allergenesMenuView.enabled = false

        add(ingredientsMenuView)
        add(ingredientsMenuEdit)
        add(allergenesMenuView)
        add(allergenesMenuEdit)

        generatePdf = new JMenuItem(getBundle("Accounting").getString("MEAL_PDF"))
        generatePdf.setMnemonic(KeyEvent.VK_P)
        generatePdf.addActionListener({ e ->
            String xmlPath = MealsIO.writeMeals(accounting, true)
            String pdfPath = MealsIO.calculatePdfPath(accounting)
            MealsPDF.createPdf(xmlPath, pdfPath)
        })
        add(generatePdf)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        setIngredients(accounting?accounting.ingredients:null)
        setAllergenes(accounting?accounting.allergenes:null)
    }

    void setIngredients(Ingredients ingredients){
        ingredientsMenuView.enabled = ingredients!=null
        ingredientsMenuEdit.enabled = ingredients!=null
    }

    void setAllergenes(Allergenes allergenes){
        this.allergenes = allergenes
        allergenesMenuView.enabled = allergenes!=null
        allergenesMenuEdit.enabled = allergenes!=null
    }
}
