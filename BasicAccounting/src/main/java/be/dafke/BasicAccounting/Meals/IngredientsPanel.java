package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Set;

import static java.util.ResourceBundle.getBundle;

public class IngredientsPanel extends JPanel {
    private final IngredientsDataTableModel ingredientsDataTableModel;
    private final AllergenesDataTableModel allergenesDataTableModel;
    private final SelectableTable<Ingredient> ingredientsTable;
    private final SelectableTable<Allergene> allergenesTable;
    private final JButton addAllergene;
    private Ingredient selectedIngredient;

    private Ingredients ingredients;
    private Allergenes allergenes;

    public IngredientsPanel(Accounting accounting) {
        ingredients = accounting.getIngredients();
        allergenes = accounting.getAllergenes();
        ingredientsDataTableModel = new IngredientsDataTableModel(this);
        ingredientsDataTableModel.setIngredients(ingredients);
        ingredientsTable = new SelectableTable<>(ingredientsDataTableModel);
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        allergenesDataTableModel = new AllergenesDataTableModel();
        allergenesTable = new SelectableTable<>(allergenesDataTableModel);
        allergenesTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values());
        TableColumn unitColumn = ingredientsTable.getColumnModel().getColumn(IngredientsDataTableModel.UNIT_COL);
        unitColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTable);
        JPanel ingredientsPanel = new JPanel(new BorderLayout());
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER);

        JScrollPane allergenesScrollPane = new JScrollPane(allergenesTable);
        JPanel allergenesPanel = new JPanel(new BorderLayout());
        allergenesPanel.add(allergenesScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(ingredientsPanel, JSplitPane.TOP);
        splitPane.add(allergenesScrollPane, JSplitPane.BOTTOM);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        JButton addIngredient = new JButton("Add Ingredient");
        add(addIngredient, BorderLayout.NORTH);
        addIngredient.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    ingredients.addBusinessObject(new Ingredient(name, Unit.PIECE));
                    allergenesDataTableModel.fireTableDataChanged();
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY);
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim());
                }
            }
        });

        addAllergene = new JButton("Update Allergenes");
        addAllergene.setEnabled(false);
        add(addAllergene, BorderLayout.SOUTH);
        addAllergene.addActionListener(e -> {
            Object[] allergeneList = allergenes.getBusinessObjects().toArray();
            if(allergeneList.length>0) {
                AllergenesPerIngredientDialog dialog = new AllergenesPerIngredientDialog(selectedIngredient, allergenes);
                dialog.setVisible(true);
                ingredientsDataTableModel.fireTableDataChanged();
                allergenesDataTableModel.fireTableDataChanged();
            }
        });


        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedIngredient = ingredientsTable.getSelectedObject();
                if(selectedIngredient !=null) {
                    addAllergene.setEnabled(true);
                    Allergenes allergenes = selectedIngredient.getAllergenes();
                    allergenesDataTableModel.setAllergenes(allergenes);
                } else {
                    addAllergene.setEnabled(false);
                }
            }
        });
        ingredientsTable.setSelectionModel(selection);
    }
}