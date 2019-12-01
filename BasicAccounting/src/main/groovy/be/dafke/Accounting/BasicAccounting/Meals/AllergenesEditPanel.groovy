package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class AllergenesEditPanel extends JPanel {
    private final AllergenesDataTableModel allergenesDataTableModel

    AllergenesEditPanel(Allergenes allergenes) {
        allergenesDataTableModel = new AllergenesDataTableModel()
        allergenesDataTableModel.setAllergenes(allergenes)
        SelectableTable<Allergene> table = new SelectableTable<>(allergenesDataTableModel)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)

        JButton add = new JButton("Add Allergene")
        add(add, BorderLayout.NORTH)
        add.addActionListener({ e ->
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            if (name != null) {
                try {
                    allergenes.addBusinessObject(new Allergene(name, "", ""))
                    allergenesDataTableModel.fireTableDataChanged()
                    Main.fireAllergeneAddedOrRemoved()
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_NAME_EMPTY)
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.INGREDIENT_DUPLICATE_NAME, name.trim())
                }
            }
        })
    }
}