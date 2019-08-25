package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class ArticlesPanel extends JPanel {
    private final JButton add;
    private final SelectableTable<Article> table;
    private JComboBox<Contact> supplierComboBox;
    private JComboBox<Ingredient> ingredientComboBox;
    private Accounting accounting;
    private final ArticlesDataTableModel articlesDataTableModel;

    public ArticlesPanel(Accounting accounting) {
        this.accounting = accounting;
        Articles articles = accounting.getArticles();
        articlesDataTableModel = new ArticlesDataTableModel(this, articles);
        table = new SelectableTable<>(articlesDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        supplierComboBox = new JComboBox<>();
        ingredientComboBox = new JComboBox<>();

        fireSupplierAddedOrRemoved();
        fireIngredientsAddedOrRemoved();
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn supplierColumn = columnModel.getColumn(ArticlesDataTableModel.SUPPLIER_COL);
        TableColumn ingredientColumn = columnModel.getColumn(ArticlesDataTableModel.INGREDIENT_COL);
        supplierColumn.setCellEditor(new DefaultCellEditor(supplierComboBox));
        ingredientColumn.setCellEditor(new DefaultCellEditor(ingredientComboBox));


        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        add = new JButton("Add Article");
        add(add, BorderLayout.NORTH);
        add.addActionListener( e -> {
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    articles.addBusinessObject(new Article(name));
                    articlesDataTableModel.fireTableDataChanged();
                    Main.fireArticleAddedOrRemoved(accounting);
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_NAME_EMPTY);
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim());
                }
            }
        });
        fireTableUpdate();
    }

    public void fireSupplierAddedOrRemoved() {
        supplierComboBox.removeAllItems();
        Contacts contacts = accounting.getContacts();
        contacts.getBusinessObjects(Contact::isSupplier).forEach(contact -> supplierComboBox.addItem(contact));
    }


    public void fireIngredientsAddedOrRemoved() {
        ingredientComboBox.removeAllItems();
        Ingredients ingredients = accounting.getIngredients();
        ingredients.getBusinessObjects().forEach(ingredient -> ingredientComboBox.addItem(ingredient));
    }

    public void fireTableUpdate(){
        articlesDataTableModel.fireTableDataChanged();
    }
}