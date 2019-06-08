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

    public IngredientsPanel(Ingredients ingredients) {
        ingredientsDataTableModel = new IngredientsDataTableModel(this);
        ingredientsDataTableModel.setIngredients(ingredients);
        SelectableTable<Ingredient> ingredientsTable = new SelectableTable<>(ingredientsDataTableModel);
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        allergenesDataTableModel = new AllergenesDataTableModel();
        SelectableTable<Allergene> allergenesTable = new SelectableTable<>(allergenesDataTableModel);
        allergenesTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values());
        TableColumn unitColumn = ingredientsTable.getColumnModel().getColumn(IngredientsDataTableModel.UNIT_COL);
        unitColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPane = new JScrollPane(ingredientsTable);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JButton add = new JButton("Add Ingredient");
        add(add, BorderLayout.NORTH);
        add.addActionListener(e -> {
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
    }
}