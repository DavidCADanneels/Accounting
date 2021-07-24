package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.DefaultListSelectionModel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.ListSelectionModel
import java.awt.BorderLayout
import java.awt.Dimension

class IngredientsViewPanel extends JPanel {
    final IngredientsViewDataTableModel ingredientsDataViewTableModel
    final AllergenesViewPanel allergenesViewPanel
    final SelectableTable<Ingredient> ingredientsTable

    IngredientsViewPanel() {
        ingredientsDataViewTableModel = new IngredientsViewDataTableModel(this)
        ingredientsTable = new SelectableTable<>(ingredientsDataViewTableModel)
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200))
        ingredientsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        allergenesViewPanel = new AllergenesViewPanel(true)

        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTable)
        JPanel ingredientsPanel = new JPanel(new BorderLayout())
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER)

        JScrollPane allergenesScrollPane = new JScrollPane(allergenesViewPanel)
        JPanel allergenesPanel = new JPanel(new BorderLayout())
        allergenesPanel.add(allergenesScrollPane, BorderLayout.CENTER)

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        splitPane.add(ingredientsPanel, JSplitPane.TOP)
        splitPane.add(allergenesScrollPane, JSplitPane.BOTTOM)

        setLayout(new BorderLayout())
        add(splitPane, BorderLayout.CENTER)

        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        ingredientsTable.setSelectionModel(selection)
    }

    void updateSelection() {
        ArrayList<Ingredient> selectedObjects = ingredientsTable.selectedObjects
        Allergenes allergenes = new Allergenes()
        selectedObjects.forEach({ ingredient ->
            Allergenes newAllergenes = ingredient.allergenes
            newAllergenes.businessObjects.forEach({ allergene ->
                try {
                    allergenes.addBusinessObject(allergene)
                } catch (EmptyNameException | DuplicateNameException e1) {
                    e1.printStackTrace()
                }
            })
        })
        allergenesViewPanel.setAllergenes(allergenes)
        allergenesViewPanel.selectAll()
    }

    void setIngredients(Ingredients ingredients) {
        ingredientsDataViewTableModel.setIngredients(ingredients)
        ingredientsDataViewTableModel.fireTableDataChanged()
    }

    void selectAll(){
//        // TODO: use select all
        int rowCount = ingredientsTable.getRowCount()
        if(rowCount >0){
            ingredientsTable.setRowSelectionInterval(0, rowCount - 1)
        }
    }
}