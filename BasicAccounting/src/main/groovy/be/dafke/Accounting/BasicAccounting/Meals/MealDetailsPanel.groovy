package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Utils.Utils

import javax.swing.*
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import static java.util.ResourceBundle.getBundle

class MealDetailsPanel extends JPanel {
    JTextField mealNr, mealName, price, description
    JButton updateButton
    Meal meal

    MealDetailsPanel(Meal meal) {
        this.meal = meal
        setLayout(new GridLayout(0,2))
        add(new JLabel(getBundle("Accounting").getString("MEAL_NR")))
        mealNr = new JTextField(20)
        mealNr.editable = false
        mealNr.setText(meal.name)
        add(mealNr)

        add(new JLabel(getBundle("Accounting").getString("MEAL_NAME")))
        mealName = new JTextField(10)
        mealName.setText(meal.mealName)
        add(mealName)

        add(new JLabel(getBundle("Accounting").getString("PRICE")))
        price = new JTextField(10)
        price.setText(meal.salesPrice.toString())
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
        description.setText(meal.description)
        add(description)

        updateButton = new JButton(getBundle("BusinessActions").getString("UPDATE_MEAL"))
        updateButton.addActionListener({ e -> saveMeal() })
        add(updateButton)
    }

    void validatePrice(){
        String priceString = price.getText()
        BigDecimal salesPrice = Utils.parseBigDecimal(priceString)
        if(salesPrice == null){
            ActionUtils.showErrorMessage(this, ActionUtils.INVALID_INPUT)
        }
        updateButton.enabled = salesPrice!=null
    }

    void saveMeal() {
        meal.setDescription(description.getText())
        meal.setMealName(mealName.getText())
        String priceText = price.getText()
        BigDecimal salesPrice = Utils.parseBigDecimal(priceText)
        meal.setSalesPrice(salesPrice)
    }
}
