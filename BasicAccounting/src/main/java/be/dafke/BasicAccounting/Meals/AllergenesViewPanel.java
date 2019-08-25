package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Allergene;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class AllergenesViewPanel extends JPanel {
    private final AllergenesNoDetailsDataTableModel allergenesDataTableModel;
    private final SelectableTable<Allergene> allergeneOverviewTable;
    private JTextArea descriptionField;

    public AllergenesViewPanel() {
        allergenesDataTableModel = new AllergenesNoDetailsDataTableModel();
        allergeneOverviewTable = new SelectableTable<>(allergenesDataTableModel);
        allergeneOverviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200));

        JScrollPane scrollPane = new JScrollPane(allergeneOverviewTable);
        setLayout(new BorderLayout());

        descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setEditable(false);

        JSplitPane splitPane = Main.createSplitPane(scrollPane, descriptionField, JSplitPane.VERTICAL_SPLIT);

        add(splitPane, BorderLayout.CENTER);


        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelection();
            }
        });
        allergeneOverviewTable.setSelectionModel(selectionModel);


//        JButton add = new JButton("Add Allergene");
//        add(add, BorderLayout.NORTH);
//        add.addActionListener(e -> {
//            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
//            while (name != null && name.equals(""))
//                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
//            if (name != null) {
//                try {
//                    allergenes.addBusinessObject(new Allergene(name, "",""));
//                    allergenesDataTableModel.fireTableDataChanged();
//                } catch (EmptyNameException ex) {
//                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY);
//                } catch (DuplicateNameException ex) {
//                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim());
//                }
//            }
//        });
    }

    public void updateTable(){
        allergenesDataTableModel.fireTableDataChanged();
    }

    public void setAllergenes(Allergenes allergenes) {
        allergenesDataTableModel.setAllergenes(allergenes);
    }

    public void selectFirstLine(){
        if(allergeneOverviewTable.getRowCount()>0) {
            allergeneOverviewTable.setRowSelectionInterval(0,0);
        }
    }

    private void updateSelection() {
        Allergene allergene = allergeneOverviewTable.getSelectedObject();
        descriptionField.setText(allergene==null?"":allergene.getDescription());
//        descriptionField.setEditable(allergene!=null);
    }
}