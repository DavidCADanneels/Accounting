package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class IngredientsPanel extends JPanel {
    private final IngredientsDataTableModel ingredientsDataTableModel;
    private final AllergenesDataTableModel allergenesDataTableModel;
    private final SelectableTable<Ingredient> ingredientsTable;
    private final SelectableTable<Allergene> allergenesTable;

    public IngredientsPanel(Ingredients ingredients) {
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

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Ingredient ingredient = ingredientsTable.getSelectedObject();
                Allergenes allergenes = ingredient.getAllergenes();
                allergenesDataTableModel.setAllergenes(allergenes);
            }
        });
        ingredientsTable.setSelectionModel(selection);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(ingredientsPanel);
        splitPane.add(allergenesScrollPane);

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

        JButton addAllergene = new JButton("Add Allergene");
        add(addAllergene, BorderLayout.SOUTH);
    }
}