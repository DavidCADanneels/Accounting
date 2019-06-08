package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.Allergene;
import be.dafke.BusinessModel.Allergenes;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class AllergenesPanel extends JPanel {
    private final AllergenesDataTableModel allergenesDataTableModel;

    public AllergenesPanel(Allergenes allergenes) {
        allergenesDataTableModel = new AllergenesDataTableModel();
        allergenesDataTableModel.setAllergenes(allergenes);
        SelectableTable<Allergene> table = new SelectableTable<>(allergenesDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JButton add = new JButton("Add Allergene");
        add(add, BorderLayout.NORTH);
        add.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    allergenes.addBusinessObject(new Allergene(name, "",""));
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