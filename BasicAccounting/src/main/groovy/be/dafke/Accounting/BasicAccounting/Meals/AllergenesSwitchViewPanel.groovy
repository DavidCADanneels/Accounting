package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class AllergenesSwitchViewPanel extends JPanel {
    CardLayout cardLayout
    JPanel center

    JButton switchButton
    boolean editMode

    static final String VIEW = "View"
    static final String EDIT = "Edit"

    final AllergenesViewPanel allergenesViewPanel
    final AllergenesEditPanel allergenesEditPanel

    AllergenesSwitchViewPanel() {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Allergenes"))
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        editMode = false
        switchButton = new JButton("Edit")
        switchButton.addActionListener( { e ->
            switchView()
        })

        allergenesViewPanel = new AllergenesViewPanel(false)
        allergenesEditPanel = new AllergenesEditPanel()

        center.add(allergenesViewPanel, VIEW)
        center.add(allergenesEditPanel, EDIT)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(switchButton,BorderLayout.NORTH)
    }

    void refresh(){
        Accounting accounting = Session.activeAccounting
        allergenesViewPanel.allergenes = accounting.allergenes
        allergenesEditPanel.accounting = accounting
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
