package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.table.TableColumn
import java.awt.*

import static java.util.ResourceBundle.getBundle

class IngredientsEditPanel extends JPanel {
    private final IngredientsDataEditTableModel ingredientsDataEditTableModel
    private final AllergenesViewPanel allergenesViewPanel
    private final SelectableTable<Ingredient> ingredientsTable
    private final JButton addAllergene
    private Ingredient selectedIngredient

    private Ingredients ingredients
    private Allergenes allergenes

    IngredientsEditPanel(Accounting accounting) {
        ingredients = accounting.getIngredients()
        allergenes = accounting.getAllergenes()
        ingredientsDataEditTableModel = new IngredientsDataEditTableModel(this)
        ingredientsDataEditTableModel.setIngredients(ingredients)
        ingredientsTable = new SelectableTable<>(ingredientsDataEditTableModel)
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        allergenesViewPanel = new AllergenesViewPanel(false)

        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values())
        TableColumn unitColumn = ingredientsTable.getColumnModel().getColumn(IngredientsDataEditTableModel.UNIT_COL)
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

        JButton addIngredient = new JButton("Add Ingredient")
        add(addIngredient, BorderLayout.NORTH)
        addIngredient.addActionListener({ e ->
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            if (name != null) {
                try {
                    ingredients.addBusinessObject(new Ingredient(name, Unit.PIECE))
                    allergenesViewPanel.updateTable()
                    Main.fireIngredientsAddedOrRemoved(accounting)
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY)
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim())
                }
            }
        })

        addAllergene = new JButton("Update Allergenes")
        addAllergene.setEnabled(false)
        add(addAllergene, BorderLayout.SOUTH)
        addAllergene.addActionListener({ e ->
            Object[] allergeneList = allergenes.getBusinessObjects().toArray()
            if (allergeneList.length > 0) {
                AllergenesPerIngredientDialog dialog = new AllergenesPerIngredientDialog(selectedIngredient, allergenes)
                dialog.setVisible(true)
                ingredientsDataEditTableModel.fireTableDataChanged()
                allergenesViewPanel.updateTable()
            }
        })


        DefaultListSelectionModel selection = new DefaultListSelectionModel()
        selection.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                selectedIngredient = ingredientsTable.getSelectedObject()
                addAllergene.setEnabled(selectedIngredient != null)
                Allergenes allergenes = selectedIngredient == null ? null : selectedIngredient.getAllergenes()
                allergenesViewPanel.setAllergenes(allergenes)
                allergenesViewPanel.selectFirstLine()
            }
        })
        ingredientsTable.setSelectionModel(selection)
        ingredientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        //SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
        //SINGLE_INTERVAL_SELECTION   ListSelectionModel.SINGLE_INTERVAL_SELECTION
        //MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
    }
}