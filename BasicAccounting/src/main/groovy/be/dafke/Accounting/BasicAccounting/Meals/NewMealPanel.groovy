package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.Meals
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

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

        price.addFocusListener(new FocusAdapter() {
            @Override
            void focusGained(FocusEvent e) {
                super.focusGained(e)
            }

            @Override
            void focusLost(FocusEvent e) {
                super.focusLost(e)
                validatePrice()
            }
        })

        add(new JLabel(getBundle("Accounting").getString("DESCRIPTION")))
        description = new JTextField(10)
        add(description)

        addButton = new JButton(getBundle("BusinessActions").getString("CREATE_NEW_MEAL"))
        addButton.enabled = false
        addButton.addActionListener({ e -> saveMeal() })
        add(addButton)
    }

    void validatePrice(){
        String priceString = price.getText()
        BigDecimal salesPrice = Utils.parseBigDecimal(priceString)
        if(salesPrice == null){
            ActionUtils.showErrorMessage(this, ActionUtils.INVALID_INPUT)
        }
        addButton.enabled = salesPrice!=null
    }

//    void setMeal(Meal meal) {
//        this.meal = meal
//        mealNr.setText(meal.name)
//        mealName.setText(meal.getMealName())
//        description.setText(meal.description)
//        BigDecimal salesPrice = meal.getSalesPrice()
//        price.setText(salesPrice.toString())
//    }

    void saveMeal() {
        String newName = mealNr.getText().trim()
        try {
            Meal meal = new Meal(newName.trim())
            meals.addBusinessObject(meal)
            meal.setDescription(description.getText())
            meal.setMealName(mealName.getText())
            String priceText = price.getText()
            BigDecimal salesPrice = Utils.parseBigDecimal(priceText)
            meal.setSalesPrice(salesPrice)
            clearFields()
        } catch (DuplicateNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_DUPLICATE_NAME, newName)
        } catch (EmptyNameException e) {
            ActionUtils.showErrorMessage(this, ActionUtils.ACCOUNT_NAME_EMPTY)
        }
    }

    void clearFields() {
        mealNr.setText("")
        mealName.setText("")
        description.setText("")
        price.setText("")
    }

}
