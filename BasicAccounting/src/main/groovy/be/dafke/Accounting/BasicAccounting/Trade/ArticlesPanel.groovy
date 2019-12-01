package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.DefaultCellEditor
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.table.TableColumn
import javax.swing.table.TableColumnModel
import java.awt.BorderLayout
import java.awt.Dimension

import static java.util.ResourceBundle.getBundle

class ArticlesPanel extends JPanel {
    final JButton addButton
    final SelectableTable<Article> table
    JComboBox<Contact> supplierComboBox
    JComboBox<Ingredient> ingredientComboBox
    Accounting accounting
    final ArticlesDataTableModel articlesDataTableModel

    ArticlesPanel(Accounting accounting) {
        this.accounting = accounting
        Articles articles = accounting.articles
        articlesDataTableModel = new ArticlesDataTableModel(this, articles)
        table = new SelectableTable<>(articlesDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))

        supplierComboBox = new JComboBox<>()
        ingredientComboBox = new JComboBox<>()

        fireSupplierAddedOrRemoved()
        fireIngredientsAddedOrRemoved()
        TableColumnModel columnModel = table.getColumnModel()
        TableColumn supplierColumn = columnModel.getColumn(ArticlesDataTableModel.SUPPLIER_COL)
        TableColumn ingredientColumn = columnModel.getColumn(ArticlesDataTableModel.INGREDIENT_COL)
        supplierColumn.setCellEditor(new DefaultCellEditor(supplierComboBox))
        ingredientColumn.setCellEditor(new DefaultCellEditor(ingredientComboBox))


        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)

        addButton = new JButton("Add Article")
        add(addButton, BorderLayout.NORTH)
        addButton.addActionListener({ e ->
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            if (name != null) {
                try {
                    articles.addBusinessObject(new Article(name))
                    articlesDataTableModel.fireTableDataChanged()
                    Main.fireArticleAddedOrRemoved(accounting)
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_NAME_EMPTY)
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim())
                }
            }
        })
        fireTableUpdate()
    }

    void fireSupplierAddedOrRemoved() {
        supplierComboBox.removeAllItems()
        Contacts contacts = accounting.contacts
        contacts.getBusinessObjects{it.supplier}.forEach({ contact -> supplierComboBox.addItem(contact) })
    }


    void fireIngredientsAddedOrRemoved() {
        ingredientComboBox.removeAllItems()
        Ingredients ingredients = accounting.ingredients
        ingredients.businessObjects.forEach({ ingredient -> ingredientComboBox.addItem(ingredient) })
    }

    void fireTableUpdate(){
        articlesDataTableModel.fireTableDataChanged()
    }
}
