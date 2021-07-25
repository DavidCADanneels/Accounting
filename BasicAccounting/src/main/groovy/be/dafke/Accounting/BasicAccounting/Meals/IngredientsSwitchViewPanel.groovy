package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import java.awt.*

class IngredientsSwitchViewPanel extends JPanel {
    CardLayout cardLayout
    JPanel center

    JButton switchButton
    boolean editMode

    static final String VIEW = "View"
    static final String EDIT = "Edit"

    final IngredientsViewPanel ingredientsViewPanel
    final IngredientsEditPanel ingredientsEditPanel

    IngredientsSwitchViewPanel() {
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        editMode = false
        switchButton = new JButton("Edit")
        switchButton.addActionListener( { e ->
            switchView()
        })

        ingredientsViewPanel = new IngredientsViewPanel()
        ingredientsEditPanel = new IngredientsEditPanel()

        center.add(ingredientsViewPanel, VIEW)
        center.add(ingredientsEditPanel, EDIT)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(switchButton,BorderLayout.NORTH)
    }

    void refresh(){
        Accounting accounting = Session.activeAccounting
        ingredientsViewPanel.ingredients = accounting.ingredients
        ingredientsEditPanel.accounting = accounting
    }

    void switchView(){
        if(editMode){
            cardLayout.show(center, VIEW)
            editMode = false
            switchButton.setText("Edit")
        } else {
            cardLayout.show(center, EDIT)
            editMode = true
            switchButton.setText("View")
        }
    }
}
