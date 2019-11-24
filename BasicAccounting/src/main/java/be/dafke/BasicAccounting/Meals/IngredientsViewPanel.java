package be.dafke.BasicAccounting.Meals;


import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

public class IngredientsViewPanel extends JPanel {
    private final IngredientsDataViewTableModel ingredientsDataViewTableModel;
    private final AllergenesViewPanel allergenesViewPanel;
    private final SelectableTable<Ingredient> ingredientsTable;
//    private final JCheckBox button;
    private boolean multiSelection = false;
    private Ingredient selectedIngredient;

    private Ingredients ingredients;

    public IngredientsViewPanel(Accounting accounting, boolean multiSelection) {
        this.multiSelection = multiSelection;
        ingredients = accounting.getIngredients();
        ingredientsDataViewTableModel = new IngredientsDataViewTableModel(this);
        ingredientsDataViewTableModel.setIngredients(ingredients);
        ingredientsTable = new SelectableTable<>(ingredientsDataViewTableModel);
        ingredientsTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        ingredientsTable.setSelectionMode(multiSelection?ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:ListSelectionModel.SINGLE_SELECTION);

//        allergenesDataTableModel = new AllergenesDataTableModel();
//        allergenesTable = new SelectableTable<>(allergenesDataTableModel);
//        allergenesTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        allergenesViewPanel = new AllergenesViewPanel(multiSelection);

        // Unit column is not editable anyway
//        JComboBox<Unit> comboBox = new JComboBox<>(Unit.values());
//        TableColumn unitColumn = ingredientsTable.getColumnModel().getColumn(IngredientsDataEditTableModel.UNIT_COL);
//        unitColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTable);
        JPanel ingredientsPanel = new JPanel(new BorderLayout());
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER);

        JScrollPane allergenesScrollPane = new JScrollPane(allergenesViewPanel);
        JPanel allergenesPanel = new JPanel(new BorderLayout());
        allergenesPanel.add(allergenesScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(ingredientsPanel, JSplitPane.TOP);
        splitPane.add(allergenesScrollPane, JSplitPane.BOTTOM);

//        button = new JCheckBox("multiSelection");


        setLayout(new BorderLayout());
//        add(button, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        ingredientsTable.setSelectionModel(selection);
    }

    private void updateSelection() {
        if (multiSelection) {
            ArrayList<Ingredient> selectedObjects = ingredientsTable.getSelectedObjects();
            Allergenes allergenes = new Allergenes();
            selectedObjects.forEach(ingredient -> {
                Allergenes newAllergenes = ingredient.getAllergenes();
                newAllergenes.getBusinessObjects().forEach(allergene -> {
                    try {
                        allergenes.addBusinessObject(allergene);
                    } catch (EmptyNameException | DuplicateNameException e1) {
                        e1.printStackTrace();
                    }
                });
            });
            allergenesViewPanel.setAllergenes(allergenes);
            allergenesViewPanel.selectAll();
        } else {
            selectedIngredient = ingredientsTable.getSelectedObject();
            Allergenes allergenes = selectedIngredient == null ? null : selectedIngredient.getAllergenes();
            allergenesViewPanel.setAllergenes(allergenes);
            allergenesViewPanel.selectFirstLine();
        }
    }

    public void setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;
        ingredientsTable.setSelectionMode(multiSelection?ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:ListSelectionModel.SINGLE_SELECTION);
        allergenesViewPanel.setMultiSelection(multiSelection);
    }

    public void setIngredients(Ingredients ingredients){
        ingredientsDataViewTableModel.setIngredients(ingredients);
        ingredientsDataViewTableModel.fireTableDataChanged();
        // TODO: use select all
        int rowCount = ingredientsTable.getRowCount();
        if(rowCount >0){
            ingredientsTable.setRowSelectionInterval(0, rowCount - 1);
        }
    }
}