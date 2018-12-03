package be.dafke.BasicAccounting.Deliveroo;


import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class MealsPanel extends JPanel {
    private final JButton add;
    private final SelectableTable<DeliverooMeal> table;
    private final MealsDataTableModel mealsDataTableModel;

    public MealsPanel(DeliverooMeals deliverooMeals) {
        mealsDataTableModel = new MealsDataTableModel(this, deliverooMeals);
        table = new SelectableTable<>(mealsDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        add = new JButton("Add Meal");
        add(add, BorderLayout.NORTH);
        add.addActionListener( e -> {
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    deliverooMeals.addBusinessObject(new DeliverooMeal(name));
                    mealsDataTableModel.fireTableDataChanged();
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_NAME_EMPTY);
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim());
                }
            }
        });
    }
}