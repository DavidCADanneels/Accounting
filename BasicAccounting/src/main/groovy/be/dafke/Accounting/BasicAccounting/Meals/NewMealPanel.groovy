package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.Meals
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class NewMealPanel extends JPanel {
    JTextField mealNr, mealName, price, description
    JButton addButton
    Meal meal
    Meals meals

    NewMealPanel(Meals meals) {
        this.meals = meals
        setLayout(new GridLayout(0,2))
        add(new JLabel(getBundle("Accounting").getString("MEAL_NR")))
        mealNr = new JTextField(20)
        add(mealNr)

        add(new JLabel(getBundle("Accounting").getString("MEAL_NAME")))
        mealName = new JTextField(10)
        add(mealName)

        add(new JLabel(getBundle("Accounting").getString("PRICE")))
        price = new JTextField(10)
        add(price)

        add(new JLabel(getBundle("Accounting").getString("DESCRIPTION")))
        description = new JTextField(10)
        add(description)

        addButton = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_MEAL"))
        addButton.addActionListener({ e -> saveMeal() })
        add(addButton)
    }

    void setMeal(Meal meal) {
        this.meal = meal
        mealNr.setText(meal.name)
        mealName.setText(meal.getMealName())
        description.setText(meal.description)
        BigDecimal salesPrice = meal.getSalesPrice()
        price.setText(salesPrice.toString())
    }

    void saveMeal() {
        String newName = mealNr.getText().trim()
        try {
            if (meal == null) {
                meal = new Meal(newName.trim())
                meals.addBusinessObject(meal)
//                Main.fireAccountDataChanged(meal)
                saveOtherProperties()
                meal = null
                clearFields()
            } else {
                String oldName = meal.name
                meals.modifyName(oldName, newName)
                saveOtherProperties()
            }
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_DUPLICATE_NAME, newName)
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_NAME_EMPTY)
        }
    }


    void saveOtherProperties(){
        meal.setDescription(description.getText())
        meal.setMealName(mealName.getText())
        String priceText = price.getText()
        BigDecimal salesPrice = Utils.parseBigDecimal(priceText)
        meal.setSalesPrice(salesPrice)

//        Main.fireAccountDataChanged(account)
    }

    void clearFields() {
        mealNr.setText("")
        mealName.setText("")
        description.setText("")
    }

}
