package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AllergenesEditPanel extends JPanel {
    final AllergenesEditDataTableModel allergenesDataTableModel
    Allergenes allergenes

    AllergenesEditPanel() {
        allergenesDataTableModel = new AllergenesEditDataTableModel()
        SelectableTable<Allergene> table = new SelectableTable<>(allergenesDataTableModel)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)

        JButton addButton = new JButton("Add Allergene")
        add(addButton, BorderLayout.SOUTH)
        addButton.addActionListener({ e ->
            addAllergene()
        })
    }

    void addAllergene() {
        String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
        while (name != null && name.equals(""))
            name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
        if (name != null) {
            try {
                allergenes.addBusinessObject(new Allergene(name, "", ""))
                allergenesDataTableModel.fireTableDataChanged()
            } catch (EmptyNameException ex) {
                ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY)
            } catch (DuplicateNameException ex) {
                ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim())
            }
        }
    }

    void setAccounting(Accounting accounting) {
        setAllergenes(accounting?.allergenes)
    }

    void setAllergenes(Allergenes allergenes){
        this.allergenes = allergenes
        allergenesDataTableModel.allergenes = allergenes
        allergenesDataTableModel.fireTableDataChanged()
    }
}