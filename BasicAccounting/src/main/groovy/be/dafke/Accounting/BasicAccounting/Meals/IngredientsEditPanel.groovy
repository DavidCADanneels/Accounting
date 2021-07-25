package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.table.TableColumn
import java.awt.*

import static java.util.ResourceBundle.getBundle

class IngredientsEditPanel extends JPanel {
    final IngredientsEditDataTableModel ingredientsDataEditTableModel
    final AllergenesViewPanel allergenesViewPanel
    final SelectableTable<Ingredient> ingredientsTable
    final JButton addAllergeneButton
    Ingredient selectedIngredient

    Ingredients ingredients
    Allergenes allergenes
    Accounting accounting

    IngredientsEditPanel() {
        ingredientsDataEditTableModel = new IngredientsEditDataTableModel(this)
        ingredientsTable = new SelectableTable<>(ingredientsDataEditTableModel)
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200))
        ingredientsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
        allergenesViewPanel = new AllergenesViewPanel(false)

        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values())
        TableColumn unitColumn = ingredientsTable.getColumnModel().getColumn(IngredientsEditDataTableModel.UNIT_COL)
        unitColumn.setCellEditor(new DefaultCellEditor(comboBox))

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

        JButton addIngredientButton = new JButton("Add Ingredient")
        add(addIngredientButton, BorderLayout.NORTH)
        addIngredientButton.addActionListener({ e ->
            addIngredient()
        })

        addAllergeneButton = new JButton("Update Allergenes")
        addAllergeneButton.enabled = false
        add(addAllergeneButton, BorderLayout.SOUTH)
        addAllergeneButton.addActionListener({ e ->
            addAllergene()
        })


        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        ingredientsTable.setSelectionModel(selection)
    }

    void updateSelection() {
        selectedIngredient = ingredientsTable.selectedObject
        addAllergeneButton.enabled = selectedIngredient != null
        Allergenes allergenes = selectedIngredient?selectedIngredient.allergenes:null
        allergenesViewPanel.allergenes = allergenes
        allergenesViewPanel.selectFirstLine()
    }

    void addIngredient(){
        String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
        while (name != null && name.equals(""))
            name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
        if (name != null) {
            try {
                ingredients.addBusinessObject(new Ingredient(name, Unit.PIECE))
                allergenesViewPanel.updateTable()
            } catch (EmptyNameException ex) {
                ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY)
            } catch (DuplicateNameException ex) {
                ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim())
            }
        }
    }

    void setAccounting(Accounting accounting){
        this.accounting = accounting
        setAllergenes(accounting?.allergenes)
        setIngredients(accounting?.ingredients)
    }

    void setAllergenes(Allergenes allergenes){
        this.allergenes = allergenes

    }

    void setIngredients(Ingredients ingredients){
        this.ingredients = ingredients
        ingredientsDataEditTableModel.setIngredients(ingredients)
    }

    void addAllergene(){
        Object[] allergeneList = allergenes.businessObjects.toArray()
        if (allergeneList.length > 0) {
            AllergenesPerIngredientDialog dialog = new AllergenesPerIngredientDialog(selectedIngredient, allergenes)
            dialog.visible = true
            ingredientsDataEditTableModel.fireTableDataChanged()
            allergenesViewPanel.updateTable()
        }
    }
}