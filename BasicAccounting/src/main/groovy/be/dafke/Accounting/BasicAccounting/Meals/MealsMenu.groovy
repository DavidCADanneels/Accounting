package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.PDFGeneration.MealsPDF
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.BusinessModelDao.MealsIO

import javax.swing.*
import java.awt.event.KeyEvent

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.ACCOUNTINGS_XML_PATH
import static be.dafke.Accounting.BusinessModelDao.XMLConstants.PDF_EXTENSION
import static java.util.ResourceBundle.getBundle

class MealsMenu extends JMenu {
    JMenuItem ordersOverview, allergenesMenuView, allergenesMenuEdit, ingredientsMenuView, ingredientsMenuEdit,
            mealRecipeEdit, mealIngredientsView, mealRecipeView, generateMealsAllergenesPdf,
    generateMealsInstructionPdf, generateMealsIngredientsPdf


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

        generateMealsAllergenesPdf = new JMenuItem(getBundle("Accounting").getString("MEAL_ALLERGENE_PDF"))
        generateMealsAllergenesPdf.setMnemonic(KeyEvent.VK_R)
        generateMealsAllergenesPdf.addActionListener({ e ->
            String xmlPath = MealsIO.writeMealsWithAllDetails(accounting)
            String pdfPath = "$ACCOUNTINGS_XML_PATH/$accounting.name/MealsWithAllergenes$PDF_EXTENSION"
            MealsPDF.createPdf(xmlPath, pdfPath, MealsPDF.xslPathWithAllergenes)
        })
        add(generateMealsAllergenesPdf)

        generateMealsInstructionPdf = new JMenuItem(getBundle("Accounting").getString("MEAL_INSTRUCTIONS_PDF"))
        generateMealsInstructionPdf.setMnemonic(KeyEvent.VK_A)
        generateMealsInstructionPdf.addActionListener({ e ->
            String xmlPath = MealsIO.writeMealsWithAllDetails(accounting)
            String pdfPath = "$ACCOUNTINGS_XML_PATH/$accounting.name/MealsWithInstructions$PDF_EXTENSION"
            MealsPDF.createPdf(xmlPath, pdfPath, MealsPDF.xslPathWithInstructions)
        })
        add(generateMealsInstructionPdf)

        generateMealsIngredientsPdf = new JMenuItem(getBundle("Accounting").getString("MEAL_INGREDIENTS_PDF"))
        generateMealsIngredientsPdf.setMnemonic(KeyEvent.VK_T)
        generateMealsIngredientsPdf.addActionListener({ e ->
            String xmlPath = MealsIO.writeMealsWithAllDetails(accounting)
            String pdfPath = "$ACCOUNTINGS_XML_PATH/$accounting.name/MealsWithIngredients$PDF_EXTENSION"
            MealsPDF.createPdf(xmlPath, pdfPath, MealsPDF.xslPathWithIngredients)
        })
        add(generateMealsIngredientsPdf)
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
