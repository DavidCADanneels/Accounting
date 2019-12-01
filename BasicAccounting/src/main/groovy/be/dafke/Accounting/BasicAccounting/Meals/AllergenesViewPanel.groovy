package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class AllergenesViewPanel extends JPanel {
    private final AllergenesViewDataTableModel allergenesDataTableModel
    private final SelectableTable<Allergene> allergeneOverviewTable
    private JTextArea descriptionField
    private boolean multiSelection

    AllergenesViewPanel(boolean multiSelection) {
        this.multiSelection = multiSelection
        allergenesDataTableModel = new AllergenesViewDataTableModel()
        allergeneOverviewTable = new SelectableTable<>(allergenesDataTableModel)
        allergeneOverviewTable.setPreferredScrollableViewportSize(new Dimension(500, 200))

        JScrollPane scrollPane = new JScrollPane(allergeneOverviewTable)
        setLayout(new BorderLayout())

        descriptionField = new JTextArea(5, 20)
        descriptionField.setLineWrap(true)
        descriptionField.setWrapStyleWord(true)
        descriptionField.setEditable(false)

        JSplitPane splitPane = Main.createSplitPane(scrollPane, descriptionField, JSplitPane.VERTICAL_SPLIT)

        add(splitPane, BorderLayout.CENTER)


        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })
        allergeneOverviewTable.setSelectionModel(selectionModel)

        allergeneOverviewTable.setSelectionMode(multiSelection? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:ListSelectionModel.SINGLE_SELECTION)

//        JButton add = new JButton("Add Allergene")
//        add(add, BorderLayout.NORTH)
//        add.addActionListener(e -> {
//            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
//            while (name != null && name.equals(""))
//                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
//            if (name != null) {
//                try {
//                    allergenes.addBusinessObject(new Allergene(name, "",""))
//                    allergenesDataTableModel.fireTableDataChanged()
//                } catch (EmptyNameException ex) {
//                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY)
//                } catch (DuplicateNameException ex) {
//                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim())
//                }
//            }
//        })
    }

    void setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection
        allergeneOverviewTable.setSelectionMode(multiSelection?ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:ListSelectionModel.SINGLE_SELECTION)
    }

    void updateTable(){
        allergenesDataTableModel.fireTableDataChanged()
    }

    void setAllergenes(Allergenes allergenes) {
        allergenesDataTableModel.setAllergenes(allergenes)
    }

    void selectFirstLine(){
        if(allergeneOverviewTable.getRowCount()>0) {
            allergeneOverviewTable.setRowSelectionInterval(0,0)
        }
    }

    void selectAll(){
//        allergeneOverviewTable.selectAll()
        int rowCount = allergeneOverviewTable.getRowCount()
        if(rowCount >0){
            allergeneOverviewTable.setRowSelectionInterval(0, rowCount - 1)
        }
    }

    private void updateSelection() {
        if (multiSelection) {
            ArrayList<Allergene> selectedObjects = allergeneOverviewTable.getSelectedObjects()
            StringBuilder builder = new StringBuilder()
            selectedObjects.forEach({ allergene -> builder.append(allergene.getDescription()).append('\n') })
            descriptionField.setText(builder.toString())
        } else {
            Allergene allergene = allergeneOverviewTable.getSelectedObject()
            descriptionField.setText(allergene == null ? "" : allergene.getDescription())
//        descriptionField.setEditable(allergene!=null)
        }
    }
}