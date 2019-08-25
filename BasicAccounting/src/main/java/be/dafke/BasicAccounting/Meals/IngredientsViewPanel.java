package be.dafke.BasicAccounting.Meals;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

public class IngredientsViewPanel extends JPanel {
    private final IngredientsDataViewTableModel ingredientsDataViewTableModel;
    private final AllergenesViewPanel allergenesViewPanel;
    private final SelectableTable<Ingredient> ingredientsTable;
    private Ingredient selectedIngredient;

    private Ingredients ingredients;

    public IngredientsViewPanel(Accounting accounting) {
        ingredients = accounting.getIngredients();
        ingredientsDataViewTableModel = new IngredientsDataViewTableModel(this);
        ingredientsDataViewTableModel.setIngredients(ingredients);
        ingredientsTable = new SelectableTable<>(ingredientsDataViewTableModel);
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

//        allergenesDataTableModel = new AllergenesDataTableModel();
//        allergenesTable = new SelectableTable<>(allergenesDataTableModel);
//        allergenesTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        allergenesViewPanel = new AllergenesViewPanel();

        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values());
        TableColumn unitColumn = ingredientsTable.getColumnModel().getColumn(IngredientsDataEditTableModel.UNIT_COL);
        unitColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTable);
        JPanel ingredientsPanel = new JPanel(new BorderLayout());
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER);

        JScrollPane allergenesScrollPane = new JScrollPane(allergenesViewPanel);
        JPanel allergenesPanel = new JPanel(new BorderLayout());
        allergenesPanel.add(allergenesScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(ingredientsPanel, JSplitPane.TOP);
        splitPane.add(allergenesScrollPane, JSplitPane.BOTTOM);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedIngredient = ingredientsTable.getSelectedObject();
                Allergenes allergenes = selectedIngredient==null?null:selectedIngredient.getAllergenes();
                allergenesViewPanel.setAllergenes(allergenes);
                allergenesViewPanel.selectFirstLine();
            }
        });
        ingredientsTable.setSelectionModel(selection);
    }

    public void setIngredients(Ingredients ingredients){
        ingredientsDataViewTableModel.setIngredients(ingredients);
        ingredientsDataViewTableModel.fireTableDataChanged();
        int rowCount = ingredientsTable.getRowCount();
        if(rowCount >0){
            ingredientsTable.setRowSelectionInterval(0, rowCount - 1);
        }
    }
}