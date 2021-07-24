package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.JButton
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.CardLayout

class IngredientsAndAllergenesSwitchViewPanel extends JPanel {
    CardLayout cardLayout
    JPanel center

    JButton switchButton
    boolean editMode

    static final String VIEW = "View"
    static final String EDIT = "Edit"

    IngredientsAndAllergenesViewPanel viewPanel
    IngredientsAndAllergenesEditPanel editPanel

    IngredientsAndAllergenesSwitchViewPanel() {
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        editMode = false
        switchButton = new JButton("Edit")
        switchButton.addActionListener( { e ->
            switchView()
        })

        viewPanel = new IngredientsAndAllergenesViewPanel()
        editPanel = new IngredientsAndAllergenesEditPanel()

        center.add(viewPanel, VIEW)
        center.add(editPanel, EDIT)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(switchButton,BorderLayout.NORTH)
    }

    void setAccounting(Accounting accounting){
        viewPanel.accounting = accounting
        editPanel.accounting = accounting
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
